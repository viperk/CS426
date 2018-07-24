package com.example.ptquy.foodfinding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FavoriteListActivity extends AppCompatActivity {

	private final ArrayList<DataFood> listFood = new ArrayList<>();
	private final ArrayList<DataFood> favoritelistFood = new ArrayList<>();
	private final ArrayList<String> userFavoritePlaceID = new ArrayList<>();
	private FoodAdapter foodAdapter;
	private FirebaseDatabase database;
	private FirebaseStorage storage;
	private DatabaseReference myRef;
	private DatabaseReference myRef2;
	private DataFood dataFood;
	private DataFood dataFood2;
	private placeIDObject placeID2;
	private String userID;
	private placeIDObject placeID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_list);

		Intent intent = getIntent();
		userID = intent.getStringExtra("userID");

		init();
		createView();

		/*ChildEventListener childEventListener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				dataFood = dataSnapshot.getValue(DataFood.class);
				Log.d("ssss", dataFood.getPlaceId());
				listFood.add(dataFood);

			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		};
		myRef.addChildEventListener(childEventListener);*/

		ChildEventListener childEventListener2 = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				dataFood = dataSnapshot.getValue(DataFood.class);
				favoritelistFood.add(dataFood);
				foodAdapter.notifyDataSetChanged();
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		};
		myRef2.addChildEventListener(childEventListener2);
		/*int size = listFood.size();
		for(int i = 0; i < size; i++){
			if(userFavoritePlaceID.contains(listFood.get(i).getPlaceId())){
				favoritelistFood.add(listFood.get(i));
				foodAdapter.notifyDataSetChanged();
			}
		}
		if(listFood.size() == 0)
			Toast.makeText(FavoriteListActivity.this, "No places", Toast.LENGTH_SHORT).show();*/

	}

	public void createView(){
		foodAdapter = new FoodAdapter(favoritelistFood, getApplicationContext(), storage);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.AllFood_recyler_view);
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
		recyclerView.setLayoutManager(linearLayoutManager);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);
		recyclerView.setAdapter(foodAdapter);
	}

	public void getFavoritelistFood(){



	}

	public void init(){
		database = FirebaseDatabase.getInstance();
		storage = FirebaseStorage.getInstance();
		myRef = database.getReference("Food");
		myRef2 = database.getReference("UserFavorite").child(userID);
		myRef.keepSynced(true);
		dataFood = new DataFood();
		placeID = new placeIDObject();
	}
}
