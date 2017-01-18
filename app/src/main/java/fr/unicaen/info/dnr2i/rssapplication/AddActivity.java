package fr.unicaen.info.dnr2i.rssapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addRssFeed(View view) {

        // Display of a toast to confirm the add
        Context context = getApplicationContext();
        String confirmText = "RSS feed added.";
        int duration = Toast.LENGTH_SHORT;

        Toast confirmToast = Toast.makeText(context, confirmText, duration);
        confirmToast.show();
    }
}
