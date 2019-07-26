package com.chen.beth.settingsfragment;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.Const;
import com.tencent.mmkv.MMKV;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private SwitchPreference notifications;

    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings,rootKey);
        findPreference("clear").setOnPreferenceClickListener(this);
        notifications = (SwitchPreference) findPreference("notifications");
        notifications.setChecked(MMKV.defaultMMKV().decodeBool(Const.IS_SHOW_NOTIFY,true));
        notifications.setOnPreferenceClickListener(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case "clear":
                showClearDialog();
                break;
            case "notifications":
                MMKV.defaultMMKV().encode(Const.IS_SHOW_NOTIFY,notifications.isChecked());
                break;
        }
        return true;
    }
    private Disposable disposable;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    private void showClearDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.setting_clear)
                .setMessage(R.string.setting_clear_msg)
                .setPositiveButton(R.string.delete_all_ok,
                        (d,w)->{deleteAll();})
                .setNegativeButton(R.string.delete_all_cancel,null)
                .create()
                .show();
    }

    private void deleteAll() {
        disposable = Observable.just(0)
                .subscribeOn(Schedulers.io())
                .map(i->{
                    BethApplication.getDBData().getBlockDao().deleteAll();
                    BethApplication.getDBData().getTransactionDao().deleteAll();
                    BethApplication.getDBData().getTransactionSummaryDAO().deleteAll();
                    BethApplication.getDBData().getSearchHistoryDao().deleteAll(); return i;})
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i-> Toast.makeText(getContext(),R.string.clear_finish,Toast.LENGTH_SHORT).show());
    }
}
