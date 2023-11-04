package com.example.megohike;

import android.app.Application;

import com.example.megohike.data.data_source.database.HikeInformationDatabase;

public class MeGoHikeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HikeInformationDatabase.getDatabase(this);
    }

}
