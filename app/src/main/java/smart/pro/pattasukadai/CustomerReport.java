package smart.pro.pattasukadai;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smart.pro.pattasukadai.app.BaseActivity;
import smart.pro.pattasukadai.app.DatabaseHelper;
import smart.pro.pattasukadai.invoice.Particularbean;

public class CustomerReport extends BaseActivity {
    DatabaseHelper databaseHelper;

    @Override
    protected void startDemo() {
        setContentView(R.layout.customer_report);
        databaseHelper = new DatabaseHelper(this);
        getSupportActionBar().setTitle(getConfigBean().getBrandName());
        getSupportActionBar().setSubtitle("Reports");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
            }
        });

        List<Mainbean> mainbeans = databaseHelper.getAllMainbeans();
        Map<String, Integer> productCostMap = new HashMap<>();
        for (int i = 0; i < mainbeans.size(); i++) {
            ArrayList<Particularbean> particularbeans = mainbeans.get(i).particularbeans;
            for (int k = 0; k < particularbeans.size(); k++) {
                try {
                    int value = 0;
                    Particularbean bean = particularbeans.get(k);
                    if (productCostMap.containsKey(particularbeans.get(k).particular)) {
                        value = productCostMap.get(bean.particular);
                    }
                    float quan = Float.parseFloat(bean.quantity);
                    float perQuanPri = Float.parseFloat(bean.perquantity);
                    float total = quan * perQuanPri;
                    productCostMap.put(particularbeans.get(k).particular, (int) (value + total));
                } catch (Exception e) {
                    Log.e("xxxxxxxxxxxxx", e.toString());
                }
            }
        }

        List<DataEntry> data = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : productCostMap.entrySet()) {
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }
        pie.data(data);

        pie.title("Particulars billed in 2020 (in ₹)");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Particulars")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
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
