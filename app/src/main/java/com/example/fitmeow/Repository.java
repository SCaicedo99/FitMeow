package com.example.fitmeow;

import androidx.lifecycle.MutableLiveData;

public class Repository {
    private MutableLiveData<Integer> mGender = new MutableLiveData<>();
    private MutableLiveData<String> mName = new MutableLiveData<>();
    private MutableLiveData<Integer> mCWeight = new MutableLiveData<>();
    private MutableLiveData<Integer> mIWeight = new MutableLiveData<>();

    public Repository(String name, int cweight, int iweight, int gender){
        mName.setValue(name);
        mCWeight.setValue(cweight);
        mIWeight.setValue(iweight);
        mGender.setValue(gender);
    }

    public Repository(){}

    public void setGender(int gender){
        mGender.setValue(gender);
    }
    public void setName(String s){
        mName.setValue(s);
    }
    public MutableLiveData<Integer> getGender(){
        return mGender;
    }
    public MutableLiveData<String> getName(){
//        mName.setValue("I am Teemo");
        return mName;
    }
}
