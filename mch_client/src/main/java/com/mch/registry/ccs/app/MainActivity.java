package com.mch.registry.ccs.app;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mch.registry.ccs.data.PatientDataHandler;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener{
	CollectionPagerAdapter mCollectionPagerAdapter;
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PatientDataHandler pdh = new PatientDataHandler(this, null, null, 1);
		if(pdh.getPatient().get_isVerified()==0){
			Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
			startActivity(intent);
		}else{
			mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

			final ActionBar actionBar = getActionBar();
			actionBar.setHomeButtonEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mCollectionPagerAdapter);
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

						@Override

						public void onPageSelected(int position) {
							actionBar.setSelectedNavigationItem(position);
						}

					});
			for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
				actionBar.addTab(actionBar.newTab()
						.setText(mCollectionPagerAdapter.getPageTitle(i))
						.setTabListener(this));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_mobilephonenumber) {
			Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
		//MainFragmentActivity.disableBookmarkFlag = MainFragmentActivity.disableShareFlag = true;
		invalidateOptionsMenu();
	}

	public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

	}

	public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

	}

	public class CollectionPagerAdapter extends FragmentPagerAdapter {

		final int NUM_ITEMS = 4; // number of tabs

		public CollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new TabFragment();
			Bundle args = new Bundle();
			args.putInt(TabFragment.ARG_OBJECT, i);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String tabLabel = null;
			switch (position) {
				case 0:
					tabLabel = getString(R.string.title_section_home);
					break;
				case 1:
					tabLabel = getString(R.string.title_section_visits);
					break;
				case 2:
					tabLabel = getString(R.string.title_section_recommendations);
					break;
				case 3:
					tabLabel = getString(R.string.title_section_diary);
					break;
			}
			return tabLabel;
		}
	}

	public static class TabFragment extends Fragment {
		public static final String ARG_OBJECT = "object";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			Bundle args = getArguments();
			int position = args.getInt(ARG_OBJECT);
			int tabLayout = 0;
			switch (position) {
				case 0:
					tabLayout = R.layout.tab1;
					break;
				case 1:
					tabLayout = R.layout.tab2;
					break;
				case 2:
					tabLayout = R.layout.tab3;
					break;
				case 3:
					tabLayout = R.layout.tab4;
					break;
			}

			View rootView = inflater.inflate(tabLayout, container, false);
			return rootView;
		}

	}

}