package id.go.kemlu.legalisasidokumen.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.google.gson.Gson;


import id.go.kemlu.legalisasidokumen.data.Preferences;
import id.go.kemlu.legalisasidokumen.data.models.UserModel;
import lib.gmsframeworkx.utils.GmsStatic;
import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.airbnb.lottie.L.TAG;


public class LegalisasiFunction {

    public static Gson gson = new Gson();

    public static void setUserPreference(Context context, String sp){
        GmsStatic.setSPString(context, Preferences.USER_ACCESS, sp);
    }

    public static UserModel getUserPreference(Context context){
        if(GmsStatic.getSPString(context, Preferences.USER_ACCESS).equals("")){
            return null;
        } else {
            return gson.fromJson(GmsStatic.getSPString(context, Preferences.USER_ACCESS), UserModel.class);
        }
    }

    public static void logoutUser(Context context){
        GmsStatic.removeSPString(context, Preferences.USER_ACCESS);
    }

    public static boolean checkUserPreference(Context context){
        if(GmsStatic.getSPString(context, Preferences.USER_ACCESS).equals("")){
            return false;
        } else {
            return true;
        }
    }

    public static String parseTimestampToDate(String data){
        try {
            String begin = data.split(" ")[0];
            return begin.split("-")[2]+" "+ GmsStatic.monthName(Integer.valueOf(begin.split("-")[1])-1)+ " "+begin.split("-")[0];
        } catch (Exception e){
            return "";
        }
    }

    public static String parseTimestampToSimpleMonthYear(String data){
        try {
            String begin = data.split(" ")[0];
            return simpleMonthName(Integer.valueOf(begin.split("-")[1])-1)+ " "+begin.split("-")[0];
        } catch (Exception e){
            return "";
        }
    }
    public static String parseDateStringToDate(String data){
        try {
            return data.split("-")[2]+" "+simpleMonthName(Integer.valueOf(data.split("-")[1])-1)+ " "+data.split("-")[0];
        } catch (Exception e){
            return "";
        }
    }

    public static String parseDateToDate(String data){
        try {
            return data.split("-")[2];
        } catch (Exception e){
            return "";
        }
    }

    public static String parseDateToMonth(String data){
        try {
            return data.split("-")[1];
        } catch (Exception e){
            return "";
        }
    }

    public static String parseDateToYear(String data){
        try {
            return data.split("-")[0];
        } catch (Exception e){
            return "";
        }
    }
    public static String parseTimestampToSimpleFullDate(String data){
        try {
            String begin = data.split(" ")[0];
            return begin.split("-")[2]+" "+ simpleMonthName(Integer.valueOf(begin.split("-")[1])-1)+ " "+begin.split("-")[0].substring(2, 4);
        } catch (Exception e){
            return "";
        }
    }

    public static String parseDateToRealDate(String begin){
        try {
            return begin.split("-")[2]+" "+ GmsStatic.monthName(Integer.valueOf(begin.split("-")[1])-1)+ " "+begin.split("-")[0];
        } catch (Exception e){
            return "";
        }
    }

    public static String parseTimestampToSimpleDate(String data){
        try {
            String begin = data.split(" ")[0];
            return begin.split("-")[2];
        } catch (Exception e){
            return "";
        }
    }

    public static String simpleMonthName(int month) {
        String m = "";
        switch (month) {
            case 0:
                m = "Jan";
                break;
            case 1:
                m = "Feb";
                break;
            case 2:
                m = "Mar";
                break;
            case 3:
                m = "Apr";
                break;
            case 4:
                m = "Mei";
                break;
            case 5:
                m = "Jun";
                break;
            case 6:
                m = "Jul";
                break;
            case 7:
                m = "Aug";
                break;
            case 8:
                m = "Sept";
                break;
            case 9:
                m = "Okt";
                break;
            case 10:
                m = "Nov";
                break;
            case 11:
                m = "Des";
                break;
        }
        return m;
    }

    public static String isToday(String data) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String str1 = data.split(" ")[0].replaceAll("-", "/");
            Date date1 = new LocalDate(formatter.parse(str1)).toDate();
            Date date2 = new LocalDate(new Date()).toDate();

            Log.d("isToday", "isToday: "+date1+" now "+date2);
            if (date1.compareTo(date2) < 0) {
                return "yesterday";
            } else if (date1.compareTo(date2) == 0) {
                return "today";
            } else {
                return "tomorrow";
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri){
        Bitmap bitmap_image = null;
        try {
//            Log.d(TAG, "handleCropResult: "+uri);
            bitmap_image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap_image;
    }

    public static boolean isPreLolipop(){
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void showKeyboard(Context context, final EditText ettext){
        ettext.requestFocus();
        ettext.postDelayed(new Runnable(){
                               @Override public void run(){
                                   InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext,0);
                               }
                           }
                ,200);
    }

    public static void hideSoftKeyboard(Context context, EditText ettext){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(ettext.getWindowToken(), 0);
    }

    public static Bitmap createBitmapFromLayout(View tv) {
        Bitmap b = Bitmap.createBitmap(tv.getMeasuredWidth(), tv.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate((-tv.getScrollX()), (-tv.getScrollY()));
        tv.draw(c);
        return b;
    }

    public static Uri saveImageExternal(Context context, Bitmap image, String id) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "legalisasi-dokumen-"+id+".png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }

    public static void shareImageUri(Context context, Uri uri){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        context.startActivity(intent);
    }
}
