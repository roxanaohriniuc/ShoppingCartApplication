package com.example.roxanaohriniuc.petmartsuper;

import java.util.ArrayList;

/**
 * Singleton Class
 * Represents the shopping cart of the current user
 * Has product and quantity in cart of each item
 */
public class ShoppingCart {

    private ArrayList<CartItem> mProducts;
    private static ShoppingCart mShoppingCart = new ShoppingCart();

    /*construct an array list of cart items*/
    private ShoppingCart() {
        mProducts = new ArrayList<CartItem>();
    }

    /**
     * Gets instance of product
     * used to initalize singleton
     * @return
     */
    public static ShoppingCart getInstance() {
        return mShoppingCart;
    }

    /**
     * Add a product to the shopping cart
     *
     * @param c
     */
    public void addProduct(CartItem c) {
        boolean exists = false;
        for (CartItem ci : mProducts) {
            if (c.getProduct().getPName().equals(ci.getProduct().getPName())) {
                if (ci.getQuantity() >= 0) {
                    ci.setQuantity(ci.getQuantity() + c.getQuantity());
                    exists = true;
                }
            }
        }
        if (!exists) {
            mProducts.add(c);
        }
    }

    /**
     * removes product from shopping cart
     *
     * @param c
     */
    public void removeProduct(CartItem c) {
        for (CartItem ci : mProducts) {
            if (c.getProduct().getPName().equals(ci.getProduct().getPName())) {
                if (ci.getQuantity() > 0) {
                    ci.setQuantity(ci.getQuantity() - 1);
                }
                if (ci.getQuantity() == 0) {
                    mProducts.remove(ci);
                }
            }
        }
    }

    public ArrayList<CartItem> getProducts() {
        return mProducts;
    }

    /**
     * Gets index of product in cart
     * @param product
     * @return
     */
    public int getCartItemIndex(Product product) {
        CartItem item = null;
        for (int i = 0; i < mProducts.size(); i++) {
            CartItem c = mProducts.get(i);
            if (c.getProduct().getId() == product.getId()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Sets products to the input list
     * @param products
     */
    public void setProducts(ArrayList<CartItem> products) {
        mProducts = products;
    }
}

