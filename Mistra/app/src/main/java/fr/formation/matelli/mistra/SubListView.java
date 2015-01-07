package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.Presentation;
import data.Selection;
import data.Tutoriel;


public class SubListView extends Activity {

    ListView listViewSubList;
    ListAdapter listAdapter;
    TextView textViewTitre;
    Tutoriel t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list_view);

        textViewTitre = (TextView) findViewById(R.id.tvTitreSubList);
        listViewSubList = (ListView) findViewById(R.id.listViewSubList);
        Intent i = this.getIntent();
        t = (Tutoriel) i.getExtras().getParcelable("objet");
        textViewTitre.setText(i.getExtras().getString("titre")+": "+t.getTitle());
        List<String> listItemToShow = new ArrayList<>();
        for( Selection s : t.getContent()){
            listItemToShow.add(s.getTitle());
             }
        listAdapter = new ArrayAdapter<String>(SubListView.this,R.layout.simple_item_list, R.id.itemSubList,listItemToShow);
        listViewSubList.setAdapter(listAdapter);


        listViewSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                for(Selection s :t.getContent()){
                    if(item.equals(s.getTitle())){
                        Presentation presentation = (Presentation) s;
                        Intent i = new Intent(SubListView.this, DetailSelection.class);
                        i.putExtra("htmlcode", presentation.getContent());
                        startActivity(i);
                    }
                }

            }
        });

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
