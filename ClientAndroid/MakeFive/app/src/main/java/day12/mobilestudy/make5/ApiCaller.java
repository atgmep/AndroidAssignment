package day12.mobilestudy.make5;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class ApiCaller extends AsyncTask<String, Integer, String> {

    private String urlStr;
    private String methodStr;
    private String[] paramName;
    private String[] paramValue;
    private int timeout;

    public ApiCaller(String urlStr, String methodStr, String[] paramName, String[] paramValue, int timeout) {
        super();
        this.urlStr = urlStr;
        this.methodStr = methodStr;
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.timeout = timeout;
    }

    protected String doInBackground(String... args) {

        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(methodStr);
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
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
                Response response = gson.fromJson(RequestTask.readStream(urlConnection.getInputStream()), Response.class);
                int statusCode = response.getStatusCode();
                if (statusCode == Response.STATUS_FAIL) {
                    functionFail(response);
                } else if (statusCode == Response.STATUS_SERVER_ERROR) {
                    functionError(response);
                } else if (statusCode == Response.STATUS_SUCCESS) {
                    functionSuccess(response);
                }
            } else {
                functionNotOk(responseCode, urlConnection);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void functionFail(Response response);

    public abstract void functionError(Response response);

    public abstract void functionSuccess(Response response);

    public abstract void functionNotOk(int responseCode, HttpURLConnection urlConnection);
}