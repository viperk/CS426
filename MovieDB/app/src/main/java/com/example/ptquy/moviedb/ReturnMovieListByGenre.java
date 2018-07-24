package com.example.ptquy.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ReturnMovieListByGenre extends AppCompatActivity {

	private ArrayList<Movie> arrayList;
	private MovieListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_return_movie_list_by_genre);
		Intent intent = getIntent();
		String genre = intent.getStringExtra("genre");

		SQLite db = new SQLite(this, SQLite.DB_NAME, null, 1);

		arrayList = db.getMovieByGenre(genre);
		Log.d("ffff", arrayList.get(0).getDescription());
		createMovieList();
	}

	public void createMovieList(){
		adapter = new MovieListAdapter(arrayList, R.layout.movie_item_list, getApplicationContext());
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerview_list_genre);
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.addItemDecoration(dividerItemDecoration);
		recyclerView.setAdapter(adapter);
	}
}
