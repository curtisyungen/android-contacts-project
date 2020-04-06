package com.example.viewmodelapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private static PersonViewModel personModel;

    private RecyclerView hRecyclerView;
    private RecyclerView vRecyclerView;
    private hRecyclerViewAdapter hAdapter;
    private vRecyclerViewAdapter vAdapter;

    private MovementHandler mMovementHandler;

    SnapHelper snapHelper = new PagerSnapHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModel();

        TextView topBar = (TextView) findViewById(R.id.topBar);
        topBar.setText(R.string.app_name);
        topBar.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                personModel.setPersonId(0);
            }
        });
    }

    private void initViewModel() {
        Log.i(TAG, "initViewModel called.");

        DataViewModel dataModel = ViewModelProviders.of(this).get(DataViewModel.class);
        dataModel.getPersonData().observe(this, new Observer<DataViewModel.Data>() {
            @Override
            public void onChanged(DataViewModel.Data data) {
                ArrayList<String> firstNames = data.getFirstNames();
                ArrayList<String> lastNames = data.getLastNames();
                ArrayList<String> titles = data.getTitles();
                ArrayList<String> abouts = data.getAbouts();
                ArrayList<String> images = data.getImages();

                initPersonIdViewModel();

                hAdapter = new hRecyclerViewAdapter(getApplicationContext(), personModel, images);
                vAdapter = new vRecyclerViewAdapter(firstNames, lastNames, titles, abouts);

                initHRecyclerView();
                initVRecyclerView();
                initMovementHandler();
            }
        });

        dataModel.getJSONData(this);
    }

    public void initPersonIdViewModel() {
        personModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        personModel.getPersonId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer personId) {
                Log.i(TAG, "new personId " + personId);
                hRecyclerView.smoothScrollToPosition(personId);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initHRecyclerView() {
        Log.i(TAG, "initHRecyclerView called.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hRecyclerView = findViewById(R.id.recyclerView);
        hRecyclerView.setLayoutManager(layoutManager);
        hRecyclerView.setHasFixedSize(true);
        hRecyclerView.setAdapter(hAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initVRecyclerView() {
        Log.i(TAG, "initVRecyclerView called.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vRecyclerView = findViewById(R.id.recyclerViewVert);
        vRecyclerView.setLayoutManager(layoutManager);
        vRecyclerView.setHasFixedSize(true);
        vRecyclerView.setAdapter(vAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initMovementHandler() {
        mMovementHandler = new MovementHandler(hRecyclerView, vRecyclerView);
        hRecyclerView.addOnScrollListener(mMovementHandler);
        vRecyclerView.addOnScrollListener(mMovementHandler);
        hRecyclerView.setOnTouchListener(mMovementHandler);
        vRecyclerView.setOnTouchListener(mMovementHandler);
    }

    public static PersonViewModel getPersonViewModel() {
        return personModel;
    }

    static class MovementHandler extends RecyclerView.OnScrollListener implements RecyclerView.OnTouchListener {
        RecyclerView mOriginatingRecyclerView;
        RecyclerView hRecyclerView;
        RecyclerView vRecyclerView;

        SnapHelper snapHelper = new PagerSnapHelper();
        PersonViewModel personModel;

        float itemHeight;
        float itemWidth;

        double roundX = 0;
        double roundY = 0;

        private boolean snapped;

        MovementHandler(RecyclerView horizontal, RecyclerView vertical) {
            hRecyclerView = horizontal;
            vRecyclerView = vertical;
            snapped = true;

            personModel = MainActivity.getPersonViewModel();
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (mOriginatingRecyclerView == null) {
                mOriginatingRecyclerView = recyclerView;
            } else {
                if (mOriginatingRecyclerView != recyclerView) {
                    return;
                }
            }

            snapped = false;

            int offset = hRecyclerView.computeHorizontalScrollOffset();
            int position = (int) (((offset + itemWidth / 2) / itemWidth));

            handleImageBorder(position);

            itemHeight = vRecyclerView.getChildAt(0).getHeight();
            itemWidth = hRecyclerView.getChildAt(0).getWidth();

            double scrollX, scrollY;

            if (recyclerView == hRecyclerView) {
                scrollY = dx * (itemHeight / itemWidth);

                roundY += scrollY - (int)scrollY;
                if (Math.abs(roundY) > 1) {
                    scrollY += roundY;
                    roundY = 0;
                }

                vRecyclerView.scrollBy(0, (int) scrollY);
            }

            if (recyclerView == vRecyclerView) {
                scrollX = dy * (itemWidth / itemHeight);

                roundX += scrollX - (int)scrollX;
                Log.d("ROUND", String.format("roundX %s", roundX));
                if (Math.abs(roundX) > 1) {
                    scrollX += roundX;
                    roundX = 0;
                }

                hRecyclerView.scrollBy((int) scrollX, 0);
            }
        }

        private void handleImageBorder(int position) {
            for (int i = 0; i < hRecyclerView.getChildCount(); i++) {
                View image = hRecyclerView.getChildAt(i);
                if (image != null) {
                    ImageView border = (ImageView) image.findViewById(R.id.image_border);
                    border.setAlpha(0.0f);
                }
            }

            View centerImage = (View) hRecyclerView.findViewWithTag(position);

            if (centerImage == null) return;

            View centerImageHolder = (View) centerImage.getParent();

            if (centerImageHolder == null) return;

            ImageView border = (ImageView) centerImageHolder.findViewById(R.id.image_border);

            if (border != null) border.setAlpha(0.5f);
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (snapped) return;

            if (RecyclerView.SCROLL_STATE_SETTLING == newState) {
                snapHelper.attachToRecyclerView(recyclerView);
            }

            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                recyclerView.setOnFlingListener(null);
                mOriginatingRecyclerView = null;
                snapped = true;
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mOriginatingRecyclerView = null;
            }
            return false;
        }
    }
}