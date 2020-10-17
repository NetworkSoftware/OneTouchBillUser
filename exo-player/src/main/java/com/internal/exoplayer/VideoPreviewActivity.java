package com.internal.exoplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class VideoPreviewActivity extends AppCompatActivity implements VideoPlayerListener {

    private AndExoPlayerView andExoPlayerView;
    String fileN = null;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    boolean result;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video_previw);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        andExoPlayerView.setParamsActivity((ArrayList<Videobean>) getIntent().getSerializableExtra("data"),
                getIntent().getIntExtra("position", 0), this);
        pDialog = new ProgressDialog(this);
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
    public void onBackClick() {
        finish();
    }

    @Override
    public void onShareClick(String url, String status, String name) {

        result = checkPermission();
        if (result) {
            checkFolder();
            newDownload(url + "@" + name);
        }

    }

    //hare you can start downloding video
    public void newDownload(String url) {
        final DownloadTask downloadTask = new DownloadTask(getApplicationContext());
        downloadTask.execute(url);
    }

    // DownloadTask for downloding video from URL
    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0].split("@")[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();
                fileN = sUrl[0].split("@")[1] + ".mp4";
                File filename = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/LisaAi/", fileN);
                output = new FileOutputStream(filename);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            pDialog.setMessage("Downloading...");
            showDialog();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            pDialog.setMessage("Downloading..." + progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            hideDialog();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/LisaAi/" + fileN}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String newpath, Uri newuri) {
                                Log.i("ExternalStorage", "Scanned " + newpath + ":");
                                Log.i("ExternalStorage", "-> uri=" + newuri);
                                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                                intentShareFile.setType(URLConnection.guessContentTypeFromName(fileN));
                                intentShareFile.putExtra(Intent.EXTRA_STREAM,
                                        Uri.parse("file://" + newpath));
                                startActivity(Intent.createChooser(intentShareFile, "Share File"));
                            }
                        });

        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(VideoPreviewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(VideoPreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(VideoPreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(VideoPreviewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(VideoPreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(VideoPreviewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    //Here you can check App Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkFolder();
                } else {
                    //code for deny
                    checkAgain();
                }
                break;
        }
    }

    //hare you can check folfer whare you want to store download Video
    public void checkFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LisaAi/";
        File dir = new File(path);
        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();
        }
        if (isDirectoryCreated) {
            // do something\
            Log.d("Folder", "Already Created");
        }
    }

}
