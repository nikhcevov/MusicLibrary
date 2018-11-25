package com.github.ovchingus.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ConnectionController {

    static String callRequest(String url) throws Exception {

        if (CacheController.cacheCheck(url))
            return CacheController.get(url);
        else {

            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            // optional default is GET
            con.setRequestMethod("GET");

            //add request header TODO: log file support
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            //int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'GET' request to URL : " + url);
            //System.out.println("Response Code : " + responseCode);

            // read from URL to string
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            CacheController.put(url, response.toString());
            return response.toString();
        }
    }
}
