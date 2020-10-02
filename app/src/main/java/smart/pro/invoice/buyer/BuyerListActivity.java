package smart.pro.invoice.buyer;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import smart.pro.invoice.Mainbean;
import smart.pro.invoice.OnItemClick;
import smart.pro.invoice.R;
import smart.pro.invoice.app.BaseActivity;
import smart.pro.invoice.app.DatabaseHelper;

import java.util.ArrayList;

public class BuyerListActivity extends BaseActivity implements OnItemClick {

    RecyclerView invoice_recyclerview;
    private ArrayList<Mainbean> mainbeans = new ArrayList<>();
    BuyerItemAdapter mbuyerItemAdapter;
    DatabaseHelper db;

    @Override
    protected void startDemo() {
        setContentView(R.layout.seller_buyer_main);
        getSupportActionBar().setTitle("ALL BUYERS");
        db = new DatabaseHelper(this);
        mainbeans.addAll(db.getAllBuyerMainbeans());
        invoice_recyclerview = (findViewById(R.id.invoice_recyclerview));
        mbuyerItemAdapter = new BuyerItemAdapter(this, mainbeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        invoice_recyclerview.setLayoutManager(addManager1);
        invoice_recyclerview.setAdapter(mbuyerItemAdapter);
    }


    @Override
    public void itemClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("data", mainbeans.get(position));
        setResult(2, intent);
        finish();
    }

    @Override
    public void itemDeleteClick(int position) {

    }

    @Override
    public void itemEditClick(int position) {

    }

}
