package app.registry.mch.mchpregnancyguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.registry.mch.mchpregnancyguide.data.PatientDataHandler;

import static android.widget.Toast.LENGTH_LONG;

public class PhoneNumberActivity extends Activity implements View.OnClickListener{

    private Button btnOK;
    private EditText mobilePhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        /* Load phone input field */
        mobilePhoneNumber = (EditText) findViewById(R.id.input_phone);
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mobilePhoneNumber.setText(tMgr.getLine1Number());
        mobilePhoneNumber.requestFocus();
        mobilePhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        /* Load OK Button */
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.phone_number, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(validatePhoneNumber(mobilePhoneNumber.getText().toString())){
            PatientDataHandler pdh = new PatientDataHandler(this, null, null, 1);
            pdh.updateMobilePhoneNumber(mobilePhoneNumber.getText().toString());

            //Start main activity
            Intent intent = new Intent(this, PregnancyInfoActivity.class);
            startActivity(intent);

        }else{
            Toast.makeText(this, getString(R.string.number_invalid), LENGTH_LONG);
        }
    }

    private boolean validatePhoneNumber(String mobilePhoneNumber) {
        //TODO: regex
        return true;
    }


}
