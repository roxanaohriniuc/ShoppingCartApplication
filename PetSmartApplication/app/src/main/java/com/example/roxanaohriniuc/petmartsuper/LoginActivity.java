package com.example.roxanaohriniuc.petmartsuper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    String url = "http://shoppingcart-api-8000.herokuapp.com/api/accounts";
    private  static final String TAG = MainActivity.class.getSimpleName();
    ConnectivityManager manager;
    //create a custom util class
    protected final PetMartSuperUtils utils = new PetMartSuperUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        TextView mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        /*
*       create login button that redirects user to main activity class
*        */
        Button mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mInformation = (TextView) findViewById(R.id.alertUserEmptyFieldText);
                TextView mUserName = (EditText) findViewById(R.id.usernameField);
                TextView mPassword = (EditText) findViewById(R.id.passwordField);

                final String username = mUserName.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    mInformation.setText(R.string.alert_login);
                } else {
                    // check user against API
                    if (utils.isNetworkAvailable(manager)) {
                        OkHttpClient client = new OkHttpClient();
                        String tempUrl = url + "/" + username + "/" + password;
                        Request request = new Request.Builder().url(tempUrl).build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                try {
                                    String jsonAccount = response.body().string().toString();
                                    if (response.isSuccessful() && !jsonAccount.equals("null")) {
                                        JSONObject obj = new JSONObject(jsonAccount);
                                        if (obj.getBoolean("admin")) {
                                            // load admin page
                                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                            intent.putExtra("jsonAccount", jsonAccount);
                                            startActivity(intent);

                                        } else {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("jsonAccount", jsonAccount);
                                            startActivity(intent);
                                        }
                                    } else {
                                        utils.alertUserAboutError(getFragmentManager());
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }

                            }
                        });
                    } else {
                        /* throw exception if there is no active network*/
                        Toast.makeText(LoginActivity.this, R.string.network_unavailable_message, Toast.LENGTH_LONG);
                    }
                }
                mPassword.setText("");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return true;
    }

}
