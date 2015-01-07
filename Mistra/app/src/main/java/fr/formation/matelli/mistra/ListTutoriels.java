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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.DBHandlerF;
import dao.DBHandlerT;
import data.Selection;
import data.Tutoriel;
import data.Presentation;


public class ListTutoriels extends Activity {

    ImageButton btnRetourHome;
    Button btnFormations;
    Button btnTutos;
    Button btnDevis;
    Button btnContact;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<Tutoriel, List<Selection>> listOfTutoriels;
    DBHandlerT db;
    JSONArray tutoriels = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent i = new Intent(ListTutoriels.this, Home.class);
                startActivity(i);
            }
        });

        btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTutoriels.this, Devis.class);
                startActivity(i);
            }
        });

        btnFormations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTutoriels.this, ListFormations.class);
                startActivity(i);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListTutoriels.this, Contact.class);
                startActivity(i);
            }
        });


        // Creation d'un objet db
        db = new DBHandlerT(this.getApplicationContext());

        // Appel du web service pour la récup des infos
        new DataFormation(this).execute();


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                long p = parent.getSelectedPosition();
                Selection selection = null;
                String item = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                String itemParentName = (String) parent.getExpandableListAdapter().getGroup(groupPosition);

                Log.i("==> item clicked :", item);
                Log.i("==> item parent clicked :", itemParentName + " at position :" + groupPosition);
                Log.i("==> listOfFormation keys :", listOfTutoriels.keySet().toString());
                for (Tutoriel c : listOfTutoriels.keySet()) {
                    if (c.getTitle().equals(itemParentName)) {
                        Log.i("==> item parent clicked detail :", c.toString());

                        selection = c.getContent().get(childPosition);

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
                    Presentation presentation = (Presentation) selection;
                    Toast.makeText(ListTutoriels.this, "clicked", Toast.LENGTH_LONG).show();
                    //Toast.makeText(ListFormations.this,item,Toast.LENGTH_LONG).show();

                    Intent i = new Intent(ListTutoriels.this, DetailSelection.class);
                    i.putExtra("htmlcode", presentation.getContent());
                    startActivity(i);
                }
                return false;
            }
        });

    }

    // Class de collect des titres et sous-titre

    public class DataFormation extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private static final String urlFormation = "http://api.mistra.fr/full.php";

        public DataFormation(Context c) {
            this.progressDialog = new ProgressDialog(c);
            this.progressDialog.setMessage("Please wait ");
            this.progressDialog.setCancelable(false);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            listOfTutoriels = new HashMap<>();

        }

        @Override
        protected void onPreExecute() {
            this.progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // ToDo verification des version ici
            // ToDo pour l'instant cette boucle ne doivent fonctinné que 1 fois pour eviter la
            // ToDo duplication de l'info il faut verifier si la base du web service est modifier pour faire un upgrade

              // Insert into tables if they are empty

            for (Tutoriel t : listOfTutoriels.keySet()) {
                Log.i("Insertion d'un tuto ", "Tutoriel");
                long creationFrom = db.createTutoriel(t);

            }
            // Enregistrement des formation dans la table de presentation
            for (Tutoriel e : listOfTutoriels.keySet()) {
                for (Selection s : listOfTutoriels.get(e)) {
                    if (s.getType().equals("categorie")) {
                        Log.i("Insertion d'un tuto ", "Tutoriel avec un sub tuto");
                        Log.i("Insertion d'un tuto ", s.toString());
                        Tutoriel t = (Tutoriel) s;
                        long creationFrom = db.createTutoriel(t);
                    } else {
                        Presentation p = (Presentation) s;
                        long creationPre = db.createPresentationT(e, p);
                    }
                }
            }



            Log.i("====>", "PostExecte");
            if (this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
            // Creation de l'adapter depuis l'url
            //listAdapter = new CustomExpandableList(ListFormations.this, listDataChild);


            List<Tutoriel> l = db.getAllTutoriels();
            HashMap<String, List<String>> listDataChildFromDB = new HashMap<>();
            for (Tutoriel t : l) {
                int idT = t.getId();
                List<Presentation> listPreFromDB = db.getPresentationT(idT);
                List<Selection> listPre = new ArrayList<>();
                for (Presentation e : listPreFromDB) {
                    listPre.add(e);
                }
                //t.setContent(listPre);
                List<String> listPreString = new ArrayList<>();
                for (Selection p : listPre) {
                    listPreString.add(p.getTitle());
                }
                listDataChildFromDB.put(t.getTitle(), listPreString);
                listOfTutoriels.put(t, listPre);
            }

            listAdapter = new CustomExpandableList(ListTutoriels.this, listDataChildFromDB);


            // setting list adapter
            expListView.setAdapter(listAdapter);
            db.closeDB();
        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean testConnection = isNetworkAvailable();
            if (testConnection) {
                // ToDo check the db version
                Log.i("Connection  ", "" + testConnection);


                String fullcode = null;
                listDataChild = new HashMap<String, List<String>>();
                try {
                    fullcode = getDataFromURL(urlFormation);
                    return titlesFromInputStream(fullcode);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i("Not connection network ", "return null");
                return null;
            }
            Log.i("Error in doInBackground ", "return null");
            return null;
        }

        private String getDataFromURL(String url)
                throws ClientProtocolException, IOException {
            HttpClient client = new DefaultHttpClient();
            HttpUriRequest request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            HttpEntity httpEntity = response.getEntity();
            return EntityUtils.toString(httpEntity);
        }

        private Void titlesFromInputStream(String fullcode)
                throws SAXException {
            //ArrayList<String> arrayList = new ArrayList<String>();
            String titreT = null;
            Presentation presentation;
            if (fullcode != null) {
                try {
                    JSONObject jsonObject = new JSONObject(fullcode);
                    // Log.e("==> fullcode :",fullcode);

                    tutoriels = jsonObject.getJSONArray("tutoriels");

                    for (int i = 0; i < tutoriels.length(); i++) {
                        JSONObject titreObject = tutoriels.getJSONObject(i);
                        titreT = new String(titreObject.getString("title").getBytes("UTF-8"));
                        int idT = Integer.parseInt(titreObject.getString("id"));
                        String typeT = titreObject.getString("type");
                        String descriptionT = titreObject.getString("description");
                        List<Selection> listItemTutoriels = new ArrayList<>();
                        //Log.i("==> titre ",titre);
                        List<String> listItems = new ArrayList<>();
                        JSONArray tabItems = titreObject.getJSONArray("content");
                        for (int j = 0; j < tabItems.length(); j++) {
                            JSONObject item = tabItems.getJSONObject(j);
                            // ToDo select if it is an other tuto or it is a presentation
                            // ToDo revoir le parse
                            if (item.getString("type").equals("categorie")) {
                                Log.i("==> test du type pour T ",item.getString("type"));
                                String titreT2 = new String(titreObject.getString("title").getBytes("UTF-8"));
                                int idT2 = Integer.parseInt(titreObject.getString("id"));
                                Log.i("==> insertion du tuto id  ",""+idT2);
                                String typeT2 = titreObject.getString("type");
                                String descriptionT2 = titreObject.getString("description");
                                JSONArray tabItems2 = titreObject.getJSONArray("content");
                                List<Selection> listItemTutoriels2 = new ArrayList<>();
                                List<String> listItems2 = new ArrayList<>();
                                for (int k = 0; k < tabItems2.length(); k++) {
                                    String name2 = item.getString("title");
                                    int idI2 = Integer.parseInt(item.getString("id"));
                                    String typeItem2 = item.getString("type");
                                    String contentItem2 = item.getString("content");
                                    Presentation presentation2 = new Presentation(idI2, name2, typeItem2, contentItem2);
                                    listItemTutoriels2.add(presentation2);
                                    listItems2.add(name2);
                                }
                                listDataChild.put(titreT2, listItems2);
                                listOfTutoriels.put(new Tutoriel(idT2, titreT2, typeT2, descriptionT2, listItemTutoriels2), listItemTutoriels2);

                            } else {
                                String name = item.getString("title");
                                int idI = Integer.parseInt(item.getString("id"));
                                String typeItem = item.getString("type");
                                Log.i("==> test du type pour P ",item.getString("type"));
                                String contentItem = item.getString("content");
                                presentation = new Presentation(idI, name, typeItem, contentItem);
                                listItemTutoriels.add(presentation);
                                listItems.add(name);
                            }
                        }
                        listDataChild.put(titreT, listItems);
                        listOfTutoriels.put(new Tutoriel(idT, titreT, typeT, descriptionT, listItemTutoriels), listItemTutoriels);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("web service", "Couldn't get any data from the url");
            }
            return null;
        }











    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

      /*  *//*
     * Preparing the list data
     *//*

     *//*
     partie a mettre dans le onCreate()
     // affichage de l'expandable list
        // preparing list data
        prepareListData();

        listAdapter = new CustomExpandableList(ListTutoriels.this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);*//*

    // partie hors onCreate()

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
*/
}
