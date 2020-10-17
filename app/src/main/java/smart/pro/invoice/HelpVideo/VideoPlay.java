package smart.pro.invoice.HelpVideo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.potyvideo.library.AndExoPlayerView;

import smart.pro.invoice.Mainbean;
import smart.pro.invoice.R;
import smart.pro.invoice.app.AppConfig;
import smart.pro.invoice.app.BaseActivity;


public class VideoPlay extends BaseActivity {

    private AndExoPlayerView andExoPlayerView;

    private String VideoURL = AppConfig.VIDEOPATH + "";
    Videobean mvideobean=null;


    @Override
    protected void startDemo() {
        setContentView(R.layout.video_play);

        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        mvideobean = (Videobean) getIntent().getSerializableExtra("data");

        getSupportActionBar().setTitle(mvideobean.title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadMP4ServerSide();
    }


    private void loadMP4ServerSide() {
        andExoPlayerView.setSource(AppConfig.VIDEOPATH+mvideobean.video);
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
