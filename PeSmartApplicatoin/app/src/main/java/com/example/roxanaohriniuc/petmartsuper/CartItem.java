package com.example.roxanaohriniuc.petmartsuper;

import java.io.Serializable;

/**
 * Created by roxanaohriniuc on 11/27/15.
 */
public class CartItem implements Serializable{
    private Product mProduct;
    private int mQuantity;


    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }



}
