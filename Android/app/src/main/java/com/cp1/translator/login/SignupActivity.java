package com.cp1.translator.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cp1.translator.R;
import com.cp1.translator.activities.MainActivity;
import com.cp1.translator.models.User;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignupActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SignupActivity";

    @Bind(R.id.etEmail)
    EditText etEmail;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Bind(R.id.etConfirm)
    EditText etConfirm;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btSignup)
    public void signup(View v) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirm = etConfirm.getText().toString();

        if (password.equals(confirm)) {
            User user = new User();
            user.setUsername(email);
            user.setPassword(password);
            user.setEmail(email);
            user.setNickname(email);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(SignupActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "***********Signup successful!!!!*************");
                        Intent i = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Toast.makeText(SignupActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "**********Signup failed!!*********");
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        }
    }
}
