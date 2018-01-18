package com.example.android.droidchef.Utils;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Astraeus on 1/10/2018.
 * This class will house all the network related tasks
 */

public class NetworkUtils {

    private static final String RECIPES_URL_STRING = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * Helper method that creates a URL object from the provided RECIPES_URL_STRING
     *
     * @return the new URL object
     */
    public static URL buildURL() {
        URL url = null;
        try {
            url = new URL(RECIPES_URL_STRING);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method takes in a URL object and makes the network request. It then returns the json response
     * as a String
     */
    public static String makeHttpRequest(URL url) throws IOException {
        // If the provided URL is null or empty, exit the method early
        if(url == null || TextUtils.isEmpty(url.toString())) return null;

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);

            // Use Scanner and StringBuilder to handle the response and build the return String
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            StringBuilder stringBuilder = new StringBuilder();

            while(scanner.hasNext()){
                stringBuilder.append(scanner.next());
            }
            inputStream.close();

            return stringBuilder.toString();
        } finally {
            urlConnection.disconnect();
        }
    }


}
