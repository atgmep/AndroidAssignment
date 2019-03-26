package day12.mobilestudy.make5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;

public class LeadActivity extends AppCompatActivity {

    private TableLayout tblLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        tblLead = findViewById(R.id.tblLead);
        getLeaderBoard();
    }


    private void getLeaderBoard() {
        String[] paramName = {};
        String[] paramValue = {};
        Thread t = new Thread(new ApiCallerThread(Fix.URL + "/api/lead", "POST", paramName, paramValue, 100) {
            @Override
            public void functionFail(Response response) {
                System.out.println(response);
            }

            @Override
            public void functionError(Response response) {
                System.out.println(response);
            }

            @Override
            public void functionSuccess(final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
                        for (int i = 0; i < response.getData().size(); i++) {
                            TableRow.LayoutParams paramsRow = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                            TableRow.LayoutParams paramsRank = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                            TableRow.LayoutParams paramsUser = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
                            TableRow.LayoutParams paramsPoint = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);

                            TableRow tableRow = new TableRow(LeadActivity.this);
                            tableRow.setLayoutParams(paramsRow);
                            MdlAccount account = gson.fromJson(response.getData().get(i), MdlAccount.class);

                            TextView txtRank = new TextView(LeadActivity.this);
                            txtRank.setLayoutParams(paramsRank);
                            txtRank.setGravity(Gravity.CENTER);

                            if (i == 0) {
                                txtRank.setText("★");
                                txtRank.setTextColor(0xFFFFD700);
                            } else if (i == 1) {
                                txtRank.setText("★");
                                txtRank.setTextColor(0xFFC0C0C0);
                            } else if (i == 2) {
                                txtRank.setText("★");
                                txtRank.setTextColor(0xFFCD7F32);
                            } else {
                                txtRank.setText((i + 1) + "");
                            }


                            TextView txtUser = new TextView(LeadActivity.this);
                            txtUser.setLayoutParams(paramsUser);
                            txtUser.setGravity(Gravity.CENTER);
                            txtUser.setText(account.getUsername());

                            TextView txtPoint = new TextView(LeadActivity.this);
                            txtPoint.setLayoutParams(paramsPoint);
                            txtPoint.setGravity(Gravity.CENTER);
                            txtPoint.setText(account.getPoint() + "");


                            TableRow tableRowBlank = new TableRow(LeadActivity.this);
                            tableRowBlank.setLayoutParams(paramsRow);
                            TextView txtBlank = new TextView(LeadActivity.this);
                            txtBlank.setLayoutParams(paramsRank);

                            tableRow.addView(txtRank);
                            tableRow.addView(txtUser);
                            tableRow.addView(txtPoint);
                            tblLead.addView(tableRow);
                            tableRowBlank.addView(txtBlank);
                            tblLead.addView(tableRowBlank);
                        }
                    }
                });

            }

            @Override
            public void functionNotOk(int responseCode, HttpURLConnection urlConnection) {
                System.out.println(responseCode);
            }
        });
        t.start();
    }

    public void clickToBack(View view) {
        Intent intent = this.getIntent();
        this.setResult(RESULT_OK, intent);
        finish();
    }
}
