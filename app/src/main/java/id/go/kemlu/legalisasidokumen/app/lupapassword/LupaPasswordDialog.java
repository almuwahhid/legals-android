package id.go.kemlu.legalisasidokumen.app.lupapassword;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.uny.riset.ride.utils.dialogs.DialogLupaPassword.DialogLupaPasswordPresenter;
import id.ac.uny.riset.ride.utils.dialogs.DialogLupaPassword.DialogLupaPasswordView;
import id.go.kemlu.legalisasidokumen.R;
import lib.gmsframeworkx.utils.DialogBuilder;
import lib.gmsframeworkx.utils.GmsStatic;


public class LupaPasswordDialog extends DialogBuilder implements DialogLupaPasswordView.View{

    @BindView(R.id.btn_kirim)
    Button btn_kirim;
    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.lay_dialog)
    RelativeLayout lay_dialog;

    DialogLupaPasswordPresenter presenter;


    public LupaPasswordDialog(Context context) {
        super(context, R.layout.dialog_lupa_password);
        ButterKnife.bind(this, getDialog());

        presenter = new DialogLupaPasswordPresenter(getContext(), this);

        setAnimation(R.style.DialogBottomAnimation);
        setFullWidth(lay_dialog);
        setGravity(Gravity.BOTTOM);

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_email.getText().toString().equals("")){
                    edt_email.setError("Isi kolom email terlebih dahulu");
                } else {
                    presenter.requestLupaPassword(edt_email.getText().toString());
                }
            }
        });

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                initButtonStyle((charSequence.toString().equals("")? false : true));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        show();
    }

    public void initEmail(String email){
        edt_email.setText(email);
        tv_title.setText("Atur Kata Sandi");
    }

    private void initButtonStyle(Boolean isFilled){
        if(isFilled){
            btn_kirim.setBackground(getContext().getResources().getDrawable(R.drawable.button_main));
            btn_kirim.setClickable(true);
        } else {
            btn_kirim.setBackground(getContext().getResources().getDrawable(R.drawable.button_disable));
            btn_kirim.setClickable(false);
        }
    }

    @Override
    public void onRequestLupaPassword(boolean isExist, String message) {
        if(isExist){
            GmsStatic.ToastShort(getContext(), message);
            dismiss();
        } else {
            GmsStatic.ToastShort(getContext(), message);
        }
    }

    @Override
    public void onLoading() {
        GmsStatic.showLoadingDialog(getContext(), R.drawable.ic_hospital);
    }

    @Override
    public void onHideLoading() {
        GmsStatic.hideLoadingDialog(getContext());
    }

    @Override
    public void onErrorConnection() {
        GmsStatic.ToastShort(getContext(), "Ada yang bermasalah dengan server");
    }
}
