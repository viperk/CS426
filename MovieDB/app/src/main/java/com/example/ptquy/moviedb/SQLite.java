package com.example.ptquy.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class SQLite extends SQLiteOpenHelper {

	private Context context;
	public static final String DB_NAME = "Movie_db";
	private static final String CREATE_GENRE = "CREATE TABLE Genre "+"( GenreID INT NOT NULL," +
			"MovieID INT," +
			"FOREIGN KEY(MovieID) REFERENCES Movies(ID)," +
			"PRIMARY KEY(GenreID, MovieID)" + ");";

	public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Movie.CREATE_TABLE);
		db.execSQL(CREATE_GENRE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Movie.TABLE_NAME);
		onCreate(db);
	}

	public ArrayList<Movie> getAllMovie(){
		ArrayList<Movie> movieList;
		Movie movie;
		Cursor cursor = this.GetData("SELECT * FROM " + Movie.TABLE_NAME);

		if(cursor != null){
			cursor.moveToFirst();
			movieList = new ArrayList<>();
			while(!cursor.isAfterLast()){
				int id = cursor.getInt(cursor.getColumnIndex(Movie.ID));
				String title = cursor.getString(cursor.getColumnIndex(Movie.Title));
				String poster = cursor.getString(cursor.getColumnIndex(Movie.Poster));
				String thumbnail = cursor.getString(cursor.getColumnIndex(Movie.Thumbnail));
				float rating = cursor.getFloat(cursor.getColumnIndex(Movie.Rating));
				String description = cursor.getString(cursor.getColumnIndex(Movie.Description));
				String releaseDate = cursor.getString(cursor.getColumnIndex(Movie.ReleaseDate));
				int duration = cursor.getInt(cursor.getColumnIndex(Movie.Duration));
				int kind = cursor.getInt(cursor.getColumnIndex(Movie.Kind));
				String trailer = cursor.getString(cursor.getColumnIndex(Movie.Trailer));

				movie = new Movie(id,title,poster,thumbnail,rating,description,releaseDate,duration, kind, trailer);

				Cursor cursor1 = this.GetData("SELECT GenreID " +
									"FROM Genre " +
									"WHERE MovieID = " + String.valueOf(id));

				if(cursor1 != null){
					cursor1.moveToFirst();
					while(!cursor1.isAfterLast()){
						int i = cursor1.getInt(cursor1.getColumnIndex("GenreID"));
						String g = getGenreName(i);
						movie.addGenre(g);
						cursor1.moveToNext();
					}
				}
				movieList.add(movie);
				cursor.moveToNext();
			}
			return movieList;
		}
		else
			return null;
	}

	public void insertMovie(int id, String title, String poster, String thumbnail, float rating, String description,
		String releaseDate, int duration, int kind, String trailer){
		SQLiteDatabase database = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Movie.ID, id);
		contentValues.put(Movie.Title, title);
		contentValues.put(Movie.Poster, poster);
		contentValues.put(Movie.Thumbnail, thumbnail);
		contentValues.put(Movie.Rating, rating);
		contentValues.put(Movie.Description, description);
		contentValues.put(Movie.Duration, duration);
		contentValues.put(Movie.Kind, kind);
		contentValues.put(Movie.Trailer, trailer);
		this.QueryData("UPDATE " + Movie.TABLE_NAME + " SET " +
				Movie.ReleaseDate + " = " + "'" + releaseDate + "'" + "WHERE " + Movie.ID + " = " + String.valueOf(id));
		long result = database.insert(Movie.TABLE_NAME, null, contentValues);
	}

	public void insertGenreForMovie(int genreID, int MovieID){
		SQLiteDatabase database = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("GenreID", genreID);
		contentValues.put("MovieID", MovieID);
		long result = database.insert("Genre", null, contentValues);
	}

	public String getGenreName(int i){
		String g = "";
		switch (i){
			case 1:
				g = "Horror";
				break;
			case 2:
				g = "Action";
				break;
			case 3:
				g = "Comedy";
				break;
			case 4:
				g = "Romance";
				break;
			case 5:
				g = "Fantasy";
				break;
			case 6:
				g = "Thriller";
				break;
			case 7:
				g = "Sci-fi";
				break;
			case 8:
				g = "Mystery";
				break;
			case 9:
				g = "Adventure";
				break;
			case 10:
				g = "Animation";
				break;
			case 11:
				g = "Family";
				break;
			case 12:
				g = "Crime";
				break;
		}
		return g;
	}

	public int getGenreID(String s){
		int i = 0;
		switch (s){
			case "horror":
				i = 1;
				break;
			case "action":
				i = 2;
				break;
			case "comedy":
				i = 3;
				break;
			case "romance":
				i = 4;
				break;
			case "fantasy":
				i = 5;
				break;
			case "thriller":
				i = 6;
				break;
			case "sci-fi":
				i = 7;
				break;
			case "mystery":
				i = 8;
				break;
			case "adventure":
				i = 9;
				break;
			case "animation":
				i = 10;
				break;
			case "family":
				i = 11;
				break;
			case "crime":
				i = 12;
				break;
		}
		return i;
	}

	public ArrayList<Movie> getMovieByGenre(String s){
		int i = getGenreID(s);
		ArrayList<Movie> movieList;
		Movie movie;
		Cursor cursor = this.GetData("SELECT * FROM " + Movie.TABLE_NAME
				+ " JOIN Genre ON "+ Movie.ID + " = MovieID " + "WHERE GenreID = " + String.valueOf(i));

		if(cursor != null){
			cursor.moveToFirst();
			movieList = new ArrayList<>();
			while(!cursor.isAfterLast()){
				int id = cursor.getInt(cursor.getColumnIndex(Movie.ID));
				String title = cursor.getString(cursor.getColumnIndex(Movie.Title));
				String poster = cursor.getString(cursor.getColumnIndex(Movie.Poster));
				String thumbnail = cursor.getString(cursor.getColumnIndex(Movie.Thumbnail));
				float rating = cursor.getFloat(cursor.getColumnIndex(Movie.Rating));
				String description = cursor.getString(cursor.getColumnIndex(Movie.Description));
				String releaseDate = cursor.getString(cursor.getColumnIndex(Movie.ReleaseDate));
				int duration = cursor.getInt(cursor.getColumnIndex(Movie.Duration));
				int kind = cursor.getInt(cursor.getColumnIndex(Movie.Kind));
				String trailer = cursor.getString(cursor.getColumnIndex(Movie.Trailer));

				movie = new Movie(id,title,poster,thumbnail,rating,description,releaseDate,duration, kind, trailer);

				Cursor cursor1 = this.GetData("SELECT GenreID " +
						"FROM Genre " +
						"WHERE MovieID = " + String.valueOf(id));

				if(cursor1 != null){
					cursor1.moveToFirst();
					while(!cursor1.isAfterLast()){
						int t = cursor1.getInt(cursor1.getColumnIndex("GenreID"));
						String g = getGenreName(t);
						movie.addGenre(g);
						cursor1.moveToNext();
					}
				}
				movieList.add(movie);
				cursor.moveToNext();
			}
			return movieList;
		}
		else
			return null;
	}

	public void QueryData(String sql){
		SQLiteDatabase database = getWritableDatabase();
		database.execSQL(sql);
	}

	public Cursor GetData(String sql){
		SQLiteDatabase database = getReadableDatabase();
		return database.rawQuery(sql, null);
	}
}
