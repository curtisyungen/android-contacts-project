package com.example.viewmodelapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonViewModel extends ViewModel {
    private MutableLiveData<Integer> personId = new MutableLiveData<>();

    public LiveData<Integer> getPersonId() {
        return personId;
    }

    public void setPersonId(Integer id) {
        personId.setValue(id);
    }
}
