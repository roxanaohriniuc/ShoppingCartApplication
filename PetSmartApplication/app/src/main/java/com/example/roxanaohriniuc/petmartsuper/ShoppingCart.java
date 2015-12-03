package com.example.roxanaohriniuc.petmartsuper;

import java.util.ArrayList;

/**
 * Created by roxanaohriniuc on 11/27/15.
 */
public class ShoppingCart {
        private ArrayList<CartItem> mProducts;
        private static ShoppingCart mShoppingCart = new ShoppingCart();

        private ShoppingCart () {
            mProducts = new ArrayList<CartItem>();
        }

        public static ShoppingCart getInstance(){
            return mShoppingCart;
        }

        public void addProduct(CartItem c) {
            boolean exists = false;
            for (CartItem ci : mProducts) {
                if (c.getProduct().getPName().equals(ci.getProduct().getPName())) {
                    if (ci.getQuantity() >= 0) {
                        ci.setQuantity(ci.getQuantity() + c.getQuantity());
                        exists =true;
                    }
                }
            }
            if(!exists) {
                mProducts.add(c);
            }
        }
        public void removeProduct(CartItem c){
            for(CartItem ci : mProducts){
                if( c.getProduct().getPName().equals(ci.getProduct().getPName())){
                    if(ci.getQuantity() > 0){
                        ci.setQuantity(ci.getQuantity()-1);
                    }
                    if(ci.getQuantity() == 0){
                        mProducts.remove(ci);
                    }
                }
            }
        }
        public ArrayList<CartItem> getProducts() {
            return mProducts;
        }

        public int getCartItemIndex(Product product)
        {
            CartItem item = null;
            for (int i = 0; i < mProducts.size(); i++)
            {
                CartItem c = mProducts.get(i);
                if(c.getProduct().getId() == product.getId()) {
                    return i;
                }
            }
           return -1;
        }

        public void setProducts(ArrayList<CartItem> products){
            mProducts = products;
        }

    }

