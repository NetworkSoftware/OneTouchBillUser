package smart.pro.invoice.invoice;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import smart.pro.invoice.Mainbean;
import smart.pro.invoice.OnItemClick;
import smart.pro.invoice.R;
import smart.pro.invoice.app.AppConfig;
import smart.pro.invoice.app.AppController;
import smart.pro.invoice.app.BaseActivity;
import smart.pro.invoice.app.DatabaseHelper;
import smart.pro.invoice.app.HeaderFooterPageEvent;
import smart.pro.invoice.app.PdfConfig;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

import static smart.pro.invoice.app.AppConfig.DELETE_INVOICE;
import static smart.pro.invoice.app.AppConfig.user_id;

public class InvoiceListActivity extends BaseActivity implements OnItemClick {
    private ProgressDialog pDialog;

    RecyclerView invoice_recyclerview;
    private ArrayList<Mainbean> mainbeans = new ArrayList<>();
    InvoiceItemAdapter minvoiceItemAdapter;
    DatabaseHelper db;
    private SearchView searchView;
    String billingMode = "INVOICE";
    TextView invoiceText, quotationTxt;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void startDemo() {
        setContentView(R.layout.all_invoice_list);
        getSupportActionBar().setTitle("BILLING LIST");
        invoiceText = findViewById(R.id.invoiceText);
        quotationTxt = findViewById(R.id.quotationTxt);
        invoiceText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));

        invoiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingMode = "INVOICE";
                mainbeans = new ArrayList<>();
                mainbeans.addAll(db.getAllMainbeans(billingMode));
                minvoiceItemAdapter.notifyData(mainbeans);
                invoiceText.setTextColor(Color.parseColor("#ffffff"));
                invoiceText.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                invoiceText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
                quotationTxt.setTextColor(Color.parseColor("#000000"));
                quotationTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
            }
        });
        quotationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingMode = "QUOTATION";
                mainbeans = new ArrayList<>();
                mainbeans.addAll(db.getAllMainbeans(billingMode));
                minvoiceItemAdapter.notifyData(mainbeans);
                quotationTxt.setTextColor(Color.parseColor("#ffffff"));
                quotationTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                quotationTxt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
                invoiceText.setTextColor(Color.parseColor("#000000"));
                invoiceText.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
            }
        });


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new DatabaseHelper(this);
        mainbeans.addAll(db.getAllMainbeans(billingMode));
        getSupportActionBar().setSubtitle("INVOICE (" + mainbeans.size() + ") - QUOTATION (" + db.getAllMainbeans("QUOTATION").size() + ")");
        invoice_recyclerview = (findViewById(R.id.invoice_recyclerview));
        minvoiceItemAdapter = new InvoiceItemAdapter(this, mainbeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        invoice_recyclerview.setLayoutManager(addManager1);
        invoice_recyclerview.setAdapter(minvoiceItemAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        billingMode = "INVOICE";
        mainbeans = new ArrayList<>();
        mainbeans.addAll(db.getAllMainbeans(billingMode));
        minvoiceItemAdapter.notifyData(mainbeans);
        invoiceText.setTextColor(Color.parseColor("#ffffff"));
        invoiceText.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
        quotationTxt.setTextColor(Color.parseColor("#000000"));
        quotationTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
        getSupportActionBar().setSubtitle("INVOICE (" + mainbeans.size() + ") - QUOTATION (" + db.getAllMainbeans("QUOTATION").size() + ")");

    }

    @Override
    public void itemClick(int position) {

        if (ContextCompat.checkSelfPermission(InvoiceListActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InvoiceListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            printClick(position);
        }

    }

    private void printClick(int position) {
        pDialog.setMessage("Printing...");
        showDialog();
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.RoundShapeTheme);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        TextView title = dialogView.findViewById(R.id.title);
        title.setText("Do you want add digital signature?");
        dialogBuilder.setTitle("Alert")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        printFunction(getApplicationContext(), mainbeans.get(position), true);
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        hideDialog();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        printFunction(getApplicationContext(), mainbeans.get(position), false);
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);


        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
    }

    @Override
    public void itemDeleteClick(int position) {
        printDialog(position);
    }

    @Override
    public void itemEditClick(int position) {

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
            PdfConfig.addContent(document, mainbean, includeGst, InvoiceListActivity.this, getConfigBean(),getPreference());
            //AppConfig.addTitlePage(document);


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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void printDialog(int position) {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.RoundShapeTheme);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);


        dialogBuilder.setTitle("Alert")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCreateInvoice(position);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);


        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                minvoiceItemAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                minvoiceItemAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getCreateInvoice(int position) {
        this.pDialog.setMessage("Deleting...");
        showDialog();
        StringRequest local16 = new StringRequest(1, DELETE_INVOICE, new Response.Listener<String>() {
            public void onResponse(String paramString) {
                Log.d("tag", "Register Response: " + paramString.toString());
                hideDialog();
                try {
                    JSONObject localJSONObject1 = new JSONObject(paramString);
                    String str = localJSONObject1.getString("message");
                    if (localJSONObject1.getInt("success") == 1) {
                        db.deleteMainbean(mainbeans.get(position));
                        mainbeans.remove(position);
                        minvoiceItemAdapter.notifyData(mainbeans);
                        getSupportActionBar().setSubtitle("INVOICE (" + db.getAllMainbeans("INVOICE").size() + ") - QUOTATION (" + db.getAllMainbeans("QUOTATION").size() + ")");
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
                localHashMap.put("surveyer", sharedpreferences.getString(user_id,""));
                localHashMap.put("id", AppConfig.intToString(Integer.parseInt(mainbeans.get(position).sellerbillNo), 5));
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }

}
