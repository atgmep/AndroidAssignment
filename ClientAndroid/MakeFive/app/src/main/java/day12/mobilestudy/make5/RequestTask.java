package day12.mobilestudy.make5;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestTask {

    public static final String COOKIES_HEADER = "Set-Cookie";
    public static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    public Thread sendHttp(String api, final String method, final String[] paramName, final String[] paramValue, final int respPos, final int timeout) {
        final String apiF = api;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection urlConnection;
                try {
                    url = new URL(apiF);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(method);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    if (timeout > 0) {
                        urlConnection.setConnectTimeout(timeout);
                    }
                    if (paramName.length > 0) {
                        Uri.Builder builder = new Uri.Builder();
                        for (int i = 0; i < paramName.length; i++) {
                            builder.appendQueryParameter(paramName[i], paramValue[i]);
                        }
                        String query = builder.build().getEncodedQuery();
                        OutputStream os = urlConnection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                        writer.write(query);
                        writer.flush();
                        writer.close();
                        os.close();
                    }

                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        MainActivity.responseList[respPos] = readStream(urlConnection.getInputStream());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return t;
    }


/*
    public Thread sendHttpCookie(String api, final String method, final String[] paramName, final String[] paramValue, final int respPos) {
        final String apiF = api;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection urlConnection;
                try {


                    url = new URL(apiF);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod(method);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    if (paramName.length > 0) {
                        Uri.Builder builder = new Uri.Builder();
                        for (int i = 0; i < paramName.length; i++) {
                            builder.appendQueryParameter(paramName[i], paramValue[i]);
                        }
                        String query = builder.build().getEncodedQuery();
                        OutputStream os = urlConnection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                        writer.write(query);
                        writer.flush();
                        writer.close();
                        os.close();
                    }

                    if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                        // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                        urlConnection.setRequestProperty("Cookie",
                                TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
                    }

                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        MainActivity.responseList[respPos] = readStream(urlConnection.getInputStream());
                    }


                    Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                    if (cookiesHeader != null) {
                        for (String cookie : cookiesHeader) {
                            msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return t;
    }*/


    // Converting InputStream to String
    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}