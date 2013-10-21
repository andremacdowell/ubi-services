package br.pucrio.inf.lac.login;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.serialization.Serialization;
import lac.cnet.sddl.objects.ApplicationObject;
import lac.cnet.sddl.objects.Message;
import lac.cnet.sddl.objects.PrivateMessage;
import lac.cnet.sddl.udi.core.SddlLayer;
import lac.cnet.sddl.udi.core.UniversalDDSLayerFactory;
import lac.cnet.sddl.udi.core.listener.UDIDataReaderListener;
import modellibrary.RequestInfo;
import modellibrary.ResponseInfo;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import crublib.CrudLib;

public class LoginCoreServer implements
		UDIDataReaderListener<ApplicationObject> {
	SddlLayer core;

	public LoginCoreServer() {
		core = UniversalDDSLayerFactory.getInstance();
		core.createParticipant(UniversalDDSLayerFactory.CNET_DOMAIN);

		core.createPublisher();
		core.createSubscriber();

		Object receiveMessageTopic = core.createTopic(Message.class,
				Message.class.getSimpleName());
		core.createDataReader(this, receiveMessageTopic);

		Object toMobileNodeTopic = core.createTopic(PrivateMessage.class,
				PrivateMessage.class.getSimpleName());
		core.createDataWriter(toMobileNodeTopic);

		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNewData(ApplicationObject topicSample) {
		Message message = (Message) topicSample;
		Serializable serializable = Serialization.fromJavaByteStream(message
				.getContent());
		String className = serializable.getClass().getCanonicalName();
		String uuid = message.getSenderId().toString();
		String token = null;
		String login = null;
		String senha = null;

		System.out.println("[LoginCoreServer] Mensagem recebida do cliente: "
				+ Serialization.fromJavaByteStream(message.getContent())
				+ "| className:" + className + " | uuid:" + uuid);

		if (className != null) {
			if (className.equals(RequestInfo.class.getCanonicalName())) {
				// REQUEST recv
				RequestInfo requestMessage = (RequestInfo) Serialization
						.fromJavaByteStream(message.getContent());
				System.out.println("[LoginCoreServer] Request type: "
						+ requestMessage.getType() + " | payload: "
						+ requestMessage.getPayload());

				JsonNode result_json = Json.newObject();
				ObjectNode result = Json.newObject();
				ObjectNode obj_r = Json.newObject();

				JsonNode json;
				switch (requestMessage.getType()) {
				case "login":
					JsonNode authReq = Json.parse(requestMessage.getPayload());

					json = CrudLib.getNode(message.getSenderId().toString());
					System.out.println("json: " + json.toString());
					if (authReq.get("token") != null) {
						token = authReq.get("token").toString();
						if (json.get("status").equals("OK")) {
							if (token.equals(json.get("token").toString())) {
								result.put("status", "OK");
								result.put("message", "Login com sucesso");
								result.put("type", requestMessage.getType());
								obj_r.put("token", token);
								result.put("payload", obj_r.toString());
								result_json = Json.fromJson(result,
										JsonNode.class);
							} else {
								result.put("status", "ERR");
								result.put("message", "token inválido");
								result.put("type", requestMessage.getType());
								result.put("payload", "");
								result_json = Json.fromJson(result,
										JsonNode.class);
							}
						} else {
							result.put("status", "ERR");
							result.put("message", "token inválido");
							result.put("type", requestMessage.getType());
							result.put("payload", "");
							result_json = Json.fromJson(result, JsonNode.class);
						}
					} else {
						// procurar login e senha
						login = authReq.get("login").toString();
						senha = authReq.get("senha").toString();
						if (json != null) {
							if (login.equals(json.get("login"))
									&& senha.equals(json.get("senha"))) {
								result.put("status", "OK");
								result.put("message", "Login com sucesso");
								result.put("type", requestMessage.getType());
								obj_r.put("token", token);
								result.put("payload", obj_r.toString());
								result_json = Json.fromJson(result,
										JsonNode.class);
							} else {
								result.put("status", "ERR");
								result.put("message",
										"login ou senha inválidos");
								result.put("type", requestMessage.getType());
								result.put("payload", "");
								result_json = Json.fromJson(result,
										JsonNode.class);
							}
						} else {
							// se não existir, criar e depois retornar a
							// token
							ObjectNode newAuth = Json.newObject();
							newAuth.put("login", login);
							newAuth.put("senha", senha);
							token = login + senha;
							byte[] bytesOfMessage;
							try {
								bytesOfMessage = token.getBytes("UTF-8");
								MessageDigest md = MessageDigest
										.getInstance("MD5");
								token = md.digest(bytesOfMessage).toString();
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchAlgorithmException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							newAuth.put("token", token);
							result_json = CrudLib.addNode(newAuth);
							obj_r.put("token", token);
							result.put("payload", obj_r.toString());
						}
					}
					break;
				case "logout":
					ObjectNode upAuth = Json.newObject();
					upAuth.put("token", "");
					result_json = CrudLib.updNode(uuid, upAuth);
					break;
				}

				// RESPONSE send
				ResponseInfo responseMessage = new ResponseInfo(
						message.getSenderId(), requestMessage.getType(),
						result_json.toString());

				PrivateMessage privateMessage = new PrivateMessage();
				privateMessage.setGatewayId(message.getGatewayId());
				privateMessage.setNodeId(message.getSenderId());

				ApplicationMessage appMessage = new ApplicationMessage();
				appMessage.setContentObject(responseMessage);
				privateMessage.setMessage(Serialization
						.toProtocolMessage(appMessage));

				core.writeTopic(PrivateMessage.class.getSimpleName(),
						privateMessage);
			} else {
				System.out
						.println("[LoginCoreServer] Objeto desconhecido recebido do cliente: "
								+ className);
			}
		} else {
			System.out
					.println("[LoginCoreServer] Objeto inválido recebido do cliente");
		}
		// JsonNode result_json = Json.newObject();
		// System.out
		// .println("[RegistryCoreServer] lstNodes() requested by client");
		// result_json = CrudLib.lstNodes();
		//
		// ApplicationMessage appMessage = new ApplicationMessage();
		// // appMessage.setContentObject("Recebi do cliente: " +
		// // Serialization.fromJavaByteStream(message.getContent()));
		// appMessage.setContentObject(result_json.toString());
		// privateMessage.setMessage(Serialization.toProtocolMessage(appMessage));
		//
		// core.writeTopic(PrivateMessage.class.getSimpleName(),
		// privateMessage);
	}
}

// Criando um node via ORM:
// Node node = new Node();
// node.name = "Oi";
// node.uuid = "12345";
// node.info = "xxx";
// node.save();
