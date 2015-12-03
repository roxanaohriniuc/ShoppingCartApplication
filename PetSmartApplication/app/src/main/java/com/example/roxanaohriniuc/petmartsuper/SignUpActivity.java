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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    protected PetMartSuperUtils utils = new PetMartSuperUtils();
    protected EditText mUserName;
    protected EditText mPassword;
    protected EditText mEmailAddress;
    protected Button mSignUpButton;
    protected CheckBox mIsAdminButton;
    protected EditText mName;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String url = "http://shoppingcart-api-8000.herokuapp.com/api/accounts";
    ConnectivityManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUserName = (EditText) findViewById(R.id.usernameField);
        mName = (EditText) findViewById(R.id.fullNameTextField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mEmailAddress = (EditText) findViewById(R.id.emailField);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);
        mIsAdminButton = (CheckBox) findViewById(R.id.isAdminCheckBox);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String username = mUserName.getText().toString();
                String password = mPassword.getText().toString();
                String emailAddress = mEmailAddress.getText().toString();
                Boolean isAdmin = mIsAdminButton.isChecked();

                username.trim();
                password.trim();
                emailAddress.trim();
                name.trim();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || emailAddress.isEmpty()) {
                    utils.alertUserAboutError(getFragmentManager());
                } else {
                    // upload inventory from API
                    if (utils.isNetworkAvailable(manager)) {
                        OkHttpClient client = new OkHttpClient();
                        String user = "{\"name\" : \"" + name + "\", " +
                                "\"username\": \"" + username + "\", " +
                                "\"password\": \"" + password + "\", " +
                                "\"email\": \"" + emailAddress + "\", " +
                                "\"admin\": " + isAdmin + "}";
                        //create a post request to API
                        RequestBody body = RequestBody.create(JSON, user);
                        Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                try {
                                    // Log.v(TAG, response.body().string());
                                    if (response.isSuccessful()) {
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {

                                    }
                                } catch (Exception e) {  // log the exception
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        });
                    } else { // inform user of network not available
                        Toast.makeText(SignUpActivity.this, R.string.network_unavailable_message, Toast.LENGTH_LONG);
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
