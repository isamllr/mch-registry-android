package com.mch.registry.ccs.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mch.registry.ccs.app.navigation.NavDrawerItem;
import com.mch.registry.ccs.app.navigation.NavDrawerListAdapter;
import com.mch.registry.ccs.data.PregnancyDataHandler;
import com.mch.registry.ccs.data.RecommendationDataHandler;
import com.mch.registry.ccs.data.VisitDataHandler;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private String visitSize = "";
	private String recommendationSize = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PregnancyDataHandler pdh = new PregnancyDataHandler(this, null, null, 1);
		if (pdh.getPregnancy().get_isVerified() == 0) {
			Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
			startActivity(intent);
		}

		final Intent intent = getIntent();
		String msg = intent.getStringExtra(Constants.KEY_MESSAGE_TXT);
		if (msg != null) {
			final NotificationManager manager =
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(Constants.NOTIFICATION_NR);
			String msgTxt = getString(R.string.msg_received, msg);
			Crouton.showText(this, msgTxt, Style.INFO);
		}

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		//Handler hand = new Handler();
		//hand.post(new Runnable() {
		//	public void run() {
				getVisitSize();
				getRecommendationSize();
		//	}
		//});

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Visits
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, visitSize));
		// Recommendations
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, recommendationSize));
		// Notes
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// About
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	private void getRecommendationSize() {
		RecommendationDataHandler rdh = new RecommendationDataHandler(getApplication(), null, null, 1);
		recommendationSize = Integer.toString(rdh.getAllRecommendations().size());
	}

	public void getVisitSize() {
		VisitDataHandler vdh = new VisitDataHandler(getApplication(), null, null, 1);
		visitSize = Integer.toString(vdh.getAllVisits().size());
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int id = item.getItemId();
		if (id == R.id.action_mobilephonenumber) {
			Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_app_on) {
			showAppOnOffDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showAppOnOffDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.pick_preference_app_sms)
				.setItems(R.array.action_array_preference_app_sms, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
							case 0:
								mobileAppOn(true);
								break;
							case 1:
								mobileAppOn(false);
								break;
						}
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void mobileAppOn(boolean status) {
		Intent msgIntent = new Intent(this, GcmIntentService.class);
		msgIntent.setAction(Constants.ACTION_ECHO);
		String msg = null;
		if(status){
			msg="_MobileAppOn";
			Crouton.showText(this, getString(R.string.not_by_app), Style.INFO);
		}else{
			msg="_MobileAppOff";
			Crouton.showText(this, getString(R.string.not_by_sms), Style.INFO);
		}

		msgIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
		this.startService(msgIntent);
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	private void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new VisitsFragment();
			break;
		case 2:
			fragment = new RecommendationsFragment();
			break;
		case 3:
			fragment = new NotesFragment();
			break;
		case 4:
			fragment = new AboutFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
