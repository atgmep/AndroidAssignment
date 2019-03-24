package day12.mobilestudy.make5;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

public class TitleActivity extends AppCompatActivity {

    public static final int PLAY_ON_REQ_CODE = 1113;
    public static final int DONATE_REQ = 7896;
    public static String username;
    public static String point;
    private TextView txtName;
    private TextView txtPoint;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        txtName = findViewById(R.id.txtName);
        txtPoint = findViewById(R.id.txtPoint);
        txtName.setText(username);
        txtPoint.setText(point);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] paramName = {};
        String[] paramValue = {};

        ApiCaller apiCaller = new ApiCaller(Fix.URL + "/api/point", "POST", paramName, paramValue, 100) {
            @Override
            public void functionFail(Response response) {

            }

            @Override
            public void functionError(Response response) {

            }

            @Override
            public void functionSuccess(final Response response) {
                System.out.println("new point");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtPoint.setText(response.getData().get(0));
                    }
                });
            }

            @Override
            public void functionNotOk(int responseCode, HttpURLConnection urlConnection) {

            }
        };
        apiCaller.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String[] paramName = {};
        String[] paramValue = {};

        ApiCaller apiCaller = new ApiCaller(Fix.URL + "/logout", "POST", paramName, paramValue, 100) {
            @Override
            public void functionFail(Response response) {

            }

            @Override
            public void functionError(Response response) {

            }

            @Override
            public void functionSuccess(final Response response) {
                System.out.println(" logout ok");
            }

            @Override
            public void functionNotOk(int responseCode, HttpURLConnection urlConnection) {

            }
        };
        apiCaller.execute();
    }

    public void clickToPlaySolo(View view) {

    }

    public void clickToPlayOnline(View view) {
        Intent intent = new Intent(TitleActivity.this, BoardActivity.class);
        startActivityForResult(intent, PLAY_ON_REQ_CODE);
    }

    public void clickToShowRank(View view) {
    }

    public void clickToDonate(View view) {
        Intent intent = new Intent(TitleActivity.this, DonateActivity.class);
        startActivityForResult(intent, DONATE_REQ);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TitleActivity.DONATE_REQ) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "Thank you for your donation", Toast.LENGTH_SHORT).show();


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickToLogout(View view) {
        Intent intent = this.getIntent();
        this.setResult(RESULT_OK, intent);
        finish();
    }
}
