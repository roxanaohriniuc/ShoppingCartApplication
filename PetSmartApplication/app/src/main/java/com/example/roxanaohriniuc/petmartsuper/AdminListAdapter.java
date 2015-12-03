package com.example.roxanaohriniuc.petmartsuper;

import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by roxanaohriniuc on 12/1/15.
 */
public class AdminListAdapter extends BaseAdapter {

        private final AdminActivity mActivity;
        private final ArrayList<Product> mProducts;
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        Inventory inventory = Inventory.getInstance();
        private PetMartSuperUtils utils = new PetMartSuperUtils();
        private final ConnectivityManager mManager;

        public AdminListAdapter(AdminActivity activity, ConnectivityManager manager) {
            mActivity = activity;
            mProducts = inventory.getProducts();
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
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item3, null);
                holder.mProductName = (TextView) convertView.findViewById(R.id.productLabelAdmin);
                holder.mProductPrice = (TextView) convertView.findViewById(R.id.priceTextViewAdmin);
                holder.mQuantityAvailable = (TextView) convertView.findViewById(R.id.quantityInStockAdmin);
                holder.mViewProduct = (Button) convertView.findViewById(R.id.viewProductButton);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mProductName.setText(mProducts.get(position).getPName());
            holder.mProductPrice.setText(mProducts.get(position).getPriceAsString());
            holder.mQuantityAvailable.setText(mProducts.get(position).getQuantityAvailable() + "");
            holder.mViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start view product page
                   Product p = mProducts.get(position);
                   mActivity.sendIntentToViewProducts(p);
                }
            });
            return convertView;
        }

        public static class ViewHolder {
            TextView mProductName;
            TextView mProductPrice;
            TextView mQuantityAvailable;
            Button mViewProduct;
        }

    }
