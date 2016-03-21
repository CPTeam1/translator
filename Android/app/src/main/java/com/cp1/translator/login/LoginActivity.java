package com.cp1.translator.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cp1.translator.R;
import com.cp1.translator.activities.MainActivity;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.ParseErrorConverter;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity{
    public static final String LOG_TAG = "LoginActivity";

    @Bind(R.id.etEmail)
    EditText etEmail;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Nullable
    @Bind(R.id.pbLoggingIn)
    ProgressBar pbLoggingIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ParseUser user = ParseUser.getCurrentUser();
        if(user!=null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        etEmail.requestFocus();
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

    @OnClick(R.id.btSignup)
	public void login(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        pbLoggingIn.setVisibility(ProgressBar.VISIBLE);
		User.logInInBackground(email, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    ParseInstallation parseInstall = ParseInstallation.getCurrentInstallation();
                    parseInstall.put("user", user);
                    parseInstall.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                String errMsg = ParseErrorConverter.getErrMsg(e.getCode());
                                Log.e(LOG_TAG, errMsg, e);
                                Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    String errMsg = ParseErrorConverter.getErrMsg(e.getCode());
                    Log.e(LOG_TAG, errMsg, e);
                    Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                }
                pbLoggingIn.setVisibility(ProgressBar.INVISIBLE);
            }
        });
	}

    @OnClick(R.id.tvCreate)
    public void createAccount(View view) {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
        finish();
    }

}
