package smart.pro.invoice.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import smart.pro.invoice.ConfigBean;
import smart.pro.invoice.PreferenceBean;
import smart.pro.invoice.R;

import static smart.pro.invoice.app.AppConfig.mypreference;

public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        try {
            setTheme(R.style.AppTheme);
            int theme = Color.parseColor(ConfigBean.getInstance().colorPrimary);
            setActionBar(theme);
        } catch (Exception e) {
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
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(theme));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getWindow().setStatusBarColor(theme);
        }
    }

    protected abstract void startDemo();

}
