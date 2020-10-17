package smart.pro.invoice;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;

import smart.pro.invoice.app.AppConfig;
import smart.pro.invoice.app.AppController;
import smart.pro.invoice.app.BaseActivity;
import smart.pro.invoice.app.DatabaseHelper;
import smart.pro.invoice.app.HeaderFooterPageEvent;
import smart.pro.invoice.app.PdfConfig;
import smart.pro.invoice.invoice.InvoiceListActivity;
import smart.pro.invoice.invoice.ParticularItemAdapter;
import smart.pro.invoice.invoice.Particularbean;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static smart.pro.invoice.app.AppConfig.UPDATE_INVOICE;
import static smart.pro.invoice.app.AppConfig.auth_key;
import static smart.pro.invoice.app.AppConfig.user_id;

public class MainEditActivity extends BaseActivity implements OnItemClick {

    TextInputLayout previousText;
    TextInputLayout pakagecostText;

    TextInputEditText previous;
    TextInputEditText pakagecost;


    private ProgressDialog pDialog;

    private DatabaseHelper db;


    RecyclerView particular_list;
    private ArrayList<Particularbean> particularbeans = new ArrayList<>();
    ParticularItemAdapter mparticularItemAdapter;
    private RoundedBottomSheetDialog mBottomSheetDialog;
    private String TAG = getClass().getSimpleName();
    Mainbean mainbeanUpdate = null;

    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_edit_main);
        try {
            mainbeanUpdate = (Mainbean) getIntent().getSerializableExtra("data");
            getSupportActionBar().setSubtitle(AppConfig.intToString(Integer.parseInt(mainbeanUpdate.sellerbillNo), 5));
        } catch (Exception e) {
            Log.e("xxxxxxx", e.toString());
        }
        db = new DatabaseHelper(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getSupportActionBar().setTitle("ProInvoice");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        previous = (findViewById(R.id.previous));
        pakagecost = (findViewById(R.id.pakagecost));
        previousText = (findViewById(R.id.previousText));
        pakagecostText = (findViewById(R.id.pakagecostText));
        MaterialButton addItems = (findViewById(R.id.addItems));
        addItems.setStrokeColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        addItems.setTextColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        addItems.setIconTint(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog = new RoundedBottomSheetDialog(MainEditActivity.this);
                LayoutInflater inflater = MainEditActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_particulars, null);
                AutoCompleteTextView particular = dialogView.findViewById(R.id.particular);
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(MainEditActivity.this, android.R.layout.simple_list_item_1, db.getAllcategoryMainbeans());
                particular.setThreshold(1);
                particular.setAdapter(adapter);
                TextInputLayout cgstText = dialogView.findViewById(R.id.cgstText);
                TextInputLayout sgstText = dialogView.findViewById(R.id.sgstText);
                TextInputLayout igstText = dialogView.findViewById(R.id.igstText);
                TextInputEditText cgst = dialogView.findViewById(R.id.cgst);
                TextInputEditText sgst = dialogView.findViewById(R.id.sgst);
                TextInputEditText igst = dialogView.findViewById(R.id.igst);
                Button submitBtn = dialogView.findViewById(R.id.submitBtn);
                Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);

                TextInputEditText quantity = dialogView.findViewById(R.id.quantity);
                TextInputEditText perQuantity = dialogView.findViewById(R.id.perQuantity);
                particular.requestFocus();
                quantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                perQuantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Particularbean particularbean = new Particularbean(
                                particular.getText().toString().replace("\"", " inch"),
                                quantity.getText().toString(),
                                perQuantity.getText().toString(),
                                cgst.getText().toString(),
                                sgst.getText().toString(), igst.getText().toString());
                        particularbeans.add(particularbean);
                        mparticularItemAdapter.notifyData(particularbeans);
                        mBottomSheetDialog.cancel();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.cancel();
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
                        },0);
                    }
                });
                mBottomSheetDialog.show();

            }
        });


        if (Boolean.parseBoolean(mainbeanUpdate.includegst)) {
            ((TextView) findViewById(R.id.cgstLabel)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.sgstLabel)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.igstLabel)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.cgstLabel)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.sgstLabel)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.igstLabel)).setVisibility(View.GONE);
        }

        particular_list = (findViewById(R.id.particular_list));
        mparticularItemAdapter = new ParticularItemAdapter(this, particularbeans, this, Boolean.parseBoolean(
                mainbeanUpdate.includegst));
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        particular_list.setLayoutManager(addManager1);
        particular_list.setAdapter(mparticularItemAdapter);


        previous.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (previous.toString().length() > 0) {
                    previousText.setError(null);
                }
            }
        });
        pakagecost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pakagecost.toString().length() > 0) {
                    pakagecostText.setError(null);
                }
            }
        });


        previous.setText(mainbeanUpdate.previous);
        pakagecost.setText(mainbeanUpdate.pakagecost);
        particularbeans = mainbeanUpdate.getParticularbeans();
        mparticularItemAdapter.notifyData(particularbeans);

        Button cancel = (findViewById(R.id.cancel));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button print = (findViewById(R.id.save));
        print.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previous.getText().toString().length() <= 0) {
                    previousText.setError("Enter the Previous Amount");
                    previousText.requestFocus();
                } else if (pakagecost.getText().toString().length() <= 0) {
                    pakagecostText.setError("Enter the Package Cost");
                    pakagecostText.requestFocus();
                } else {
                    if (ContextCompat.checkSelfPermission(MainEditActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainEditActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    } else {
                        printFunction();
                    }
                }

            }

        });

    }

    private void printFunction() {
        pDialog.setMessage("Printing...");
        showDialog();

        mainbeanUpdate.setPrevious(previous.getText().toString());
        mainbeanUpdate.setPakagecost(pakagecost.getText().toString());
        mainbeanUpdate.setParticularbeans(particularbeans);
        String jsonVal = new Gson().toJson(mainbeanUpdate);
        getCreateInvoice(String.valueOf(mainbeanUpdate.id), jsonVal, mainbeanUpdate);


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
            File file = new File(dir, mainbean.getBuyername().replace(" ", "_") + "_" + mainbean.getSellerbillNo() + ".pdf");
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

            boolean includeGst = false;
            if (mainbean.includegst != null && mainbean.includegst.length() > 0 &&
                    Boolean.parseBoolean(mainbean.includegst)) {
                includeGst = true;
            }
            HeaderFooterPageEvent event = new HeaderFooterPageEvent(Image.getInstance(byteArray), Image.getInstance(byteArray1), isDigital, getConfigBean());
            pdfWriter.setPageEvent(event);
            PdfConfig.addContent(document, mainbean, includeGst, MainEditActivity.this, getConfigBean(), getPreference());

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
            finish();
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
                printFunction();
            } else {
                Toast.makeText(MainEditActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainEditActivity.this, InvoiceListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void itemClick(int position) {

    }

    @Override
    public void itemDeleteClick(int position) {
        particularbeans.remove(position);
        mparticularItemAdapter.notifyData(particularbeans);
    }

    @Override
    public void itemEditClick(int position) {
        mBottomSheetDialog = new RoundedBottomSheetDialog(MainEditActivity.this);
        LayoutInflater inflater = MainEditActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_particulars, null);
        AutoCompleteTextView particular = dialogView.findViewById(R.id.particular);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(MainEditActivity.this, android.R.layout.simple_list_item_1, db.getAllcategoryMainbeans());
        particular.setThreshold(1);
        particular.setAdapter(adapter);
        TextInputLayout cgstText = dialogView.findViewById(R.id.cgstText);
        TextInputLayout sgstText = dialogView.findViewById(R.id.sgstText);
        TextInputLayout igstText = dialogView.findViewById(R.id.igstText);
        TextInputEditText cgst = dialogView.findViewById(R.id.cgst);
        TextInputEditText sgst = dialogView.findViewById(R.id.sgst);
        TextInputEditText igst = dialogView.findViewById(R.id.igst);
        Button submitBtn = dialogView.findViewById(R.id.submitBtn);
        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        submitBtn.setText("Update");
        TextInputEditText quantity = dialogView.findViewById(R.id.quantity);
        TextInputEditText perQuantity = dialogView.findViewById(R.id.perQuantity);

        Particularbean particularbean = particularbeans.get(position);
        quantity.setText(particularbean.quantity);
        particular.setText(particularbean.particular.replace(" inch", "\""));
        perQuantity.setText(particularbean.perquantity);
        cgst.setText(particularbean.cgst);
        sgst.setText(particularbean.sgst);
        igst.setText(particularbean.igst);

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        perQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                particularbeans.get(position).setParticular(particular.getText().toString().replace("\"", " inch"));
                particularbeans.get(position).setQuantity(quantity.getText().toString());
                particularbeans.get(position).setPerquantity(perQuantity.getText().toString());
                particularbeans.get(position).setCgst(cgst.getText().toString());
                particularbeans.get(position).setSgst(sgst.getText().toString());
                particularbeans.get(position).setIgst(igst.getText().toString());
                mparticularItemAdapter.notifyData(particularbeans);
                mBottomSheetDialog.cancel();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
            }
        });
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.show();
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        particular.requestFocus();


    }

    private void getCreateInvoice(final String mId, final String data, Mainbean tempMainbean) {
        this.pDialog.setMessage("Updating...");
        showDialog();
        StringRequest local16 = new StringRequest(1, UPDATE_INVOICE, new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString("message");
                    if (localJSONObject1.getInt("success") == 1) {
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        db.updateMainbean(tempMainbean);
                        printFunction(getApplicationContext(), tempMainbean, Boolean.parseBoolean(tempMainbean.includegst));
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
                localHashMap.put("id", AppConfig.intToString(Integer.parseInt(tempMainbean.sellerbillNo), 5));
                localHashMap.put("surveyer", sharedpreferences.getString(user_id, ""));
                localHashMap.put("auth_key", sharedpreferences.getString(auth_key, ""));
                localHashMap.put("data", data);


                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
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