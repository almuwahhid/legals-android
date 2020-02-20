package id.go.kemlu.legalisasidokumen.data;


import id.go.kemlu.legalisasidokumen.BuildConfig;

public class EndPoints {
    public static String a = BuildConfig.base_url;
//    public static String a = "";
    private static String b = BuildConfig.api;
//    private static String b = "";
    private static String c = a+b;

    public static String stringLogin() {
        return c + BuildConfig.login;
    }

    public static String stringAuthEmail() {
        return c + BuildConfig.authEmail;
    }

    public static String stringLoginGoogle() {
        return c + BuildConfig.loginGoogle;
    }

    public static String stringLoginFacebook() {
        return c + BuildConfig.loginFacebook;
    }

    public static String stringUpdateDeviceToken() {
        return c + BuildConfig.updateDeviceToken;
    }

    //
    public static String stringRegister() {
        return c + BuildConfig.register;
    }

    public static String stringUploadPaymentTransfer() {
        return c + BuildConfig.uploadPaymentTransfer;
    }

    public static String stringUpdatePaymentRequest() {
        return c + BuildConfig.updatePaymentRequest;
    }

    public static String stringUpdateRequestStatus() {
        return c + BuildConfig.updateRequestStatus;
    }

    public static String stringUpdateDocumentStatus() {
        return c + BuildConfig.updateDocumentStatus;
    }

    public static String stringReferensiInstansi() {
        return c + BuildConfig.reffInstitution;
    }

    public static String stringDaftarRequest(String page) {
        return c + BuildConfig.daftarRequest+"?page="+page;
    }

    public static String stringNotifikasi(String page) {
        return c + BuildConfig.getNotification+"?page="+page;
    }

    public static String stringDetailRequest(String id) {
        return c + BuildConfig.detailOnlineRequest+"?request_id="+id;
    }

    public static String stringDaftarPengesah(String page) {
        return c + BuildConfig.daftarPengesah+"?page="+page;
    }

    public static String stringDaftarPejabat(String page) {
        return c + BuildConfig.daftarPejabat+"?page="+page;
    }

    public static String stringDaftarRequestToVerif(String page, String keyword) {
        return c + BuildConfig.daftarRequestToVerif+"?page="+page+"&keyword"+(keyword.equals("") ? "" : "="+keyword);
    }
    public static String stringDaftarPembayaranToVerif(String page, String keyword) {
        return c + BuildConfig.getPaymentRequest+"?page="+page+"&keyword"+(keyword.equals("") ? "" : "="+keyword);
    }

    public static String stringGetIKM(String date1, String date2) {
        return c + BuildConfig.getikm+"?date_from="+date1+"&date_to"+(date2.equals("") ? "" : "="+date2);
    }

    public static String stringGetIKMComment(String date1, String date2, String page) {
        return c + BuildConfig.getikmComment+"?date_from="+date1+"&date_to"+(date2.equals("") ? "" : "="+date2+"&page="+page);
    }

    public static String stringDetailRequestByGroup(String group_no) {
        return c + BuildConfig.detailOnlineRequestByGroup+"?group_no="+group_no;
    }

    public static String stringDaftarJabatan() {
        return c + BuildConfig.listPosition;
    }

    public static String stringDaftarInstansi() {
        return c + BuildConfig.listInstitusi;
    }

    public static String stringRequestAddPejabat() {
        return c + BuildConfig.addPejabat;
    }

    public static String stringDetailRequestByGroupNotif(String group_no, String notif_id) {
        return c + BuildConfig.detailOnlineRequestByGroup+"?group_no="+group_no+"&notif_id="+notif_id;
    }

    public static String stringBuatPermohonan() {
        return c + BuildConfig.submitLegalisasi;
    }

    public static String stringSubmitIKM() {
        return c + BuildConfig.submitikm;
    }

    public static String stringReferensiTipeDokumen() {
        return c + BuildConfig.reffDocType;
    }

        public static String stringReferensiNegara() {
        return c + BuildConfig.reffCountry;
    }
//
//    public static String stringArtikel() {
//        return c + BuildConfig.getArtikel;
//    }
//
//    public static String stringCheckSurvey() {
//        return c + BuildConfig.checkSurvey;
//    }
////
//    public static String stringMakeSurvey() {
//        return c + BuildConfig.makeSurvey;
//    }
////
//    public static String stringUpdateBiodata() {
//        return c + BuildConfig.biodata;
//    }
//
//    public static String stringUbahPasswod() {
//        return c + BuildConfig.ubahPassword;
//    }
////
//    public static String stringPernyataan() {
//        return c + BuildConfig.pernyataan;
//    }
////
////    public static String stringDetailSurveySaya() {
////        return c + BuildConfig.getDetailSurveySaya;
////    }
////
////    public static String stringNilaiSurvey() {
////        return c + BuildConfig.getNilaiSurvey;
////    }
////
////    public static String stringNilaiSurveyByAspek() {
////        return c + BuildConfig.getNilaiSurveyByAspek;
////    }
////
//    public static String stringPertanyaanSaya() {
//        return c + BuildConfig.getPertanyaan;
//    }
////
//    public static String stringSubmitPertanyaan() {
//        return c + BuildConfig.submitPertanyaan;
//    }
////
////    public static String stringSurveySaya() {
////        return c + BuildConfig.getSurveySaya;
////    }
////
//    public static String stringUpdateTaskPertanyaan() {
//        return c + BuildConfig.updateTaskPertanyaan;
//    }
////
    public static String stringLupaPassword() {
        return c + BuildConfig.lupaPassword;
    }
//
////    public static String stringCheckIntervensiToday() {
////        return c + BuildConfig.checkIntervensiToday;
////    }
//    public static String stringCheckIntervensiToday() {
//        return c + BuildConfig.checkIntervensiToday;
//    }


}
