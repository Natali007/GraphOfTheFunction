package com.natali007.com.myapplication;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.String.*;


public class MainActivity extends AppCompatActivity {

    private Button buttonCancel;
    private Button buttonLagrange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyDrawView Plane = (MyDrawView) findViewById(R.id.plane);

        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonLagrange = (Button) findViewById(R.id.buttonLagrange);

        buttonLagrange.setOnClickListener(new OnClickListener() {
            public void onClick(View V) {
                Plane.onClicklLagrange();
            }
        });

        buttonCancel.setOnClickListener(new OnClickListener(){
            public void onClick(View V){
                Plane.onClicklClear();
            }
        });


    }

}
