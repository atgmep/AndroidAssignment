package day12.mobilestudy.make5;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class DonateActivity extends AppCompatActivity {



    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Fix.PAYPAL_CLIENT_ID);

    private Button btnPay;
    private EditText edtAmount;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        btnPay = findViewById(R.id.btnPay);
        edtAmount = findViewById(R.id.edtAmount);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        amount = edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), Fix.PAYPAL_CURRENCY, "Donate to MakeFive", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, TitleActivity.DONATE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TitleActivity.DONATE_REQ) {
            if (resultCode == RESULT_OK) {


                Intent intent = this.getIntent();
                this.setResult(RESULT_OK, intent);
                finish();
//                Toast.makeText(this, "Thank you for your donation", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "cancel", Toast.LENGTH_LONG).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//            Toast.makeText(this, "INVALID", Toast.LENGTH_LONG).show();
        }
    }

}
