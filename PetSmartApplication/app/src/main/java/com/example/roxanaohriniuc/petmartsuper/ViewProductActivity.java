package com.example.roxanaohriniuc.petmartsuper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewProductActivity extends AppCompatActivity {

    protected TextView mProductName;
    protected TextView mProductDescription;
    protected TextView mUnitCost;
    protected TextView mUnitPrice;
    protected TextView mQuantityOH;
    protected TextView mCategory;
    protected Button mEditProductButton;
    protected Button mReturnToSearch;
    protected  Inventory inventory = Inventory.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        Intent intent=getIntent();
        // INITIATE AND SET ALL FIELDS TO THE CORRESPONDING FIELDS.
        final Product product= (Product) intent.getSerializableExtra("product");
        mProductName = (TextView) findViewById(R.id.aProductNameTextView);
        mProductDescription  = (TextView) findViewById(R.id.aProductDescriptionTextView);
        mUnitCost  = (TextView) findViewById(R.id.aUnitCostTextView);
        mUnitPrice  = (TextView) findViewById(R.id.aUnitPricetextView);
        mQuantityOH  = (TextView) findViewById(R.id.aQuantityInStockTextView);
        mCategory  = (TextView) findViewById(R.id.aCategoryTextView);

        mProductName.setText("Name: "+ product.getPName());
        mProductDescription.setText("Description: " + product.getDescription());
        mUnitCost.setText("Unit Cost: " + product.getCost() +"");
        mUnitPrice.setText("Unit Price: " + product.getPriceAsString());
        mQuantityOH.setText("Quantity: " + product.getQuantityAvailable() + "");
        mCategory.setText("Category: " + product.getCategory());

        mEditProductButton = (Button) findViewById(R.id.aEditProductfromDescriptionButton);
        mEditProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open Edit Page
                Intent intent = new Intent(ViewProductActivity.this, EditProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
        mReturnToSearch = (Button) findViewById(R.id.aReturnToInventoryButton);
        mReturnToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // return to inventory view
                Intent intent = new Intent(ViewProductActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_product, menu);
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
            Intent intent = new Intent(ViewProductActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
