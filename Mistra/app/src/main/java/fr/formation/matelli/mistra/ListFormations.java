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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.DBHandlerF;
import data.Formation;
import data.Presentation;


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
    HashMap<Formation, List<Presentation>> listOfFormations;
    // Handler de db
    DBHandlerF db;

    JSONArray formations = null;

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
                db.closeDB();
                Intent i = new Intent(ListFormations.this, Home.class);
                startActivity(i);
            }
        });

        btnDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.closeDB();
                Intent i = new Intent(ListFormations.this, Devis.class);
                startActivity(i);
            }
        });

        btnTutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.closeDB();
                Intent i = new Intent(ListFormations.this, ListTutoriels.class);
                startActivity(i);
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.closeDB();
                Intent i = new Intent(ListFormations.this, Contact.class);
                startActivity(i);
            }
        });


        // affichage de l'expandable list
        // preparing list data
        //prepareListData();

        // Creation d'un objet db
        db = new DBHandlerF(this.getApplicationContext());

        // Appel du web service pour la récup des infos
        new DataFormation(this).execute();


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                long p = parent.getSelectedPosition();
                Presentation presentation = null;
                String item = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                String itemParentName = (String) parent.getExpandableListAdapter().getGroup(groupPosition);

                Log.i("==> item clicked :", item);
                Log.i("==> item parent clicked :", itemParentName + " at position :" + groupPosition);
                Log.i("==> listOfFormation keys :", listOfFormations.keySet().toString());
                for (Formation c : listOfFormations.keySet()) {
                    if (c.getTitle().equals(itemParentName)) {
                        Log.i("==> item parent clicked detail :", c.toString());
                        presentation = c.getContent().get(childPosition);

                    }
                }
                Log.i("==> item object clicked :", presentation.toString());
                Toast.makeText(ListFormations.this, "clicked", Toast.LENGTH_LONG).show();
                //Toast.makeText(ListFormations.this,item,Toast.LENGTH_LONG).show();

                Intent i = new Intent(ListFormations.this, DetailSelection.class);
                i.putExtra("htmlcode", presentation.getContent());
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


    // Class de collect des titres et sous-titre

    public class DataFormation extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private static final String urlFormation = "http://api.mistra.fr/full.php";

        public DataFormation(Context c) {
            this.progressDialog = new ProgressDialog(c);
            this.progressDialog.setMessage("Please wait ");
            this.progressDialog.setCancelable(false);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            listOfFormations = new HashMap<>();

        }

        @Override
        protected void onPreExecute() {
            this.progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

             // Insert dans les tables
            // ToDo version la version de la base a ce niveau
            // Enregistrement des formation dans la table de formation
            for (Formation f : listOfFormations.keySet()){
                Log.i("Creation de la table ","Formation");
                long creationFrom = db.createFormation(f);

            }
            // Enregistrement des formation dans la table de presentation
            for (Formation e : listOfFormations.keySet()){
                for (Presentation p : listOfFormations.get(e)) {
                    long creationPre = db.createPresentation(e,p);
                }
            }

            Log.i("====>", "PostExecte");
            if (this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
            // Creation de l'adapter depuis l'url
            //listAdapter = new CustomExpandableList(ListFormations.this, listDataChild);


            List<Formation> l = db.getAllFormations();
            HashMap<String, List<String>> listDataChildFromDB = new HashMap<>();
            for (Formation f : l) {
                int idF = f.getId();
                List<Presentation> listPre = db.getPresentation(idF);
                //f.setContent(listPre);
                List<String> listPreString = new ArrayList<>();
                for (Presentation p : listPre) {
                    listPreString.add(p.getTitle());
                }
                listDataChildFromDB.put(f.getTitle(), listPreString);
                listOfFormations.put(f, listPre);
            }

            listAdapter = new CustomExpandableList(ListFormations.this, listDataChildFromDB);


            // setting list adapter
            expListView.setAdapter(listAdapter);
            db.closeDB();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Get the network connection
            boolean testConnection = isNetworkAvailable();
            // Get the database file
            File databaseFile =getApplicationContext().getDatabasePath("MistraDB.db");
            if (testConnection) {
                // ToDo check the db version


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
            String titreF = null;
            Presentation presentation;
            if (fullcode != null) {
                try {
                    JSONObject jsonObject = new JSONObject(fullcode);
                    // Log.e("==> fullcode :",fullcode);

                    formations = jsonObject.getJSONArray("formation");

                    for (int i = 0; i < formations.length(); i++) {
                        JSONObject titreObject = formations.getJSONObject(i);
                        titreF = new String(titreObject.getString("title").getBytes("UTF8"));
                        int idF = Integer.parseInt(titreObject.getString("id"));
                        String typeF = titreObject.getString("type");
                        String descriptionF = titreObject.getString("description");
                        List<Presentation> listItemFormations = new ArrayList<>();
                        //Log.i("==> titre ",titre);
                        JSONArray tabItems = titreObject.getJSONArray("content");
                        List<String> listItems = new ArrayList<>();
                        for (int j = 0; j < tabItems.length(); j++) {
                            JSONObject item = tabItems.getJSONObject(j);
                            String name = item.getString("title");
                            int idI = Integer.parseInt(item.getString("id"));
                            String typeItem = item.getString("type");
                            String contentItem = item.getString("content");
                            presentation = new Presentation(idI, name, typeItem, contentItem);
                            listItemFormations.add(presentation);
                            listItems.add(name);
                        }
                        listDataChild.put(titreF, listItems);
                        listOfFormations.put(new Formation(idF, titreF, typeF, descriptionF, listItemFormations), listItemFormations);
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

    // Test if there the database has been changed

    private void testDatabasseModification(){
        //AsyncHttpClient client = new AsyncHttpClient();



    }



    /*
     * Preparing the list data version Java
     */
    /*
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


        listAdapter = new CustomExpandableList(ListFormations.this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                long p = parent.getSelectedPosition();
                String item = (String ) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                Log.i("==> item clicked :",item);
                //Toast.makeText(ListFormations.this,"clicked",Toast.LENGTH_LONG).show();
                Toast.makeText(ListFormations.this,item,Toast.LENGTH_LONG).show();
                Intent i = new Intent(ListFormations.this, DetailSelection.class);




                startActivity(i);

                return false;
            }
        });
    }*/


}
