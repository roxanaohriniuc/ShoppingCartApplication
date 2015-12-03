package com.example.roxanaohriniuc.petmartsuper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductDescriptionActivity extends AppCompatActivity {
    protected Button mAddToCartButton;
    protected Button mReturnToSearchButton;
    protected Button mViewCartButton;
    protected TextView mProductName;
    protected TextView mProductDescription;
    protected TextView mProductPrice;
    protected TextView mProductQuantity;
    protected TextView mProductCategory;
    private  ArrayList<Product> mProducts;
    private  ArrayList<CartItem> mCart;
    protected PetMartSuperUtils utils = new PetMartSuperUtils();
    protected ShoppingCart cart = ShoppingCart.getInstance();
    Inventory inventory = Inventory.getInstance();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        setContentView(R.layout.activity_product_description);
        mProducts = inventory.getProducts();
        mCart = cart.getProducts();

        // get bundle and initialize it to a product
        Intent intent=getIntent();
        // INITIATE AND SET ALL FIELDS TO THE CORRESPONDING FIELDS.
        final Product product= (Product) intent.getSerializableExtra("product");
        final String mAccount = intent.getStringExtra("accountId");

        // set fields
        mAddToCartButton = (Button) findViewById(R.id.dAddToCartButton);
        mViewCartButton = (Button) findViewById(R.id.dViewShoppingCartP);
        mReturnToSearchButton = (Button) findViewById(R.id.dReturnToInventoryButton);

        mProductName = (TextView) findViewById(R.id.dProductNameTextView);
        mProductDescription= (TextView)findViewById(R.id.dProductDescriptionTextView);
        mProductPrice= (TextView)findViewById(R.id.dUnitPricetextView);
        mProductQuantity= (TextView)findViewById(R.id.dQuantityInStockTextView);
        mProductCategory= (TextView)findViewById(R.id.dCategoryTextView);
        // add information to fields.
        mProductName.setText("Name: "+ product.getPName());
        mProductDescription.setText("Description: " + product.getDescription());
        mProductPrice.setText("Unit Price: " + product.getPriceAsString());
        mProductQuantity.setText("Quantity: " + product.getQuantityAvailable() + "");
        mProductCategory.setText("Category: " + product.getCategory());
        mAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(1);
                cart.addProduct(item);
                Toast.makeText(ProductDescriptionActivity.this,
                        "Your item was added to the shopping cart.", Toast.LENGTH_LONG);
                int updatedQuantity = product.getQuantityAvailable();
                int cartItemPos = cart.getCartItemIndex(product);
                if (updatedQuantity == 0) {
                    //Toast infoM = new Toast("Out of stock");
                    Toast toast = Toast.makeText(ProductDescriptionActivity.this,
                            "Sorry, the Product is out of stock!", Toast.LENGTH_LONG);
                } else {
                    if (cartItemPos == -1) {
                        mCart.add(item);
                    } else {
                        int cartQuantity = mCart.get(cartItemPos).getQuantity() + 1;
                        mCart.get(cartItemPos).setQuantity(cartQuantity);
                    }
                    // update database with quantity of products
                    updatedQuantity--;
                    String mProductId =product.getId();

                    product.setQuantityAvailable((updatedQuantity));
                    //update singletons
                    inventory.setProducts(mProducts);
                    cart.setProducts(mCart);
                    mProductQuantity.setText("Quantity: " + updatedQuantity);
                    utils.addToShoppingCartDB(manager, mAccount, mProductId);

                }
            }
        });
        mViewCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescriptionActivity.this,
                        ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
        mReturnToSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescriptionActivity.this,
                        MainActivity.class);
                intent.putExtra("accountId", mAccount);
                intent.putExtra("fromDescription", true);
                startActivity(intent);
                // add functionality here --> needs to pass json account id.


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_description, menu);
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
            Intent intent = new Intent(ProductDescriptionActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
