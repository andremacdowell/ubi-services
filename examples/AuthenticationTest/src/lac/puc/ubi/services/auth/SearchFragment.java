package lac.puc.ubi.services.auth;

import java.util.List;

import lac.puc.ubi.services.R;
import lac.puc.ubi.services.auth.connection.RequestTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchFragment extends ListFragment implements OnClickListener {
	
	//control
	private App ap;
	
	//Widgets
	private View rootView;
	private Button btn_search;
	private EditText et_name;	
	
	//interface related
	private ArrayAdapter<String> adapter;
	private LayoutInflater inflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = inflater.inflate(R.layout.fragment_search, container, false);
		
		btn_search = (Button) rootView.findViewById(R.id.btnSearch);
		et_name = (EditText) rootView.findViewById(R.id.etName);
		
		btn_search.setOnClickListener(this);
		
		ap = (App) getActivity().getApplication();
		
		//populateList(new String[] { "1", "2", "3", "4" });
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) 
		{
		case R.id.btnSearch:
        	if(ap.connectionTask != null)
        	{
				RequestTask requestTask = new RequestTask(ap.connectionTask.getMyConnection(), ap.uuid, "srchNodes", et_name.getText().toString());
				requestTask.execute();
        	}
        	else
        		Toast.makeText(getActivity(), "No connection!", Toast.LENGTH_SHORT).show();
            break;
        }
	}
	
	public void populateList(List<String> resultList) {
		adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, resultList);
		setListAdapter(adapter);
	}
}
