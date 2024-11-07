package com.lxt.framework.ui.splash;

import androidx.lifecycle.MutableLiveData;

import com.lxt.framework.ui.base.viewmodel.BaseViewModel;

public class SplashVM extends BaseViewModel<String> {
    public MutableLiveData<String> text = new MutableLiveData<>();
    public SplashVM() {
        text.postValue("");
    }
    @Override public void release() {}
}
