package com.example.roxanaohriniuc.petmartsuper;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Serializable;

public class AdminActivity extends ListActivity {
    //
    String url = "http://shoppingcart-api-8000.herokuapp.com/api/inventory";
    private  static final String TAG = MainActivity.class.getSimpleName();
    private AdminListAdapter mInventoryListAdapter;
    private Button mViewProfits;
    private Inventory inventory = Inventory.getInstance();
    protected Button mAddItem;
    private PetMartSuperUtils utils = new PetMartSuperUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // upload inventory from API
        if(utils.isNetworkAvailable(manager)){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        // if the response is successful, set product and update total.
                        if (response.isSuccessful()) {
                            String information = response.body().string();
                            inventory.setProducts(information);
                        } else {
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                }
            }); }
        else{
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG );
        }
        // insert MainListAdapter
        mInventoryListAdapter = new AdminListAdapter(AdminActivity.this, manager);
        setListAdapter(mInventoryListAdapter);

        mAddItem = (Button) findViewById(R.id.adminAddProductToInventory);
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, EditProductActivity.class);
                startActivity(intent);
            }
        });
        mViewProfits= (Button) findViewById(R.id.viewProfitsTextView);
        mViewProfits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start view profits activity
                // pass over the profit
                Intent intent = new Intent(AdminActivity.this, ViewProfitsActivity.class );
                startActivity(intent);
            }
        });
    }
    public void sendIntentToViewProducts(Product product){

        Intent intent = new Intent(this, ViewProductActivity.class );
        intent.putExtra("product", (Serializable) product);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays an Error message to screen.
     */
    public  void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }


}
