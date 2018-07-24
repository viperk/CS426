package com.example.ptquy.moviedb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MovieInfoActivity extends AppCompatActivity {

	private Bundle bundle;
	private Movie movie = null;
	ViewPager pager;
	TabLayout tabLayout;
	ViewGroup viewGroup;
	ImageView coverPhoto;
	ImageView thumbnail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_info);
		getDataFromIntent();
		createViewInfo();
	}

	public void createViewInfo(){
		viewGroup = (ViewGroup) findViewById(android.R.id.content);
		pager = (ViewPager) findViewById(R.id.view_pager);
		tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		coverPhoto = (ImageView) findViewById(R.id.coverphoto);
		thumbnail = (ImageView) findViewById(R.id.thumbnail_trailer);
		FragmentManager manager = getSupportFragmentManager();
		PagerAdapter adapter = new PagerAdapter(manager, movie);
		pager.setAdapter(adapter);
		tabLayout.setupWithViewPager(pager);
		pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setTabsFromPagerAdapter(adapter);

		String url_poster = movie.getPoster();
		String url_thumbnail = movie.getThumbnailTrailer();
		coverPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
		Picasso.get().load(url_poster).into(coverPhoto);
		thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
		Picasso.get().load(url_thumbnail).into(thumbnail);

		thumbnail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	public void getDataFromIntent(){
		bundle = this.getIntent().getExtras();
		if(bundle != null){
			movie = (Movie) bundle.getSerializable("value");
		}
	}

	public void trailerClicked(View view) {
		// Build the intent
		Uri videourl = Uri.parse(movie.getTrailerURL());
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, videourl);
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
		boolean isIntentSafe = activities.size() > 0;

		if (isIntentSafe) {
			startActivity(mapIntent);
		}
	}
}
