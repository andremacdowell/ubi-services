package lac.puc.ubi.services.auth.connection;

import java.util.LinkedList;
import java.util.List;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.puc.ubi.services.modellibrary.AuthInfo;
import android.os.AsyncTask;

/**
 * AsynchTask para envio de objeto do tipo PingInfo para o Controlador.
 * 
 * @author andremd
 *
 */
public class AuthTask extends AsyncTask<Void, byte[], Boolean> {
	
	private NodeConnection myConnection;
	private AuthInfo rawInfo;
	private ApplicationMessage authInfo;
	private List<String> tags;
	
	public AuthTask(NodeConnection con, AuthInfo _info)
	{
		myConnection = con;

		//Construindo o serializavel a ser enviado
		rawInfo = _info;
		authInfo = new ApplicationMessage();
    	tags = new LinkedList<String>();
        tags.add("authentication");
	}
	
	@Override
	protected Boolean doInBackground(Void... arg0) 
	{		
		Boolean result = false;
		
        //Empacotando o serializavel na mensagem
		authInfo.setContentObject(rawInfo);
		authInfo.setTagList(tags);
		authInfo.setSenderID(rawInfo.getUuid());
    	
    	try {
    		myConnection.sendMessage(authInfo);
    		result = true;
        }
        catch (Exception e) {
        	result = false;
        	e.printStackTrace();
        }

		return result;
	}
}
