package com.example.ptquy.moviedb;

import android.provider.MediaStore;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Movie implements Serializable{

	public static final String TABLE_NAME = "Movies";

	public static final String ID = "ID";
	public static final String Title = "Title";
	public static final String Poster = "Poster";
	public static final String Thumbnail = "Thumbnail";
	public static final String Rating = "Rating";
	public static final String Description = "Description";
	public static final String ReleaseDate = "ReleaseDate";
	public static final String Duration = "Duration";
	public static final String Kind = "Kind";
	public static final String Trailer = "Trailer";

	private int id;
	private String title;
	private String poster;
	private String thumbnailTrailer;
	private float rating;
	private String description;
	private String releaseDate;
	private int duration;
	private ArrayList<String> genre;
	private String kind;
	private String trailerURL;

	public Movie(int id, String title, String poster, String thumbnailTrailer,
				 float rating, String description,
				 String releaseDate, int duration, int kind, String trailerURL) {
		this.id = id;
		this.title = title;
		this.poster = poster;
		this.thumbnailTrailer = thumbnailTrailer;
		this.rating = rating;
		this.description = description;
		this.releaseDate = releaseDate;
		this.duration = duration;
		this.trailerURL = trailerURL;
		if(kind == 1){
			this.kind = "Phim le";
		}
		else{
			this.kind = "Phim bo";
		}
		genre = new ArrayList<>();
	}

	public Movie(){

	}

	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
											  + ID + " INT NOT NULL PRIMARY KEY,"
											  + Title + " NVARCHAR(100) NOT NULL,"
											  + Poster + " NVARCHAR(200),"
											  + Thumbnail + " NVARCHAR(200),"
											  + Rating + " FLOAT CHECK(Rating >= 0 AND Rating <= 10),"
											  + Description + " NVARCHAR(1000),"
										      + ReleaseDate + " DATE,"
											  + Duration + " INT,"
										      + Kind + " INT,"
											  + Trailer + " VARCHAR(200)" + ");";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getThumbnailTrailer() {
		return thumbnailTrailer;
	}

	public void setThumbnailTrailer(String thumbnailTrailer) {
		this.thumbnailTrailer = thumbnailTrailer;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void addGenre(String s){
		this.genre.add(s);
	}

	public ArrayList<String> getGenre(){
		return this.genre;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getTrailerURL() {
		return trailerURL;
	}

	public void setTrailerURL(String trailerURL) {
		this.trailerURL = trailerURL;
	}
}
