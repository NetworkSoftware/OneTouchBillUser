package smart.pro.invoice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import smart.pro.invoice.app.AppConfig;
import smart.pro.invoice.app.AppController;
import smart.pro.invoice.app.BaseActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static smart.pro.invoice.app.AppConfig.LOGIN_INVOICE;
import static smart.pro.invoice.app.AppConfig.isLogin;
import static smart.pro.invoice.app.AppConfig.mypreference;
import static smart.pro.invoice.app.AppConfig.serialNo;

public class SplashActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputLayout usernameTxt;
    TextInputEditText password;
    TextInputLayout passwordText;
    TextView request;
    ProgressDialog dialog;
    protected SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        setContentView(R.layout.activity_splash);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        username = findViewById(R.id.username);
        usernameTxt = findViewById(R.id.usernameTxt);
        password = findViewById(R.id.password);
        passwordText = findViewById(R.id.passwordText);
        request=findViewById(R.id.request);
        if (sharedpreferences.contains(AppConfig.configKey)) {
            username.setText(sharedpreferences.getString(AppConfig.configKey, ""));
        }

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, RequestActivity.class));
            }
        });
        FloatingActionButton submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    checkLogin();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkLogin() {
        String tag_string_req = "req_register";
        dialog.setMessage("Login ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, LOGIN_INVOICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    String msg = jObj.getString("message");
                    if (success == 1) {
                        String auth_key = jObj.getString("auth_key");
                        String user_id = jObj.getString("user_id");
                        String data = jObj.getString("data");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(AppConfig.configKey, username.getText().toString());
                        editor.putString(AppConfig.auth_key, auth_key);
                        editor.putString(AppConfig.user_id, user_id);
                        editor.commit();

                        PreferenceBean.getInstance().setSerial(sharedpreferences.getBoolean(serialNo, false));

                        ConfigBean configBean = new Gson().fromJson(data, ConfigBean.class);
                        Bitmap ownersign = AppConfig.getBitmapFromURL(AppConfig.IMAGEPATH + configBean.getOwnerSign());
                        configBean.setOwnersignBit(ownersign);

                        Bitmap pdflogo = AppConfig.getBitmapFromURL(AppConfig.IMAGEPATH + configBean.getPdfLogo());
                        configBean.setPdfLogoBit(pdflogo);

                        Bitmap poweredbylogo = AppConfig.getBitmapFromURL(AppConfig.IMAGEPATH + configBean.getPoweredByLogo());
                        configBean.setPoweredByLogoBit(poweredbylogo);

                        ConfigBean.set_instance(configBean);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
                hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Slow network found.Try again later", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("email", username.getText().toString());
                localHashMap.put("password", password.getText().toString());
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
