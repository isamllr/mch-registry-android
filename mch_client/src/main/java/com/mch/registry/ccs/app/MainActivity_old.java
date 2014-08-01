package com.mch.registry.ccs.app;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.Context;
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

import com.mch.registry.ccs.data.PregnancyDataHandler;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity_old extends FragmentActivity implements ActionBar.TabListener{
	CollectionPagerAdapter mCollectionPagerAdapter;
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_old);

		final ActionBar actionBar = getActionBar();

		PregnancyDataHandler pdh = new PregnancyDataHandler(this, null, null, 1);
		if (pdh.getPregnancy().get_isVerified() == 0) {
			actionBar.hide();
			Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
			startActivity(intent);
		} else {

			final Intent intent = getIntent();
			String msg = intent.getStringExtra(Constants.KEY_MESSAGE_TXT);
			if (msg != null) {
				final NotificationManager manager =
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				manager.cancel(Constants.NOTIFICATION_NR);
				String msgTxt = getString(R.string.msg_received, msg);
				Crouton.showText(this, msgTxt, Style.INFO);
			}

			mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
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

	private void showAboutDialog() {
		int[] resIds = new int[]{
				-1,
				-1,
				R.string.common_about_text,
				R.string.app_name,
				R.string.gcm_demo_copyright,
				R.string.repo_link};
	}

	public static class TabFragment extends Fragment {
		public static final String ARG_OBJECT = "object";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.fragment_recommendation_list, container, false);



			Bundle args = getArguments();
			int position = args.getInt(ARG_OBJECT);
			int tabLayout = 0;
			switch (position) {
				case 0:
					tabLayout = R.layout.fragment_recommendation_grid;
					break;
				case 1:
					tabLayout = R.layout.fragment_recommendation_grid;
					break;
				case 2:
					tabLayout = R.layout.fragment_recommendation_list;
					break;
				default:
					tabLayout = R.layout.fragment_recommendation_grid;
					break;
			}

			View rootView = inflater.inflate(tabLayout, container, false);
			return rootView;
		}

	}

	public class CollectionPagerAdapter extends FragmentPagerAdapter {

		final int NUM_ITEMS = 3;

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
			}
			return tabLabel;
		}
	}

}