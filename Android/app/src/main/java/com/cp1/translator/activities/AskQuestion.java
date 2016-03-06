package com.cp1.translator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cp1.translator.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AskQuestion extends AppCompatActivity {
    @Bind(R.id.btAskQs) Button btAskQs;
    @Bind(R.id.etQs) EditText etQs;
    @Bind(R.id.tvChars) TextView tvCharsLeft;
    int textColor;

    private static int QS_CHAR_LIMIT = 140;
    private static boolean qsPosted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(this);

        etQs.addTextChangedListener(textWatcher);
        textColor = tvCharsLeft.getCurrentTextColor();

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

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int charsRemaining = QS_CHAR_LIMIT - s.length();
            tvCharsLeft.setText(Integer.toString(charsRemaining));

            if (charsRemaining >= 0 && charsRemaining < QS_CHAR_LIMIT) {
                btAskQs.setEnabled(true);
                tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                btAskQs.setEnabled(false);
                if (charsRemaining < 0)
                    tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                qsPosted = false;
            }
        }
    };
}
