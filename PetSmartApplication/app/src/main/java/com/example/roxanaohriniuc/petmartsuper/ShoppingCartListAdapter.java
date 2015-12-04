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


public class ShoppingCartListAdapter extends BaseAdapter {
    ShoppingCart shoppingCart = ShoppingCart.getInstance();
    Inventory inventory = Inventory.getInstance();
    private String accountID;
    private PetMartSuperUtils utils = new PetMartSuperUtils();
    private final ConnectivityManager mManager;

    private TextView mTotalText;
    private final ShoppingCartActivity mShoppingCartActivity;

    public ShoppingCartListAdapter(ShoppingCartActivity shoppingCartActivity, String accountId, ConnectivityManager manager, TextView totalText){
        accountID = accountId;
        mShoppingCartActivity = shoppingCartActivity;
        mManager = manager;
        mTotalText = totalText;
    }
    @Override
    public int getCount() {
        return shoppingCart.getProducts().size();
    }

    @Override
    public CartItem getItem(int position) {
        return shoppingCart.getProducts().get(position);
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
        final CartItem selectedItem = shoppingCart.getProducts().get(position);

        holder.mProductName.setText(selectedItem.getProduct().getPName());
        holder.mProductPrice.setText(selectedItem.getProduct().getPrice()+ "");
        holder.mQuantityInCart.setText(selectedItem.getQuantity() + "");

        // on pressing the "+" button
        holder.mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = inventory.getProductById(selectedItem.getProduct().getId());
                int cartQuantity = selectedItem.getQuantity();
                int updatedQuantity = product.getQuantityAvailable();

                if (updatedQuantity == 0) {
                    //Toast infoM = new Toast("Out of stock");
                    Toast toast = Toast.makeText(mShoppingCartActivity, "Sorry, the Product is out of stock!", Toast.LENGTH_LONG);
                } else {
                    // update database with quantity of products
                    updatedQuantity--;
                    cartQuantity++;

                    inventory.getProductById(product.getId()).setQuantityAvailable(updatedQuantity);
                    shoppingCart.getProducts().get(position).setQuantity(cartQuantity);

                    holder.mQuantityInCart.setText(cartQuantity + "");
                    utils.addToShoppingCartDB(mManager, accountID, product.getId());
                    mShoppingCartActivity.UpdateTotal();
                }
            }
        });
        // on pressing the "-" button
        holder.mDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = shoppingCart.getProducts().get(position);
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
                    cartQuantity--;
                    if(cartQuantity == 0) {
                        shoppingCart.getProducts().remove(position);
                        notifyDataSetChanged();
                    }
                    else
                        shoppingCart.getProducts().get(position).setQuantity(cartQuantity);


                    holder.mQuantityInCart.setText(cartQuantity + "");
                    utils.removeFromShoppingCartDB(mManager, accountID, product.getId());
                }
                mShoppingCartActivity.UpdateTotal();
                }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mShoppingCartActivity,
                        ProductDescriptionActivity.class);
                intent.putExtra("product", inventory.getProducts().get(position));
                intent.putExtra("accountID", accountID);
                mShoppingCartActivity.startActivity(intent);
            }
        });
        return convertView;
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
