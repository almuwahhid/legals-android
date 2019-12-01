package id.go.kemlu.legalisasidokumen.module.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import id.go.kemlu.legalisasidokumen.R;
import lib.gmsframeworkx.Activity.ActivityPermission;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LegalisasiPermissionActivity extends ActivityPermission {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SanFransisco-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    protected Activity getActivity(){
        return this;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
