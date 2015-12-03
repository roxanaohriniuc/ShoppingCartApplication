package com.example.roxanaohriniuc.petmartsuper;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShoppingCartActivity extends ListActivity {

    protected ShoppingCartListAdapter mListAdapter;
    protected TextView mTotalText;
    protected Button mCheckout;

    Inventory inventory = Inventory.getInstance();
    ShoppingCart shoppingCart = ShoppingCart.getInstance();
    protected String accountID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            accountID = extras.getString("accountID");
        }
         //transfer data from main activity.
        mTotalText = (TextView) findViewById(R.id.totalPriceTextView);

        mCheckout = (Button) findViewById(R.id.CheckoutButton);
        // when checkout button is clicked.
        mCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingCartActivity.this, CheckoutActivity.class);
                intent.putExtra("accountID" ,accountID);
                startActivity(intent);
            }
        });
        mListAdapter = new ShoppingCartListAdapter(ShoppingCartActivity.this, accountID, manager, mTotalText);
        setListAdapter(mListAdapter);
        UpdateTotal();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {
            Intent intent = new Intent(ShoppingCartActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

}
