package com.lxt.framework.data.proxy;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lxt.framework.MainApplication;
import com.lxt.framework.data.model.common.Message;
import com.lxt.framework.data.model.common.User;
import com.lxt.framework.data.model.response.BaseResponse;
import com.lxt.framework.data.model.response.Hotkey;
import com.lxt.framework.data.service.MessageService;
import com.lxt.framework.data.service.LoginService;
import com.lxt.framework.data.service.MessageService2;
import com.lxt.framework.data.service.UserService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import kotlin.coroutines.Continuation;

public class MockDataProxy implements MessageService2, LoginService, UserService {
    Context context = MainApplication.Companion.getInstance();

    public MockDataProxy() {

    }

    @Nullable
    @Override
    public Object getMessages(@NonNull Continuation<? super List<Message>> $completion) {
        return null;
    }


    @Override
    public User login(String account, String pwd, String appCode) {
        return null;
    }

    @Override
    public void directAuth(String authUrl) {

    }

    @Override
    public String testWan() {
        return "";
    }


    private Observable<List<User>> fetchUsersFromJson(String jsonFile) {
        return Observable.create(emitter -> {
            try {
                InputStream is = context.getAssets().open(jsonFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                User[] usersArray = gson.fromJson(jsonBuilder.toString(), User[].class);
                emitter.onNext(Arrays.asList(usersArray));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
                e.printStackTrace();
            }
        });
    }

    private Observable<User> fetchLoginInfoFromJson(String jsonFile) {
        return Observable.create(emitter -> {
            try {
                InputStream is = context.getAssets().open(jsonFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                User loginInfo = gson.fromJson(jsonBuilder.toString(), User.class);
                emitter.onNext(loginInfo);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
                e.printStackTrace();
            }
        });
    }

    @Nullable
    @Override
    public Object getUsers(@NonNull Continuation<? super BaseResponse<List<User>>> $completion) {
        return null;
    }

    @Nullable
    @Override
    public Object getHotkey(@NonNull Continuation<? super BaseResponse<List<Hotkey>>> $completion) {
        return null;
    }
}