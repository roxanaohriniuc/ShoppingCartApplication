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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    private  static final String TAG = MainActivity.class.getSimpleName();
    private MainListAdapter mMainListAdapter;
    private Button mViewCart;
    protected TextView mTotalText;

    private Inventory inventory = Inventory.getInstance();
    private ShoppingCart shoppingCart = ShoppingCart.getInstance();
    private String accountID;
    private PetMartSuperUtils utils = new PetMartSuperUtils();

    private String jsonAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ListView listV = (ListActivity) findViewById(R.)
        // shopping cart amount
        mTotalText = (TextView) findViewById(R.id.totalPriceTextView);

        mMainListAdapter = new MainListAdapter(MainActivity.this, accountID, manager);
        setListAdapter(mMainListAdapter);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            if(extras.getBoolean("fromDescription")){
                accountID = extras.getString("accountId");
            }
            else{
            jsonAccount = extras.getString("jsonAccount");
            try {
                JSONObject jsonO = new JSONObject(jsonAccount);
                accountID = jsonO.getString("_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        }
        if(inventory.getProducts().size() == 0) {//first time only
            // upload inventory from API
            if (utils.isNetworkAvailable(manager)) {
                OkHttpClient client = new OkHttpClient();
                String url = "http://shoppingcart-api-8000.herokuapp.com/api/inventory";
                Request request = new Request.Builder().url(url).build();

                Call call = client.newCall(request);
                // set activity to a new thread.
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        // add error message here
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        try {
                            // if the response is successful, set product and update total.
                            if (response.isSuccessful()) {
                                String information = response.body().string();
                                inventory.setProducts(information);
                                InitialShoppingCart();
                                UpdateTotal();
                                mMainListAdapter.notifyDataSetChanged();
                            } else {
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }

                    }
                });
            } else {
                Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG);
            }

        }else{
            UpdateTotal();
        }
        //set shopping cart button redirects user to shopping cart activty class
        mViewCart= (Button) findViewById(R.id.ShoppingCartButton);
        mViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                intent.putExtra("accountID", accountID);
                startActivity(intent);
            }
        });
    }


    /**
     * Sets the shopping cart from a JSON account object
     */
    private void InitialShoppingCart()
    {
        ArrayList<CartItem> mCart = new ArrayList<CartItem>();
        try {
            JSONObject obj = new JSONObject(jsonAccount);
            JSONArray arr = obj.getJSONArray("shoppingcart");
            for (int i = 0; i < arr.length(); i++) {
                CartItem item = new CartItem();
                Product p = inventory.getProductById(arr.getJSONObject(i).getString("productId"));
                int quantity = arr.getJSONObject(i).getInt("quantity");
                if (p != null) {
                    item.setProduct(p);
                    item.setQuantity(quantity);
                    mCart.add(item);
                }
            }
            shoppingCart.setProducts(mCart);
        }
        catch(Exception e)
        {e.printStackTrace();}
    }

    /**
     * Updates the total value of the shopping cart
     */
    public void UpdateTotal()
    {
        double total = 0;
        for(CartItem item : shoppingCart.getProducts() )
        {
            total += (item.getQuantity() * item.getProduct().getPrice());
        }
        mTotalText.setText(String.format("%.2f", total));
        mTotalText.invalidate();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {
          Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
