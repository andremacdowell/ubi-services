package puc.pos.ubiqua.sddlexample.connection;

import java.net.SocketAddress;
import java.util.List;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import puc.pos.ubiqua.sddlexample.modellibrary.PingInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyNodeConnectionListener implements NodeConnectionListener {

	private Handler handler;

	public MyNodeConnectionListener(Handler handler) {
		this.handler = handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private void handleNewStatus(String status) {
		Bundle bund = new Bundle();
		bund.putString("status", status);
		Message msg = new Message();
		msg.setData(bund);
		handler.sendMessage(msg);
	}

	private void handleNewMessage(String message) {
		Bundle bund = new Bundle();
		bund.putString("status", "message");
		bund.putString("message", message);
		Message msg = new Message();
		msg.setData(bund);
		handler.sendMessage(msg);
	}

	@SuppressWarnings("unused")
	private void handleNewEvent(String className, PingInfo obj) {
		Bundle bund = new Bundle();
		bund.putString("status", "event");
		bund.putString("className", className);
		Message msg = new Message();
		msg.setData(bund);
		handler.sendMessage(msg);
	}

	public void connected(NodeConnection remoteCon) {
		handleNewStatus("connected");
	}

	public void reconnected(NodeConnection remoteCon, SocketAddress endPoint,
			boolean wasHandover, boolean wasMandatory) {
		handleNewStatus("reconnected - newend=" + endPoint + " handover="
				+ wasHandover + " mandatory=" + wasMandatory);
	}

	public void disconnected(NodeConnection remoteCon) {
		handleNewStatus("disconnected");
	}

	public void newMessageReceived(NodeConnection remoteCon,
			lac.cnclib.sddl.message.Message message) {
		String className = message.getContentObject().getClass().getCanonicalName();
		Object obj = message.getContentObject();
		if (className != null) {
			if (className.equals(PingInfo.class.getCanonicalName())) {
				handleNewMessage((String) obj);
			}
		}
		else {
			handleNewStatus("new object received: " + obj.toString());
		}

	}

	public void unsentMessages(NodeConnection remoteCon,
			List<lac.cnclib.sddl.message.Message> unsentMessages) {
		handleNewStatus("objects not sent: " + unsentMessages.size());
	}

	public void internalException(NodeConnection remoteCon, Exception e) {
		handleNewStatus("connection internal exception " + e);

	}

}
