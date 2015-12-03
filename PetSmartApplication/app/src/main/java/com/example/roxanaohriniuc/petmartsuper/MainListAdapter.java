package com.example.roxanaohriniuc.petmartsuper;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Roxana Ohriniuc on 11/21/2015.
 */
public class MainListAdapter extends BaseAdapter {
    private final MainActivity mActivity;

    ShoppingCart shoppingCart = ShoppingCart.getInstance();
    Inventory inventory = Inventory.getInstance();
    private String accountID;
    private PetMartSuperUtils utils = new PetMartSuperUtils();

    private final ConnectivityManager mManager;

    public MainListAdapter(MainActivity activity, String accountId, ConnectivityManager manager){
        this.accountID = accountId;
        mActivity = activity;
        mManager = manager;
    }

    @Override
    public int getCount() {
        return inventory.getProducts().size();
    }

    @Override
    public Product getItem(int position) {
        return inventory.getProducts().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item, null);
            holder.mProductName = (TextView) convertView.findViewById(R.id.productLabel);
            holder.mProductPrice = (TextView) convertView.findViewById(R.id.priceTextViewAdmin);
            holder.addProduct = (Button) convertView.findViewById(R.id.addProductButton);
            holder.mQuantityAvailable = (TextView) convertView.findViewById(R.id.quantity);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        final Product selectedProduct = inventory.getProducts().get(position);
        holder.mProductName.setText(selectedProduct.getPName());
        holder.mProductPrice.setText(selectedProduct.getPriceAsString());
        holder.mQuantityAvailable.setText(selectedProduct.getQuantityAvailable() + "");

        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int updatedQuantity = selectedProduct.getQuantityAvailable();
                int cartItemPos = shoppingCart.getCartItemIndex(selectedProduct);

                if (updatedQuantity == 0) {
                    //Toast infoM = new Toast("Out of stock");
                    Toast toast = Toast.makeText(mActivity, "Sorry, the Product is out of stock!", Toast.LENGTH_LONG);
                } else {
                    if (cartItemPos == -1) {
                        CartItem item = new CartItem();
                        item.setQuantity(1);
                        item.setProduct(selectedProduct);
                        shoppingCart.addProduct(item);
                    } else {
                        int cartQuantity = shoppingCart.getProducts().get(cartItemPos).getQuantity() + 1;
                        shoppingCart.getProducts().get(cartItemPos).setQuantity(cartQuantity);
                    }
                    // update database with quantity of products
                    String mProductId = selectedProduct.getId();
                    inventory.getProducts().get(position).setQuantityAvailable((updatedQuantity));

                    holder.mQuantityAvailable.setText(updatedQuantity + "");
                    utils.addToShoppingCartDB(mManager, accountID, mProductId);

                    mActivity.UpdateTotal();
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,
                        ProductDescriptionActivity.class);
                intent.putExtra("product", inventory.getProducts().get(position));
                intent.putExtra("accountID", accountID);
                mActivity.startActivity(intent);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        TextView mProductName;
        Button addProduct;
        TextView mProductPrice;
        TextView mQuantityAvailable;
        Button mViewProduct;
    }
    }
