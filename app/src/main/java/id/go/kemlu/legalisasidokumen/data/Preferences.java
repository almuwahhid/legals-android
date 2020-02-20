package id.go.kemlu.legalisasidokumen.data;

import id.go.kemlu.legalisasidokumen.BuildConfig;

public class Preferences {
    private static String package_name = BuildConfig.APPLICATION_ID;
    public static final String USER_ACCESS                  = package_name+"_user_access";
    public static final String NILAI                  = package_name+"_nilai";
    public static final String USER_INTRO                  = package_name+"_user_intro";
    public static final String FIREBASE_TOKEN			= package_name+"firebase_token";

    public static final String LAYANAN_ON_REFRESH                  = package_name+"_layanan_refresh";
    public static final String LAYANAN_TOVERIF_ON_REFRESH                  = package_name+"_layanan_toverif_refresh";
}
