package smart.pro.invoice;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import smart.pro.invoice.app.AppConfig;
import smart.pro.invoice.app.AppController;
import smart.pro.invoice.app.BaseActivity;
import smart.pro.invoice.app.DatabaseHelper;
import smart.pro.invoice.app.HeaderFooterPageEvent;
import smart.pro.invoice.app.PdfConfig;
import smart.pro.invoice.buyer.BuyerListActivity;
import smart.pro.invoice.invoice.InvoiceListActivity;
import smart.pro.invoice.invoice.ParticularItemAdapter;
import smart.pro.invoice.invoice.Particularbean;
import smart.pro.invoice.seller.SellerListActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static smart.pro.invoice.app.AppConfig.CREATE_INVOICE;
import static smart.pro.invoice.app.AppConfig.GET_ALL_INVOICE;
import static smart.pro.invoice.app.AppConfig.auth_key;
import static smart.pro.invoice.app.AppConfig.user_id;

public class MainActivity extends BaseActivity implements OnItemClick {

    TextInputLayout sellernameText;
    TextInputLayout selleraddressText;
    TextInputLayout sellerphoneText;
    TextInputLayout sellergstNoText;
    TextInputLayout sellerbillNoText;
    TextInputLayout buyernameText;
    TextInputLayout buyeraddressText;
    TextInputLayout buyerphoneText;
    TextInputLayout buyergstNoText;
    TextInputLayout banknameText;
    TextInputLayout accountNoText;
    TextInputLayout ifcnoText;
    TextInputLayout previousText;
    TextInputLayout pakagecostText;

    TextInputEditText sellername;
    TextInputEditText selleraddress;
    TextInputEditText sellerphone;
    TextInputEditText sellergstNo;
    TextInputEditText sellerbillNo;
    TextInputEditText buyername;
    TextInputEditText buyeraddress;
    TextInputEditText buyerphone;
    TextInputEditText buyergstNo;
    TextInputEditText bankname;
    TextInputEditText accountNo;
    TextInputEditText ifcno;
    TextInputEditText previous;
    TextInputEditText pakagecost;
    NestedScrollView nestScroll;
    TextView seller_name;
    TextView tittle;
    ImageView action_settings, action_about;
    LinearLayout headerbar;
    RelativeLayout scroll;


    private ProgressDialog pDialog;

    private DatabaseHelper db;

    MaterialButton selectseller;
    MaterialButton selectbuyer;
    CheckBox checkGST, checkDigi;

