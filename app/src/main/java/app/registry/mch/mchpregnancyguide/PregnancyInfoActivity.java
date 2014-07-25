package app.registry.mch.mchpregnancyguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.mch.registry.server.registration.Registration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.registry.mch.mchpregnancyguide.data.PatientDataHandler;

import static android.widget.Toast.LENGTH_LONG;


public class PregnancyInfoActivity extends Activity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy_info);

        // Check device for Play Services APK.
        if (checkPlayServices()) {
            new GcmRegistrationAsyncTask().execute(this);
        }else{
            // Otherwise, prompt user to get valid Play Services APK.
        }

        PatientDataHandler pdh = new PatientDataHandler(this, null, null, 1);

        if(pdh.getPatient().get_mobileNumber().compareTo("mobileNumber")==0) {
            Intent intent = new Intent(this, PhoneNumberActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pregnancy_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_login){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_recommendations){
            Intent intent = new Intent(this, RecommendationsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_visits){
            Intent intent = new Intent(this, VisitActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_phonenumber){
            Intent intent = new Intent(this, PhoneNumberActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Warning", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}

class GcmRegistrationAsyncTask extends AsyncTask<Context, Void, String> {
    private Registration regService;
    private GoogleCloudMessaging gcm;
    private Context context;

    private static String SENDER_ID;

    public GcmRegistrationAsyncTask() {
        /*Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                // Need setRootUrl and setGoogleClientRequestInitializer only for local testing, otherwise they can be skipped
                .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
        // end of optional local run code
         */

        Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);

        regService = builder.build();
    }

    @Override
    protected String doInBackground(Context... params) {
        context = params[0];

        PatientDataHandler pdh = new PatientDataHandler(context, null, null, 1);
        String senderId = pdh.getPatient().get_projectID();

        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            String regId = gcm.register(senderId);
            msg = "Device registered, registration ID=" + regId;
            //Save received regID
            pdh.updateRegId(regId);

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            //TODO: accounts, auth
            regService.register(regId).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context, msg, LENGTH_LONG).show();
        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
    }
}
