package puc.pos.ubiqua.sddlexample;

import java.util.UUID;

import puc.pos.ubiqua.sddlexample.connection.ConnectionTask;
import puc.pos.ubiqua.sddlexample.connection.PingTask;
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

public class MainActivity extends Activity {

	private static Handler msgHandler;
	private Context context;
	private Boolean connected;
	private ConnectionTask connectionTask;
	private PingTask pingTask;
	
	//Widgets
	private EditText et_ip;
	private Button btn_ping;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_ping = (Button) findViewById(R.id.btnPing);
		et_ip = (EditText) findViewById(R.id.etIp);
		
		connected = false;
		context = this;
		initHandler();
		btn_ping.setText(R.string.connect);
		
		if(btn_ping != null)
		{
			btn_ping.setOnClickListener( new OnClickListener()
		    {
				@Override
				public void onClick(View v) 
				{
					if(!connected) //connect!
					{
						connectionTask = new ConnectionTask(et_ip.getText().toString(), msgHandler, MainActivity.this.getApplicationContext());
						connectionTask.execute();
					}
					else //ping!
					{
						pingTask = new PingTask(connectionTask.getMyConnection(), new UUID(1,1));
						pingTask.execute();
					}
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
					connected = true;
					et_ip.setText(R.string.ping);
				}
				else if (status.equals("disconnected")) {
					Toast.makeText(context, "Desconectado!", Toast.LENGTH_SHORT).show();
					connected = false;
					et_ip.setText(R.string.connect);
				}
				else if (status.equals("message")) {
					Toast.makeText(context, msg.getData().getString("message"), Toast.LENGTH_LONG).show(); //Pong!
				}
				else if (status.equals("event")) {
					//event, never gets in here because listener is ignoring this case
				}
			}
		}
		};
	}}
}
