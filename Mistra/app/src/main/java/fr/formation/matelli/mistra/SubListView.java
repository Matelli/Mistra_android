package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import data.Tutoriel;


public class SubListView extends Activity {

    ListView listViewSubList;
    ListAdapter listAdapter;
    TextView textViewTitre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list_view);

        textViewTitre = (TextView) findViewById(R.id.tvTitreSubList);
        listViewSubList = (ListView) findViewById(R.id.listViewSubList);
        Intent i = this.getIntent();
        textViewTitre.setText(i.getExtras().getString("titre"));
        Tutoriel t = (Tutoriel) i.getExtras().getParcelable("objet");
        // ToDo completer la récuperation de la liste de presentation a afficher avec le onClick associé



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sub_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
