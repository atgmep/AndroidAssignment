package day12.mobilestudy.make5;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    private class SendRegisterTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... args) {

            URL url;
            HttpURLConnection urlConnection;
            String urlStart = Fix.URL + "/api/register";
            String method = "POST";
            final String[] paramName = {"username", "password"};
            final String[] paramValue = {username, password};
            try {
                url = new URL(urlStart);
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
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
                    final Response response = gson.fromJson(RequestTask.readStream(urlConnection.getInputStream()), Response.class);

                    int statusCode = response.getStatusCode();
                    if (statusCode == Response.STATUS_FAIL) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        return null;
                    } else if (statusCode == Response.STATUS_SERVER_ERROR) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        return null;
                    } else if (statusCode == Response.STATUS_SUCCESS) {

                        Intent intent = RegisterActivity.this.getIntent();
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        RegisterActivity.this.setResult(RESULT_OK, intent);
                        finish();
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }


    public void clickToRegister(View view) {
        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtConfirm = findViewById(R.id.edtConfirm);
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        String confirm = edtConfirm.getText().toString();


        if (!password.equals(confirm)) {
            Toast.makeText(RegisterActivity.this, "Confirm password not match", Toast.LENGTH_LONG).show();
        } else {
            SendRegisterTask sendRegisterTask = new SendRegisterTask();
            sendRegisterTask.execute();
        }


    }
}
