package lac.puc.ubi.services.auth.utils;

import lac.puc.ubi.services.auth.AuthenticationFragment;
import lac.puc.ubi.services.auth.RegistrationFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	
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
			return new AuthenticationFragment();
		case 1:
			return new RegistrationFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return itemCount;
	}

}
