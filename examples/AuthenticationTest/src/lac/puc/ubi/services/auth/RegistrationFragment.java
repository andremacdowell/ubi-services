package lac.puc.ubi.services.auth;

import lac.puc.ubi.services.R;
import lac.puc.ubi.services.auth.connection.RequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationFragment extends Fragment implements OnClickListener{

	//control
	private App ap;
	
	//Widgets
	private View rootView;
	private EditText et_email;
	private EditText et_name;
	private EditText et_city;
	private DatePicker dp_birth;
	private EditText et_phone;
	private EditText et_pass;
	private Button btn_register;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_registration, container, false);

		btn_register = (Button) rootView.findViewById(R.id.btnRegister);
		et_email = (EditText) rootView.findViewById(R.id.etEmail);
		et_name = (EditText) rootView.findViewById(R.id.etName);
		et_city = (EditText) rootView.findViewById(R.id.etCity);
		et_phone = (EditText) rootView.findViewById(R.id.etPhone);
		et_pass = (EditText) rootView.findViewById(R.id.etPass);
		
		dp_birth = (DatePicker) rootView.findViewById(R.id.dpBirth);
		
		btn_register.setOnClickListener(this);
		
		ap = (App) getActivity().getApplication();
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) 
		{
        case R.id.btnRegister:
        	if(ap.connectionTask != null)
        	{
				RequestTask requestTask = new RequestTask(ap.connectionTask.getMyConnection(), ap.uuid, "addNode", packageRegistrationInfo());
				requestTask.execute();
        	}
        	else
        		Toast.makeText(getActivity(), "No connection!", Toast.LENGTH_SHORT).show();
            break;
        }
	}

	private String parseDateString(DatePicker dp)
	{
		int day = dp.getDayOfMonth() + 1;
		int month = dp.getMonth() + 1;
		int year = dp.getYear() + 1;
		
		return day + "-" + month + "-" + year;
	}
	
	private String packageRegistrationInfo()
	{
		JSONObject result = new JSONObject();
		JSONObject info = new JSONObject();
		
		try {
			result.put("uuid", ap.uuid.toString());
			result.put("name", et_name.getText().toString());
			
			info.put("email", et_email.getText().toString());
			info.put("city", et_city.getText().toString());
			info.put("phone", et_phone.getText().toString());
			info.put("birthday", parseDateString(dp_birth));
			info.put("pass", et_pass.getText().toString());
			
			result.put("info", info.toString());
			
			} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return result.toString();
	}
}
