package lac.puc.ubi.services.auth;

import lac.puc.ubi.services.R;
import lac.puc.ubi.services.auth.connection.ConnectionTask;
import lac.puc.ubi.services.modellibrary.AuthInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AuthenticationFragment extends Fragment implements OnClickListener {
	
	//Connection Control
	private static App ap;
	
	//Widgets
	private View rootView;
	private EditText et_email;
	private EditText et_pass;
	private Button btn_connect;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_authentication, container, false);

		btn_connect = (Button) rootView.findViewById(R.id.btnConnect);
		et_email = (EditText) rootView.findViewById(R.id.etEmail);
		et_pass = (EditText) rootView.findViewById(R.id.etPass);
		
		btn_connect.setOnClickListener(this);
		
		ap = (App) getActivity().getApplication();
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) 
		{
        case R.id.btnConnect:
        	AuthInfo info = new AuthInfo(ap.uuid, et_email.getText().toString(), et_pass.getText().toString());
			ap.connectionTask = new ConnectionTask(ap.IP, ap.msgHandler, getActivity(), info);
			ap.connectionTask.execute();
            break;
        }
	}
}