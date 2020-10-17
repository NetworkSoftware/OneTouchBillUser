package smart.pro.invoice.buyer;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
