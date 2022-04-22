package com.example.mvvmarchitechre.utility.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.mvvmarchitechre.R;
import com.example.mvvmarchitechre.utility.Utilities;
import com.example.mvvmarchitechre.utility.session.SessionManager;


public class DialogMultiLanguage {
    private final Context mContext;
    private Dialog dialog;
    private MultiLanguageListner multiLanguageListner;
    String selectedtext;

    public DialogMultiLanguage(Context mContext){
        this.mContext = mContext;
        OfferCodeDialog();
    }

    public void show() {
        dialog.show();
    }

    private void OfferCodeDialog() {
        dialog = new Dialog(mContext);
        Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }

        dialog.setContentView(R.layout.dialog_multi_language);

        RadioGroup language_group = dialog.findViewById(R.id.language_radio_group);
        RadioButton rb_eng = dialog.findViewById(R.id.rb_english);
        RadioButton rb_arabic = dialog.findViewById(R.id.rb_arabic);
        RadioButton rb_chinese = dialog.findViewById(R.id.rb_chinese);
        AppCompatImageView dialog_cancel = dialog.findViewById(R.id.iv_dialogcancel);
        AppCompatTextView btn_save = dialog.findViewById(R.id.btn_save);

        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Utilities.getApplicationLanguage((Activity)mContext);
                    dialog.dismiss();
            }
        });

        if(SessionManager.get().getLanguage().equals("en") || SessionManager.get().getLanguage().isEmpty()) {
            selectedtext = "English";
            SessionManager.get().setLanguage("en");
            rb_eng.setChecked(true);
        }
        else if(SessionManager.get().getLanguage().equals("ar")){
            SessionManager.get().setLanguage("ar");
            rb_arabic.setChecked(true);
        }
        else{
            SessionManager.get().setLanguage("zh");
            rb_chinese.setChecked(true);
        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedtext.equals("English")) {
                    Utilities.setApplicationlanguage((Activity)mContext, Utilities.LANGUAGE_ENGLISH);
                } else if(selectedtext.equals("arabic")){
                    Utilities.setApplicationlanguage((Activity)mContext,Utilities.LANGUAGE_ARABIC);
                }
                else{
                    Utilities.setApplicationlanguage((Activity)mContext,Utilities.LANGUAGE_CHINESE);
                }
                dialog.dismiss();
                ((Activity) mContext).finish();
                mContext.startActivity(((Activity) mContext).getIntent());
            }
        });


        language_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = dialog.findViewById(checkedId);
                selectedtext = checkedRadioButton.getText().toString();
            }
        });
        show();
    }


    public MultiLanguageListner multiLanguageDialogListner() {
        return multiLanguageListner;
    }

    public void setMultiLanguageListner(MultiLanguageListner listner) {
        this.multiLanguageListner = listner;
    }

    public interface MultiLanguageListner {
        void onSelectedLanguage(String language);
    }

}
