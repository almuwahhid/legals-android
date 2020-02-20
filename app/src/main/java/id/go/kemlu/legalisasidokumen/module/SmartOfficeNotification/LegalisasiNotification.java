package id.go.kemlu.legalisasidokumen.module.SmartOfficeNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import id.go.kemlu.legalisasidokumen.BuildConfig;
import id.go.kemlu.legalisasidokumen.R;
import id.go.kemlu.legalisasidokumen.app.detaillayanan.DetailLayananActivity;
import id.go.kemlu.legalisasidokumen.data.models.NotifikasiModel;

public class LegalisasiNotification {
    private Context context;
    private static LegalisasiNotification instance;
    private PendingIntent pendingIntent;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    public LegalisasiNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void cancelNotif(int id){
        notificationManager.cancel(id);
    }

    public void showLegalisasiNotif(NotifikasiModel notifikasiModel){
        String GROUP_KEY = BuildConfig.APPLICATION_ID+"_"+notifikasiModel.getIntNotifid();
        String NOTIFICATION_CHANNEL_ID = ""+ notifikasiModel.getIntNotifid();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription(BuildConfig.application_name);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(ContextCompat.getColor(context, R.color.colorPrimary));
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo)
                .setTicker(BuildConfig.application_name)
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(notifikasiModel.getDtmNotif())
                .setContentText(notifikasiModel.getStrNotifDesc())
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setContentInfo(BuildConfig.application_name)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notifikasiModel.getDtmNotif()));

        String replyLabel = "Enter your reply here";

        Intent resultIntent = new Intent(context, DetailLayananActivity.class);
        resultIntent.putExtra("raw_data", notifikasiModel);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Set a unique request code for this pending intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, notifikasiModel.getIntNotifid(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notifikasiModel.getIntNotifid(), notificationBuilder.build());
    }

}
