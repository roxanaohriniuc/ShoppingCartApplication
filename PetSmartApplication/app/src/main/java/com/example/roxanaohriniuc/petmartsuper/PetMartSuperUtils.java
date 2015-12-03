package com.example.roxanaohriniuc.petmartsuper;

import android.app.FragmentManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class PetMartSuperUtils {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * Default constructor.
     */
    public PetMartSuperUtils(){
    }

    /**
     * Creates a thread sleep of 2s
     */
    public void waitHere() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     *  Creates an alert dialog to inform the user of any errors.
     */
    public  void alertUserAboutError(FragmentManager fragmentManager) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(fragmentManager, "error_dialog");
    }

    /**
     * Checks for connectivity to network.
     * @param manager
     * @return
     */
    public boolean isNetworkAvailable(ConnectivityManager manager) {

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }
    public void addToShoppingCartDB(ConnectivityManager manager, String mAccount, String mProductId){
        String url = "http://shoppingcart-api-8000.herokuapp.com/api/shoppingcart";
        if (isNetworkAvailable(manager)) {
            OkHttpClient client = new OkHttpClient();
            String b = "{\"accountId\" : \"" + mAccount + "\", " +
                    "\"productId\": \"" + mProductId + "\"}";
            //create a post request to API
            RequestBody body = RequestBody.create(JSON, b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        // Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                        }
                    } catch (Exception e) {  // log the exception
                         e.printStackTrace();
                    }
                }
            });
        } else { // inform user of network not available

        }
    }
    public void removeFromShoppingCartDB(ConnectivityManager manager, String mAccount, String mProductId){
        String url = "http://shoppingcart-api-8000.herokuapp.com/api/shoppingcart";
        if (isNetworkAvailable(manager)) {
            OkHttpClient client = new OkHttpClient();
            String b = "{\"accountId\" : \"" + mAccount + "\", " +
                    "\"productId\": \"" + mProductId + "\"}";
            //create a delete request to API
            RequestBody body = RequestBody.create(JSON, b);
            Request request = new Request.Builder()
                    .url(url)
                    .delete(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        // Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                        }
                    } catch (Exception e) {  // log the exception
                        e.printStackTrace();
                    }
                }
            });
        } else { // inform user of network not available

        }
    }

    public void placeOrder(ConnectivityManager manager, String mAccount, double totalPrice, double totalCost){
        String url = "http://shoppingcart-api-8000.herokuapp.com/api/orders";
        if (isNetworkAvailable(manager)) {
            OkHttpClient client = new OkHttpClient();
            String order = "{\"buyerId\" : \"" + mAccount + "\", " +
                    "\"totalPrice\": " + totalPrice + ", " +
                    "\"totalCost\": " + totalCost + "}";
            //create a post request to API
            RequestBody body = RequestBody.create(JSON, order);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        // Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                        }
                    } catch (Exception e) {  // log the exception
                        e.printStackTrace();
                    }
                }
            });
        } else { // inform user of network not available

        }

    }

    public void clearShoppingCart(ConnectivityManager manager, String mAccount) {
        String url = "http://shoppingcart-api-8000.herokuapp.com/api/accounts/" + mAccount;
        if (isNetworkAvailable(manager)) {
            OkHttpClient client = new OkHttpClient();
            String user = "{\"shoppingCart\" : []}";
            //create a put request to API
            RequestBody body = RequestBody.create(JSON, user);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        // Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                        }
                    } catch (Exception e) {  // log the exception
                        e.printStackTrace();
                    }
                }
            });
        } else { // inform user of network not available

        }
    }

    public void addToInventory(ConnectivityManager manager, Product product){
        String url = "http://shoppingcart-api-8000.herokuapp.com/api/inventory";
        if (isNetworkAvailable(manager)) {
            OkHttpClient client = new OkHttpClient();
            String b = "{\"name\" : \"" + product.getPName() + "\", " +
                    "\"unitCost\" : " + product.getCost() + ", " +
                    "\"unitPrice\" : " + product.getPrice() + ", " +
                    "\"quantity\" : " + product.getQuantityAvailable() + ", " +
                    "\"category\" : \"" + product.getCategory() + "\", " +
                    "\"description\": \"" + product.getDescription() + "\"}";
            //create a post request to API
            RequestBody body = RequestBody.create(JSON, b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        // Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                        }
                    } catch (Exception e) {  // log the exception
                        e.printStackTrace();
                    }
                }
            });
        } else { // inform user of network not available

        }
    }

    public Boolean isDouble(String text)
    {
        try
        {
            Double.parseDouble(text);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public Boolean isInteger(String text)
    {
        try
        {
            Integer.parseInt(text);
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }


    }

