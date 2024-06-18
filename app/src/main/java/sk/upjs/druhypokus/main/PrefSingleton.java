package sk.upjs.druhypokus.main;

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

    public void writePreference(String key, String value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.commit();
    }

    public void writePreference(String key, boolean value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public String getPreferenceString (String key) {
        return mMyPreferences.getString(key, null);
    }

    public boolean getPreferenceBoolean (String key) {
        return mMyPreferences.getBoolean(key, true);
    }
}