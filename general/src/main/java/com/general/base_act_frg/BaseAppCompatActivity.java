package com.general.base_act_frg;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.general.utils.LanguageUtil;

import static com.general.utils.LanguageUtil.getAppLanguage;


public abstract class BaseAppCompatActivity
        extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        String lang_code = getAppLanguage(); //load it from SharedPref
        Context context = LanguageUtil.setAppLocale(newBase, lang_code);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setLanguage();
    }
    public void setLanguage() {
        LanguageUtil.setAppLocale(this, getAppLanguage());
    }
    @Override
    protected void onResume() {
        super.onResume();
        setLanguage();
    }

}


