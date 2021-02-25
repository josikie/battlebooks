package com.josikie.b.battlebooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getSimpleName();
    private static String baseURL = "https://www.googleapis.com/books/v1/volumes?q=battle&maxResults=20";
    private static String searchURL = "https://www.googleapis.com/books/v1/volumes?q=battle+intitle:";

    public static List<Books> fetchBooksData(String link) {
        List<Books> booksList = null;
        Log.i(LOG_TAG, "TEST : fetchEarthquakeData method is called");
        if (link.equals(baseURL)) {
            URL url = checkedURL(link);

            String urlJSON = null;
            try {
                urlJSON = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);

            }
            booksList = booksparseJSON(urlJSON);
            // Kembalikan daftar {@link Earthquake}
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(link.equals(searchURL)){
            URL url = checkedURL(baseURL);

            String urlJSON = null;
            try{
                urlJSON = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
            booksList = booksparseJSON(urlJSON);
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }else{
            URL url = checkedURL(link);
            String urls = null;
            try {
                urls = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
            booksList = booksparseJSON(urls);
            // Kembalikan daftar {@link Earthquake}
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return booksList;
    }

    private static URL checkedURL(String urlJSON){
        URL url = null;
        try {
            url = new URL(urlJSON);
        }catch (MalformedURLException exception){
            Log.e(LOG_TAG, "Error with creating URL" + exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        HttpURLConnection httpsURLConnection = null;
        InputStream inputStream = null;

        // If the URL is null, then return early.
        if (url == null){
            return jsonResponse;
        }

        try{
            httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setReadTimeout(10000);
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() == 200){
                inputStream = httpsURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else if(httpsURLConnection.getResponseCode() != 200){
                Log.e(LOG_TAG, "Error exception code : " + httpsURLConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results : " + httpsURLConnection.getResponseCode());
        }finally {
            if (httpsURLConnection != null){
                httpsURLConnection.disconnect();
            }
            if (inputStream != null){
                // Menutup input stream bisa memunculkan IOException, karena itu
                // lambang metode (method signature) makeHttpRequest(URL url) harus menyatakan IOException
                // dapat dimunculkan.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Books> booksparseJSON(String urlJSON){

        if (TextUtils.isEmpty(urlJSON)){
            return null;
        }

        List<Books> booksList = new ArrayList<>();


        try {
            JSONObject baseJSON = new JSONObject(urlJSON);
            JSONArray items = baseJSON.getJSONArray("items");
            for (int i = 0; i < items.length(); i++){
                JSONObject currentBooks = items.getJSONObject(i);
                JSONObject volumeinfo = currentBooks.getJSONObject("volumeInfo");


                // title
                String title = volumeinfo.getString("title");

                // author
                JSONArray bookAuthors = null;
                try{
                    bookAuthors = volumeinfo.getJSONArray("authors");
                }catch (JSONException ignored){

                }

                // convert the author to string
                String bookAuthorsString = "";
                if (bookAuthors == null){
                    bookAuthorsString = "Unknown";
                }else{
                    int countAuthors = bookAuthors.length();
                    for (int e = 0; e < countAuthors; e++){
                        String currentName = bookAuthors.getString(e);
                        if (bookAuthorsString.isEmpty()){
                            bookAuthorsString = currentName;
                        }else if (e == countAuthors - 1){
                            bookAuthorsString = bookAuthorsString + " and " + currentName;
                        }else {
                            bookAuthorsString = bookAuthorsString + ", " + bookAuthorsString;
                        }
                    }
                }

                //image
                JSONObject bookImageLinks = null;
                try {
                    bookImageLinks = volumeinfo.getJSONObject("imageLinks");
                } catch (JSONException ignored) {
                }
                // Convert the image link to a string
                String bookSmallThumbnail = "";
                if ( bookImageLinks == null){
                    bookSmallThumbnail = null;
                }else{
                    bookSmallThumbnail  = bookImageLinks.getString("smallThumbnail");
                }

                String canonicalVolumeLink = volumeinfo.getString("infoLink");
                Books books = new Books(bookSmallThumbnail, title, bookAuthorsString, canonicalVolumeLink);
                booksList.add(books);
            }

        } catch (JSONException e){

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return booksList;
    }



}
