package smart.pro.pattasukadai.app;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bitly.Bitly;
import com.bitly.Error;
import com.bitly.Response;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    private Locale firstLaunchLocale;
    private HashSet<Locale> supportedLocales;


    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mInstance = this;
        Bitly.initialize(this, "274ca5095071991793b0a7bb7f94840b6847fb44");

        init();
    }

    private void init() {
        AutomatedSupportedLocales();
        manualSupportedLocales();
        Bitly.shorten("http://thestockbazaar.com", new Bitly.Callback() {
            @Override
            public void onResponse(Response response) {
                // response provides a Response object which contains the shortened Bitlink
                // response includes a status code
                // Your custom logic goes here...
            }

            @Override
            public void onError(Error error) {

            }
        });

    }


    private void AutomatedSupportedLocales() {

    }

    private void manualSupportedLocales() {
        // This is the locale that you wanna your app to launch with.
        firstLaunchLocale = new Locale("en");

        // You can use a HashSet<String> instead and call 'setSupportedStringLocales()' :)
        supportedLocales = new HashSet<>();
        supportedLocales.add(new Locale("en"));
        supportedLocales.add(new Locale("ta"));
        supportedLocales.add(new Locale("ml"));
        supportedLocales.add(new Locale("kn"));
        supportedLocales.add(new Locale("te"));
        supportedLocales.add(new Locale("mr"));
        supportedLocales.add(new Locale("or"));
        supportedLocales.add(new Locale("bn"));
        supportedLocales.add(new Locale("as"));
        supportedLocales.add(new Locale("my"));
        supportedLocales.add(new Locale("th"));
        supportedLocales.add(new Locale("ms"));
        supportedLocales.add(new Locale("km"));
        supportedLocales.add(new Locale("vi"));
        supportedLocales.add(new Locale("zh"));
        supportedLocales.add(new Locale("pt"));
        supportedLocales.add(new Locale("fr"));
        supportedLocales.add(new Locale("es"));
        supportedLocales.add(new Locale("sw"));
    }

    Map<String, String> lang=new HashMap<>();

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}