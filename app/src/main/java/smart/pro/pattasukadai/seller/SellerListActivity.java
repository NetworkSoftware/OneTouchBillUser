package smart.pro.pattasukadai.seller;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import smart.pro.pattasukadai.Mainbean;
import smart.pro.pattasukadai.OnItemClick;
import smart.pro.pattasukadai.R;
import smart.pro.pattasukadai.app.BaseActivity;
import smart.pro.pattasukadai.app.DatabaseHelper;

import java.util.ArrayList;

public class SellerListActivity extends BaseActivity implements OnItemClick {

    RecyclerView invoice_recyclerview;
    private ArrayList<Mainbean> mainbeans = new ArrayList<>();
    SellerItemAdapter msellerItemAdapter;
    DatabaseHelper db;

    @Override
    protected void startDemo() {
        setContentView(R.layout.seller_buyer_main);
        getSupportActionBar().setTitle("ALL SELLERS");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseHelper(this);
        mainbeans.addAll(db.getAllSellerMainbeans());
        invoice_recyclerview = (findViewById(R.id.invoice_recyclerview));
        msellerItemAdapter = new SellerItemAdapter(this, mainbeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        invoice_recyclerview.setLayoutManager(addManager1);
        invoice_recyclerview.setAdapter(msellerItemAdapter);


    }


    @Override
    public void itemClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("data", mainbeans.get(position));
        setResult(1, intent);
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
