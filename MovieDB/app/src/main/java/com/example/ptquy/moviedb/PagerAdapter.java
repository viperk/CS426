package com.example.ptquy.moviedb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	private Movie movie;
	public PagerAdapter(FragmentManager fm, Movie movie) {
		super(fm);
		this.movie = movie;
	}


	@Override
	public Fragment getItem(int position) {
		Fragment frag = null;
		Bundle bundle = null;
		switch(position){
			case 0:
				frag = new MovieInfoFragment();
				bundle = new Bundle();
				bundle.putSerializable("movieInfo", movie);
				frag.setArguments(bundle);
				break;
			case 1:
				frag = new CastingInfoFragment();
				bundle = new Bundle();
				bundle.putSerializable("movieInfo", movie);
				frag.setArguments(bundle);
				break;
		}
		return frag;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		String title = "";
		switch (position){
			case 0:
				title = "Overview";
				break;
			case 1:
				title = "Casting";
				break;
		}
		return title;
	}
}