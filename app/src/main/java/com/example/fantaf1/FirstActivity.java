package com.example.fantaf1;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.example.fantaf1.buisness_logic.BgTask;
import com.example.fantaf1.buisness_logic.Gestore;

import java.util.Calendar;
import java.util.Map;

import io.reactivex.Single;

public class FirstActivity extends AppCompatActivity {

    RxDataStore<Preferences> dataStore;

    Preferences pref_error = new Preferences() {
        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }

        @Nullable
        @Override
        public <T> T get(@NonNull Key<T> key) {
            return null;
        }

        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        dataStore = new RxPreferenceDataStoreBuilder(this, "database").build();
        Calendar calendar = Calendar.getInstance();
        int create = 1;
        int isCreated = getIntegerValue("created");
        int currDay = calendar.get(Calendar.DAY_OF_MONTH);
        int lastDay = getIntegerValue("day");
        if(/*create != isCreated*/true){
            //putIntegerValue("created",create);
            Gestore g = new Gestore(this);
            new BgTask(g, "fetchData");
        } else {
            if (isDeviceOnline()  && currDay != lastDay) {
                putIntegerValue("day",currDay);
                Gestore g = new Gestore(this);
                new BgTask(g, "readFile");
            } else {
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
    }

    private Boolean isDeviceOnline() {
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connManager.getNetworkCapabilities(connManager.getActiveNetwork());
        if (networkCapabilities == null) {
            return false;
        } else {
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED);
        }
    }

    public boolean putIntegerValue(String KEY,Integer value){
        boolean returnvalue;
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(KEY);
        Single<Preferences> updateResult =  dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        }).onErrorReturnItem(pref_error);
        returnvalue = updateResult.blockingGet() != pref_error;
        return returnvalue;
    }

    int getIntegerValue(String KEY) {
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(KEY);
        Single<Integer> value = dataStore.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem(0);
        return value.blockingGet();
    }
}