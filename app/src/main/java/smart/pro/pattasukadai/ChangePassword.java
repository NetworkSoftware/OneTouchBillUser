package smart.pro.pattasukadai;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import smart.pro.pattasukadai.app.AppConfig;
import smart.pro.pattasukadai.app.AppController;
import smart.pro.pattasukadai.app.BaseActivity;

import static smart.pro.pattasukadai.app.AppConfig.CHANGE_PASSWORD;
import static smart.pro.pattasukadai.app.AppConfig.auth_key;

public class ChangePassword extends BaseActivity {

    TextInputEditText username, password, newpassword, newCpassword;
    TextInputLayout usernameTxt, passwordText, newCpasswordText, newpasswordText;
    ProgressDialog dialog;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        username = findViewById(R.id.username);
        usernameTxt = findViewById(R.id.usernameTxt);
        password = findViewById(R.id.password);
        passwordText = findViewById(R.id.passwordText);
        newCpassword = findViewById(R.id.newCpassword);
        newCpasswordText = findViewById(R.id.newCpasswordText);
        newpassword = findViewById(R.id.newpassword);
        newpasswordText = findViewById(R.id.newpasswordText);

        if (sharedpreferences.contains(AppConfig.configKey)) {
            username.setText(sharedpreferences.getString(AppConfig.configKey, ""));
        }

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setBackgroundColor(Color.parseColor(getConfigBean().getColorPrimary()));
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newpassword.getText().toString().length() < 8) {
                    showToast("Password minimum 8 characters needed");
                } else if (!newpassword.getText().toString().equals(newCpassword.getText().toString())) {
                    showToast("Password not match");
                } else if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    changePassword();
                } else {
                    showToast("Invalid Credentials");
                }
            }
        });

    }

    private void changePassword() {
        String tag_string_req = "req_register";
        dialog.setMessage("Updateing Password ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    String msg = jObj.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    if (success == 1) {
                        finish();
                    }
                    else if (msg.equals("Invalid authtoken")) {
                        logout();
                    }
                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
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
                localHashMap.put("phone", username.getText().toString());
                localHashMap.put("oldpw", password.getText().toString());
                localHashMap.put("newpw", newpassword.getText().toString());
                localHashMap.put("auth_key", sharedpreferences.getString(auth_key, ""));
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
