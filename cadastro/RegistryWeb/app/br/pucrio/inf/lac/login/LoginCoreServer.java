package br.pucrio.inf.lac.login;

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
		System.out.println("[LoginCoreServer] Mensagem recebida do cliente: "
				+ Serialization.fromJavaByteStream(message.getContent()));

		// PrivateMessage privateMessage = new PrivateMessage();
		// privateMessage.setGatewayId(message.getGatewayId());
		// privateMessage.setNodeId(message.getSenderId());

		String className = message.getContent().getClass().getCanonicalName();
		String uuid = message.getSenderId().toString();
		System.out.println("className:"+className+" \nuuid:"+uuid);
		if (className != null) {
			if (className.equals(RequestInfo.class.getCanonicalName())) {
				JsonNode json;
				// REQUEST
				RequestInfo req = (RequestInfo) Serialization
						.fromJavaByteStream(message.getContent());
				System.out.println("[LoginCoreServer] Request type: "
						+ req.getType() + " | payload: " + req.getPayload());

				String type = req.getType();
				ObjectNode result_json = Json.newObject();
				JsonNode authReq = Json.parse(req.getPayload());
				String login = authReq.get("login").toString();
				String senha = authReq.get("senha").toString();
				String token = authReq.get("token").toString();
				if (type.equals("login")) {
					// se já está autenticado, validar autenticação
					if (token != null) {
						json = CrudLib
								.getNode(message.getSenderId().toString());
						if (token.equals(json.get("token").toString())) {
							result_json.put("status", "ok");
							result_json.put("token", token);
						} else {
							result_json.put("status", "erro");
						}
					} else {
						// procurar!
						json = CrudLib.getNode(uuid);
						if (json != null) {
							if (login.equals(json.get("login"))
									&& senha.equals(json.get("senha"))) {
								result_json.put("status", "ok");
								result_json.put("token", token);
							} else {
								result_json.put("status", "erro");
							}
						} else {
							// se não existir, criar e depois retornar a token
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
							json = CrudLib.addNode(newAuth);
							if (json.get("status").toString().equals("OK")) {
								result_json.put("status", "ok");
								result_json.put("token", token);
							}

						}
					}
				}
				else if (type.equals("logout")){
					ObjectNode upAuth = Json.newObject();
					upAuth.put("token", "");
					json = CrudLib.updNode(uuid, upAuth);
					if (json.get("status").toString().equals("OK")) {
						result_json.put("status", "ok");
						result_json.put("token", token);
					}
				}
					
				// RESPONSE
				PrivateMessage privateMessage = new PrivateMessage();
				privateMessage.setGatewayId(message.getGatewayId());
				privateMessage.setNodeId(message.getSenderId());

				// JsonNode result_json = Json.newObject();
				System.out
						.println("[LoginCoreServer] lstNodes() requested by client");
				// result_json = CrudLib.lstNodes();

				ApplicationMessage appMessage = new ApplicationMessage();
				appMessage.setContentObject(result_json.toString());
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
