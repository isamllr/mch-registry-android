package app.registry.mch.mchpregnancyguide;

import android.app.Activity;
import android.content.Context;
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
        mobilePhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mobilePhoneNumber.requestFocus();

            PatientDataHandler pdh = new PatientDataHandler(getApplicationContext(), null, null, 1);

            if(pdh.getPatient().get_mobileNumber().length()>0){
                mobilePhoneNumber.setText(pdh.getPatient().get_mobileNumber());
            }else{
                TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
                mobilePhoneNumber.setText(tMgr.getLine1Number());
            }

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

            PatientDataHandler pdh = new PatientDataHandler(getApplicationContext(), null, null, 1);

            //Save phone number
            pdh.updateMobilePhoneNumber(mobilePhoneNumber.getText().toString());

            //Send new phone number to server
            String projectId = pdh.getPatient().get_projectID();
            String nextMessageId = pdh.createNextMessageID();

	        /*
            //TODO: Send phone number to server with regID
            try {
                Bundle data = new Bundle();
                // the account is used for keeping
                // track of user notifications
                data.putString("patientloginname", account);
                // the action is used to distinguish different message types on the server
                data.putString("updateMobilePhoneNumber", SyncStateContract.Constants.ACTION_REGISTER);
                String msgId = nextMessageId;

                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
                gcm.send(projectId + "@gcm.googleapis.com", msgId, SyncStateContract.Constants.GCM_DEFAULT_TTL, data);

            } catch (IOException e) {
                Log.e("ERROR", "IOException while sending registration id", e);
            }

            //Start main activity
            Intent intent = new Intent(this, PregnancyInfoActivity.class);
            startActivity(intent);*/

        }else{
            Toast.makeText(this, getString(R.string.number_invalid), LENGTH_LONG);
        }
    }

    private boolean validatePhoneNumber(String mobilePhoneNumber) {
        //TODO: regex
        return true;
    }


}
