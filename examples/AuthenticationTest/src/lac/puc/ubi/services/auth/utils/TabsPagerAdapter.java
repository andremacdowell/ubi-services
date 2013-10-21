package lac.puc.ubi.services.auth.utils;

import lac.puc.ubi.services.auth.AuthenticationFragment;
import lac.puc.ubi.services.auth.RegistrationFragment;
import lac.puc.ubi.services.auth.SearchFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
	//control
	private Fragment currentFrag;
	
	//interface
	private int itemCount;
	

	public TabsPagerAdapter(FragmentManager fm, int itemCount) {
		super(fm);
		this.itemCount = itemCount;
	}
	
	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			currentFrag = new AuthenticationFragment();
			return currentFrag;
		case 1:
			currentFrag =  new RegistrationFragment();
			return currentFrag;
		case 2:
			currentFrag = new SearchFragment();
			return currentFrag;
		}

		return null;
	}

	public Fragment getCurrentFragment() {
		return currentFrag;
	}
	
	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return itemCount;
	}

}
