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

    private static final int LOGIN_REQ_CODE = 1112;
    public static final String[] responseList = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    public void clickToLogin(View view) {

//192.168.100.179
        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);

        RequestTask requestTask = new RequestTask();
        final String[] paramName = {"username", "password"};
        final String[] paramValue = {edtUsername.getText().toString(), edtPassword.getText().toString()};
        try {
            Thread t = requestTask.sendHttp(Fix.URL + "/sign_in", "POST", paramName, paramValue, 9);
            t.start();
            t.join();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();

            Response response = gson.fromJson(responseList[9], Response.class);
            String role = response.getData().get(0);
//            response.getStatusCode() == Response.STATUS_SUCCESS
//            Toast.makeText(MainActivity.this, responseList[9], Toast.LENGTH_LONG).show();

//            Toast.makeText(MainActivity.this, "MEMBER".equals(responseList[9]) + " "+ responseList[9], Toast.LENGTH_LONG).show();
            if (role.equals(Fix.ROL_MEM)) {
                Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_LONG).show();
            }
//            edtUsername.setText(responseList[9]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void clickToRegister(View view) {

    }


//    public void clickToInput(View view) {
//        Intent intent = new Intent(MainActivity.this, InputActivity.class);
//        startActivityForResult(intent, LOGIN_REQ_CODE);
////        startActivity(intent);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == LOGIN_REQ_CODE) {
//            if (resultCode == RESULT_OK) {
//
////                TextView txtResult = findViewById(R.id.txtResult);
////                String username = data.getStringExtra("username");
////                txtResult.setText("Welcome " + username + " to Main Activity");
//            }
//        }
//    }
}
