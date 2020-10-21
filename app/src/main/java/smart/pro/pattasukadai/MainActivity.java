package smart.pro.pattasukadai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;

import smart.pro.pattasukadai.app.AppConfig;
import smart.pro.pattasukadai.app.AppController;
import smart.pro.pattasukadai.app.BaseActivity;
import smart.pro.pattasukadai.app.DatabaseHelper;
import smart.pro.pattasukadai.app.DbPattasuHelper;
import smart.pro.pattasukadai.app.HeaderFooterPageEvent;
import smart.pro.pattasukadai.app.Pattasu;
import smart.pro.pattasukadai.app.PdfConfig;
import smart.pro.pattasukadai.buyer.BuyerListActivity;
import smart.pro.pattasukadai.invoice.InvoiceListActivity;
import smart.pro.pattasukadai.invoice.ParticularItemAdapter;
import smart.pro.pattasukadai.invoice.Particularbean;
import smart.pro.pattasukadai.seller.SellerListActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static smart.pro.pattasukadai.app.AppConfig.CREATE_INVOICE;
import static smart.pro.pattasukadai.app.AppConfig.GET_ALL_DATA;
import static smart.pro.pattasukadai.app.AppConfig.auth_key;
import static smart.pro.pattasukadai.app.AppConfig.shopAddreesKey;
import static smart.pro.pattasukadai.app.AppConfig.shopIdKey;
import static smart.pro.pattasukadai.app.AppConfig.shopNameKey;
import static smart.pro.pattasukadai.app.AppConfig.shopPhoneKey;
import static smart.pro.pattasukadai.app.AppConfig.user_id;

public class MainActivity extends BaseActivity implements OnItemClick {

    NestedScrollView nestScroll;
    TextView seller_name;
    TextView tittle;
    ImageView action_settings, action_about;
    LinearLayout headerbar;
    RelativeLayout scroll;
    private ProgressDialog pDialog;
    private DatabaseHelper db;
    private DbPattasuHelper dbPattasuHelper;

