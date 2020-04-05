package com.example.viewmodelapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class hRecyclerViewAdapter extends RecyclerView.Adapter<hRecyclerViewAdapter.ViewHolder> {

    private PersonViewModel viewModel;
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<ViewHolder> mViewHolders = new ArrayList<>();
    private Context mContext;

    hRecyclerViewAdapter(Context mContext, PersonViewModel viewModel, ArrayList<String> mImageUrls) {
        this.mContext = mContext;
        this.viewModel = viewModel;
        this.mImageUrls = mImageUrls;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView border;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            border = itemView.findViewById(R.id.image_border);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder called.");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recyclerview, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), String.valueOf(v.getWidth()), Toast.LENGTH_SHORT).show();
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //Log.d(TAG, "onBindViewHolder called.");

        Resources resources = mContext.getResources();
        final int imgId = resources.getIdentifier(mImageUrls.get(position), "drawable", mContext.getPackageName());

        if (imgId == 0) {
            return;
        }

        Drawable img = resources.getDrawable(imgId);
        holder.image.setImageDrawable(img);
        holder.image.setTag(position);

        Drawable borderImg = resources.getDrawable(R.drawable.circular_border);
        holder.border.setImageDrawable(borderImg);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setPersonId(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }
}
