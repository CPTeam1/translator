package com.cp1.translator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cp1.translator.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AskQuestion extends AppCompatActivity {
    @Bind(R.id.btAskQs) Button btAskQs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(this);

        btAskQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                // 1) save qs in DB
                // 2) pass qs back to main activity
                // 3) display in adapter

                Intent displayQsIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(displayQsIntent);
            }
        });
    }
}
