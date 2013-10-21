package br.pucrio.inf.lac.login;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.message.Message;
import lac.cnclib.sddl.serialization.Serialization;
import modellibrary.RequestInfo;
import play.libs.Json;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class LoginCoreClient implements NodeConnectionListener {

	private static String gatewayIP = "127.0.0.1";
	private static int gatewayPort = 5500;
	private MrUdpNodeConnection connection;
	/** Client UUID */
	private static UUID uuid = null;

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.OFF);

		new LoginCoreClient();
	}

	public LoginCoreClient() {
		if (uuid == null) {
			uuid = UUID.randomUUID();
		}

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
		System.out.println("[LoginCoreClient] Conectou, enviando saudação.");
		ApplicationMessage appMessage = new ApplicationMessage();

		// REQUEST login: passando login e senha (criação)
		ObjectNode objLogin = Json.newObject();
		objLogin.put("login", "ivan");
		objLogin.put("senha", "123");
		RequestInfo requestMessage = new RequestInfo(appMessage.getSenderID(),
				"login", objLogin.toString());
		appMessage.setContentObject(requestMessage);
		try {
			remoteCon.sendMessage(appMessage);
			Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		// REQUEST login: passando token (validação)
		objLogin = Json.newObject();
		objLogin.put("token", "[B@123b94a");
		requestMessage = new RequestInfo(appMessage.getSenderID(), "login",
				objLogin.toString());
		appMessage.setContentObject(requestMessage);
		try {
			remoteCon.sendMessage(appMessage);
			Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		// REQUEST logout
		requestMessage = new RequestInfo(appMessage.getSenderID(), "logout", "");
		appMessage.setContentObject(requestMessage);
		try {
			remoteCon.sendMessage(appMessage);
			Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void newMessageReceived(NodeConnection remoteCon, Message message) {
		System.out.println("[LoginCoreClient] Mensagem recebida do servidor: "
				+ Serialization.fromJavaByteStream(message.getContent()));
	}

	// other methods

	@Override
	public void reconnected(NodeConnection remoteCon, SocketAddress endPoint,
			boolean wasHandover, boolean wasMandatory) {
		System.out.println("[LoginCoreClient] Reconectou");
	}

	@Override
	public void disconnected(NodeConnection remoteCon) {
		System.out.println("[LoginCoreClient] Desconectou");
	}

	@Override
	public void unsentMessages(NodeConnection remoteCon,
			List<Message> unsentMessages) {
	}

	@Override
	public void internalException(NodeConnection remoteCon, Exception e) {
	}
}
