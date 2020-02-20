package id.go.kemlu.legalisasidokumen.module.Firebase;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import id.go.kemlu.legalisasidokumen.data.models.NotifikasiModel;
import id.go.kemlu.legalisasidokumen.module.SmartOfficeNotification.LegalisasiNotification;


/**
 * Dibuat oleh Rizki Maulana pada 9/13/16.
 * rizmaulana@live.com
 * Mobile Apps Developer
 */
public class FirebaseMService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMService";
    Gson gson = new Gson();
    LegalisasiNotification legalisasiNotification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getData().get("data"));
        NotifikasiModel ntf = gson.fromJson(remoteMessage.getData().get("data"), NotifikasiModel.class);

        legalisasiNotification = new LegalisasiNotification(getApplicationContext());
        showPushNotification(ntf);
    }

    private void showPushNotification(NotifikasiModel ntf){
        legalisasiNotification.showLegalisasiNotif(ntf);
    }
}
