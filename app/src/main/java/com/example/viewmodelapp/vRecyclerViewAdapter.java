package com.example.viewmodelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class vRecyclerViewAdapter extends RecyclerView.Adapter<vRecyclerViewAdapter.ViewHolder> {

    private static String TAG = "RecyclerViewVertAdapter";

    private ArrayList<String> mFirstNames = new ArrayList<>();
    private ArrayList<String> mLastNames = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mAbouts = new ArrayList<>();

    vRecyclerViewAdapter(ArrayList<String> mFirstNames, ArrayList<String> mLastNames, ArrayList<String> mTitles, ArrayList<String> mAbouts) {
        this.mFirstNames = mFirstNames;
        this.mLastNames = mLastNames;
        this.mTitles = mTitles;
        this.mAbouts = mAbouts;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView title;
        TextView about;
        TextView aboutHeader;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstNameTextView);
            lastName = itemView.findViewById(R.id.lastNameTextView);
            title = itemView.findViewById(R.id.titleTextView);
            about = itemView.findViewById(R.id.aboutTextView);
            aboutHeader = itemView.findViewById(R.id.aboutHeaderTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_scrollview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.firstName.setText(mFirstNames.get(position));
        holder.lastName.setText(mLastNames.get(position));
        holder.title.setText(mTitles.get(position));
        holder.about.setText(mAbouts.get(position));
        holder.aboutHeader.setText(R.string.aboutHeader);
    }

    @Override
    public int getItemCount() {
        return mLastNames.size();
    }
}
