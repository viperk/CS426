package com.example.ptquy.foodfinding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FoodAdapter extends  RecyclerView.Adapter<FoodAdapter.ViewHolder>{
	private ArrayList<DataFood> arrayList;
	private Context context;
	private FirebaseStorage storage;

	public FoodAdapter(ArrayList<DataFood> arrayList, Context context, FirebaseStorage storage) {
		this.arrayList = arrayList;
		this.context = context;
		this.storage = storage;
	}

	public ArrayList<DataFood> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<DataFood> arrayList) {
		this.arrayList = arrayList;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View itemView = layoutInflater.inflate(R.layout.item_food_row, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		holder.RestName.setText(arrayList.get(position).getRestaurantName());
		holder.RestLocation.setText(arrayList.get(position).getDetailAddress());
		holder.ReviewCount.setText("(" + String.valueOf(arrayList.get(position).getRating())+")");
		holder.ratingBar.setRating(arrayList.get(position).getRating());
		Resources resources = context.getResources();
		final int avtid = resources.getIdentifier(arrayList.get(position)
														   .getAvtID(),"drawable",context.getPackageName());

		if(avtid != 0)
			holder.avtFood.setImageResource(avtid);
		StorageReference storageReference = storage.getReference().child("FoodAvatar/"+arrayList.get(position).getAvtID()+".jpg");
		if(storageReference != null) {
			Glide.with(context)
					.using(new FirebaseImageLoader())
					.load(storageReference)
					.into(holder.avtFood);
		}
		else
			holder.avtFood.setImageResource(R.drawable.foodicon);

		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position, boolean isLongClick) {
				if(!isLongClick){
					DataFood dataFood = (DataFood) arrayList.get(position);
					Intent intent = new Intent(view.getContext(), DetailFoodInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					Bundle bundle = new Bundle();
					bundle.putSerializable("value", dataFood);
					intent.putExtras(bundle);
					context.startActivity(intent);
				}
				else{

				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return arrayList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener{

		ImageView avtFood;
		TextView RestName;
		TextView RestLocation;
		TextView ReviewCount;
		RatingBar ratingBar;
		private ItemClickListener itemClickListener;

		public void setItemClickListener(ItemClickListener itemClickListener) {
			this.itemClickListener = itemClickListener;
		}

		public ViewHolder(View itemView) {
			super(itemView);
			avtFood = (ImageView) itemView.findViewById(R.id.FoodAvatar);
			RestName = (TextView) itemView.findViewById(R.id.RestaurantName);
			RestLocation = (TextView) itemView.findViewById(R.id.RestaurantLocation);
			ReviewCount = (TextView) itemView.findViewById(R.id.ReviewCount);
			ratingBar = (RatingBar) itemView.findViewById(R.id.ratingItemRow);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View v) {
			itemClickListener.onClick(v, getAdapterPosition(), false);
		}

		@Override
		public boolean onLongClick(View v) {
			itemClickListener.onClick(v, getAdapterPosition(), true);
			return true;
		}
	}
}
