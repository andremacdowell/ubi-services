package lac.puc.ubi.services.sddlexample.connection;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.puc.ubi.services.modellibrary.PingInfo;
import android.os.AsyncTask;

/**
 * AsynchTask para envio de objeto do tipo PingInfo para o Controlador.
 * 
 * @author andremd
 *
 */
public class PingTask extends AsyncTask<Void, byte[], Boolean> {
	
	private NodeConnection myConnection;
	private UUID uuid;
	
	private PingInfo rawInfo;
	private ApplicationMessage pingInfo;
	private List<String> tags;
	
	public PingTask(NodeConnection con, UUID id)
	{
		myConnection = con;
		uuid = id;
		
		//Construindo o serializavel a ser enviado
		rawInfo = new PingInfo(uuid);
		pingInfo = new ApplicationMessage();
    	tags = new LinkedList<String>();
        tags.add("ping");
	}
	
	@Override
	protected Boolean doInBackground(Void... arg0) 
	{		
		Boolean result = false;
    	
        //Empacotando o serializavel na mensagem
        pingInfo.setContentObject(rawInfo);
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
