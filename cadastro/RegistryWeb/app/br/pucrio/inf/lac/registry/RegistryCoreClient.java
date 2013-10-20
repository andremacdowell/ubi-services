package br.pucrio.inf.lac.registry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.message.Message;
import lac.cnclib.sddl.serialization.Serialization;
import modellibrary.RequestInfo;
import modellibrary.ResponseInfo;

public class RegistryCoreClient implements NodeConnectionListener {
	private static String gatewayIP = "127.0.0.1";
	private static int gatewayPort = 5500;
	private MrUdpNodeConnection connection;

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.OFF);

		new RegistryCoreClient();
	}

	public RegistryCoreClient() {
		InetSocketAddress address = new InetSocketAddress(gatewayIP,
				gatewayPort);
		try {
			connection = new MrUdpNodeConnection();
			connection.connect(address);
			connection.addNodeConnectionListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connected(NodeConnection remoteCon) {
		System.out.println("[RegistryCoreClient] Conectou, enviando saudação.");
		ApplicationMessage appMessage = new ApplicationMessage();
		String serializableContent = "Registering mobile node requested by client.";
		appMessage.setContentObject(serializableContent);

		try {
			remoteCon.sendMessage(appMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// REQUEST
	}

	@Override
	public void newMessageReceived(NodeConnection remoteCon, Message message) {
		System.out
				.println("[RegistryCoreClient] Mensagem recebida do servidor: "
						+ Serialization.fromJavaByteStream(message.getContent()));

		String className = message.getContentObject().getClass()
				.getCanonicalName();

		if (className != null) {
			// RESPONSE
			if (className.equals(ResponseInfo.class.getCanonicalName())) {
				ResponseInfo resp = (ResponseInfo) Serialization
						.fromJavaByteStream(message.getContent());
				System.out.println("[RegistryCoreClient] Response type: "
						+ resp.getType() + " | payload: " + resp.getPayload());
			} else {
				System.out
						.println("[RegistryCoreClient] Objeto desconhecido recebido do servidor: "
								+ className);
			}
		} else {
			System.out
					.println("[RegistryCoreClient] Objeto inválido recebido do servidor");
		}
	}

	// other methods

	@Override
	public void reconnected(NodeConnection remoteCon, SocketAddress endPoint,
			boolean wasHandover, boolean wasMandatory) {
		System.out.println("[RegistryCoreClient] Reconectou");
	}

	@Override
	public void disconnected(NodeConnection remoteCon) {
		System.out.println("[RegistryCoreClient] Desconectou");
	}

	@Override
	public void unsentMessages(NodeConnection remoteCon,
			List<Message> unsentMessages) {
	}

	@Override
	public void internalException(NodeConnection remoteCon, Exception e) {
	}
}
