package com.example.roxanaohriniuc.petmartsuper;

import java.util.ArrayList;


public class ShoppingCart {
        private ArrayList<CartItem> mProducts;
        private static ShoppingCart mShoppingCart = new ShoppingCart();
        /*construct an array list of cart items*/
        private ShoppingCart () {
            mProducts = new ArrayList<CartItem>();
        }

        public static ShoppingCart getInstance(){
            return mShoppingCart;
        }
    /*@param cart item
      adds product to shopping cart
     */
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
        /*
        @param cart item
        removes product from shopping cart
         */
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
        /*
        @param products
        searches cart array for matching id number
        @return the id number
         */
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

        /*
        @param arraylist of products
        sets the products to field variable mProducts
         */
        public void setProducts(ArrayList<CartItem> products){
            mProducts = products;
        }

    }

