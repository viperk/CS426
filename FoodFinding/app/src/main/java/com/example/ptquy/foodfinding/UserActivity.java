package com.example.ptquy.foodfinding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class UserActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private TextView userEmail;
	private String userID;
	final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
	private String API_KEY = "AIzaSyB4cSAEQi0uINGwRHokQj7qJhQyG0dE0-g";
	Boolean open_now = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
				try {
					Intent intent =
							new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
									.build(UserActivity.this);
					startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
				} catch (GooglePlayServicesRepairableException e) {
					// TODO: Handle the error.
				} catch (GooglePlayServicesNotAvailableException e) {
					// TODO: Handle the error.
				}
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		View header=navigationView.getHeaderView(0);
		Intent intent = this.getIntent();
		userEmail = (TextView) header.findViewById(R.id.emailDisplay);
		String email = intent.getStringExtra("Email");
		userID = intent.getStringExtra("userID");
		if(email != null)
			userEmail.setText(email);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place myPlace = PlaceAutocomplete.getPlace(this, data);

				String placeId = myPlace.getId();
				new ParseJSON().execute("https://maps.googleapis.com/maps/api/place/details/json?" +
						"placeid=" + placeId + "&fields=opening_hours&key=" + API_KEY);

				String RestaurantName = String.valueOf(myPlace.getName());
				String phone = String.valueOf(myPlace.getPhoneNumber());
				String DetailAddress = String.valueOf(myPlace.getAddress());
				String webstiteURL = String.valueOf(myPlace.getWebsiteUri());

				int Price = myPlace.getPriceLevel();
				float rating = myPlace.getRating();

				DataFood dataFood = new DataFood(placeId, RestaurantName, rating, placeId, DetailAddress
						, phone, webstiteURL, Price, open_now);

				Intent intent = new Intent(UserActivity.this, DetailFoodInfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				Bundle bundle = new Bundle();
				bundle.putSerializable("value", dataFood);
				intent.putExtras(bundle);
				this.startActivity(intent);

			} else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
				Status status = PlaceAutocomplete.getStatus(this, data);
				// TODO: Handle the error.

			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_favorite) {
			// Handle the camera action
			Intent intent = new Intent(UserActivity.this, FavoriteListActivity.class);
			intent.putExtra("userID", userID);
			startActivity(intent);
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void buttonMainMenuClicked(View view) {
		int id = view.getId();
		if(id == R.id.NewHotFoodButton){

		}

		else if(id == R.id.NearWithYouButton){
			Intent intent = new Intent(UserActivity.this, NearWithMeActivity.class);
			startActivity(intent);
		}

		else if(id == R.id.AllFoodList){
			Intent intent = new Intent(UserActivity.this, AllFoodListActivity.class);
			startActivity(intent);
		}
	}

	public class ParseJSON extends AsyncTask<String , Void , Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {

			String link = params[0];

			try {
				Log.e("tryyyy", "rrrrrrrrrrrrr");
				URL url = new URL(link);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				BufferedReader br = new BufferedReader(inputStreamReader);
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					line = br.readLine();
				}

				JSONObject jsonObject = new JSONObject(sb.toString());
				JSONObject result = jsonObject.getJSONObject("result");
				JSONObject opening_hours = result.getJSONObject("opening_hours");
				if (opening_hours.has("open_now")) {
					open_now = opening_hours.getBoolean("open_now");
				}
				else open_now = false;
				return open_now;
			} catch (MalformedURLException me) {
				Log.d("123", "Malformed");
			} catch (IOException ioe) {
				Log.d("456", "IOE");
			} catch (JSONException je) {
				Log.d("789", "JSONE");
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean s) {
			super.onPostExecute(s);
			open_now = s;
		}
	}
}
