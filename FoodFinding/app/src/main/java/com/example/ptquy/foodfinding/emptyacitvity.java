package com.example.ptquy.foodfinding;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class emptyacitvity extends AppCompatActivity {

	protected GeoDataClient mGeoDataClient;
	protected PlaceDetectionClient mPlaceDetectionClient;
	int PLACE_PICKER_REQUEST = 1;
	String TAG = "eeee";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emptyacitvity);

		mGeoDataClient = Places.getGeoDataClient(this, null);

		mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

		PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

		try {
			startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
		}catch(GooglePlayServicesRepairableException gpe){

		}catch (GooglePlayServicesNotAvailableException gpeab){

		}

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
						!= PackageManager.PERMISSION_GRANTED) {
			Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
			placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
				@Override
				public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
					PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
					for (PlaceLikelihood placeLikelihood : likelyPlaces) {

						Log.i(TAG, String.format("Place '%s' has likelihood: %g",
								placeLikelihood.getPlace().getName(),
								placeLikelihood.getLikelihood()));
					}
					likelyPlaces.release();
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				Place place = PlacePicker.getPlace(data, this);
				String toastMsg = String.format("Place: %s", place.getName());
				Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
			}
		}
	}

}
