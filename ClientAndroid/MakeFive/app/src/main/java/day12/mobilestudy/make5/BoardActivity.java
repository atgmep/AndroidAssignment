package day12.mobilestudy.make5;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
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

public class BoardActivity extends AppCompatActivity {

    private GridLayout gridBoard;
    private TextView txtMsg;
    private TextView txtOppName;
    private TextView txtOppPoint;
    private ImageView[][] imageViews = new ImageView[10][10];
    private int[][] board = new int[10][10];
    private int colClick = -1;
    private int rowClick = -1;
    private String matchId;
    private final String API = Fix.MAP_API + "/match";
    private boolean lockBoard = false;
    private AsyncTask<String, Integer, String> startMatchTask;
    private AsyncTask<String, Integer, String> sendMoveTask;
    private String playerNo;
    private int colResp = -1;
    private int rowResp = -1;

    String startMatchRespStr;
    Response startMatchResp;

    String moveRespStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        gridBoard = findViewById(R.id.gridBoard);
        txtMsg = findViewById(R.id.txtMsg);
        txtOppName = findViewById(R.id.txtOppName);
        txtOppPoint = findViewById(R.id.txtOppPoint);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

//                Button btnInput = new Button(this);
//                btnInput.setText("" + x.nextInt(10));
//                btnInput.setPadding(0, 0, 0, 0);
//                btnInput.setLayoutParams();
//                btnInput.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

                final int col = j;
                final int row = i;
                final ImageView imageView = new ImageView(this);
                imageView.setBackgroundResource(R.drawable.cell_blank);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(col, row);
                    }
                });
                board[col][row] = 0;
                imageViews[col][row] = imageView;
                gridBoard.addView(imageView);
            }
        }


        startMatchTask = new StartMatchTask();
        startMatchTask.execute();
//        startMatch();
    }


    private class StartMatchTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... args) {
            lockBoard = true;
            txtMsg.setText("wait for player");
            URL url;
            HttpURLConnection urlConnection;
            String urlStart = Fix.URL + API + "/start";
            String urlWait = Fix.URL + API + "/wait";
            String method = "POST";
            String[] paramName = {};
            String[] paramValue = {};
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
            try {
                url = new URL(urlStart);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(method);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String respStr = RequestTask.readStream(urlConnection.getInputStream());
                    startMatchResp = gson.fromJson(respStr, Response.class);




                    if (startMatchResp.getStatusCode() != Response.STATUS_SUCCESS) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtOppName.setText("Fail to Start");
                                txtOppPoint.setText("00:00");
                            }
                        });

                    } else {
                        playerNo = startMatchResp.getMessage();
                        matchId = startMatchResp.getData().get(0);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String s = "2".equals(playerNo) ? "You are player 2" : "Your turn";
                                txtMsg.setText(s);
                                txtOppName.setText(startMatchResp.getData().get(1));
                                txtOppPoint.setText(startMatchResp.getData().get(2));
                            }
                        });


                        if ("2".equals(playerNo)) {
                            final String[] paramName2 = {"id"};
                            final String[] paramValue2 = {matchId};
                            URL url2 = new URL(urlWait);
                            HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                            urlConnection2.setRequestMethod(method);
                            urlConnection2.setDoInput(true);
                            urlConnection2.setDoOutput(true);
                            if (paramName2.length > 0) {
                                Uri.Builder builder = new Uri.Builder();
                                for (int i = 0; i < paramName2.length; i++) {
                                    builder.appendQueryParameter(paramName2[i], paramValue2[i]);
                                }
                                String query = builder.build().getEncodedQuery();
                                OutputStream os = urlConnection2.getOutputStream();
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                                writer.write(query);
                                writer.flush();
                                writer.close();
                                os.close();
                            }
                            urlConnection2.connect();
                            responseCode = urlConnection2.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                handlingMove(RequestTask.readStream(urlConnection2.getInputStream()));
                            }
                        }
                        if ("1".equals(playerNo)) {
                            lockBoard = false;
                        }
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


    private class SendMoveTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... args) {
            lockBoard = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtMsg.setText("wait for player");
                }
            });
            URL url;
            HttpURLConnection urlConnection;
            String urlStart = Fix.URL + API + "/move";

            String method = "POST";
            final String[] paramName = {"id", "col", "row"};
            final String[] paramValue = {matchId, colClick + "", rowClick + ""};

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


                    handlingMove(RequestTask.readStream(urlConnection.getInputStream()));

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


