package com.example.fitmeow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    /**
     * Live Data Instance
     */
    private MutableLiveData<Integer> mGender = new MutableLiveData<>();
    private MutableLiveData<String> mName = new MutableLiveData<>();
    private MutableLiveData<Integer> mCWeight = new MutableLiveData<>();
    private MutableLiveData<Integer> mIWeight = new MutableLiveData<>();


    public void setGender(int i) {
        mGender.setValue(i);
    }
    public void setName(String name) {
        mName.setValue(name);
    }
    public void setCWeight(int cweight) {
        mCWeight.setValue(cweight);
    }
    public void setIWeight(int iweight) {
        mIWeight.setValue(iweight);
    }

    public LiveData<Integer> getGender() {
        return mGender;
    }
    public LiveData<String> getName() {
        return mName;
    }
    public LiveData<Integer> getCWeight() {
        return mCWeight;
    }
    public LiveData<Integer> getIWeight() {
        return mIWeight;
    }
}
