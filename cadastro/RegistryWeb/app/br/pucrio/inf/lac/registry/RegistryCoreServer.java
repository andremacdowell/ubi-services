package br.pucrio.inf.lac.registry;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;

import crublib.CrudLib;

import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.serialization.Serialization;
import lac.cnet.sddl.objects.ApplicationObject;
import lac.cnet.sddl.objects.Message;
import lac.cnet.sddl.objects.PrivateMessage;
import lac.cnet.sddl.udi.core.SddlLayer;
import lac.cnet.sddl.udi.core.UniversalDDSLayerFactory;
import lac.cnet.sddl.udi.core.listener.UDIDataReaderListener;

public class RegistryCoreServer implements
		UDIDataReaderListener<ApplicationObject> {
	SddlLayer core;

	public RegistryCoreServer() {
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
		System.out
				.println("[RegistryCoreServer] Mensagem recebida do cliente: "
						+ Serialization.fromJavaByteStream(message.getContent()));

		PrivateMessage privateMessage = new PrivateMessage();
		privateMessage.setGatewayId(message.getGatewayId());
		privateMessage.setNodeId(message.getSenderId());

		JsonNode result_json = Json.newObject();
		System.out
				.println("[RegistryCoreServer] lstNodes() requested by client");
		result_json = CrudLib.lstNodes();

		ApplicationMessage appMessage = new ApplicationMessage();
		// appMessage.setContentObject("Recebi do cliente: " +
		// Serialization.fromJavaByteStream(message.getContent()));
		appMessage.setContentObject(result_json.toString());
		privateMessage.setMessage(Serialization.toProtocolMessage(appMessage));

		core.writeTopic(PrivateMessage.class.getSimpleName(), privateMessage);
	}
}

// Criando um node via ORM:
// Node node = new Node();
// node.name = "Oi";
// node.uuid = "12345";
// node.info = "xxx";
// node.save();
