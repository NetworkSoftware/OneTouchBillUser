package smart.pro.pattasukadai;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import smart.pro.pattasukadai.app.AppConfig;
import smart.pro.pattasukadai.app.AppController;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static smart.pro.pattasukadai.app.AppConfig.LOGIN_USER;
import static smart.pro.pattasukadai.app.AppConfig.mypreference;
import static smart.pro.pattasukadai.app.AppConfig.serialNo;
import static smart.pro.pattasukadai.app.AppConfig.shopIdKey;

public class SplashActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputLayout usernameTxt;
    TextInputEditText password;
    TextInputLayout passwordText;
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
        if (sharedpreferences.contains(AppConfig.configKey)) {
            username.setText(sharedpreferences.getString(AppConfig.configKey, ""));
        }

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
        StringRequest strReq = new StringRequest(Request.Method.POST, LOGIN_USER, new Response.Listener<String>() {
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
                        String shopId = jObj.getString("shopId");
                        String shopName = jObj.getString("shopName");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(AppConfig.configKey, username.getText().toString());
                        editor.putString(AppConfig.auth_key, auth_key);
                        editor.putString(AppConfig.user_id, user_id);
                        editor.putString(AppConfig.shopIdKey, shopId);
                        editor.putString(AppConfig.shopNameKey, shopName);
                        editor.putString(AppConfig.shopPhoneKey, jObj.getString("shopPhone"));
                        editor.putString(AppConfig.shopAddreesKey, jObj.getString("shopAddress"));
                        editor.commit();
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
