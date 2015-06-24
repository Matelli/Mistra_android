package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

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
import java.util.List;

import dao.DBHandler;
import dao.DBHandlerArticle;
import dao.DBHandlerFormation;
import dao.DBHandlerTutoriel;
import data.Article;
import data.Categorie;
import data.Formation;
import data.Selection;
import data.Tutoriel;
import data.Type;

/**
 * Created by hdmytrow on 25/02/2015.
 */
public class UpdateDBService<T> extends Service {

    public UpdateDBService() {
        super();
    }


    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        // Appel du web service pour la récup des infos
        new DataFormation(this).execute();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    // Class de collect des titres et sous-titre
    public class DataFormation extends AsyncTask<Void, Void, Void> {

        private Context context;
        private static final String URL_FORMATIONS = "http://api.mistra.fr/full.php";


        public DataFormation(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Get the network connection
            boolean testConnection = isNetworkAvailable();
            // Get the database file
            File databaseFile = getApplicationContext().getDatabasePath(DBHandler.DATABASE_NAME);
            if (testConnection) {
                // ToDo check the db version

                try {

                    String fullJSON = getDataFromURL(this.URL_FORMATIONS);
                    final List<Formation> formations = parserFormations(fullJSON);
                    final List<Tutoriel> tutoriels = parserTutoriels(fullJSON);
                    alimenterDBFormation(formations);
                    alimenterDBTutoriel(tutoriels);
                    return null;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i(UpdateDBService.class.getName(), "Not connection network return null");
                return null;
            }
            Log.i(UpdateDBService.class.getName(),"Error in doInBackground return null");
            return null;
        }

        private String getDataFromURL(final String url) throws ClientProtocolException, IOException {
            final HttpClient client = new DefaultHttpClient();
            final HttpUriRequest request = new HttpGet(url);
            if(request != null && client != null) {
                HttpResponse response = client.execute(request);
                if(response != null) {
                    HttpEntity httpEntity = response.getEntity();
                    if(httpEntity != null)
                        return EntityUtils.toString(httpEntity);
                }
            }
            return new String();
        }

        /**
         * Méthode qui parse l'arborescence "Formation"
         * @param fullcode : le JSON à parser
         * @return une liste de {@link data.Formation}
         * @throws SAXException
         */
        //TODO peut etre à mutualiser avec parserTutoriel
        private List<Formation> parserFormations(final String fullcode) throws SAXException {
            final List<Formation> listFormations = new ArrayList<Formation>();

            if (fullcode != null) {
                try {
                    final JSONObject jsonObject = new JSONObject(fullcode);

                    final JSONArray formations = jsonObject.getJSONArray("formation");
                    for (int i = 0; i < formations.length(); i++) {
                        final JSONObject titreObject = formations.getJSONObject(i);

                        final int idF = Integer.parseInt(titreObject.getString("id"));
                        final String titreF = new String(titreObject.getString("title").getBytes("UTF8"));
                        final String tmpCat = titreObject.getString("type");
                        final Type typeF = Type.valueOf(tmpCat.toUpperCase());
                        final String descriptionF = titreObject.getString("description");
                        final List<Article> listArticles = new ArrayList<Article>();

                        final Formation form = new Formation(idF,titreF,typeF,descriptionF,listArticles);

                        //Log.i("==> titre ",titre);
                        final JSONArray tabItems = titreObject.getJSONArray("content");
                        //List<String> listItems = new ArrayList<String>();
                        for (int j = 0; j < tabItems.length(); j++) {
                            final JSONObject item = tabItems.getJSONObject(j);

                            final int idI = Integer.parseInt(item.getString("id"));
                            final String titreI = new String(item.getString("title").getBytes("UTF8"));
                            final String cat = item.getString("type");
                            final Type typeItem = Type.valueOf(cat.toUpperCase());
                            final String contentItem = item.getString("content");

                            final Article article = new Article(idI, titreI, typeItem, contentItem,-1, Categorie.FORMATION);
                            form.addArticle(article);
                        }
                        listFormations.add(form);
                    }

                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.e(UpdateDBService.class.getName(), "Erreur de parsing du JSON "+ e.getStackTrace());
                    return listFormations;
                } catch (Exception e) {
                    //e.printStackTrace();
                    Log.e(UpdateDBService.class.getName(), "Exception lors du parsing du JSON "+ e.getStackTrace());
                    return listFormations;
                }
            } else {
                Log.e(UpdateDBService.class.getName(), "Couldn't get any data from the url");
            }
            return listFormations;
        }
    }

