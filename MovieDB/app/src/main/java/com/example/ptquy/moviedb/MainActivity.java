package com.example.ptquy.moviedb;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private FloatingActionButton actionButton;
	private SubActionButton.Builder itemBuilder;
	private SubActionButton button;
	private SubActionButton button1;
	private SubActionButton button2;
	private SubActionButton button3;
	private FloatingActionMenu actionMenu;
	private ArrayList<Movie> arrayList;
	private MovieAdapter adapter;
	private BottomNavigationView bottomNav;
	SQLite db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createMenuView();
		createDatabase();
		insertData();
		createMovieList();

		bottomNav = (BottomNavigationView) findViewById(R.id.Bottom_NavBar);
		bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()){
					case R.id.action_list_all:
						Intent intent = new Intent(MainActivity.this, AllListMovieActivity.class);
						startActivity(intent);
						return true;
				}
				return false;
			}
		});
		/*db.QueryData("DROP TABLE IF EXISTS " + Movie.TABLE_NAME);
		this.deleteDatabase(SQLite.DB_NAME);*/
	}

	public void createDatabase(){
		db = new SQLite(this, SQLite.DB_NAME, null, 1);
	}

	public void createMenuView(){
		ImageView icon = new ImageView(this); // Create an icon
		icon.setImageResource(R.drawable.menuicon);

		actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();
		itemBuilder = new SubActionButton.Builder(this);
		// repeat many times:
		ImageView itemIcon = new ImageView(this);
		itemIcon.setImageResource(R.drawable.topicon);
		button = itemBuilder.setContentView(itemIcon).build();

		ImageView itemIcon1 = new ImageView(this);
		itemIcon.setImageResource(R.drawable.topicon);
		button1 = itemBuilder.setContentView(itemIcon1).build();

		ImageView itemIcon2 = new ImageView(this);
		itemIcon.setImageResource(R.drawable.topicon);
		button2 = itemBuilder.setContentView(itemIcon2).build();

		ImageView itemIcon3 = new ImageView(this);
		itemIcon.setImageResource(R.drawable.topicon);
		button3 = itemBuilder.setContentView(itemIcon3).build();

		actionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(button)
				.addSubActionView(button1)
				.addSubActionView(button2)
				.addSubActionView(button3)
				.attachTo(actionButton)
				.build();

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AllListMovieActivity.class);
				startActivity(intent);
			}
		});
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "button1", Toast.LENGTH_SHORT).show();
			}
		});
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	public void createMovieList(){
		adapter = new MovieAdapter(arrayList, R.layout.movie_item_launch, getApplicationContext());
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerview_launch);
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);
	}

	public void insertData(){
		db.insertMovie(123, "Incredibles 2", "https://www.cosmicbooknews.com/sites/default/files/incredibles-2-trailer-poster.jpe",
				"https://img.youtube.com/vi/i5qOzqD9Rms/default.jpg",
				8.2f, "Bob Parr (Mr. Incredible) is left to care for the kids while Helen (Elastigirl) is out saving the world.",
				"2018-06-15",	118, 1, "https://www.youtube.com/watch?v=i5qOzqD9Rms");

		db.insertMovie(332,"Jonh Wick (2014)",	"https://upload.wikimedia.org/wikipedia/vi/a/a8/Johnwick_filmreview_poster200.jpg",
				"https://img.youtube.com/vi/2AUmvWm5ZDQ/default.jpg",7.3f, "An ex-hitman comes out of retirement to track down the gangsters that killed his dog and took everything from him.",
				"2014-10-24", 101, 1, "https://www.youtube.com/watch?v=2AUmvWm5ZDQ");

		db.insertMovie(456,"Ant-Man and the Wasp",	"https://m.media-amazon.com/images/M/MV5BYjcyYTk0N2YtMzc4ZC00Y2E0LWFkNDgtNjE1MzZmMGE1YjY1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SY1000_CR0,0,675,1000_AL_.jpg",
				"https://img.youtube.com/vi/UUkn-enk2RU/default.jpg",	7.6f,	"As Scott Lang balances being both a Super Hero and a father, Hope van Dyne and Dr. Hank Pym present an urgent new mission that finds the Ant-Man fighting alongside The Wasp to uncover secrets from their past.",
				"2018-06-07",	118, 1, "https://www.youtube.com/watch?v=8_rTIAOohas");

		db.insertMovie(488, "Love, Rosie (2014)","https://d32qys9a6wm9no.cloudfront.net/images/movies/poster/ce/ce5d75028d92047a9ec617acb9c34ce6_500x735.jpg",
			   "https://img.youtube.com/vi/SqSE6Kzuht0/default.jpg",7.2f,"Rosie and Alex have been best friends since they were 5, so they couldn't possibly be right for one another...or could they? When it comes to love, life and making the right choices, these two are their own worst enemies.",
				"2014-10-24",	102, 1, "https://www.youtube.com/watch?v=SqSE6Kzuht0");

		db.insertMovie(789,	"The Conjuring 2 (2016)","https://m.media-amazon.com/images/M/MV5BZjU5OWVlN2EtODNlYy00MjhhLWI0MDUtMTA3MmQ5MGMwYTZmXkEyXkFqcGdeQXVyNjE5MTM4MzY@._V1_SY1000_CR0,0,674,1000_AL_.jpg	",
				"https://img.youtube.com/vi/VFsmuRPClr4/default.jpg",7.4f ,"Ed and Lorraine Warren travel to North London to help a single mother raising 4 children alone in a house plagued by a supernatural spirit.",
				"2016-06-10",	134, 1, "https://www.youtube.com/watch?v=VFsmuRPClr4");

		db.insertGenreForMovie(10,123);
		db.insertGenreForMovie(2,123);
		db.insertGenreForMovie(3,123);
		db.insertGenreForMovie(7,123);
		db.insertGenreForMovie(2,332);
		db.insertGenreForMovie(6,332);
		db.insertGenreForMovie(12,332);
		db.insertGenreForMovie(2,456);
		db.insertGenreForMovie(7,456);
		db.insertGenreForMovie(9,456);
		db.insertGenreForMovie(3,488);
		db.insertGenreForMovie(4,488);
		db.insertGenreForMovie(1,789);
		db.insertGenreForMovie(6,789);
		db.insertGenreForMovie(8,789);
		arrayList = db.getAllMovie();
	}
}
