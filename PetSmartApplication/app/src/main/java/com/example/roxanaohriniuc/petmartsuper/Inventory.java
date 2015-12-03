package com.example.roxanaohriniuc.petmartsuper;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventory implements Serializable {
    private ArrayList<Product> mProducts;
    private static Inventory inventory = new Inventory();

    private Inventory () {
        mProducts = new ArrayList<Product>();
    }

    public static Inventory getInstance(){
        return inventory;
    }
    /*
    @param product
    @return boolean value when product is added
     */
    public boolean addProduct(Product p){
        mProducts.add(p);
        return true;
    }
    /*
    @param product
     */
    public void removeProduct(Product p){

    }
    /*
    @return arraylist of products
     */
    public ArrayList<Product> getProducts() {
        return mProducts;
    }
    /*
    @param arraylist of products
     */
    public void setProducts(ArrayList<Product> products){
        mProducts = products;
    }

    public void setProducts(String jsonString)  {
        try{
        JSONArray json = new JSONArray(jsonString);
        for(int i = 0; i < json.length(); i++)
            {
                JSONObject obj = json.getJSONObject(i);
                String id = obj.getString("_id");
                String name = obj.getString("name");
                String description = obj.getString("description");
                String category = obj.getString("category");
                double price = obj.getDouble("unitPrice");
                double cost = obj.getDouble("unitCost");
                int quantity = obj.getInt("quantity");
                mProducts.add(new Product(id, name, category, description, price, cost, quantity));
            }
    }
        catch(Exception e){

        }
    }
    /*
    @param string id of product
    @return product based off id
     */
    public Product getProductById(String id) {

        for (Product p : mProducts) {
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }
}
