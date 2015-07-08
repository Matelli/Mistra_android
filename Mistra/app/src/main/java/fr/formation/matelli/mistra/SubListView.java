package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.Article;
import data.Formation;
import data.Presentation;
import data.Selection;
import data.Tutoriel;


public class SubListView extends Activity {

    ImageButton btnRetourHome;
    ListView listViewSubList;
    ListAdapter listAdapter;
    TextView textViewTitre;
    Tutoriel t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list_view);

        btnRetourHome = (ImageButton) findViewById(R.id.btnRetourHome);

        textViewTitre = (TextView) findViewById(R.id.tvTitreSubList);
        listViewSubList = (ListView) findViewById(R.id.listViewSubList);
        Intent i = this.getIntent();
        final String titre = (String) i.getSerializableExtra("titre");
        if(titre!=null)
            textViewTitre.setText(titre);

        t = (Tutoriel) i.getSerializableExtra("objet");
        //textViewTitre.setText(i.getExtras().getString("titre")+": "+t.getTitle());
        List<String> listItemToShow = new ArrayList<String>();
        if(t!=null) {
            for (Object s : t.getContent()) {
                listItemToShow.add(((Selection) s).getTitle());
            }
            listAdapter = new ArrayAdapter<String>(SubListView.this, R.layout.simple_item_list, R.id.itemSubList, listItemToShow);
            listViewSubList.setAdapter(listAdapter);
        }

        this.btnRetourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubListView.this, Home.class);

                if (getIntent() != null && getIntent().getExtras() != null) {
                    String c = getIntent().getExtras().getString("whoIwas");
                    if (c != null) {
                        if (c.equals(Formation.class.toString())) {
                            i = new Intent(SubListView.this, ListFormations.class);
                        } else if (c.equals(Tutoriel.class.toString())) {
                            i = new Intent(SubListView.this, ListTutoriels.class);
                        }
                    }
                }

                //i.putExtra("whoIam", SubListView.class.toString());
                startActivity(i);
                finish();
            }
        });

        listViewSubList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                for(Object s :t.getContent()){
                    if(item.equals(((Selection)s).getTitle())){
                        Article presentation = (Article) s;

                        Intent i = new Intent(SubListView.this, DetailSelection.class);
                        //i.putExtra("ListeFormation","detailTitre"+ article.getTitle());
                        //i.putExtra("ListeFormation","htmlcode"+ article.getTitle());
                        i.putExtra("detailTitre", presentation.getTitle());
                        i.putExtra("htmlcode", presentation.getDescription());
                        i.putExtra("whoIwas", SubListView.class.toString());
                        startActivity(i);
                        finish();
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
