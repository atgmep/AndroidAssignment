package day12.mobilestudy.make5;

import android.content.Intent;
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
import java.util.Date;

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
    private String oppPointStr;
    private boolean isStartMatchTimerOn;
    private Response startMatchResp;
    private boolean hasQuit = false;



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

        isStartMatchTimerOn = true;
//        AsyncTask<String, Integer, String> startMatchTimer = new StartMatchTimer();

//        startMatchTimer.execute();


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

//                System.out.println("t 0");
                final long time0 = new Date().getTime();
//                System.out.println(time0);
                while (isStartMatchTimerOn) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int dTime = (int) ((new Date().getTime() - time0) / 1000);
                            int min = dTime / 60;
                            int sec = dTime % 60;
                            String timeStr = String.format("%02d:%02d", min, sec);
                            txtOppPoint.setText(timeStr);
                        }
                    });
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtOppPoint.setText(oppPointStr);
                    }
                });
            }
        });
        t.start();
//        startMatch();
    }

    @Override
    protected void onDestroy() {
        quitMatch();
        super.onDestroy();
    }

    private void quitMatch() {
        if(!hasQuit){
            String[] paramName = {};
            String[] paramValue = {};
            Thread t = new Thread(new ApiCallerThread(Fix.URL + "/api/match/quit", "POST", paramName, paramValue, 100) {
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
//                + response.getData().get(0)
                    System.out.println(response);
                    System.out.println("quit ");
                }

                @Override
                public void functionNotOk(int responseCode, HttpURLConnection urlConnection) {
                    System.out.println(responseCode);
                }
            });
            hasQuit = true;
            t.start();
        }
    }


    public void clickToQuit(View view) {
//        quitMatch();
        Intent intent = this.getIntent();
        this.setResult(RESULT_OK, intent);
        finish();
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
                                setOppPoint("Time out");
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
                                setOppPoint(startMatchResp.getData().get(2));
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
                    txtMsg.setText("Opponent turn");
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




    private void setOppPoint(String str) {
        oppPointStr = str;
        System.out.println("stop");
        isStartMatchTimerOn = false;
    }

//    private class StartMatchTimer extends AsyncTask<String, Integer, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            System.out.println("t 0");
//            final long time0 = new Date().getTime();
//            System.out.println(time0);
//            while (isStartMatchTimerOn) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int dTime = (int) ((new Date().getTime() - time0) / 1000);
//                        int min = dTime / 60;
//                        int sec = dTime % 60;
//                        String timeStr = String.format("%02d:%02d", min, sec);
//                        txtOppPoint.setText(timeStr);
//                    }
//                });
//                try {
//                    Thread.sleep(250);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            txtOppPoint.setText(oppPointStr);
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//        }
//    }


//    @Override
//    public void run() {
//
//    }

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
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("dd/MM/yyyy HH:mm").create();
        final Response response = gson.fromJson(responseStr, Response.class);

        if (response.getStatusCode() != Response.STATUS_SUCCESS) {
            return;
        }
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
                        txtMsg.setText("Your turn");
                    }
                });
                lockBoard = false;
                return;
            case Fix.OPP_LOS:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsg.setText("Opponent quit");
                    }
                });
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
                        txtMsg.setText("Opponent win");
                    }
                });
                return;
            case Fix.OPP_AFK:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsg.setText("Opponent quit");
                    }
                });
                return;
            case Fix.YOU_WIN:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsg.setText("You win");
                    }
                });
                return;
            default:
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