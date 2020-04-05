package com.example.viewmodelapp;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DataViewModel extends ViewModel {

    private final String TAG = "DataViewModel";
    private MutableLiveData<Data> mPersonData = new MutableLiveData<>();

    public static class Data {
        ArrayList<String> firstNames = new ArrayList<String>();
        ArrayList<String> lastNames = new ArrayList<String>();
        ArrayList<String> images = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> abouts = new ArrayList<String>();

        ArrayList<String> getFirstNames() {
            return firstNames;
        }

        ArrayList<String> getLastNames() {
            return lastNames;
        }

        ArrayList<String> getImages() {
            return images;
        }

        ArrayList<String> getTitles() {
            return titles;
        }

        ArrayList<String> getAbouts() {
            return abouts;
        }
    }

    public LiveData<Data> getPersonData() {
        return mPersonData;
    }

    // Read data from contacts.json file
    public void getJSONData(Context context) {
        Log.i(TAG, "getJSONData called.");

        String json;
        try {
            // Get data from file
            InputStream input = context.getAssets().open("contacts.json");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // Store data in JSON Array
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            // Read each JSON object in array
            Data data = new Data();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String filename = obj.getString("avatar_filename");
                filename = filename.replace(' ', '_'); // replace spaces
                filename = filename.substring(0, filename.length() - 4);  // remove extension
                filename = filename.toLowerCase();

                data.images.add(filename);
                data.firstNames.add(obj.getString("first_name"));
                data.lastNames.add(obj.getString("last_name"));
                data.titles.add(obj.getString("title"));
                data.abouts.add(obj.getString("introduction"));
            }

            mPersonData.postValue(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