    RecyclerView particular_list;
    private ArrayList<Particularbean> particularbeans = new ArrayList<>();
    ParticularItemAdapter mparticularItemAdapter;
    TextView invoiceText, quotationTxt;
    String billingMode = "INVOICE";
    TextInputEditText holdername;
    private RoundedBottomSheetDialog mBottomSheetDialog;
    private String TAG = getClass().getSimpleName();


    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_main);


        db = new DatabaseHelper(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        seller_name = (findViewById(R.id.seller_name));
        nestScroll = (findViewById(R.id.nestScroll));
        seller_name.setText(getConfigBean().brandName);
        tittle = (findViewById(R.id.tittle));
        action_about = (findViewById(R.id.action_about));
        action_settings = (findViewById(R.id.action_settings));
        headerbar = (findViewById(R.id.headerbar));
        scroll = (findViewById(R.id.scroll));


        holdername = (findViewById(R.id.holdername));
        sellernameText = (findViewById(R.id.sellernameText));
        selleraddressText = (findViewById(R.id.selleraddressText));
        sellerphoneText = (findViewById(R.id.sellerphoneText));
        sellergstNoText = (findViewById(R.id.sellergstNoText));
        sellerbillNoText = (findViewById(R.id.sellerbillNoText));
        buyernameText = (findViewById(R.id.buyernameText));
        buyeraddressText = (findViewById(R.id.buyeraddressText));
        buyerphoneText = (findViewById(R.id.buyerphoneText));
        buyergstNoText = (findViewById(R.id.buyergstNoText));
        selectseller = (findViewById(R.id.selectseller));
        selectbuyer = (findViewById(R.id.selectbuyer));

        banknameText = (findViewById(R.id.banknameText));
        accountNoText = (findViewById(R.id.accountNoText));
        ifcnoText = (findViewById(R.id.ifcnoText));
        previousText = (findViewById(R.id.previousText));
        pakagecostText = (findViewById(R.id.pakagecostText));

        sellername = (findViewById(R.id.sellername));
        selleraddress = (findViewById(R.id.selleraddress));
        sellerphone = (findViewById(R.id.sellerphone));
        sellergstNo = (findViewById(R.id.sellergstNo));
        sellerbillNo = (findViewById(R.id.sellerbillNo));
        buyername = (findViewById(R.id.buyername));
        buyeraddress = (findViewById(R.id.buyeraddress));
        buyerphone = (findViewById(R.id.buyerphone));
        buyergstNo = (findViewById(R.id.buyergstNo));

        bankname = (findViewById(R.id.bankname));
        accountNo = (findViewById(R.id.accountNo));
        ifcno = (findViewById(R.id.ifcno));
        previous = (findViewById(R.id.previous));
        pakagecost = (findViewById(R.id.pakagecost));
        checkGST = (findViewById(R.id.checkGST));
        checkDigi = (findViewById(R.id.checkDigi));
        MaterialButton addItems = (findViewById(R.id.addItems));
        validDateGst();
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog = new RoundedBottomSheetDialog(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_particulars, null);
                AutoCompleteTextView particular = dialogView.findViewById(R.id.particular);
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, db.getAllcategoryMainbeans());
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
//                sgst.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if (checkGST.isChecked() && s.toString().length() > 0) {
//                            int sgstVal = Integer.parseInt(s.toString());
//                            int cgstVal = 0;
//                            if (cgst.getText().toString().length() > 0) {
//                                cgstVal = Integer.parseInt(cgst.getText().toString());
//                            }
//                            int igstVal = 0;
//                            if (igst.getText().toString().length() > 0) {
//                                igstVal = Integer.parseInt(igst.getText().toString());
//                            }
//                            if (sgstVal + cgstVal + igstVal > 18) {
//                                sgstText.setError("Total GST must equal to 18%");
//                            } else {
//                                sgstText.setError(null);
//                            }
//                        }
//                    }
//                });
//
//                cgst.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable c) {
//                        if (checkGST.isChecked() && c.toString().length() > 0) {
//                            int cgstVal = Integer.parseInt(c.toString());
//                            int sgstVal = 0;
//                            if (sgst.getText().toString().length() > 0) {
//                                sgstVal = Integer.parseInt(cgst.getText().toString());
//                            }
//                            int igstVal = 0;
//                            if (igst.getText().toString().length() > 0) {
//                                igstVal = Integer.parseInt(igst.getText().toString());
//                            }
//                            if (cgstVal + sgstVal + igstVal > 18) {
//                                cgstText.setError("Total GST must less than 18");
//                            } else {
//                                cgstText.setError(null);
//                            }
//                        }
//                    }
//                });
//
//                igst.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable i) {
//                        if (checkGST.isChecked() && i.toString().length() > 0) {
//                            int igstVal = Integer.parseInt(i.toString());
//                            int cgstVal = 0;
//                            if (cgst.getText().toString().length() > 0) {
//                                cgstVal = Integer.parseInt(cgst.getText().toString());
//                            }
//                            int sgstVal = 0;
//                            if (sgst.getText().toString().length() > 0) {
//                                sgstVal = Integer.parseInt(igst.getText().toString());
//                            }
//                            if (sgstVal + cgstVal + igstVal > 18) {
//                                igst.setError("Total GST must less than 18");
//                            } else {
//                                igst.setError(null);
//                            }
//                        }
//                    }
//                });
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkGST.isChecked() && cgst.getText().toString().length() <= 0 &&
                                igst.getText().toString().length() <= 0 &&
                                sgst.getText().toString().length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter valid GST", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Particularbean particularbean = new Particularbean(
                                particular.getText().toString().replace("\"", " inch"),
                                quantity.getText().toString(),
                                perQuantity.getText().toString(),
                                cgst.getText().toString(),
                                sgst.getText().toString(), igst.getText().toString());
                        particularbeans.add(particularbean);
                        mparticularItemAdapter.notifyData(particularbeans);
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

        invoiceText = findViewById(R.id.invoiceText);
        quotationTxt = findViewById(R.id.quotationTxt);
        invoiceText.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));

        invoiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingMode = "INVOICE";
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
                quotationTxt.setTextColor(Color.parseColor("#ffffff"));
                quotationTxt.setBackground(getResources().getDrawable(R.drawable.rectangle_filed));
                quotationTxt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
                invoiceText.setTextColor(Color.parseColor("#000000"));
                invoiceText.setBackground(getResources().getDrawable(R.drawable.rectangle_trans));
            }
        });


        particular_list = (findViewById(R.id.particular_list));
        mparticularItemAdapter = new ParticularItemAdapter(this, particularbeans, this, checkGST.isChecked());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        particular_list.setLayoutManager(addManager1);
        particular_list.setAdapter(mparticularItemAdapter);

        checkGST.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                validDateGst();
                mparticularItemAdapter.notifyData(checkGST.isChecked());
            }
        });
        sellername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sellername.toString().length() > 0) {
                    sellernameText.setError(null);
                }
            }
        });
        selleraddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (selleraddress.toString().length() > 0) {
                    selleraddressText.setError(null);
                }
            }
        });
        sellerphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sellerphone.toString().length() > 0) {
                    sellerphoneText.setError(null);
                }
            }
        });
        sellergstNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (sellergstNo.toString().length() > 0) {
                    sellergstNoText.setError(null);
                }
            }
        });

        buyername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (buyername.toString().length() > 0) {
                    buyernameText.setError(null);
                }
            }
        });
        buyeraddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (buyeraddress.toString().length() > 0) {
                    buyeraddressText.setError(null);
                }
            }
        });
        buyerphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (buyerphone.toString().length() > 0) {
                    buyerphoneText.setError(null);
                }
            }
        });
        buyergstNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (buyergstNo.toString().length() > 0) {
                    buyergstNoText.setError(null);
                }
            }
        });


        bankname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bankname.toString().length() > 0) {
                    banknameText.setError(null);
                }
            }
        });

        accountNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (accountNo.toString().length() > 0) {
                    accountNoText.setError(null);
                }
            }
        });

        ifcno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ifcno.toString().length() > 0) {
                    ifcnoText.setError(null);
                }
            }
        });
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

        try {
            sellerbillNo.setText(getBillNo());

        } catch (Exception e) {
            Log.e("xxxxxxxxxxxx", e.toString());
        }
        ExtendedFloatingActionButton print = (findViewById(R.id.print));
        print.setBackgroundColor(Color.parseColor(getConfigBean().getColorPrimary()));
        headerbar.setBackgroundColor(Color.parseColor(getConfigBean().getColorPrimary()));
        scroll.setBackgroundColor(Color.parseColor(getConfigBean().getColorPrimary()));
        selectseller.setStrokeColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        selectbuyer.setStrokeColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
