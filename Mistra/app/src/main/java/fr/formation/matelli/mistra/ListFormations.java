package fr.formation.matelli.mistra;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListFormations extends Activity {
    ImageButton btnRetourHome;
    Button btnFormations;
    Button btnTutos;
    Button btnDevis;
    Button btnContact;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_formations);
        // ImageButton
        btnRetourHome = (ImageButton) findViewById(R.id.btnRetourHome);
        // Button
        btnFormations = (Button) findViewById(R.id.btnFormationFooter);
        btnTutos = (Button) findViewById(R.id.btnTutosFooter);
        btnDevis = (Button) findViewById(R.id.btnDevisFooter);
        btnContact = (Button) findViewById(R.id.btnContactFooter);
        // ExpandableList
        expListView = (ExpandableListView) findViewById(R.id.expandablelisteFormations);


        // Listeners
        btnRetourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListFormations.this, Home.class);
                startActivity(i);
            }
        });

        btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListFormations.this, Devis.class);
                startActivity(i);
            }
        });

        btnTutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListFormations.this, ListTutoriaux.class);
                startActivity(i);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListFormations.this, Contact.class);
                startActivity(i);
            }
        });


        // affichage de l'expandable list
        // preparing list data
        prepareListData();

        listAdapter = new CustomExpandableList(ListFormations.this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Java");
        listDataHeader.add("MICROSOFT .NET");
        listDataHeader.add("ANDROID");

        // Adding child data
        List<String> fjava = new ArrayList<String>();
        fjava.add("Formation Java 01");
        fjava.add("Formation Java 02");
        fjava.add("Formation Java 03");
        fjava.add("Formation Java 04");
        fjava.add("Formation Java 05");
        fjava.add("Formation Java 06");
        fjava.add("Formation Java 07");

        List<String> fmicrosoft = new ArrayList<String>();
        fmicrosoft.add("Formation Microsoft .net 01 ");
        fmicrosoft.add("Formation Microsoft .net 02");
        fmicrosoft.add("Formation Microsoft .net 03");
        fmicrosoft.add("Formation Microsoft .net 04");
        fmicrosoft.add("Formation Microsoft .net 05");
        fmicrosoft.add("Formation Microsoft .net 06");

        List<String> fandroid = new ArrayList<String>();
        fandroid.add("Formation Android 01");
        fandroid.add("Formation Android 02");
        fandroid.add("Formation Android 03");
        fandroid.add("Formation Android 04");
        fandroid.add("Formation Android 05");

        listDataChild.put(listDataHeader.get(0), fjava); // Header, Child data
        listDataChild.put(listDataHeader.get(1), fmicrosoft);
        listDataChild.put(listDataHeader.get(2), fandroid);


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Toast.makeText(ListFormations.this,"clicked",Toast.LENGTH_LONG).show();
                Intent i = new Intent(ListFormations.this, DetailSelection.class);
                startActivity(i);

                return false;
            }
        });






    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_formations, menu);
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
