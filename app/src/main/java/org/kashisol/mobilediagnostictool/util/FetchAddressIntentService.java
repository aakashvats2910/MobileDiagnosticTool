package org.kashisol.mobilediagnostictool.util;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {
    private ResultReceiver resultReceiver;

    private static final String PACKAGE_NAME = "com.example.mobliediagnosis";
    static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    static final String LOCATION_DATA_KEY = PACKAGE_NAME + ".LOCATION_DATA_KEY";
    static final int SUCCESS_RESULT = 1;
    static final int FAILURE_RESULT = 0;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String errorMassage = "";
            resultReceiver = intent.getParcelableExtra(RECEIVER);
            Location location = intent.getParcelableExtra(LOCATION_DATA_KEY);
            if(location == null) {
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            }catch (Exception exception){
                errorMassage = exception.getMessage();
            }
            if (addresses == null ||addresses.isEmpty()) {
                deliverResultToRecevier(FAILURE_RESULT, errorMassage);
            }else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResultToRecevier(
                        SUCCESS_RESULT,
                        TextUtils.join(
                                Objects.requireNonNull(System.getProperty("line.separator")),
                                addressFragments
                        )

                );
            }
        }
    }
    private void deliverResultToRecevier(int resultcode, String addressMassage){
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY, addressMassage);
        resultReceiver.send(resultcode, bundle);
    }
}
