package puc.pos.ubiqua.sddlexample.connection;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import puc.pos.ubiqua.sddlexample.modellibrary.PingInfo;
import android.os.AsyncTask;

public class PingTask extends AsyncTask<Void, byte[], Boolean> {
	
	private NodeConnection myConnection;
	private UUID uuid;
	
	public PingTask(NodeConnection con, UUID id)
	{
		myConnection = con;
		uuid = id;
	}
	
	@Override
	protected Boolean doInBackground(Void... arg0) 
	{		
		Boolean result = false;
		
		//enviar mensagem
		PingInfo info = new PingInfo(uuid); //mensagem poderia ter sido passada como parametro no construtor da task
		ApplicationMessage pingInfo = new ApplicationMessage();
    	List<String> tags = new LinkedList<String>();
        tags.add("ping");
    	
        pingInfo.setContentObject(info);
        pingInfo.setTagList(tags);
        pingInfo.setSenderID(uuid);
    	
    	try {
    		myConnection.sendMessage(pingInfo);
    		result = true;
        }
        catch (Exception e) {
        	result = false;
        	e.printStackTrace();
        }

		return result;
	}
}
