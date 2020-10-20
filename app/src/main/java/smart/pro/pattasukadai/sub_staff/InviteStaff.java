package smart.pro.pattasukadai.sub_staff;

import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import smart.pro.pattasukadai.R;
import smart.pro.pattasukadai.app.BaseActivity;

public class InviteStaff extends BaseActivity {

    TextInputEditText staff_name, phone, subpassword, otp;
    TextInputLayout staff_nameText, phoneText, subpasswordText, otpText;
    ProgressDialog dialog;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_invitestaff);
        getSupportActionBar().setTitle("Invite Staff");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        staff_name = findViewById(R.id.staff_name);
        staff_nameText = findViewById(R.id.staff_nameText);
        phone = findViewById(R.id.phone);
        phoneText = findViewById(R.id.phoneText);
        subpassword = findViewById(R.id.subpassword);
        subpasswordText = findViewById(R.id.subpasswordText);
        otp = findViewById(R.id.otp);
        otpText = findViewById(R.id.otpText);


        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

/*
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
*/

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
