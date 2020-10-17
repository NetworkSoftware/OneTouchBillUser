package smart.pro.invoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import smart.pro.invoice.help_video.HelpVideosActivity;
import smart.pro.invoice.app.BaseActivity;

import static smart.pro.invoice.app.AppConfig.serialNo;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((LinearLayout) findViewById(R.id.changePassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePassword.class));
            }
        });
        ((LinearLayout) findViewById(R.id.help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, HelpVideosActivity.class));
            }
        });
        ((LinearLayout) findViewById(R.id.contactus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:09787665850"));
                startActivity(intent);
            }
        });
        CheckBox serialCheckBox = findViewById(R.id.serialCheckBox);
        serialCheckBox.setChecked(sharedpreferences.getBoolean(serialNo, false));
        ((CheckBox) findViewById(R.id.serialCheckBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(serialNo, isChecked);
                editor.commit();
                getPreference().setSerial(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
