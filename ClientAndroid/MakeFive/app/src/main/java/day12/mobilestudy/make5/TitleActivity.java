package day12.mobilestudy.make5;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TitleActivity extends AppCompatActivity {

    public static final int PLAY_ON_REQ_CODE = 1113;
    public static final int DONATE_REQ = 7896;

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
}
