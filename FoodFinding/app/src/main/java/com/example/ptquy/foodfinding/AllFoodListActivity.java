package com.example.ptquy.foodfinding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AllFoodListActivity extends AppCompatActivity {

	private ArrayList<DataFood> listFood;
	private FoodAdapter foodAdapter;
	private FirebaseDatabase database;
	private FirebaseStorage storage;
	private DatabaseReference myRef;
	private DataFood dataFood;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_food_list);

		getSupportActionBar().setTitle("All restaurants");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		init();
		createView();

		ChildEventListener childEventListener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				dataFood = dataSnapshot.getValue(DataFood.class);
				listFood.add(dataFood);
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
		myRef.addChildEventListener(childEventListener);
	}

	public void createView(){
		listFood = new ArrayList<>();
		foodAdapter = new FoodAdapter(listFood, getApplicationContext(), storage);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.AllFood_recyler_view);
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
		recyclerView.setLayoutManager(linearLayoutManager);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);
		recyclerView.setAdapter(foodAdapter);

	}

	public void init(){
		database = FirebaseDatabase.getInstance();
		storage = FirebaseStorage.getInstance();
		myRef = database.getReference("Food");
		myRef.keepSynced(true);
		dataFood = new DataFood();
	}
}
