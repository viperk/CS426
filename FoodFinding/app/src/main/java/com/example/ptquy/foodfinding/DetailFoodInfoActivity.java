package com.example.ptquy.foodfinding;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailFoodInfoActivity extends AppCompatActivity implements OnMapReadyCallback{

	private DataFood dataFood;
	private Bundle bundle;

	protected GeoDataClient mGeoDataClient;
	private TextView phone;
	private TextView price;
	private TextView Description;
	private TextView Address;
	private TextView openHours;
	private ImageView coverphoto;
	private ImageView[] photoimg = new ImageView[3];
	private ImageView phoneicon;
	private ImageView phoneiconDialog;
	private ImageView smsiconDialog;
	private ImageView btnLove;
	private RatingBar ratingBar;
	private CollapsingToolbarLayout collapsingToolbarLayout;
	private LatLng position = null;
	private PlacePhotoMetadataBuffer tmpPlacePhotoMetadata;
	private FirebaseDatabase database;
	private DatabaseReference myRef;
	private FirebaseAuth mAuth;
	private FirebaseUser currentUser;
	private Dialog dialog;
	private placeIDObject tmp;
	GoogleMap mMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_food_info);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle("ffsf");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		mGeoDataClient = Places.getGeoDataClient(this, null);
		collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

		init();
		InitDialogMediaInfo();
		getDataFromIntent();

		btnLove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String userID = currentUser.getUid();
				tmp = new placeIDObject(dataFood.getPlaceId());
				myRef.child(userID).child(dataFood.getPlaceId()).setValue(dataFood);//myRef: firebase
				myRef.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {//firebase
						Toast.makeText(DetailFoodInfoActivity.this, "Added to favorite", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						Toast.makeText(DetailFoodInfoActivity.this, "Cannot add, please try again later", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void moveCameraToThisLocation() {
		Geocoder geocoder = new Geocoder(this);
		List<Address> addressList;
		try{
			addressList = geocoder.getFromLocationName(dataFood.getDetailAddress(), 3);
			if(addressList == null)
				return;
			Address location = addressList.get(0);
			position = new LatLng(location.getLatitude(), location.getLongitude());
			mMap.addMarker(new MarkerOptions().title(dataFood.getRestaurantName())
					.position(position));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
		}catch (IOException ioe){
			Log.d("geocoder fail", "ioesdsdsd");
		}
	}

	public void getDataFromIntent(){
		bundle = this.getIntent().getExtras();
		if(bundle != null){
			dataFood = (DataFood) bundle.getSerializable("value");
			phone.setText(dataFood.getPhone());
			price.setText(String.valueOf(dataFood.getPriceRange()));
			Description.setText(dataFood.getDescription());
			Address.setText(dataFood.getDetailAddress());
			if(dataFood.getOpen()) {
				openHours.setText("open now");
				openHours.setTextColor(Color.GREEN);
			}
			collapsingToolbarLayout.setTitle(dataFood.getRestaurantName());
			ratingBar.setRating(dataFood.getRating());
			getPhotos();
		}
	}

	public void init(){
		phone = (TextView) findViewById(R.id.Phone);
		price = (TextView) findViewById(R.id.Price);
		Description = (TextView) findViewById(R.id.Description);
		openHours = (TextView) findViewById(R.id.openhours);
		photoimg[0] = (ImageView) findViewById(R.id.PlacePhoto1);
		photoimg[1] = (ImageView) findViewById(R.id.PlacePhoto2);
		photoimg[2] = (ImageView) findViewById(R.id.PlacePhoto3);
		coverphoto = (ImageView) findViewById(R.id.coverphoto);
		ratingBar = (RatingBar) findViewById(R.id.RatingInCover);

		phoneicon = (ImageView) findViewById(R.id.phoneicon);
		Address = (TextView) findViewById(R.id.address);
		btnLove = (ImageView) findViewById(R.id.buttonLove);
		database = FirebaseDatabase.getInstance();
		myRef = database.getReference("UserFavorite");
		mAuth = FirebaseAuth.getInstance();
		currentUser = mAuth.getCurrentUser();

	}

	public void InitDialogMediaInfo(){

		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_media);
		dialog.setCanceledOnTouchOutside(true);

		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.copyFrom(dialog.getWindow().getAttributes());
		layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(layoutParams);

		phoneiconDialog = (ImageView) dialog.findViewById(R.id.phoniconDialog);
		smsiconDialog = (ImageView) dialog.findViewById(R.id.smsiconDialog);


	}

	public void showDialog(){
		phoneiconDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri number = Uri.parse("tel:" + dataFood.getPhone());

				Intent callIntent = new Intent(Intent.ACTION_CALL, number);
				if (ActivityCompat.checkSelfPermission(DetailFoodInfoActivity.this, Manifest.permission.CALL_PHONE)
						!= PackageManager.PERMISSION_GRANTED) {
					return;
				}
				startActivity(callIntent);
			}
		});

		smsiconDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("sms_body", "Hi");
				smsIntent.putExtra("address", dataFood.getPhone());
				startActivity(smsIntent);
			}
		});
		dialog.show();
	}

	private void getPhotos() {
		final String placeId = dataFood.getPlaceId();
		final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
		photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
			@Override
			public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
				// Get the list of photos.
				PlacePhotoMetadataResponse photos = task.getResult();
				// Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
				PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
				tmpPlacePhotoMetadata = photoMetadataBuffer;
				// Get the first photo in the list.
				for(int i = 0; i < 4; i++) {
					PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
					// Get the attribution text.
					CharSequence attribution = photoMetadata.getAttributions();
					// Get a full-size bitmap for the photo.
					Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
					final int index = i;
					photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
						@Override
						public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
							PlacePhotoResponse photo = task.getResult();
							Bitmap bitmap = photo.getBitmap();
							if(index == 0)
								coverphoto.setImageBitmap(bitmap);
							else
								photoimg[index-1].setImageBitmap(bitmap);
						}
					});
				}
			}
		});
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		moveCameraToThisLocation();
	}

	public void photoClicked(View view) {
		String place_ID = dataFood.getPlaceId();
		Intent intent = new Intent(DetailFoodInfoActivity.this, ImageSlideViewActivity.class);
		intent.putExtra("placeID", place_ID);
		startActivity(intent);
	}

	public void MapsClicked(View view) {
		Intent intent = new Intent(DetailFoodInfoActivity.this, MapsActivity.class);
		intent.putExtra("Address", dataFood.getDetailAddress());
		intent.putExtra("Name", dataFood.getRestaurantName());
		intent.putExtra("Lat", position.latitude);
		intent.putExtra("Lng", position.longitude);
		startActivity(intent);
	}

	public void MakeMedia(View view) {
		showDialog();
	}
}
