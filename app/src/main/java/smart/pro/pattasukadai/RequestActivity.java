package smart.pro.pattasukadai;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.naz013.colorslider.ColorSlider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import smart.pro.pattasukadai.app.AndroidMultiPartEntity;
import smart.pro.pattasukadai.app.AppConfig;
import smart.pro.pattasukadai.app.AppController;
import smart.pro.pattasukadai.app.BaseActivity;
import smart.pro.pattasukadai.app.DatabaseHelper;
import smart.pro.pattasukadai.app.GlideApp;
import smart.pro.pattasukadai.app.Imageutils;

import static smart.pro.pattasukadai.app.AppConfig.USER_REG;

public class RequestActivity extends BaseActivity implements Imageutils.ImageAttachmentListener {
    private ColorSlider.OnColorSelectedListener mListener = new ColorSlider.OnColorSelectedListener() {
        @Override
        public void onColorChanged(int position, int color) {
            updateView(color);
            colorPrimary.setText(((String.valueOf(color)).replaceAll("-", "#")));

        }
    };
    Imageutils imageutils;
    private static final int START_SIGN_CODE = 198;

    TextInputLayout brandNameText;
    TextInputLayout colorPrimaryText;
    TextInputLayout mailText;
    TextInputLayout telegramText;
    TextInputLayout websiteText;
    TextInputLayout whatsappText;

    ColorSlider color_slider;
    TextInputEditText brandName;
    TextInputEditText colorPrimary;
    TextInputEditText mail;
    TextInputEditText telegram;
    TextInputEditText website;
    TextInputEditText whatsapp;

    MaterialButton colorPrimaryclick;
    ImageView image_placeholder, image_wallpaper;
    ImageView image_placeholder1, image_wallpaper1;

    CardView itemsAdd, itemsAdd1;
    ExtendedFloatingActionButton submitBtn;
    private String imageUrllogo = "";
    private String imageUrlsign = "";

    private ProgressDialog pDialog;

    private DatabaseHelper db;

    UserBean userBean = null;
    private String TAG = getClass().getSimpleName();
    ColorSlider slider;
    private boolean logoClick = true;
    private boolean signClick = true;
    ImageView tempImage;
    ImageView signImage;
    ImageView tempPlace;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_request);
        getSupportActionBar().setTitle("ProInvoice - Register");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageutils = new Imageutils(this);

        db = new DatabaseHelper(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        brandNameText = findViewById(R.id.brandNameText);
        colorPrimaryText = findViewById(R.id.colorPrimaryText);
        mailText = findViewById(R.id.mailText);
        telegramText = findViewById(R.id.telegramText);
        websiteText = findViewById(R.id.websiteText);
        whatsappText = findViewById(R.id.whatsappText);
        brandName = findViewById(R.id.brandName);
        colorPrimary = findViewById(R.id.colorPrimary);
        mail = findViewById(R.id.mail);
        telegram = findViewById(R.id.telegram);
        website = findViewById(R.id.website);
        whatsapp = findViewById(R.id.whatsapp);

        image_wallpaper = findViewById(R.id.image_wallpaper);
        image_placeholder = findViewById(R.id.image_placeholder);
        itemsAdd = findViewById(R.id.itemsAdd);
        image_wallpaper1 = findViewById(R.id.image_wallpaper1);
        image_placeholder1 = findViewById(R.id.image_placeholder1);
        itemsAdd1 = findViewById(R.id.itemsAdd1);
        submitBtn = findViewById(R.id.submitBtn);
        itemsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempImage = image_wallpaper;
                tempPlace = image_placeholder;
                imageutils.imagepicker(1);
            }
        });
        itemsAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signImage = image_wallpaper1;
                tempPlace = image_placeholder1;
                signpicker(0);

            }
        });

        try {
            userBean = (UserBean) getIntent().getSerializableExtra("data");
            GlideApp.with(getApplicationContext())
                    .load(userBean.applogo)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(image_wallpaper);
            imageUrllogo = userBean.applogo;
            signImage = image_wallpaper1;
            imageUrlsign = userBean.usersign;
            if (imageUrlsign != null) {
                GlideApp.with(RequestActivity.this).load(imageUrlsign)
                        .into(image_wallpaper1);
            }
            image_placeholder.setVisibility(View.GONE);
            image_placeholder1.setVisibility(View.GONE);
            brandName.setText(userBean.brandName);
            colorPrimary.setText(userBean.colorPrimary);
            mail.setText(userBean.mail);
            telegram.setText(userBean.telegram);
            website.setText(userBean.website);
            whatsapp.setText(userBean.whatsapp);


        } catch (Exception e) {
        }

         slider = findViewById(R.id.color_slider);
        slider.setSelectorColor(Color.BLUE);
        slider.setListener(mListener);

        updateView(slider.getSelectedColor());

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(imageutils.getPath(uri));
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        String filepath;
        public long totalSize = 0;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (String.valueOf(progress[0])));
        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result.toString());

                if (!jsonObject.getBoolean("error")) {

                    GlideApp.with(getApplicationContext())
                            .load(filepath)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(false)
                            .into(tempImage);
                    imageUrllogo = AppConfig.ip + "/admin/invoice/image/" + imageutils.getfilename_from_path(filepath);
                    tempPlace.setVisibility(View.GONE);
                } else {
                    tempImage.setVisibility(View.VISIBLE);
                    tempImage = null;
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);
            super.onPostExecute(result);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_SIGN_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                String returnValue = data.getStringExtra("sign");
                int returnPosition = Integer.parseInt(
                        data.getStringExtra("position"));
                showDialog();
                new UploadSignToServer().execute(returnValue);
            }
            return;
        }

        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    private class UploadSignToServer extends AsyncTask<String, Integer, String> {
        public long totalSize = 0;
        String filepath;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (String.valueOf(progress[0])));
        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                if (!jsonObject.getBoolean("error")) {
                    GlideApp.with(RequestActivity.this).load(filepath)
                            .into(signImage);
                    imageUrlsign = AppConfig.ip + "/admin/invoice/image/" + imageutils.getfilename_from_path(filepath);
                    tempPlace.setVisibility(View.GONE);
                } else {
                    signImage.setVisibility(View.VISIBLE);
                    signImage = null;
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                USER_REG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String msg = jsonObject.getString("message");
                    if (success == 1) {
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                if (userBean != null) {
                    localHashMap.put("id", userBean.id);
                }
                localHashMap.put("brandName", brandName.getText().toString());
                localHashMap.put("colorPrimary",colorPrimary.getText().toString());
                localHashMap.put("mail", mail.getText().toString());
                localHashMap.put("telegram", telegram.getText().toString());
                localHashMap.put("website", website.getText().toString());
                localHashMap.put("whatsapp", whatsapp.getText().toString());
                localHashMap.put("applogo", imageUrllogo);
                localHashMap.put("usersign", imageUrlsign);
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }


    public void signpicker(final int position) {

        final CharSequence[] items;


        items = new CharSequence[1];
        items[0] = "Signpad";


        android.app.AlertDialog.Builder alertdialog =
                new android.app.AlertDialog.Builder(RequestActivity.this);
        alertdialog.setTitle("Add Signature");
        alertdialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Signpad")) {
                    Intent intent = new Intent(RequestActivity.this, MainActivitySignature.class);
                    intent.putExtra("position", String.valueOf(position));
                    startActivityForResult(intent, START_SIGN_CODE);
                }
            }
        });
        alertdialog.show();
    }

    private void updateView(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        }
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