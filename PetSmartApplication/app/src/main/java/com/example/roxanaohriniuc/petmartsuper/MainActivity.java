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
//
    String url = "http://shoppingcart-api-8000.herokuapp.com/api/inventory";
    private  static final String TAG = MainActivity.class.getSimpleName();
    private InventoryListAdapter mInventoryListAdapter;
    private Button mViewCart;
    private Inventory inventory = Inventory.getInstance();
    private ShoppingCart mShoppingCart = ShoppingCart.getInstance();
    private String jsonAccount;
    protected TextView mTotalText;
    private ArrayList<CartItem> mCart;
    private String mAccountID;
    private PetMartSuperUtils utils = new PetMartSuperUtils();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ListView listV = (ListActivity) findViewById(R.)
        // shopping cart amount
        mTotalText = (TextView) findViewById(R.id.totalPriceTextView);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            if(extras.getBoolean("fromDescription")){
                mAccountID = extras.getString("accountId");
            }
            else{
            jsonAccount = extras.getString("jsonAccount");
            try {
                JSONObject jsonO = new JSONObject(jsonAccount);
                mAccountID = jsonO.getString("_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        }
        // upload inventory from API
        if(utils.isNetworkAvailable(manager)){
            OkHttpClient client = new OkHttpClient();
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

                        } else {
                        }
                    } catch (Exception e) {
                       Log.e(TAG, e.getMessage());
                    }

                }
            }); }
        else{
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG);
        }

        mViewCart= (Button) findViewById(R.id.ShoppingCartButton);
        mViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTotal();
                Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class );
                intent.putExtra("jsonAccount" ,jsonAccount);
                startActivity(intent);
            }
        });
        // insert InventoryListAdapter
        utils.waitHere();
        mInventoryListAdapter = new InventoryListAdapter(MainActivity.this, mAccountID, manager);
        setListAdapter(mInventoryListAdapter);
    }

    /**
     * Sets the shopping cart from a JSON account object
     */
    private void InitialShoppingCart()
    {
        mCart = new ArrayList<CartItem>();
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
            mShoppingCart.setProducts(mCart);
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
        for(CartItem item : mCart )
        {
            total += (item.getQuantity() * item.getProduct().getPrice());
        }
        mTotalText.setText(String.format("%.2f", total));
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {
          Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
