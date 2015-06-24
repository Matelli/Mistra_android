package fr.formation.matelli.mistra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import dao.DBHandlerFormation;
import dao.DBHandlerTutoriel;
import data.Article;
import data.Formation;
import data.Selection;
import data.Tutoriel;
import data.Presentation;


public class ListTutoriels extends Activity {

    Context context;

    ImageButton btnRetourHome;
    Button btnFormations;
    Button btnTutos;
    Button btnDevis;
    Button btnContact;

    DBHandlerTutoriel db;

    List<Tutoriel> listOfTutoriels = new ArrayList<Tutoriel>();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_list_tutoriels);

        // ImageButton
        btnRetourHome = (ImageButton) findViewById(R.id.btnRetourHome);
        // Button
        btnFormations = (Button) findViewById(R.id.btnFormationFooterTutos);
        btnTutos = (Button) findViewById(R.id.btnTutosFooterTutos);
        btnDevis = (Button) findViewById(R.id.btnDevisFooterTutos);
        btnContact = (Button) findViewById(R.id.btnContactFooterTutos);
        // ExpandableList
        expListView = (ExpandableListView) findViewById(R.id.expandablelisteTutos);

        // Listeners
        btnRetourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListTutoriels.this, Home.class);
                startActivity(i);
            }
        });

        btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListTutoriels.this, Devis.class);
                startActivity(i);
            }
        });

        btnFormations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListTutoriels.this, ListFormations.class);
                startActivity(i);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListTutoriels.this, Contact.class);
                startActivity(i);
            }
        });

        remplirListData();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                long p = parent.getSelectedPosition();
                Selection selection = null;
                String item = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                String itemParentName = (String) parent.getExpandableListAdapter().getGroup(groupPosition);

                Log.i("==> item clicked :", item);
                Log.i("==> item parent clicked :", itemParentName + " at position :" + groupPosition);
                //Log.i("==> listOfFormation keys :", listOfTutoriels.toString());
                for (Tutoriel c : listOfTutoriels) {
                    if (c.getTitle().equals(itemParentName)) {
                        Log.i("==> item parent clicked detail :", "" + c.getId());

                        selection = (Selection) c.getContent().get(childPosition);
                        break;//on l'a trouvé, on sort !
                    }
                }
                Log.i("==> item object clicked :", selection.toString());
                if (selection.getType().equals("categorie")) {
                    // ToDo Ajouter la redirection pour tuto qui contient des d'autre tuto
                    Tutoriel t = (Tutoriel) selection;
                    Intent i = new Intent(ListTutoriels.this, SubListView.class);
                    // toDo check the parcelable element
                    i.putExtra("objet", (android.os.Parcelable) t);
                    i.putExtra("titre", "Tutoriels");
                    startActivity(i);
                } else {
                    /*Presentation presentation = (Presentation) selection;
                    //Toast.makeText(ListTutoriels.this, "clicked", Toast.LENGTH_LONG).show();
                    //Toast.makeText(ListFormations.this,item,Toast.LENGTH_LONG).show();

                    Intent i = new Intent(ListTutoriels.this, DetailSelection.class);
                    i.putExtra("htmlcode", presentation.getContent());
                    startActivity(i);*/

                    Article presentation = (Article) selection;
                    //Toast.makeText(ListTutoriels.this, "clicked", Toast.LENGTH_LONG).show();
                    //Toast.makeText(ListFormations.this,item,Toast.LENGTH_LONG).show();

                    Intent i = new Intent(ListTutoriels.this, DetailSelection.class);
                    i.putExtra("detailTitre", presentation.getTitle());
                    i.putExtra("htmlcode", presentation.getDescription());
                    startActivity(i);
                }
                return false;
            }
        });

    }

    private void remplirListData() {
        // Creation d'un objet db
        this.db = new DBHandlerTutoriel(this.context);
        //récupération des infos depuis la DB
        this.listOfTutoriels.addAll(this.db.getAll());

        //clé : titre parent donc titre contenu dans la List "titres" | valeur : le sous titre (l'enfant)
        final HashMap<String, List<String>> listTitres = new HashMap<String, List<String>>();
        for(final Tutoriel tuto : this.listOfTutoriels) {
            //test si c'est le titre principal !
            if(tuto.isMainTutorial()) {
                //pas de Doublons !
                if(!listTitres.containsKey(tuto.getTitle()))
                    listTitres.put(tuto.getTitle(), new ArrayList<String>());
            }
            for (final Object sel : tuto.getContent()) {
                if(listTitres.size()>0 && tuto != null &&  listTitres.get(tuto.getTitle()) != null)
                    listTitres.get(tuto.getTitle()).add(((Selection) sel).getTitle());
            }
        }

        this.listAdapter = new CustomExpandableList(ListTutoriels.this, listTitres);
        // setting list adapter
        this.expListView.setAdapter(this.listAdapter);
        this.db.close();

        //on expand toutes les catégories
        for(int i=0;i<this.listAdapter.getGroupCount();i++)
        {
            this.expListView.expandGroup(i);
        }
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

    /* private void affichageDesElements(List<Selection> listSelection, List<Integer> listIdCategorieParent) {

        for (int i = 0; i < listSelection.size(); i++) {
            Selection s = listSelection.get(i);
            if (s instanceof Tutoriel) {
                if (listIdCategorieParent.contains(s.getId())) {
                    ArrayList<String> titreItems = new ArrayList<String>();
                    for (Object select : ((Tutoriel) s).getContent()) {
                        titreItems.add(((Selection)select).getTitle());
                        Log.i("Tutotriel parent a afficher  ", "titre: " + s.getTitle() + " elems : " + titreItems);
                    }

                    listDataChild.put(s.getTitle(), titreItems);
                }
                listOfTutoriels.put((Tutoriel) s, ((Tutoriel) s).getContent());
                affichageDesElements(((Tutoriel) s).getContent(), listIdCategorieParent);
            }
        }
    }
    */


}

