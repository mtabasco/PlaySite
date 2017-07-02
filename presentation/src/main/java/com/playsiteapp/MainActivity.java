package com.playsiteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.playsiteapp.contracts.MainContract;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }
}
