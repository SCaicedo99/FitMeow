package com.example.fitmeow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    /**
     * Live Data Instance
     */
    public MutableLiveData<Integer> mGender = new MutableLiveData<>();
    public MutableLiveData<String> mName = new MutableLiveData<>();
    public MutableLiveData<Integer> mCWeight = new MutableLiveData<>();
    public MutableLiveData<Integer> mIWeight = new MutableLiveData<>();
    public Repository mRepository;

//    public PageViewModel(Repository mRepository){
//        this.mRepository = mRepository;
//    }

    public PageViewModel(String name, int cweight, int iweight, int gender){
        mRepository = new Repository(name, cweight, iweight, gender);
        mName.setValue(name);
        mCWeight.setValue(cweight);
        mIWeight.setValue(iweight);
        mGender.setValue(gender);
    }

    public void setGender(int gender) {
//        mGender.setValue(gender);
        mRepository.setGender(gender);
    }
    public void setName(String s) {
//        mRepository.setName(s);
    }

    public LiveData<Integer> getGender() {
        mGender = mRepository.getGender();
        return mGender;
    }
    public MutableLiveData<String> getName(){
//        mName = mRepository.getName();
        return mName;
    }
}
