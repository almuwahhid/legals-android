package id.go.kemlu.legalisasidokumen.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputEditText;

public class PhoneEditText extends TextInputEditText {


    public PhoneEditText(Context context) {
        super(context);
    }

    public PhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhoneEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.toString().length() > 0){
            if (text.toString().equals("0")){
                setText("");
            }
        }
    }
}
