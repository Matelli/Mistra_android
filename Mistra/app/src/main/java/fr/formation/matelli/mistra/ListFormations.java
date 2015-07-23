package fr.formation.matelli.mistra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import org.json.JSONArray;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import dao.DBHandlerArticle;
import dao.DBHandlerFormation;
import data.Article;
import data.Formation;
import data.Presentation;
import data.Selection;
import data.Tutoriel;


public class ListFormations extends Activity {

    Context context;

    ImageButton btnRetourHome;
    /*Button btnFormations;
    Button btnTutos;
    Button btnDevis;
    Button btnContact;*/
    RelativeLayout noFormations;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    final List<Formation> listOfFormations = new ArrayList<Formation>();
    DBHandlerFormation db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;

        setContentView(R.layout.activity_list_formations);

        noFormations = (RelativeLayout) findViewById(R.id.no_formations);
        // ImageButton
        this.btnRetourHome = (ImageButton) findViewById(R.id.btnRetourHome);
        // Button
        /*this.btnFormations = (Button) findViewById(R.id.btnFormationFooter);
        this.btnTutos = (Button) findViewById(R.id.btnTutosFooter);
        this.btnDevis = (Button) findViewById(R.id.btnDevisFooter);
        this.btnContact = (Button) findViewById(R.id.btnContactFooter);*/
        // ExpandableList
        this.expListView = (ExpandableListView) findViewById(R.id.expandablelisteFormations);


        // Listeners
        this.btnRetourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListFormations.this, Home.class);

                /*if(getIntent() != null && getIntent().getExtras() != null) {
                    String c = getIntent().getExtras().getString("whoIam");
                    if (c != null) {
                        if (c.equals(Formation.class.toString())) {
                            i = new Intent(ListFormations.this, ListFormations.class);
                        } else if (c.equals(Tutoriel.class.toString())) {
                            i = new Intent(ListFormations.this, ListTutoriels.class);
                        } else if (c.equals(Devis.class.toString())) {
                            i=new Intent(ListFormations.this, Devis.class);
                        } else if (c.equals(SubListView.class.toString())) {
                            i = new Intent(ListFormations.this, SubListView.class);
                        }
                    }
                }*/

                //i.putExtra("whoIam", Formation.class.toString());
                startActivity(i);
                finish();
            }
        });

        /*this.btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListFormations.this, Devis.class);
                i.putExtra("whoIam", Formation.class.toString());
                startActivity(i);
                finish();
            }
        });

        this.btnTutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListFormations.this, ListTutoriels.class);
                i.putExtra("whoIam", Formation.class.toString());
                startActivity(i);
                finish();
            }
        });

        this.btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent i = new Intent(ListFormations.this, Contact.class);
                i.putExtra("whoIam", Formation.class.toString());
                startActivity(i);
                finish();
            }
        });*/


        remplirListData();


        this.expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                long p = parent.getSelectedPosition();
                Article article = null;
                String itemParentName = (String) parent.getExpandableListAdapter().getGroup(groupPosition);
                String item = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);


                Log.e("ListeFormation","==> item clicked :"+item);
                Log.e("ListeFormation","==> item parent clicked :"+ itemParentName + " at position :" + groupPosition);
                for (Formation c : listOfFormations) {
                    if (c.getTitle().equals(itemParentName)) {
                        Log.e("ListeFormation","==> item parent clicked detail :"+ c.toString());
                        article = c.getArticles().get(childPosition);
                        break;//on l'a trouvé, on sort !
                    }
                }
                Log.e("ListeFormation","==> item object clicked :"+ article.toString());

                Intent i = new Intent(ListFormations.this, DetailSelection.class);
                //i.putExtra("ListeFormation","detailTitre"+ article.getTitle());
                //i.putExtra("ListeFormation","htmlcode"+ article.getTitle());
                i.putExtra("detailTitre", article.getTitle());
                i.putExtra("htmlcode", article.getDescription());
                i.putExtra("whoIwas", Formation.class.toString());
                startActivity(i);
                finish();

                return false;
            }
        });


    }


    private void remplirListData() {
        // Creation d'un objet db
        //this.db = new DBHandlerFormation(this.getApplicationContext());
        this.db = new DBHandlerFormation(this);
        //récupération des infos depuis la DB
        this.listOfFormations.addAll(this.db.getAll());

        if(this.listOfFormations != null && this.listOfFormations.size()>0) {
            noFormations.setVisibility(View.GONE);
            this.expListView.setVisibility(View.VISIBLE);

            //clé : titre parent donc titre contenu dans la List "titres" | valeur : le sous titre (l'enfant)
            final HashMap<String, List<String>> listTitres = new HashMap<String, List<String>>();
            for (final Formation form : this.listOfFormations) {
                //pas de Doublons !
                if (!listTitres.containsKey(form.getTitle())) {
                    listTitres.put(form.getTitle(), new ArrayList<String>());
                }

                for (final Article art : form.getArticles()) {
                    listTitres.get(form.getTitle()).add(art.getTitle());
                }
            }

            this.listAdapter = new CustomExpandableList(ListFormations.this, listTitres);
            // setting list adapter
            this.expListView.setAdapter(this.listAdapter);

            //on expand toutes les catégories
            //TODO corriger bug sur l'expand !
            for(int i=0;i<this.listAdapter.getGroupCount();i++)
            {
                this.expListView.expandGroup(i);
            }

        } else {
            //pas d'article de formation, on affiche un message d'erreur.
            noFormations.setVisibility(View.VISIBLE);
            this.expListView.setVisibility(View.GONE);
        }
        this.db.close();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    @Override
    protected void onStop() {
        super.onStop();
    }
}
