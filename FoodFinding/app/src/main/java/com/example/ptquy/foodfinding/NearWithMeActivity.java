package com.example.ptquy.foodfinding;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NearWithMeActivity extends AppCompatActivity {

	private String PLACE_SEARCH_API = "https://maps.googleapis.com/maps/api/place/";
	private String API_KEY = "AIzaSyB4cSAEQi0uINGwRHokQj7qJhQyG0dE0-g";
	private String NEARBY = "nearbysearch/json?location=";
	private String TEXTSEARCH = "textsearch/json?query=";
	private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	private Boolean mLocationPermissionGranted = false;

	private LocationManager locationManager;
	private LocationListener locationListener;

	protected GeoDataClient mGeoDataClient;
	protected PlaceDetectionClient mPlaceDetectionClient;

	private ArrayList<DataFood> listFood;
	private FoodAdapter foodAdapter;
	private DataFood dataFood;
	private RecyclerView recyclerView;

	private FirebaseDatabase database;
	private FirebaseStorage storage;
	private DatabaseReference myRef;

	private Location currentLocation;

	private String radius = "1500";
	private String type = "restaurant";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_with_me);

		mGeoDataClient = Places.getGeoDataClient(this, null);

		mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				// Location la vi tri hien tai
				// lam gi trong nay cung dc
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

		init();
		createView();
		getLocationPermission();
		collectLocationsFrom(currentLocation);
	}

	public void rangeButtonClicked(View view) {
		int id = view.getId();
		if(id == R.id.twokm){
			radius = "2000";
		}
		else if(id == R.id.fourkm){
			radius = "4000";
		}
		else{
			radius = "6000";
		}
		if(listFood.size() != 0){
			listFood.clear();
			foodAdapter.notifyDataSetChanged();
		}
		getLocationPermission();
		collectLocationsFrom(currentLocation);
	}

	public void typeButtonClicked(View view) {
		int id = view.getId();
		if(id == R.id.restaurant){
			type = "restaurant";
		}
		else{
			type = "movie_theater";
		}
		if(listFood.size() != 0){
			listFood.clear();
			foodAdapter.notifyDataSetChanged();
		}
		getLocationPermission();
		collectLocationsFrom(currentLocation);
	}

	public class ParseJSON extends AsyncTask<String , Void , ArrayList<DataFood>> {

		@Override
		protected ArrayList<DataFood> doInBackground(String... params) {

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
				JSONArray results = jsonObject.getJSONArray("results");

				for (int i = 0; i < results.length(); i++) {
					JSONObject resultsChild = results.getJSONObject(i);

					final String placeId = resultsChild.getString("place_id");
					JSONObject opening_hours = null;
					Boolean open_now = false;

					if(resultsChild.has("opening_hours")) {
						opening_hours = resultsChild.getJSONObject("opening_hours");
						open_now = opening_hours.getBoolean("open_now");

					}

					final Boolean final_open_now = open_now;
					mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
						@Override
						public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
							if (task.isSuccessful()) {
								PlaceBufferResponse places = task.getResult();
								Place tmpPlace = places.get(0);
								Place myPlace = tmpPlace.freeze();
								places.release();

								String RestaurantName = String.valueOf(myPlace.getName());
								String phone = String.valueOf(myPlace.getPhoneNumber());
								String DetailAddress = String.valueOf(myPlace.getAddress());
								String webstiteURL = String.valueOf(myPlace.getWebsiteUri());

								int Price = myPlace.getPriceLevel();
								float rating = myPlace.getRating();

								dataFood = new DataFood(placeId, RestaurantName, rating, placeId, DetailAddress
								, phone, webstiteURL, Price, final_open_now);
								Log.e("placID", placeId);
								Log.e("RestaurantName", RestaurantName);
								Log.e("rating", String.valueOf(rating));
								Log.e("AvtID", placeId);
								Log.e("DetailAddress", DetailAddress);
								Log.e("phone", phone);
								Log.e("website", webstiteURL);
								Log.e("price", String.valueOf(Price));
								Log.e("open_now", String.valueOf(final_open_now));
								listFood.add(dataFood);
								foodAdapter.notifyDataSetChanged();
								myRef.child(dataFood.getPlaceId()).setValue(dataFood);//myRef: firebase
								myRef.addValueEventListener(new ValueEventListener() {
									@Override
									public void onDataChange(DataSnapshot dataSnapshot) {//firebase
										//Toast.makeText(NearWithMeActivity.this, "Added new food", Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onCancelled(DatabaseError databaseError) {
										//Toast.makeText(NearWithMeActivity.this, "Cannot add food, please try again later", Toast.LENGTH_SHORT).show();
									}
								});
								Log.e("ddddd", "Place not found.");

							} else {
								Log.e("qwer", "Place not found.");
							}
						}
					});
				}
				Log.d("jjjj", "yuewyfudys");
				return listFood;
			} catch (MalformedURLException me) {
				Log.d("123", "Malformed");
			} catch (IOException ioe) {
				Log.d("456", "IOE");
			} catch (JSONException je) {
				Log.d("789", "JSONE");
			} catch (Exception e){

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(ArrayList<DataFood> s) {
			super.onPostExecute(s);
		}
	}

	public void init(){
		database = FirebaseDatabase.getInstance();
		storage = FirebaseStorage.getInstance();
		myRef = database.getReference("Cinema");
		myRef.keepSynced(true);
		dataFood = new DataFood();
	}

	public void makeURLNearBY(){

	}

	public void collectLocationsFrom(Location location){
		if(location != null) {
			String loc = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
			String urlJSON = PLACE_SEARCH_API + NEARBY + loc + "&radius="
			+ radius+"&type=" + type+ "&key=" + API_KEY;
			new ParseJSON().execute(urlJSON);
		}
		else{
			Toast.makeText(NearWithMeActivity.this, "Make sure to turn on GPS", Toast.LENGTH_SHORT).show();
		}
	}

	public void createView(){
		listFood = new ArrayList<>();
		foodAdapter = new FoodAdapter(listFood, getApplicationContext(), storage);
		recyclerView = (RecyclerView) findViewById(R.id.AllFood_recyler_view);
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
		recyclerView.setLayoutManager(linearLayoutManager);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);
		recyclerView.setAdapter(foodAdapter);


	}

	public void getLocationPermission(){
		if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
				android.Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			mLocationPermissionGranted = true;
			//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(currentLocation == null)
				currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else {
			ActivityCompat.requestPermissions(this,
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
					PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mLocationPermissionGranted = false;
		Log.d("das", "dqueyueqy");
		switch(requestCode) {
			case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
						mLocationPermissionGranted = true;
						//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
						currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if(currentLocation == null)
							currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}
			}
		}
	}
}
