package com.example.ptquy.moviedb;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieInfoFragment extends Fragment{

	public MovieInfoFragment(){
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		Movie movie = (Movie) getArguments().getSerializable("movieInfo");
		View v = inflater.inflate(R.layout.fragment_overview, container, false);
		LinearLayout genreLayout = (LinearLayout) v.findViewById(R.id.genreLayout);
		TextView title = (TextView) v.findViewById(R.id.movieTitle);
		TextView duration = (TextView) v.findViewById(R.id.duration);
		TextView releaseDate = (TextView) v.findViewById(R.id.releaseDate);
		TextView description = (TextView) v.findViewById(R.id.description);

		title.setText(movie.getTitle());
		duration.setText("Duration: " + movie.getDuration());
		releaseDate.setText("Release date: " + movie.getReleaseDate());
		description.setText(movie.getDescription());

		genreLayout.setOrientation(LinearLayout.HORIZONTAL);
		ArrayList<String> genreList = movie.getGenre();
		int size = genreList.size();
		for(int i = 0; i < size; i++){
			Button btn = new Button(getContext());
			btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			final String s = genreList.get(i);
			btn.setText(s);
			btn.setTextSize(10);

			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), ReturnMovieListByGenre.class);
					intent.putExtra("genre", s.toLowerCase());
					startActivity(intent);
				}
			});
			genreLayout.addView(btn);
		}
		return v;
	}
}
