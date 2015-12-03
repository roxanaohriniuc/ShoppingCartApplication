package com.example.roxanaohriniuc.petmartsuper;

import java.io.Serializable;


public class CartItem implements Serializable{
    private Product mProduct;
    private int mQuantity;
    /*@return product*/

    public Product getProduct() {
        return mProduct;
    }
    // @param product
    //sets new product
    public void setProduct(Product product) {
        mProduct = product;
    }
    //@return amount of products in cart
    public int getQuantity() {
        return mQuantity;
    }
    //@param amount of products in cart
    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }



}
