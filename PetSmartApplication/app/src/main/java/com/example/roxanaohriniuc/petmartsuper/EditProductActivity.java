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

public class EditProductActivity extends AppCompatActivity {
    protected Product product;
    protected EditText mProductName;
    protected EditText mProductDescription;
    protected EditText mUnitCost;
    protected EditText mUnitPrice;
    protected EditText mQuantityOH;
    protected EditText mCategory;
    protected Button mEditProductButton;
    protected Button mReturnToSearch;
    protected  Inventory inventory = Inventory.getInstance();
    protected  PetMartSuperUtils util = new PetMartSuperUtils();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        final ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            // if you get a product, set the fields to its contents for editing
            product = (Product) extras.getSerializable("product");
            mProductName = (EditText) findViewById(R.id.eProductNameTextView);
            mProductDescription  = (EditText) findViewById(R.id.eProductDescriptionTextView);
            mUnitCost  = (EditText) findViewById(R.id.eUnitCostTextView);
            mUnitPrice  = (EditText) findViewById(R.id.eUnitPricetextView);
            mQuantityOH  = (EditText) findViewById(R.id.eQuantityInStockTextView);
            mCategory  = (EditText) findViewById(R.id.eCategoryTextView);
            mEditProductButton = (Button) findViewById(R.id.editORaddProductButton);
            mReturnToSearch = (Button) findViewById(R.id.eReturnToInventoryButton);

            // set fields with existing product info
            mProductName.setText(product.getPName());
            mProductDescription.setText( product.getDescription());
            mUnitCost.setText( product.getCost() +"");
            mUnitPrice.setText( product.getPriceAsString());
            mQuantityOH.setText( product.getQuantityAvailable() + "");
            mCategory.setText(product.getCategory());
            mEditProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // edit existing product or add product  to inventory
                    String name = mProductName.getText().toString();
                    String desc = mProductDescription.getText().toString();
                    String cost = mUnitCost.getText().toString();
                    String price = mUnitPrice.getText().toString();
                    String quant = mQuantityOH.getText().toString();
                    String cat = mCategory.getText().toString();

                }
            });
            mReturnToSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //return to inventory view.
                    Intent intent = new Intent(EditProductActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            });

        }
        else{
            // set empty fields for adding new product
            //check if any field is left empty
            mProductName = (EditText) findViewById(R.id.eProductNameTextView);
            mProductDescription  = (EditText) findViewById(R.id.eProductDescriptionTextView);
            mUnitCost  = (EditText) findViewById(R.id.eUnitCostTextView);
            mUnitPrice  = (EditText) findViewById(R.id.eUnitPricetextView);
            mQuantityOH  = (EditText) findViewById(R.id.eQuantityInStockTextView);
            mCategory  = (EditText) findViewById(R.id.eCategoryTextView);
            mEditProductButton = (Button) findViewById(R.id.editORaddProductButton);
            mReturnToSearch = (Button) findViewById(R.id.eReturnToInventoryButton);

            mEditProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get inputs and validate

                    String name = mProductName.getText().toString().trim();
                    String desc = mProductDescription.getText().toString().trim();
                    String cost = mUnitCost.getText().toString().trim();
                    String price = mUnitPrice.getText().toString().trim();
                    String quant = mQuantityOH.getText().toString().trim();
                    String cat = mCategory.getText().toString().trim();

                    ArrayList<String> allowedCat = new ArrayList<String>();
                    allowedCat.add("toys");
                    allowedCat.add("food");
                    allowedCat.add("grooming");
                    allowedCat.add("bedding");

                    if(name.trim().equals("") || desc.trim().equals("")
                            || cost.trim().equals("")
                            || price.trim().equals("")
                            || cat.trim().equals("")
                            || quant.trim().equals("")){
                        // inform user of emtpy field
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductActivity.this);
                        builder.setMessage("All fields are mandatory")
                                .setTitle("mandatory_fields")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                    else if(!(util.isDouble(cost) && util.isDouble(price)
                            && util.isInteger(quant) && allowedCat.contains(cat.toLowerCase())
                            ))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductActivity.this);
                        builder.setMessage("Invalid Values")
                                .setTitle("invalid_values")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //inform user of invalid input
                    }
                    else {
                        //create product

                        Product newProduct = new Product("nil", name, cat, desc, Double.parseDouble(price),
                                Double.parseDouble(cost),Integer.parseInt(quant));
                        inventory.addProduct(newProduct);
                        util.addToInventory(manager, newProduct);
                        Intent intent = new Intent(EditProductActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }


                }
            });
            mReturnToSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //return to inventory view.
                    Intent intent = new Intent(EditProductActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            });


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_product, menu);
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
            Intent intent = new Intent(EditProductActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