    /**
     * Méthode qui se charge de parser l'arborescence "Tutoriel"
     * @param fullcode : le JSON à parser
     * @return une liste de {@link data.Tutoriel}
     * @throws SAXException
     */
    private List<Tutoriel> parserTutoriels(final String fullcode) throws SAXException {
        List<Tutoriel> listSelection = new ArrayList<Tutoriel>();
        if (fullcode != null) {
            try {
                final JSONObject jsonObject = new JSONObject(fullcode);
                final JSONArray tutoriels = jsonObject.getJSONArray("tutoriels");
                for (int i = 0; i < tutoriels.length(); i++) {
                    final JSONObject titreObject = tutoriels.getJSONObject(i);

                    final int idT = Integer.parseInt(titreObject.getString("id"));
                    final String titreT = new String(titreObject.getString("title").getBytes("UTF8"));
                    final String tmpCat = titreObject.getString("type");
                    final Type typeT = Type.valueOf(tmpCat.toUpperCase());
                    final String descriptionT = titreObject.getString("description");
                    final List<Selection> listItems = new ArrayList<Selection>();
                    final int idParent = Tutoriel.NO_PARENT_VALUE;

                    final Tutoriel tuto = new Tutoriel(idT, titreT, typeT, descriptionT, listItems, idParent);

                    Tutoriel item = loopContent(titreObject, tuto);

                    listSelection.add(item);
                }

            } catch (JSONException e) {
                //e.printStackTrace();
                Log.e(UpdateDBService.class.getName(), "Erreur de parsing du JSON " + e.getStackTrace());
                //return listSelection;
                return listSelection;
            } catch (Exception e) {
                //e.printStackTrace();
                Log.e(UpdateDBService.class.getName(), "Exception lors du parsing du JSON " + e.getStackTrace());
                //return (List<Tutoriel>)listSelection;
                return listSelection;
            }
        }

        return listSelection;
    }

    /**
     * Méthode qui se charge de parser le content de chaque "tutoriel" en profondeurs
     * @param parentJSON : l'objet JSON à parser
     * @param parentObj : l'instance JAVA de l'objet parent
     * @return le parametre parentObj MAIS qui contient maintenant la list de ses enfants
     */
    private Tutoriel loopContent(JSONObject parentJSON, Tutoriel parentObj) {
        try {
            final JSONArray tabItems = parentJSON.getJSONArray("content");
            for (int i = 0; i < tabItems.length(); i++) {
                final JSONObject obj = tabItems.getJSONObject(i);

                final String tmpCat = obj.getString("type");
                final Type t = Type.valueOf(tmpCat.toUpperCase());
                switch (t) {
                    case CATEGORIE:
                        final int idTChild = Integer.parseInt(obj.getString("id"));
                        final String titreTChild = new String(obj.getString("title").getBytes("UTF8"));
                        final Type typeTChild = t;
                        final String descriptionTChild = obj.getString("description");
                        final List<Article> listItemsChild = new ArrayList<Article>();//Nous savons qu'il n'y a pas plus de deux niveaux, on défini le type de la liste
                        final int idParentChild = Tutoriel.NO_PARENT_VALUE;

                        final Tutoriel tutoChild = new Tutoriel(idTChild, titreTChild, typeTChild, descriptionTChild, listItemsChild, idParentChild);

                        final Tutoriel tutoLoop = loopContent(obj, tutoChild);

                        parentObj.addContent(tutoLoop);

                        break;
                    case ARTICLE:
                        final int idAChild = Integer.parseInt(obj.getString("id"));
                        final String titreAChild = new String(obj.getString("title").getBytes("UTF8"));
                        final Type typeAChild = t;
                        final String descriptionAChild = obj.getString("content");
                        final long idParent = Article.NO_PARENT_VALUE;

                        final Article article = new Article(idAChild,titreAChild,typeAChild,descriptionAChild,idParent,Categorie.TUTORIEL);
                        parentObj.addContent(article);

                        break;
                }
            }

            return parentObj;

        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e(UpdateDBService.class.getName(), "Erreur de parsing du JSON " + e.getStackTrace());
            //return listSelection;
            return parentObj;
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(UpdateDBService.class.getName(), "Exception lors du parsing du JSON " + e.getStackTrace());
            //return (List<Tutoriel>)listSelection;
            return parentObj;
        }
    }


    private void alimenterDBFormation(List<Formation> formations) {
        if(formations != null && formations.size() > 0) {
            final DBHandlerFormation dbhF = new DBHandlerFormation(this);


            for (final Formation form : formations) {
                final long idParent = dbhF.insert(form);


                final DBHandlerArticle dbhA = new DBHandlerArticle(this);
                for (final Article art : form.getArticles()) {
                    art.setItemParent(idParent);
                    dbhA.insert(art);
                }
                dbhA.close();
            }
            dbhF.close();
        }
    }

    private void alimenterDBTutoriel(List<Tutoriel> tutoriels) {
        if(tutoriels != null && tutoriels.size() > 0) {

            final DBHandlerTutoriel dbhT = new DBHandlerTutoriel(this);
            for (final Tutoriel tuto : tutoriels) {
                final long idParent = dbhT.insert(tuto);

                loopDBTutoriel(dbhT, tuto, idParent);


            }
            dbhT.close();
        }
    }


    private void loopDBTutoriel(DBHandlerTutoriel dbhT, Tutoriel parent, long idParent) {
        for (Selection sel : (List<Selection>)parent.getContent()) {
            if(sel instanceof Tutoriel) {
                final Tutoriel t = (Tutoriel) sel;
                t.setParent(idParent);
                final long idEnfant = dbhT.insert(t);

                loopDBTutoriel(dbhT,t,idEnfant);

            } else if (sel instanceof Article) {
                final Article art = (Article) sel;
                art.setItemParent(idParent);
                final DBHandlerArticle dbhA = new DBHandlerArticle(this);
                dbhA.insert(art);
                dbhA.close();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}