package com.example.ptquy.foodfinding;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	protected GeoDataClient mGeoDataClient;
	protected PlaceDetectionClient mPlaceDetectionClient;
	int PLACE_PICKER_REQUEST = 1;
	String TAG = "eeee";
	private GoogleMap mMap;
	private static String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
	private static String API_KEY = "AIzaSyD8wO2cdaTXXMmxYPC1BegR298y2O9XkuM";
	private String urlOrigin;
	private String urlDestination;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Location currentLocation;
	private LatLng desLocation;
	private String resName;
	private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		Intent intent = getIntent();
		urlDestination = intent.getStringExtra("Address");

		desLocation = new LatLng(intent.getDoubleExtra("Lat", 1), intent.getDoubleExtra("Lng", 1));
		resName = intent.getStringExtra("Name");

		mGeoDataClient = Places.getGeoDataClient(this, null);

		mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				//moveCameraToCurrentLocation(location);
				Log.d("Address", urlDestination);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.addMarker(new MarkerOptions().title(resName)
		                                  .position(desLocation));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(desLocation, 15));
	}

	public void moveCameraToCurrentLocation(Location location){
		LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
		mMap.addMarker(new MarkerOptions().position(position)
				.title("You're here"));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
		urlOrigin = String.valueOf(position.latitude)+","+String.valueOf(position.longitude);
	}

	public void getLocationPermission(){
		if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
				android.Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			currentLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
			if(currentLocation == null){
				currentLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
			}
		} else {
			ActivityCompat.requestPermissions(this,
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
					PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		Log.d("das", "dqueyueqy");
		switch(requestCode) {
			case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
						currentLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
						if(currentLocation == null){
							currentLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
						}
					}
				}
			}
		}
	}

	public String MakeURLDirectionAPI(){

		try {
			urlDestination = URLEncoder.encode(urlDestination, "utf-8");
		} catch (UnsupportedEncodingException ue) {

		}
		return (DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + API_KEY);
	}

	public void DirectionButtonClicked(View view) {
		//mMap.clear();
		getLocationPermission();
		moveCameraToCurrentLocation(currentLocation);
		new ParseJSON().execute(MakeURLDirectionAPI());
	}

	public class ParseJSON extends AsyncTask<String , Void , ArrayList<LatLng>> {

		private ArrayList<LatLng> arrayList = new ArrayList<>();

		@Override
		protected ArrayList<LatLng> doInBackground(String... params) {

			String link=params[0];

			try {
				URL url=new URL(link);
				HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
				InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
				BufferedReader br= new BufferedReader(inputStreamReader);
				StringBuilder sb=new StringBuilder();
				String line=br.readLine();

				while (line!=null)
				{
					sb.append(line);
					line=br.readLine();
				}

				JSONObject jsonObject = new JSONObject(sb.toString());
				JSONArray route = jsonObject.getJSONArray("routes");

				for(int i = 0; i < route.length(); i++){
					JSONObject routeChild = route.getJSONObject(i);
					JSONArray legs = routeChild.getJSONArray("legs");

					for(int j = 0; j < legs.length(); j++){
						JSONObject legChild = legs.getJSONObject(j);
						Integer distance = legChild.getJSONObject("distance")
								.getInt("value");
						Integer duration = legChild.getJSONObject("duration")
								.getInt("value");
						String end_address = legChild.getString("end_address");
						String start_address = legChild.getString("start_address");

						double startlat = legChild.getJSONObject("start_location").getDouble("lat");
						double startlng = legChild.getJSONObject("start_location").getDouble("lng");
						double endlat = legChild.getJSONObject("end_location").getDouble("lat");
						double endlng = legChild.getJSONObject("end_location").getDouble("lng");

						JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");
						for(int k = 0; k < steps.length(); k++){
							JSONObject stepChild = steps.getJSONObject(k);
							decodePolyLine(stepChild.getJSONObject("polyline").getString("points"));
						}
					}
				}
				return arrayList;
			} catch (MalformedURLException me) {
				Log.d("123", "Malformed");
			}catch (IOException ioe){
				Log.d("456", "IOE");
			}catch (JSONException je){
				Log.d("789", "JSONE");
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(ArrayList<LatLng> s) {
			super.onPostExecute(s);

			Log.d("quyen", "doInBackground: weqeqw");

			if(s == null || s.size() == 0){
				Log.d("tttt", "nullllll");
				Toast.makeText(MapsActivity.this, "Can not find direction", Toast.LENGTH_SHORT).show();
				return;
			}


			PolylineOptions polylineOptions = new PolylineOptions()
					.color(Color.rgb(100,250,35))
					.width(10);

			int i;

			for(i = 0; i < s.size(); i++){
				polylineOptions.clickable(true)
						.add(s.get(i));
			}

			Polyline polyline = mMap.addPolyline(polylineOptions);
			LatLng LatLngOrigin = s.get(0);
			LatLng LatLngDestination = s.get(s.size() - 1);
			mMap.addMarker(new MarkerOptions().title("Origin")
					.position(LatLngOrigin));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLngOrigin,18));

			mMap.addMarker(new MarkerOptions().title("Destination")
					.position(LatLngDestination));

		}

		public void decodePolyLine(final String poly) {
			int len = poly.length();
			int index = 0;

			int lat = 0;
			int lng = 0;

			while (index < len) {
				int b;
				int shift = 0;
				int result = 0;
				do {
					b = poly.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lat += dlat;

				shift = 0;
				result = 0;
				do {
					b = poly.charAt(index++) - 63;
					result |= (b & 0x1f) << shift;
					shift += 5;
				} while (b >= 0x20);
				int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
				lng += dlng;

				arrayList.add(new LatLng(
						lat / 100000d, lng / 100000d
				));
			}
		}
	}
}
