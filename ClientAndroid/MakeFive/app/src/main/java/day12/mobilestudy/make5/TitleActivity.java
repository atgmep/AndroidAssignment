package day12.mobilestudy.make5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TitleActivity extends AppCompatActivity {

    private static final int PLAY_ON_REQ_CODE = 1113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
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
    }
}