/*        seller_name.setTextColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        tittle.setTextColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
     */
        selectseller.setTextColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        selectbuyer.setTextColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        addItems.setStrokeColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        addItems.setTextColor(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        addItems.setIconTint(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        checkGST.setButtonTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        checkGST.setTextColor(Color.parseColor(getConfigBean().getColorPrimary()));
        checkDigi.setButtonTintList(ColorStateList.valueOf(Color.parseColor(getConfigBean().getColorPrimary())));
        checkDigi.setTextColor(Color.parseColor(getConfigBean().getColorPrimary()));
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sellername.getText().toString().length() <= 0) {
                    sellernameText.setError("Enter the name");
                    sellernameText.requestFocus();
                } else if (selleraddress.getText().toString().length() <= 0) {
                    selleraddressText.setError("Enter the address");
                    selleraddressText.requestFocus();
                } else if (sellerphone.getText().toString().length() <= 0) {
                    sellerphoneText.setError("Enter the phone No");
                    sellerphoneText.requestFocus();
                } else if (sellergstNo.getText().toString().length() <= 0 && checkGST.isChecked()) {
                    sellergstNoText.setError("Enter the GSTNo");
                    sellergstNoText.requestFocus();
                } else if (buyername.getText().toString().length() <= 0) {
                    buyernameText.setError("Enter the name");
                    buyernameText.requestFocus();
                } else if (buyeraddress.getText().toString().length() <= 0) {
                    buyeraddressText.setError("Enter the address");
                    buyeraddressText.requestFocus();
                } else if (buyerphone.getText().toString().length() <= 0) {
                    buyerphoneText.setError("Enter the phone No");
                    buyerphoneText.requestFocus();
                } else if (buyergstNo.getText().toString().length() <= 0 && checkGST.isChecked()) {
                    buyergstNoText.setError("Enter the GSTNo");
                    buyergstNoText.requestFocus();
                }

//                else if (bankname.getText().toString().length() <= 0) {
//                    banknameText.setError("Enter the Bank Name");
//                    banknameText.requestFocus();
//                } else if (accountNo.getText().toString().length() <= 0) {
//                    accountNoText.setError("Enter the Account No");
//                    accountNoText.requestFocus();
//                } else if (ifcno.getText().toString().length() <= 0) {
//                    ifcnoText.setError("Enter the IFC No");
//                    ifcnoText.requestFocus();
//                }
/*
                else if (pakagecost.getText().toString().length() <= 0) {

                    pakagecostText.setError("Enter the Package Cost");
                    pakagecostText.requestFocus();
                } else if (previous.getText().toString().length() <= 0) {
                    previousText.setError("Enter the Previous Amount");
                    previousText.requestFocus();
                }*/
                else {
                    printDialog();
                }

            }

        });
        selectseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SellerListActivity.class);
                startActivityForResult(intent, 10);
            }
        });
        selectbuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BuyerListActivity.class);
                startActivityForResult(intent, 20);
            }
        });

        getAllInvoice();
    }

    private void bottomSheetCancel() {
        if(mBottomSheetDialog!=null) {
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

    private void printFunction() {
        pDialog.setMessage("Printing...");
        showDialog();


        Mainbean tempMainbean = new Mainbean(
                sellername.getText().toString(),
                selleraddress.getText().toString(),
                sellerphone.getText().toString(),
                sellergstNo.getText().toString(),
                getBillNo(),
                buyername.getText().toString(),
                buyeraddress.getText().toString(),
                buyerphone.getText().toString(),
                buyergstNo.getText().toString(),
                "0",
                "0",
                "0",
                bankname.getText().toString(),
                accountNo.getText().toString(),
                ifcno.getText().toString(),
                previous.getText().toString().length() <= 0 ? "0" : previous.getText().toString(),
                pakagecost.getText().toString().length() <= 0 ? "0" : pakagecost.getText().toString(),
                billingMode,
                holdername.getText().toString(),
                db.getUniqueSeller(buyername.getText().toString(), buyerphone.getText().toString()),
                String.valueOf(checkGST.isChecked()),
                particularbeans
        );
        tempMainbean.setId(Integer.parseInt(getBillNo()));

        String jsonVal = new Gson().toJson(tempMainbean);
        getCreateInvoice(jsonVal, tempMainbean);


    }

    private void justprintFunction() {
        pDialog.setMessage("Printing...");
        showDialog();
        Mainbean tempMainbean = new Mainbean(

                sellername.getText().toString(),
                selleraddress.getText().toString(),
                sellerphone.getText().toString(),
                sellergstNo.getText().toString(),
                getBillNo(),
                buyername.getText().toString(),
                buyeraddress.getText().toString(),
                buyerphone.getText().toString(),
                buyergstNo.getText().toString(),
                "0",
                "0",
                "0",
                bankname.getText().toString(),
                accountNo.getText().toString(),
                ifcno.getText().toString(),
                previous.getText().toString().length() <= 0 ? "0" : previous.getText().toString(),
                pakagecost.getText().toString().length() <= 0 ? "0" : pakagecost.getText().toString(),
                billingMode,
                holdername.getText().toString(),
                db.getUniqueSeller(buyername.getText().toString(), buyerphone.getText().toString()),
                String.valueOf(checkGST.isChecked()),
                particularbeans

        );

        tempMainbean.setId(Integer.parseInt(sellerbillNo.getText().toString()));
        printFunction(getApplicationContext(), tempMainbean, checkDigi.isChecked());


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
            PdfConfig.addContent(document, mainbean, includeGst, MainActivity.this, getConfigBean(), getPreference());

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
                printFunction();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 1) {
                Mainbean tempMainForSeller = (Mainbean) data.getSerializableExtra("data");
                sellername.setText(tempMainForSeller.sellername);
                selleraddress.setText(tempMainForSeller.selleraddress);
                sellerphone.setText(tempMainForSeller.sellerphone);
                sellergstNo.setText(tempMainForSeller.sellergstNo);
                holdername.setText(tempMainForSeller.holdername);
                bankname.setText(tempMainForSeller.bankname);
                accountNo.setText(tempMainForSeller.accountNo);
                ifcno.setText(tempMainForSeller.ifcno);
            }
        } else if (requestCode == 20) {
            if (resultCode == 2) {
                Mainbean tempMainForBuyer = (Mainbean) data.getSerializableExtra("data");
                buyername.setText(tempMainForBuyer.buyername);
                buyeraddress.setText(tempMainForBuyer.buyeraddress);
                buyerphone.setText(tempMainForBuyer.buyerphone);
                buyergstNo.setText(tempMainForBuyer.buyergstNo);
            }
        }


    }

    public void printDialog() {

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.RoundShapeTheme);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.print_dialog, null);


        dialogBuilder.setTitle("Alert")
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        } else {
                            dialog.cancel();
                            printFunction();
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        hideDialog();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                        } else {
                            justprintFunction();
                            dialogInterface.cancel();
                        }
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(true);


        WindowManager.LayoutParams lp = b.getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        b.show();
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
        mBottomSheetDialog = new RoundedBottomSheetDialog(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_particulars, null);
        AutoCompleteTextView particular = dialogView.findViewById(R.id.particular);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, db.getAllcategoryMainbeans());
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

