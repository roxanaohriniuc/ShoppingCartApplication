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
 * Created by roxanaohriniuc on 11/30/15.
 */
public class OrderView extends BaseAdapter {
    private ArrayList<Product> mProducts;
    private ArrayList<CartItem> mCartItems;
    private double mTotalPrice = 0;
    ShoppingCart shoppingCart = ShoppingCart.getInstance();
    Inventory inventory = Inventory.getInstance();
    private String mAccount;
    private PetMartSuperUtils utils = new PetMartSuperUtils();
    private final ConnectivityManager mManager;
    private TextView mTotalText;
    private final ShoppingCartActivity mShoppingCartActivity;

    public OrderView(ShoppingCartActivity shoppingCartActivity, String accountId, ConnectivityManager manager, TextView totalText){
        mAccount = accountId;
        mShoppingCartActivity = shoppingCartActivity;
        mProducts =  inventory.getProducts();
        mCartItems = shoppingCart.getProducts();
        mManager = manager;
        mTotalText = totalText;
    }
    @Override
    public int getCount() {
        return mCartItems.size();
    }

    @Override
    public CartItem getItem(int position) {
        return mCartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){

            convertView = LayoutInflater.from(mShoppingCartActivity).inflate(R.layout.list_item2, null);
            holder = new ViewHolder();
            holder.mProductName = (TextView) convertView.findViewById(R.id.cartProductLabel);
            holder.mProductPrice = (TextView) convertView.findViewById(R.id.cartPriceTextView);
            holder.mAddProduct = (Button) convertView.findViewById(R.id.cartAddProductButton);
            holder.mDeleteProduct = (Button) convertView.findViewById(R.id.cartDeleteProductButton);
            holder.mQuantityInCart = (TextView) convertView.findViewById(R.id.cartProductQuantity);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mProductName.setText(mCartItems.get(position).getProduct().getPName());
        holder.mProductPrice.setText(mCartItems.get(position).getProduct().getPrice()+ "");
        holder.mQuantityInCart.setText(mCartItems.get(position).getQuantity() + "");

        // on pressing the "+" button
        holder.mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = mCartItems.get(position);
                Product product = inventory.getProductById(item.getProduct().getId());

                int cartQuantity = item.getQuantity();
                int updatedQuantity = product.getQuantityAvailable();

                if (updatedQuantity == 0) {
                    //Toast infoM = new Toast("Out of stock");
                    Toast toast = Toast.makeText(mShoppingCartActivity, "Sorry, the Product is out of stock!", Toast.LENGTH_LONG);
                } else {
                    // update database with quantity of products
                    updatedQuantity--;
                    cartQuantity++;

                    inventory.getProductById(product.getId()).setQuantityAvailable(updatedQuantity);
                    mProducts = inventory.getProducts();
                    mCartItems.get(position).setQuantity(cartQuantity);
                    shoppingCart.setProducts(mCartItems);
                    holder.mQuantityInCart.setText(cartQuantity + "");
                    utils.addToShoppingCartDB(mManager, mAccount, product.getId());
                    mShoppingCartActivity.UpdateTotal();

                }
            }
        });
        // on pressing the "-" button
        holder.mDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = mCartItems.get(position);
                Product product = inventory.getProductById(item.getProduct().getId());

                int cartQuantity = item.getQuantity();
                int updatedQuantity = product.getQuantityAvailable();

                if (cartQuantity == 0) {
                    //Toast infoM = new Toast("Out of stock");
                    Toast toast = Toast.makeText(mShoppingCartActivity, "None in shopping cart!", Toast.LENGTH_LONG);
                } else {
                    // update database with quantity of products
                    updatedQuantity++;
                    inventory.getProductById(product.getId()).setQuantityAvailable(updatedQuantity);
                    mProducts = inventory.getProducts();
                    cartQuantity--;
                    if(cartQuantity == 0) {
                        mCartItems.remove(position);
                        notifyDataSetChanged();
                    }
                    else
                        mCartItems.get(position).setQuantity(cartQuantity);


                    holder.mQuantityInCart.setText(cartQuantity + "");
                    utils.removeFromShoppingCartDB(mManager, mAccount, product.getId());
                }
                shoppingCart.setProducts(mCartItems);
               // ((MainActivity) holder).UpdateTotal();
                mShoppingCartActivity.UpdateTotal();
                }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mShoppingCartActivity,
                        ProductDescriptionActivity.class);
                intent.putExtra("product", mProducts.get(position));
                intent.putExtra("accountId", mAccount);
                mShoppingCartActivity.startActivity(intent);
            }
        });
        return convertView;
    }

    private void UpdateTotal()
    {
        double total = 0;
        for(CartItem item : shoppingCart.getProducts() )
        {
            total += (item.getQuantity() * item.getProduct().getPrice());
        }
        mTotalText.setText(String.format("%.2f", total));
    }

    /**
     * Helper class. Holds the items in the view.
     */
    public static class ViewHolder {
        TextView mProductName;
        Button mAddProduct;
        Button mDeleteProduct;
        TextView mProductPrice;
        TextView mQuantityInCart;
    }
}
