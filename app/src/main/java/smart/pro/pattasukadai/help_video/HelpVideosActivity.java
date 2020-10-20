package smart.pro.pattasukadai.help_video;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.internal.exoplayer.VideoPreviewActivity;
import com.internal.exoplayer.Videobean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import smart.pro.pattasukadai.R;
import smart.pro.pattasukadai.app.AppConfig;
import smart.pro.pattasukadai.app.AppController;
import smart.pro.pattasukadai.app.BaseActivity;

import static smart.pro.pattasukadai.app.AppConfig.VIDEO_PLAY;
import static smart.pro.pattasukadai.app.AppConfig.auth_key;
import static smart.pro.pattasukadai.app.AppConfig.mypreference;


public class HelpVideosActivity extends BaseActivity implements OnVideoClick {

    RecyclerView video_list;
    private ArrayList<Videobean> videobeans = new ArrayList<>();
    HelpVideoListAdapter videoListAdapter;
    SharedPreferences sharedpreferences;
    private ProgressDialog pDialog;
    @Override
    protected void startDemo() {
        setContentView(R.layout.activity_videoplay);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        getSupportActionBar().setTitle("Help Videos");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        video_list = findViewById(R.id.video_list);
        videoListAdapter = new HelpVideoListAdapter(getApplication(), videobeans, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(getApplication(), LinearLayoutManager.VERTICAL, false);
        video_list.setLayoutManager(addManager1);
        video_list.setAdapter(videoListAdapter);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        get_video();
    }

    private void get_video() {
        pDialog.setMessage("Fetching ...");
        showDialog();
        StringRequest local16 = new StringRequest(Request.Method.POST, VIDEO_PLAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        Log.e("video", responseString);
                        hideDialog();
                        try {
                            JSONObject response=new JSONObject(responseString);
                            videobeans=new ArrayList<>();
                            JSONArray dataArray=response.getJSONArray("data");
                            int success = response.getInt("success");
                            if (success == 1) {
                                for(int i=0;i<dataArray.length();i++){
                                    JSONObject dataObject=dataArray.getJSONObject(i);
                                    Videobean videobean=new Videobean();
                                    videobean.setId(dataObject.getString("id"));
                                    videobean.setTitle(dataObject.getString("title"));
                                    videobean.setThumbnail(dataObject.getString("thumbnail"));
                                    videobean.setVideo(AppConfig.VIDEOPATH+dataObject.getString("videolink"));
                                    videobean.setDuration(dataObject.getString("duration"));
                                    videobeans.add(videobean);
                                }
                                videoListAdapter.notifyData(videobeans);
                            }

                        } catch (JSONException localJSONException) {
                            localJSONException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError paramVolleyError) {
                Log.e("tag", "Fetch Error: " + paramVolleyError.getMessage());
                Toast.makeText(getApplication(), paramVolleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put("auth_key", sharedpreferences.getString(auth_key, ""));
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(local16, "TAG");
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
    @Override
    public void itemClick(int position) {
        Intent videoIntent = new Intent(HelpVideosActivity.this, VideoPreviewActivity.class);
        videoIntent.putExtra("data", videobeans);
        videoIntent.putExtra("position", position);
        startActivity(videoIntent);

    }
}
