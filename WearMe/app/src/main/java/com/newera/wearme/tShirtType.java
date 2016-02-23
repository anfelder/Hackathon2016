package com.newera.wearme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class tShirtType extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.newera.wearme.shirtType";
    String tShirtType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("");
        setContentView(R.layout.activity_t_shirt_type);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void plainSelect(View view) {
        Intent intent = new Intent(this, customize.class);
        tShirtType = "1";
        intent.putExtra(EXTRA_MESSAGE, tShirtType);
        startActivity(intent);
    }

    public void tankSelect(View view) {
        Intent intent = new Intent(this, customize.class);
        tShirtType = "2";
        intent.putExtra(EXTRA_MESSAGE, tShirtType);
        startActivity(intent);
    }

    public void cubanSelect(View view) {
        Intent intent = new Intent(this, customize.class);
        tShirtType = "3";
        intent.putExtra(EXTRA_MESSAGE, tShirtType);
        startActivity(intent);
    }

}
