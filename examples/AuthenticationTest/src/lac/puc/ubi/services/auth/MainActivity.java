package lac.puc.ubi.services.auth;

import java.util.UUID;

import lac.puc.ubi.services.R;
import lac.puc.ubi.services.auth.utils.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

/**
 * MainActivity. Tela inicial da aplicação exemplo. 
 * O handler de mensagens recebidas está aqui.
 * 
 * @author andremd
 *
 */
@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	//Connection Control
	private static String IP = "192.168.0.7";	
	private static Handler msgHandler;
	private UUID clientUUID;
	
	//Interface
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = {"Login", "New User"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* Interface Initialization */
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabs.length);
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
		
		/* Adding Tabs */
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		/* Server Message Handler */
		recoverUUID();
		initHandler();

        ((App)getApplication()).setConnectionData(clientUUID, IP, msgHandler);
	}
	
	private void recoverUUID()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("uuid", prefs.getString("uuid", UUID.randomUUID().toString()));
		editor.commit();
		
		clientUUID = UUID.fromString(prefs.getString("uuid", UUID.randomUUID().toString()));
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
					Toast.makeText(MainActivity.this, "Conectado!", Toast.LENGTH_SHORT).show();
				}
				else if (status.equals("disconnected")) {
					Toast.makeText(MainActivity.this, "Desconectado!", Toast.LENGTH_SHORT).show();
				}
				else if (status.equals("message")) {
					Toast.makeText(MainActivity.this, msg.getData().getString("message"), Toast.LENGTH_LONG).show();
				}
				else if (status.equals("event")) {
					//outro tipo de evento, podemos usar isso para tratar o recebimento de outros tipos de objetos
				}
			}
		}
		};
	}}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}