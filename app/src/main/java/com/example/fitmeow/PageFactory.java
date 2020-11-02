package com.example.fitmeow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PageFactory extends ViewModelProvider.NewInstanceFactory {

//    Repository mRepository;
    String mName;
    int cweight, iweight, gender;

    public PageFactory(String name, int cweight, int iweight, int gender){
//        mRepository =  new Repository(name, cweight, iweight, gender);
        this.mName = name;
        this.cweight = cweight;
        this.iweight = iweight;
        this.gender = gender;
    }
//    public PageFactory(){
//        mRepository =  new Repository();
//    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass==PageViewModel.class) {
//            return (T) new PageViewModel(mRepository);
            return (T) new PageViewModel(mName, cweight, iweight, gender);
        }
        return null;
    }
}
