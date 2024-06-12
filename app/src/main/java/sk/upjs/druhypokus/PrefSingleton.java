package sk.upjs.druhypokus;
// zdroj https://stackoverflow.com/questions/11552579/is-there-a-way-to-make-sharedpreferences-global-throughout-my-whole-android-app
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefSingleton{
    private static PrefSingleton mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private PrefSingleton(){ }

    public static PrefSingleton getInstance(){
        if (mInstance == null) mInstance = new PrefSingleton();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        //
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
