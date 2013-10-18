package lac.puc.ubi.services.auth;

import java.util.UUID;

import lac.puc.ubi.services.R;
import lac.puc.ubi.services.auth.connection.ConnectionTask;
import lac.puc.ubi.services.modellibrary.AuthInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * MainActivity. Tela inicial da aplicação exemplo. 
 * O handler de mensagens recebidas está aqui.
 * 
 * @author andremd
 *
 */
public class MainActivity extends Activity {

	private static String IP = "192.168.1.255";
	
	private static Handler msgHandler;
	private Context context;
	private ConnectionTask connectionTask;
	private AuthInfo info;
	
	//Widgets
	private EditText et_email;
	private EditText et_pass;
	private Button btn_connect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_connect = (Button) findViewById(R.id.btnConnect);
		et_email = (EditText) findViewById(R.id.etEmail);
		et_pass = (EditText) findViewById(R.id.etPass);
		
		context = this;
		initHandler();
		
		if(btn_connect != null)
		{
			btn_connect.setOnClickListener( new OnClickListener()
		    {
				@Override
				public void onClick(View v) 
				{
					info = new AuthInfo(new UUID(1,1), et_email.getText().toString(), et_pass.getText().toString());
					
					connectionTask = new ConnectionTask(IP, msgHandler, MainActivity.this.getApplicationContext(), info);
					connectionTask.execute();
				} 	
		    });
		}
	}
	
	@SuppressLint("HandlerLeak")
	private void initHandler() 
	{
		if(msgHandler == null) {
		msgHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if (msg.getData().getString("status") != null) 
			{
				String status = msg.getData().getString("status");
				
				if (status.equals("connected")) {
					Toast.makeText(context, "Conectado!", Toast.LENGTH_SHORT).show();
				}
				else if (status.equals("disconnected")) {
					Toast.makeText(context, "Desconectado!", Toast.LENGTH_SHORT).show();
				}
				else if (status.equals("message")) {
					Toast.makeText(context, msg.getData().getString("message"), Toast.LENGTH_LONG).show();
				}
				else if (status.equals("event")) {
					//outro tipo de evento, podemos usar isso para tratar o recebimento de outros tipos de objetos
				}
			}
		}
		};
	}}
}
