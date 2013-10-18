package lac.puc.ubi.services.sddlexample.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

/**
 * AsynchTask para tentativa de conexão
 * 
 * @author andremd
 *
 */
public class ConnectionTask extends AsyncTask<Void, byte[], Boolean> {

	private String ipAddress;
	private NodeConnection myConnection;
	private MyNodeConnectionListener myNodeConnectionListener;
	private Handler messageHandler;
	private Context context;
	private UUID uuid;
  
	/**
	 * Which port to attempt a connection.
	 */
	private static final int DEFAULT_SDDL_PORT = 5500;

	public ConnectionTask(String ip, Handler handler, Context apContext, UUID id) 
	{
		ipAddress = ip;
	    messageHandler = handler;
	    context = apContext;
	    uuid = id;
	}

	/**
	 * Overriten methods
	 */
	@Override
	protected Boolean doInBackground(Void... arg0) 
	{
		boolean result = true;
	
	    try {
	    	myConnection = new MrUdpNodeConnection(uuid);
	    }
	    catch (IOException e) {
	    	result = false;
	    	e.printStackTrace();
	    }
	
	    if (result) 
	    {
	    	myNodeConnectionListener = new MyNodeConnectionListener(messageHandler);
	    	myConnection.addNodeConnectionListener(myNodeConnectionListener);
	
	    	SocketAddress sc = new InetSocketAddress(ipAddress, DEFAULT_SDDL_PORT);
	
	    	try {
	    		myConnection.connect(sc);
	    	}
	    	catch (IOException e) {
	    		result = false;
	    		e.printStackTrace();
	    	}
	    }
	
	    return result;
	}
	
	@Override
	protected void onPostExecute(Boolean result) 
	{
		if(result)
		{
			//TODO: Send authentication message with client UUID for example
		}
		else
			Toast.makeText(context, "Impossible to Connect!", Toast.LENGTH_SHORT).show();
	}

	public NodeConnection getMyConnection() {
		return myConnection;
	}
	
	public UUID getUUID() {
		return uuid;
	}
}