//        sgst.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString().length() > 0) {
//                    int sgstVal = Integer.parseInt(s.toString());
//                    int cgstVal = 0;
//                    if (cgst.getText().toString().length() > 0) {
//                        cgstVal = Integer.parseInt(cgst.getText().toString());
//                    }
//                    int igstVal = 0;
//                    if (igst.getText().toString().length() > 0) {
//                        igstVal = Integer.parseInt(igst.getText().toString());
//                    }
//                    if (sgstVal + cgstVal + igstVal > 18) {
//                        sgstText.setError("Total GST must less than 18");
//                    } else {
//                        sgstText.setError(null);
//                    }
//                }
//            }
//        });
//
//        cgst.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable c) {
//                if (c.toString().length() > 0) {
//                    int cgstVal = Integer.parseInt(c.toString());
//                    int sgstVal = 0;
//                    if (sgst.getText().toString().length() > 0) {
//                        sgstVal = Integer.parseInt(cgst.getText().toString());
//                    }
//                    int igstVal = 0;
//                    if (igst.getText().toString().length() > 0) {
//                        igstVal = Integer.parseInt(igst.getText().toString());
//                    }
//                    if (cgstVal + sgstVal + igstVal > 18) {
//                        cgstText.setError("Total GST must less than 18");
//                    } else {
//                        cgstText.setError(null);
//                    }
//                }
//            }
//        });
//
//        igst.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable i) {
//                if (i.toString().length() > 0) {
//                    int igstVal = Integer.parseInt(i.toString());
//                    int cgstVal = 0;
//                    if (cgst.getText().toString().length() > 0) {
//                        cgstVal = Integer.parseInt(cgst.getText().toString());
//                    }
//                    int sgstVal = 0;
//                    if (sgst.getText().toString().length() > 0) {
//                        sgstVal = Integer.parseInt(igst.getText().toString());
//                    }
//                    if (sgstVal + cgstVal + igstVal > 18) {
//                        igst.setError("Total GST must less than 18");
//                    } else {
//                        igst.setError(null);
//                    }
//                }
//            }
//        });

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
                if (checkGST.isChecked() && cgst.getText().toString().length() <= 0 &&
                        igst.getText().toString().length() <= 0 &&
                        sgst.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter valid GST", Toast.LENGTH_SHORT).show();
                    return;
                }
                particularbeans.get(position).setParticular(particular.getText().toString().replace("\"", " inch"));
                particularbeans.get(position).setQuantity(quantity.getText().toString());
                particularbeans.get(position).setPerquantity(perQuantity.getText().toString());
                particularbeans.get(position).setCgst(cgst.getText().toString());
                particularbeans.get(position).setSgst(sgst.getText().toString());
                particularbeans.get(position).setIgst(igst.getText().toString());
                mparticularItemAdapter.notifyData(particularbeans);
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

    private void validDateGst() {

        if (checkGST.isChecked()) {
            ((TextView) findViewById(R.id.cgstLabel)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.sgstLabel)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.igstLabel)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.cgstLabel)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.sgstLabel)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.igstLabel)).setVisibility(View.GONE);
        }
    }

    private String getBillNo() {
        List<Mainbean> mainbeans = db.getLastBillNo();
        String billNo = "00001";
        if (mainbeans.size() > 0) {
            billNo = AppConfig.intToString(Integer.parseInt(mainbeans.get(0).sellerbillNo) + 1, 5);
        }
        return billNo;
    }

    private void getCreateInvoice(final String data, Mainbean tempMainbean) {
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
                        db.insertMainbean(tempMainbean);
                        printFunction(getApplicationContext(), tempMainbean, checkDigi.isChecked());

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
                localHashMap.put("surveyer", sharedpreferences.getString(user_id, ""));
                localHashMap.put("auth_key", sharedpreferences.getString(auth_key, ""));
                localHashMap.put("data", data);


                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, TAG);
    }


    private void getAllInvoice() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Fetching ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                GET_ALL_INVOICE, new Response.Listener<String>() {
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
                                Mainbean mainbean = new Gson().fromJson(inputVal, Mainbean.class);
                                mainbean.setTimestamp(createdon);
                                db.insertMainbean(mainbean);
                            } catch (Exception e) {
                                Log.e("xxxxxxxxxxx", e.toString());
                            }
                        }
                        sellerbillNo.setText(getBillNo());
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
                localHashMap.put("surveyer", sharedpreferences.getString(user_id, ""));
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