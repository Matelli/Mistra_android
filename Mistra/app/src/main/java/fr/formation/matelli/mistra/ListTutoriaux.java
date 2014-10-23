package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListTutoriaux extends Activity {

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
        setContentView(R.layout.activity_list_tutoriaux);

        // ImageButton
        btnRetourHome = (ImageButton) findViewById(R.id.btnRetourHome);
        // Button
        btnFormations = (Button) findViewById(R.id.btnFormationFooterTutos);
        btnTutos = (Button) findViewById(R.id.btnTutosFooterDevis);
        btnDevis = (Button) findViewById(R.id.btnDevisFooterTutos);
        btnContact = (Button) findViewById(R.id.btnContactFooterDevis);
        // ExpandableList
        expListView = (ExpandableListView) findViewById(R.id.expandablelisteTutos);

        // Listeners
        btnRetourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTutoriaux.this, Home.class);
                startActivity(i);
            }
        });

        btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTutoriaux.this, Devis.class);
                startActivity(i);
            }
        });

        btnFormations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTutoriaux.this, ListFormations.class);
                startActivity(i);
            }
        });


        // affichage de l'expandable list
        // preparing list data
        prepareListData();

        listAdapter = new CustomExpandableList(ListTutoriaux.this, listDataHeader, listDataChild);

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
        List<String> tjava = new ArrayList<String>();
        tjava.add("Tutoriel Java 01");
        tjava.add("Tutoriel Java 02");
        tjava.add("Tutoriel Java 03");
        tjava.add("Tutoriel Java 04");
        tjava.add("Tutoriel Java 05");
        tjava.add("Tutoriel Java 06");
        tjava.add("Tutoriel Java 07");

        List<String> tmicrosoft = new ArrayList<String>();
        tmicrosoft.add("Tutoriel Microsoft .net 01 ");
        tmicrosoft.add("Tutoriel Microsoft .net 02");
        tmicrosoft.add("Tutoriel Microsoft .net 03");
        tmicrosoft.add("Tutoriel Microsoft .net 04");
        tmicrosoft.add("Tutoriel Microsoft .net 05");
        tmicrosoft.add("Tutoriel Microsoft .net 06");

        List<String> tandroid = new ArrayList<String>();
        tandroid.add("Tutoriel Android 01");
        tandroid.add("Tutoriel Android 02");
        tandroid.add("Tutoriel Android 03");
        tandroid.add("Tutoriel Android 04");
        tandroid.add("Tutoriel Android 05");

        listDataChild.put(listDataHeader.get(0), tjava); // Header, Child data
        listDataChild.put(listDataHeader.get(1), tmicrosoft);
        listDataChild.put(listDataHeader.get(2), tandroid);









        





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_tutoriaux, menu);
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
