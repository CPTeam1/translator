package com.cp1.translator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cp1.translator.R;
import com.cp1.translator.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity{
    public static final String LOG_TAG = "LoginActivity";
    @Bind(R.id.btLogin)
    Button btLogin;

    @Bind(R.id.etEmail)
    EditText etEmail;

    @Bind(R.id.etPassword)
    EditText etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        // signUp();
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

    // Helper method to create a new user.
    // This will be formalized through a signup workflow.
    // For now use edit this method with new user detials and call this method in onCreate to create new users for testing
    private void signUp() {
        User user = new User();
        user.setUsername("ishanpande@gmail.com");
        user.setPassword("ishanpande");
        user.setEmail("ishanpande@gmail.com");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e(LOG_TAG, "***********Signup successful!!!!*************");
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e(LOG_TAG, "**********Signup failed!!*********");
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btLogin)
	public void login(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
		ParseUser.logInInBackground(email, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
	}

}
