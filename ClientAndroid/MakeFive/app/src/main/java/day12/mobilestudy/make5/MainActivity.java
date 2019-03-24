package day12.mobilestudy.make5;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final int REGISTER_REQ_CODE = 1112;
    public static final int TITLE_REQ_CODE = 1113;


    public static final String[] responseList = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }


//    private void login(String username, String password) {
//        RequestTask requestTask = new RequestTask();
//        final String[] paramName = {"username", "password"};
//        final String[] paramValue = {username, password};
//        try {
//            Thread t = requestTask.sendHttp(Fix.URL + "/sign_in", "POST", paramName, paramValue, 9, Fix.TIME_OUT);
//            t.start();
//            t.join();
//            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
//            Response response = gson.fromJson(responseList[9], Response.class);
//            String role = response.getMessage();
//            if (role.equals(Fix.ROL_MEM)) {
//                String point = response.getData().get(0);
//                TitleActivity.username = username;
//                TitleActivity.point = point;
//
//                Intent intent = new Intent(MainActivity.this, TitleActivity.class);
//                startActivity(intent);
//            } else {
//                Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_LONG).show();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


    private void login(final String username, String password) {

        final String[] paramName = {"username", "password"};
        final String[] paramValue = {username, password};


        ApiCaller apiCaller = new ApiCaller(Fix.URL + "/sign_in", "POST", paramName, paramValue, 100) {
            @Override
            public void functionFail(Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void functionError(Response response) {

            }

            @Override
            public void functionSuccess(final Response response) {
                String role = response.getMessage();
                if (role.equals(Fix.ROL_MEM)) {
                    String point = response.getData().get(0);
                    TitleActivity.username = username;
                    TitleActivity.point = point;
                    Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                    startActivityForResult(intent, TITLE_REQ_CODE);
                }
            }

            @Override
            public void functionNotOk(int responseCode, HttpURLConnection urlConnection) {
                System.out.println(responseCode);
            }
        };
        apiCaller.execute();
    }


    public void clickToLogin(View view) {
        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);
        login(edtUsername.getText().toString(), edtPassword.getText().toString());
    }

    public void clickToRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REGISTER_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                login(username, password);
            }
        }
    }
}
