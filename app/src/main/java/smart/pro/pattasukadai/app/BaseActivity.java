package smart.pro.pattasukadai.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import smart.pro.pattasukadai.ConfigBean;
import smart.pro.pattasukadai.PreferenceBean;
import smart.pro.pattasukadai.SplashActivity;

import static smart.pro.pattasukadai.app.AppConfig.auth_key;
import static smart.pro.pattasukadai.app.AppConfig.configKey;
import static smart.pro.pattasukadai.app.AppConfig.mypreference;
import static smart.pro.pattasukadai.app.AppConfig.user_id;

public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        try {
            //setTheme(R.style.AppTheme);
            String colorPrimary="#EC2517";
            int theme = Color.parseColor(colorPrimary);
            setActionBar(theme);
        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", "");

        }
        startDemo();
    }

    protected ConfigBean getConfigBean() {
        return ConfigBean.getInstance();
    }

    protected PreferenceBean getPreference() {
        return PreferenceBean.getInstance();
    }

    protected void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void setActionBar(int theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(theme);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(theme));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }
    protected void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(configKey);
        editor.remove(auth_key);
        editor.remove(user_id);
        editor.commit();
        editor.apply();
        startActivity(new Intent(BaseActivity.this, SplashActivity.class));
        finishAffinity();
    }
    protected abstract void startDemo();

}
