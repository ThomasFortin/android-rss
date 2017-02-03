package fr.unicaen.info.dnr2i.rssapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import fr.unicaen.info.dnr2i.rssapplication.entity.NetworkPreferences;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Toggle the appropriate button (from preferences) when loading the form:
        SharedPreferences sharedPref = getSharedPreferences("network", 0);
        int choice = sharedPref.getInt("networkPreferences", NetworkPreferences.AUTOMATICALLY.ordinal());

        RadioButton selectedButton;
        if (choice == NetworkPreferences.NEVER.ordinal()) {
            selectedButton = (RadioButton) findViewById(R.id.switchNeverUpdate);
        } else if (choice == NetworkPreferences.WIFI_ONLY.ordinal()) {
            selectedButton = (RadioButton) findViewById(R.id.switchWifiUpdate);
        } else {
            selectedButton = (RadioButton) findViewById(R.id.switchAutoUpdate);
        }
        selectedButton.toggle();
    }

    public void changeSettings(View view) {

        RadioGroup networkButton = (RadioGroup) findViewById(R.id.radioNetwork);

        int idChecked = networkButton.getCheckedRadioButtonId();
        int idWifi = findViewById(R.id.switchWifiUpdate).getId();
        int idNever = findViewById(R.id.switchNeverUpdate).getId();

        int pref;
        if (idChecked == idWifi) {
            pref = NetworkPreferences.WIFI_ONLY.ordinal();
        } else if (idChecked == idNever) {
            pref = NetworkPreferences.NEVER.ordinal();
        } else {
            pref = NetworkPreferences.AUTOMATICALLY.ordinal();
        }

        SharedPreferences sharedPref = getSharedPreferences("network", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("networkPreferences", pref);
        editor.commit();

    }
}
