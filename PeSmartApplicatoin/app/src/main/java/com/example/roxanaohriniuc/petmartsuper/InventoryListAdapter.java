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

import java.util.ArrayList;

/**
 * Created by Roxana Ohriniuc on 11/21/2015.
 */
public class InventoryListAdapter extends BaseAdapter {
    private final MainActivity mActivity;
    private final ArrayList<Product> mProducts;
    private final ArrayList<CartItem> mCart;
    ShoppingCart shoppingCart = ShoppingCart.getInstance();
    Inventory inventory = Inventory.getInstance();
    private String mAccount;
    private PetMartSuperUtils utils = new PetMartSuperUtils();
    private final ConnectivityManager mManager;

    public InventoryListAdapter(MainActivity activity, String accountId, ConnectivityManager manager){
        mAccount = accountId;
        mActivity = activity;
        mProducts =  inventory.getProducts();
        mCart = shoppingCart.getProducts();
        mManager = manager;
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Product getItem(int position) {
        return mProducts.get(position);
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
        holder.mProductName.setText(mProducts.get(position).getPName());
        holder.mProductPrice.setText(mProducts.get(position).getPriceAsString());
        holder.mQuantityAvailable.setText(mProducts.get(position).getQuantityAvailable() + "");
        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int updatedQuantity = mProducts.get(position).getQuantityAvailable();
                int cartItemPos = shoppingCart.getCartItemIndex(mProducts.get(position));

                if (updatedQuantity == 0) {
                    //Toast infoM = new Toast("Out of stock");
                    Toast toast = Toast.makeText(mActivity, "Sorry, the Product is out of stock!", Toast.LENGTH_LONG);
                } else {
                    if (cartItemPos == -1) {
                        CartItem item = new CartItem();
                        item.setQuantity(1);
                        item.setProduct(mProducts.get(position));
                        mCart.add(item);
                    } else {
                        int cartQuantity = mCart.get(cartItemPos).getQuantity() + 1;
                        mCart.get(cartItemPos).setQuantity(cartQuantity);
                    }
                    // update database with quantity of products
                    updatedQuantity--;
                    String mProductId = mProducts.get(position).getId();

                    mProducts.get(position).setQuantityAvailable((updatedQuantity));
                    //update singletons
                    inventory.setProducts(mProducts);
                    shoppingCart.setProducts(mCart);
                    holder.mQuantityAvailable.setText(updatedQuantity + "");
                    utils.addToShoppingCartDB(mManager, mAccount, mProductId);

                    mActivity.UpdateTotal();
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,
                        ProductDescriptionActivity.class);
                intent.putExtra("product", mProducts.get(position));
                intent.putExtra("accountId", mAccount);
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
