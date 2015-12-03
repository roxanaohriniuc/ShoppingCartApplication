package com.example.roxanaohriniuc.petmartsuper;

import java.io.Serializable;


public class Product implements Serializable {

    private String mPName;
    private String mDescription;
    private String mCategory;
    private double mPrice;
    private double mCost;
    private int mQuantityAvailable;
    private String mId;


    /**
     *
     * @param id
     * @param PName
     * @param category
     * @param PDescription
     * @param PPrice
     * @param cost
     * @param quantityAvailable
     */
    public Product(String id, String PName, String category,String PDescription, double PPrice,
             double cost, int quantityAvailable){
        mId= id;
        mPName = PName;
        mCategory = category;
        mCost = cost;
        mQuantityAvailable = quantityAvailable;
        mDescription = PDescription;
        mPrice = PPrice;
    }

    /**
     * Returns description of product
     * @return
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Sets the description of product.
     * @param description
     */
    public void setDescription(String description) {
        mDescription = description;
    }

    /**
     * Returns the price.
     * @return
     */
    public double getPrice() {
        return mPrice;
    }
    /**
     * Returns the price.
     * @return
     */
    public String getPriceAsString() {

        return String.format("%.2f", mPrice);
    }


    /**
     * Sets the price of the product.
     * @param price
     */
    public void setPrice(double price) {
        mPrice = price;
    }
    /*
    @return cost
     */
    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

    /**
     * Returns the Product Name.
     * @return Pname
     */
    public String getPName() {
        return mPName;
    }

    /**
     * Sets the product name.
     * @param PName
     */
    public void setPName(String PName) {
        mPName = PName;
    }

    /**
     * Returns the quantity available.
     * @return
     */
    public int getQuantityAvailable() {
        return mQuantityAvailable;
    }

    /**
     * Sets the quantity available.
     * @param quantityAvailable
     */
    public void setQuantityAvailable(int quantityAvailable) {
        mQuantityAvailable = quantityAvailable;
    }

    /**
     * Returns the category of the product
     * @return
     */
    public String getCategory() {
        return mCategory;
    }

    /**
     * Sets the category of the product
     * @param category
     */
    public void setCategory(String category) {
        mCategory = category;
    }
    /*
    @return product id
     */
    public String getId() {
        return mId;
    }


}
