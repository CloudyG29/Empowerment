package com.nullandvoid.empowerment.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BeEmpoweredViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BeEmpoweredViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}