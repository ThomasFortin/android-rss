package fr.unicaen.info.dnr2i.rssapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView mListView;
    String[] titles = new String[]{
            "Chèvre", "Bouquetin", "Cygne", "Tigre",
            "Écureuil", "Ratel", "Chien", "Paresseux",
            "Pie", "Chat", "Lion", "Dindon"
    };

    String[] descriptions = new String[]{
            "Une belle chèvre", "Un superbe bouquetin", "Un très beau cygne", "Un tigre originaire du Bengale",
            "Agile et adore les noisettes", "Blaireau intrépide et inconscient", "Le meilleur ami de l'Homme", "Heeeeuu..",
            "Y a une pie, dans le poirier..", "Trois petits chats", "Roi de la jungle", "Glou glou glou glou"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listViewFeeds);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, titles);
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
