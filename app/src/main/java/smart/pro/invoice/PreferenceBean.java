package smart.pro.invoice;

public class PreferenceBean {
    private static PreferenceBean _instance;

    boolean isSerial;

    public boolean isSerial() {
        return isSerial;
    }

    public void setSerial(boolean serial) {
        isSerial = serial;
    }

    public static void set_instance(PreferenceBean _instanceV) {
        _instance = _instanceV;
    }

    public static PreferenceBean getInstance() {
        if (_instance == null) {
            _instance = new PreferenceBean();
        }
        return _instance;
    }
}
