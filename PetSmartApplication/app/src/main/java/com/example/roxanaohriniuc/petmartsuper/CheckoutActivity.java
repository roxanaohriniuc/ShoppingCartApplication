package com.example.roxanaohriniuc.petmartsuper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private Button mCheckout;
    private EditText mFirstName;
    private  EditText mLastName;
    private EditText mAddress;
    private EditText mCityStateZip;
    private EditText mCreditCard;
    private EditText mExpDate;
    PetMartSuperUtils utils = new PetMartSuperUtils();
    private Inventory inventory = Inventory.getInstance();
    private ShoppingCart shoppingCart = ShoppingCart.getInstance();
    private String accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mCheckout = (Button) findViewById(R.id.checkoutButton2);
        mFirstName = (EditText) findViewById(R.id.firstNameTextField) ;
        mLastName = (EditText) findViewById(R.id.lastNameTextField);
        mAddress = (EditText) findViewById(R.id.addressTextField);
        mCityStateZip = (EditText) findViewById(R.id.cityStateTextField) ;
        mCreditCard = (EditText) findViewById(R.id.creditCardTextField);
        mExpDate = (EditText) findViewById(R.id.expirationdateTextField) ;
        // if any of the fields are left blank...

        final ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            accountID = extras.getString("accountID");
        }


        mCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirstName.getText().toString().isEmpty() || mLastName.getText().toString().isEmpty()
                        || mAddress.getText().toString().isEmpty()
                        || mCityStateZip.getText().toString().isEmpty()
                        || mCreditCard.getText().toString().isEmpty()
                        || mExpDate.getText().toString().isEmpty()){
                    // error message
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                    builder.setMessage(R.string.empty_field_error_message)
                            .setTitle(R.string.empty_field_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{

                    //calculate totals
                    double totalPrice =0; double totalCost = 0;
                    for(CartItem c : shoppingCart.getProducts())
                    {
                        totalPrice += (c.getQuantity() * c.getProduct().getPrice());
                        totalCost += (c.getQuantity() * c.getProduct().getCost());
                    }
                    //POST new order (totalPrice, totalCost, accountId)
                    utils.placeOrder(manager, accountID, totalPrice, totalCost);
                    //Clear Shoppingcart
                    shoppingCart.setProducts(new ArrayList<CartItem>());
                    utils.clearShoppingCart(manager, accountID);

                    // confirmation message after checking all fields.
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                    builder.setMessage(R.string.successful_checkout_message)
                            .setTitle(R.string.successful_checkout_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    // update order history
                    // clear person shopping cart


                    // return to main activity
                    Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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
            Intent intent = new Intent(CheckoutActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