//    public void startMatch() {
//        lockBoard = true;
//
//        BoardActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                URL url;
//                HttpURLConnection urlConnection;
//                String urlStart = Fix.URL + API + "/start";
//                String urlWait = Fix.URL + API + "/wait";
//                String method = "POST";
//                String[] paramName = {};
//                String[] paramValue = {};
//                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
//                try {
//                    url = new URL(urlStart);
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod(method);
//                    urlConnection.setDoInput(true);
//                    urlConnection.setDoOutput(true);
//
//                    urlConnection.connect();
//                    int responseCode = urlConnection.getResponseCode();
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        String respStr = RequestTask.readStream(urlConnection.getInputStream());
//                        Response response = gson.fromJson(respStr, Response.class);
//                        String playerNo = response.getMessage();
//                        matchId = response.getData().get(0);
//                        txtMsg.setText(playerNo);
//                        if (playerNo == "2") {
//
//                            final String[] paramName2 = {"id"};
//                            final String[] paramValue2 = {matchId};
//
//                            url = new URL(urlWait);
//                            urlConnection = (HttpURLConnection) url.openConnection();
//                            urlConnection.setRequestMethod(method);
//                            urlConnection.setDoInput(true);
//                            urlConnection.setDoOutput(true);
//                            if (paramName2.length > 0) {
//                                Uri.Builder builder = new Uri.Builder();
//                                for (int i = 0; i < paramName2.length; i++) {
//                                    builder.appendQueryParameter(paramName2[i], paramValue2[i]);
//                                }
//                                String query = builder.build().getEncodedQuery();
//                                OutputStream os = urlConnection.getOutputStream();
//                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
//                                writer.write(query);
//                                writer.flush();
//                                writer.close();
//                                os.close();
//                            }
//
//                            urlConnection.connect();
//                            responseCode = urlConnection.getResponseCode();
//                            if (responseCode == HttpURLConnection.HTTP_OK) {
//                                respStr = RequestTask.readStream(urlConnection.getInputStream());
//                                response = gson.fromJson(respStr, Response.class);
//                                handlingMove(response);
//                            }
//                        }
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public void startMatchOld() {
//        lockBoard = true;
//        RequestTask requestTask = new RequestTask();
//        final String[] paramName = {};
//        final String[] paramValue = {};
//        try {
//            Thread t = requestTask.sendHttp(Fix.URL + API + "/start", "POST", paramName, paramValue, 8);
//            t.start();
//            t.join();
//            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
//            Response response = gson.fromJson(MainActivity.responseList[8], Response.class);
//            String playerNo = response.getMessage();
//            matchId = response.getData().get(0);
//            txtMsg.setText(playerNo);
//            if (playerNo == "2") {
//
//                final String[] paramName2 = {"id"};
//                final String[] paramValue2 = {matchId};
//                t = requestTask.sendHttp(Fix.URL + API + "/wait", "POST", paramName2, paramValue2, 7);
//                t.start();
//                t.join();
//
//                handlingMove(gson.fromJson(MainActivity.responseList[7], Response.class));
//            } else {
//                lockBoard = false;
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private void handlingMove(String responseStr) {
        moveRespStr = responseStr;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
        Response response = gson.fromJson(responseStr, Response.class);

        if (response.getStatusCode() != Response.STATUS_SUCCESS) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtMsg.setText(moveRespStr);
            }
        });
        String messageStr = response.getMessage();
        String[] coordinate;


        switch (messageStr) {
            case Fix.OPP_MOV:
                coordinate = response.getData().get(0).split("x");
                colResp = Integer.parseInt(coordinate[0]);
                rowResp = Integer.parseInt(coordinate[1]);
                board[colResp][rowResp] = 2;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageViews[colResp][rowResp].setBackgroundResource(R.drawable.cell_x);
                    }
                });
                lockBoard = false;
                return;
            case Fix.OPP_LOS:

                return;
            case Fix.OPP_WIN:
                coordinate = response.getData().get(0).split("x");
                colResp = Integer.parseInt(coordinate[0]);
                rowResp = Integer.parseInt(coordinate[1]);
                board[colResp][rowResp] = 2;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageViews[colResp][rowResp].setBackgroundResource(R.drawable.cell_x);
                    }
                });
                return;
            case Fix.OPP_AFK:
                return;
            case Fix.YOU_WIN:
                return;
        }

    }


//    private void handlingMoveOld(Response response) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                txtMsg.setText(startMatchRespStr);
//            }
//        });
//        String messageStr = response.getMessage();
//        String[] coordinate;
//
//
//        switch (messageStr) {
//            case Fix.OPP_MOV:
//                coordinate = response.getData().get(0).split("x");
//                colResp = Integer.parseInt(coordinate[0]);
//                rowResp = Integer.parseInt(coordinate[1]);
//                board[colResp][rowResp] = 2;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imageViews[colResp][rowResp].setBackgroundResource(R.drawable.cell_x);
//                    }
//                });
//                lockBoard = false;
//                return;
//            case Fix.OPP_LOS:
//
//                return;
//            case Fix.OPP_WIN:
//                coordinate = response.getData().get(0).split("x");
//                colResp = Integer.parseInt(coordinate[0]);
//                rowResp = Integer.parseInt(coordinate[1]);
//                board[colResp][rowResp] = 2;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imageViews[colResp][rowResp].setBackgroundResource(R.drawable.cell_x);
//                    }
//                });
//                return;
//            case Fix.OPP_AFK:
//                return;
//            case Fix.YOU_WIN:
//                return;
//        }
//
//    }

    public void cellClick(int col, int row) {
        if (lockBoard) {
            return;
        }
        if (board[col][row] != 0) {
            return;
        }

        if (col != colClick || row != rowClick) {
            // First click
            if (colClick != -1 && rowClick != -1 && board[colClick][rowClick] == 0) {
                imageViews[colClick][rowClick].setBackgroundResource(R.drawable.cell_blank);
            }
            colClick = col;
            rowClick = row;
            imageViews[col][row].setBackgroundResource(R.drawable.cell_click);
        } else {
            lockBoard = true;
            board[col][row] = 1;
            imageViews[col][row].setBackgroundResource(R.drawable.cell_o);
            colClick = col;
            rowClick = row;
            // Send move
            sendMoveTask = new SendMoveTask();
            sendMoveTask.execute();
        }
    }
}