package lac.puc.ubi.services.auth;

import java.util.UUID;

import lac.puc.ubi.services.auth.connection.ConnectionTask;
import android.app.Application;
import android.os.Handler;

public class App extends Application {
	
	//connection
	public ConnectionTask connectionTask;
	public UUID uuid;
	public String IP;
	public Handler msgHandler;
	
	public void setConnectionData(UUID id, String ip, Handler handler)
	{
		uuid = id;
		IP = ip;
		msgHandler = handler;
	}
}