    RecyclerView particular_list;
    private ArrayList<Particularbean> particularbeans = new ArrayList<>();
    ParticularItemAdapter mparticularItemAdapter;
    private RoundedBottomSheetDialog mBottomSheetDialog;
    private String TAG = getClass().getSimpleName();
    EditText no, quantity;
    TextView proName, proTotal, grandTotal;
    int selectedPosition = -1;
    MaterialButton addItems;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_main);


        db = new DatabaseHelper(this);
        dbPattasuHelper = new DbPattasuHelper(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        seller_name = findViewById(R.id.seller_name);
        seller_name.setText(sharedpreferences.getString(shopNameKey, ""));
        nestScroll = findViewById(R.id.nestScroll);
        tittle = findViewById(R.id.tittle);
        action_about = findViewById(R.id.action_about);
        action_settings = findViewById(R.id.action_settings);
        headerbar = findViewById(R.id.headerbar);
        scroll = findViewById(R.id.scroll);
        proName = findViewById(R.id.proName);
        no = findViewById(R.id.no);
        quantity = findViewById(R.id.quantity);
        proTotal = findViewById(R.id.proTotal);
        grandTotal = findViewById(R.id.grandTotal);
        no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getAutoFill(s.toString());
            }
        });
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getAutoFill(no.getText().toString());
            }
        });
        addItems = findViewById(R.id.addItems);
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proTotal.getText().toString().length() > 0) {
                    if (selectedPosition == -1) {
                        Particularbean particularbean = new Particularbean();
                        particularbean.setId(no.getText().toString());
                        particularbean.setParticular(proName.getText().toString());
                        particularbean.setPerquantity(dbPattasuHelper.getPattasu(no.getText().toString()).price);
                        particularbean.setQuantity(quantity.getText().toString());
                        particularbeans.add(particularbean);
                    } else {
                        particularbeans.get(selectedPosition).setId(no.getText().toString());
                        particularbeans.get(selectedPosition).setParticular(proName.getText().toString());
                        particularbeans.get(selectedPosition).setPerquantity(dbPattasuHelper.getPattasu(no.getText().toString()).price);
                        particularbeans.get(selectedPosition).setQuantity(quantity.getText().toString());
                    }
                    no.requestFocus();
                    selectedPosition = -1;
                    addItems.setText("ADD");
                    mparticularItemAdapter.notifyData(particularbeans);
                    proName.setText("");
                    proTotal.setText("");
                    no.setText("");
                    quantity.setText("");

                    calculateTotal();

                }
            }
        });

        action_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), InvoiceListActivity.class);
                startActivity(intent);
            }
        });

        action_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SettingsActivity.class);
                startActivity(intent);
            }
        });


        particular_list = (findViewById(R.id.particular_list));
        mparticularItemAdapter = new ParticularItemAdapter(this, particularbeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        particular_list.setLayoutManager(addManager1);
        particular_list.setAdapter(mparticularItemAdapter);

        ExtendedFloatingActionButton print = findViewById(R.id.print);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog = new RoundedBottomSheetDialog(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.customer_details, null);
                TextInputEditText customerName = dialogView.findViewById(R.id.customerName);
                TextInputEditText whatsappNumber = dialogView.findViewById(R.id.whatsappNumber);
                Button submitBtn = dialogView.findViewById(R.id.submitBtn);
                Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customerName.getText().toString().length() <= 0 &&
                                whatsappNumber.getText().toString().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter valid Contact", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Mainbean mainbean = new Mainbean();
                        mainbean.setSellername(sharedpreferences.getString(shopNameKey, ""));
                        mainbean.setSellerphone(sharedpreferences.getString(shopPhoneKey, ""));
                        mainbean.setSelleraddress(sharedpreferences.getString(shopAddreesKey, ""));
                        mainbean.setParticularbeans(particularbeans);
                        mainbean.setBuyerphone(whatsappNumber.getText().toString());
                        mainbean.setBuyername(customerName.getText().toString());
                        getCreateInvoice(mainbean);
                        bottomSheetCancel();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetCancel();
                    }
                });
                mBottomSheetDialog.setContentView(dialogView);
                mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BottomSheetDialog d = (BottomSheetDialog) dialog;
                                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }, 0);
                    }
                });
                mBottomSheetDialog.show();

            }
        });

        getAllData();
    }

    private void calculateTotal() {
        float grandT = 0;
        for (int i = 0; i < particularbeans.size(); i++) {
            Particularbean particularbean = particularbeans.get(i);
            String quanS = "1";
            if (particularbean.getQuantity() != null && particularbean.getQuantity().length() > 0) {
                quanS = particularbean.getQuantity();
            }
            float quan = Float.parseFloat(quanS);
            float perQuanPri = Float.parseFloat(particularbean.perquantity);
            grandT = grandT + quan * perQuanPri;
        }
        grandTotal.setText("₹ " + grandT);
    }

    private void getAutoFill(String s) {
        if (s.length() > 0) {
            if (dbPattasuHelper.getPattasu(s) != null) {
                Pattasu pattasu = dbPattasuHelper.getPattasu(s);
                proName.setText(pattasu.items);
                int quan = 1;
                if (quantity.getText().toString().length() > 0) {
                    quan = Integer.parseInt(quantity.getText().toString());
                }
                proTotal.setText("* " + pattasu.getPrice() + " = " + quan * Integer.parseInt(pattasu.price));
            } else {
                proName.setText("");
                proTotal.setText("");
            }
        } else {
            proName.setText("");
            proTotal.setText("");
        }
    }

    private void bottomSheetCancel() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.cancel();
        }
        AppConfig.hideKeyboard(MainActivity.this);
        nestScroll.post(new Runnable() {
            @Override
            public void run() {
                nestScroll.fullScroll(View.FOCUS_DOWN);
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

    public void printFunction(Context context, Mainbean mainbean, boolean isDigital) {

        try {

            String path = Environment.getExternalStorageDirectory().getPath() + "/PDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, mainbean.getBuyername().replace(" ", "_") + "_" + mainbean.getDbid() + ".pdf");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fOut = new FileOutputStream(file);


            Document document = new Document(PageSize.A4, 30, 28, 40, 119);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, fOut);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            getConfigBean().getOwnersignBit().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            getConfigBean().getPoweredByLogoBit().compress(Bitmap.CompressFormat.PNG, 100, stream1);
            byte[] byteArray1 = stream1.toByteArray();


            document.open();
            PdfConfig.addMetaData(document);

            HeaderFooterPageEvent event = new HeaderFooterPageEvent(Image.getInstance(byteArray), Image.getInstance(byteArray1), isDigital, getConfigBean());
            pdfWriter.setPageEvent(event);
            PdfConfig.addContent(document, mainbean, true, MainActivity.this, getConfigBean(), getPreference());

            document.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file), "application/pdf");
                Intent intent = Intent.createChooser(target, "Open File");
                startActivity(intent);
            }

        } catch (Error | Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        hideDialog();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, InvoiceListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_chart) {
            Intent intent = new Intent(MainActivity.this, CustomerReport.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void itemClick(int position) {

    }

    @Override
    public void itemDeleteClick(int position) {
        particularbeans.remove(position);
        mparticularItemAdapter.notifyData(particularbeans);
        calculateTotal();
    }

    @Override
    public void itemEditClick(int position) {
        if (selectedPosition == -1) {
            selectedPosition = position;
            no.setText(particularbeans.get(position).id);
            quantity.setText(particularbeans.get(position).quantity);
            addItems.setText("Update");
        }
    }


    private String getBillNo() {
        List<Mainbean> mainbeans = db.getLastBillNo();
        String billNo = "00001";
        if (mainbeans.size() > 0) {
            billNo = AppConfig.intToString(Integer.parseInt(mainbeans.get(0).dbid) + 1, 5);
        }
        return billNo;
    }

    private void getCreateInvoice(Mainbean tempMainbean) {
        this.pDialog.setMessage("Creating...");
        showDialog();
        StringRequest local16 = new StringRequest(1, CREATE_INVOICE, new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString("message");
                    if (localJSONObject1.getInt("success") == 1) {
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        Date d = new Date();
                        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
                        String dbId = localJSONObject1.getString("dbid");
                        tempMainbean.setTimestamp(s.toString());
                        tempMainbean.setDbid(dbId);
                        db.insertMainbean(tempMainbean);
                        printFunction(getApplicationContext(), tempMainbean, true);

                    } else if (str.equals("Invalid authtoken")) {
                        logout();
                    }
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException localJSONException) {
                    localJSONException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Log.e("tag", "Fetch Error: " + paramVolleyError.getMessage());
                Toast.makeText(getApplicationContext(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put("userid", sharedpreferences.getString(user_id, ""));
                localHashMap.put("shopid", sharedpreferences.getString(shopIdKey, ""));
                localHashMap.put("auth_key", sharedpreferences.getString(auth_key, ""));
                localHashMap.put("data", new Gson().toJson(tempMainbean));
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }


    private void getAllData() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Fetching ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, GET_ALL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    db.deleteAll();
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("invoice");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject dataObject = jsonArray.getJSONObject(i);
                                String inputVal = dataObject.getString("data");
                                String createdon = dataObject.getString("createdon");
                                String id = dataObject.getString("id");
                                Mainbean mainbean = new Gson().fromJson(inputVal, Mainbean.class);
                                mainbean.setDbid(id);
                                mainbean.setTimestamp(createdon);
                                db.insertMainbean(mainbean);
                            } catch (Exception e) {
                                Log.e("xxxxxxxxxxx", e.toString());
                            }
                        }
                        JSONArray pattasu = jObj.getJSONArray("pattasu");
                        dbPattasuHelper.deleteAll();
                        for (int i = 0; i < pattasu.length(); i++) {
                            try {
                                JSONObject dataObject = pattasu.getJSONObject(i);
                                Pattasu pattasuBean = new Pattasu(

                                        dataObject.getString("title"),
                                        dataObject.getString("items"),
                                        dataObject.getString("price"),
                                        dataObject.getString("id")
                                );
                                dbPattasuHelper.insertPattasu(pattasuBean);
                            } catch (Exception e) {
                                Log.e("xxxxxxxxxxx", e.toString());
                            }
                        }
                        dbPattasuHelper.getAllPattasu();
                    }
                } catch (Exception e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    //     Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }
                hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("userid", sharedpreferences.getString(user_id, ""));
                localHashMap.put("shopid", sharedpreferences.getString(shopIdKey, ""));
                localHashMap.put("auth_key", sharedpreferences.getString(auth_key, ""));
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}