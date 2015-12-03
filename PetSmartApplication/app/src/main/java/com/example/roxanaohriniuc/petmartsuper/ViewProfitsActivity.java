package com.example.roxanaohriniuc.petmartsuper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ViewProfitsActivity extends AppCompatActivity {
    protected PetMartSuperUtils utils = new PetMartSuperUtils();
    private  static final String TAG = ViewProfitsActivity.class.getSimpleName();
    protected TextView mProfitsText;
    protected Button mReturn;
;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profits);
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        mProfitsText = (TextView) findViewById(R.id.profitsTextView);
        mReturn = (Button) findViewById(R.id.returnToInventoryProfits);

        String url = "http://shoppingcart-api-8000.herokuapp.com/api/orders";
        if (utils.isNetworkAvailable(manager)) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        double totalPrice = 0;double totalCost = 0;
                        final double aTotalPrice; final double aTotalCost;
                        if (response.isSuccessful()) {
                            JSONArray orders = new JSONArray(response.body().string());
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject order = orders.getJSONObject(i);
                                totalPrice += (order.getDouble("totalPrice"));
                                totalCost += (order.getDouble("totalCost"));
                            }
                            aTotalPrice = totalPrice;
                            aTotalCost = totalCost;
                            mProfitsText.setText("$ " + String.format("%.2f", (aTotalPrice - aTotalCost)));
                            mProfitsText.invalidate();
                        }
                         else {
                        }
                    } catch (Exception e) {  // log the exception
                        e.printStackTrace();
                    }
                }
            });
        } else { // inform user of network not available

        }
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return to inventory
                Intent intent = new Intent(ViewProfitsActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_profits, menu);
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
            Intent intent = new Intent(ViewProfitsActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
