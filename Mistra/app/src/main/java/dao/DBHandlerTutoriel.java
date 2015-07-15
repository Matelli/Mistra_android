package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.GestureOverlayView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.Article;
import data.Formation;
import data.Selection;
import data.Tutoriel;
import data.Type;

/**
 * Created by hdmytrow on 25/02/2015.
 */
public class DBHandlerTutoriel extends DBHandler implements IHandlerDB<Tutoriel> {

    private Context context;

    private static final String TABLE_TUTORIELS = "tutoriels";
    // Column name of the tutoriels table
    private static final String  id_T = "id_T";
    private static final String  type_T = "type_T";
    private static final String  title_T = "title_T";
    private static final String  descriptif_T = "descriptif_T";
    private static final String  parent_T = "parent_T";

    // Requete de creation de la table Tutoriels
    public static String creationTutoriel_table = "CREATE TABLE "
            + TABLE_TUTORIELS + "(" + id_T + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + title_T + " TEXT,"
            + type_T + " TEXT,"
            + descriptif_T + " TEXT,"
            + parent_T + " TEXT" + " );";

    public DBHandlerTutoriel(Context context) {
        //super(context, creationTutoriel_table, TABLE_TUTORIELS);
        super(context);
        this.context = context;
    }

    /*public DBHandlerTutoriel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public DBHandlerTutoriel(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }*/

    @Override
    public long insert(Tutoriel t) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!existOnDb(db,t)) {
            ContentValues values = new ContentValues();
            values.put(id_T, t.getId());
            Log.i("DBHandlerTutoriel", "===> insertion de tutoriel" + t.getId());
            values.put(title_T, t.getTitle());
            values.put(type_T, t.getType().name());
            values.put(descriptif_T, t.getDescription());
            values.put(parent_T, t.getParent());

            Long cat_id = db.insert(TABLE_TUTORIELS, null, values);

            db.close();
            return cat_id;
        } else {

            final int rs = update(t);
            db.close();
            return rs;
        }
    }

    public boolean existOnDb(final SQLiteDatabase db,final Selection f) {
        boolean b = false;
        StringBuilder qry = new StringBuilder("SELECT ").append(id_T).append(" FROM ").append(TABLE_TUTORIELS).append(" WHERE ").append(id_T).append(" = ").append(f.getId()).append(";");
        Cursor c = db.rawQuery(qry.toString() , null);
        if(c.getCount() > 0) {
            b = true;
        }
        //db.close();
        c.close();
        return b;
    }

    @Override
    public int update(Tutoriel t) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_T, t.getTitle());
        values.put(type_T, t.getType().name());
        values.put(descriptif_T, t.getDescription());

        final int id = db.update(TABLE_TUTORIELS,values,id_T + " = ?",new String[]{String.valueOf(t.getId())});

        db.close();
        return t.getId();
    }

    @Override
    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TUTORIELS, id_T + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public List<Tutoriel> getAll() {
        Log.i("===> Get all tutoriel", " " + "");
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_TUTORIELS+" ORDER BY "+title_T;
        List<Tutoriel> listeParents = new ArrayList<Tutoriel>();
        List<Tutoriel> listeEnfants = new ArrayList<Tutoriel>();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        //1ere itération, on insere les tutos "mère", ceux n'ayant pas d'idParent. Si l'on tombe sur un tuto avec idParent mais qu'on a pas itéré sur le parent, on est baisé !
        while (!c.isAfterLast()){
            final Tutoriel tuto = new Tutoriel(c);

            //normalement inutile car il n'y a que des tuto et pas d'article.
            if(tuto.getType().equals(Type.CATEGORIE)) {

                if(tuto.getParent() == Tutoriel.NO_PARENT_VALUE) {
                    listeParents.add(tuto);
                } else {
                    listeEnfants.add(tuto);
                }
                /*else {
                    final Tutoriel t = findTutoByIdParent(listeForm, tuto.getParent());
                    if(t!=null) {
                        tuto.addContent(t);
                    }

                    DBHandlerArticle dba = new DBHandlerArticle(this.context);
                    final Article a = dba.getById(t.getId());
                }*/
            }
            c.moveToNext();
        }

        List<Tutoriel> listeTuto = loopTuto(listeParents, listeEnfants);


        c.close();
        db.close();
        return listeTuto;
    }

    /**
     * Méthode qui se charge de définir qui sont les enfants d'un Tutoriel parent et se charge de récupérer les "Content" correspondant.
     * Que ces derniers soient de type Article ou Tutoriel
     * @param listeParents
     * @param listeEnfants
     * @return
     */
    private List<Tutoriel> loopTuto(final List<Tutoriel> listeParents, final List<Tutoriel> listeEnfants ){
        final List<Tutoriel> tutos = new ArrayList<Tutoriel>();

        for(Tutoriel t : listeParents) {
            tutos.add(t);
            List<Tutoriel> enfants = getSelectionByParentId(listeEnfants,t.getId());
            //si on a des enfants, on peut descendre de niveu. SINON on doit aller chercher l'article correspondant
            if(enfants!= null && enfants.size() > 0)
                t.setContent(loopTuto(enfants,listeEnfants));
            else {
                final DBHandlerArticle dba = new DBHandlerArticle(this.context);
                List<Article> rs = dba.getByIdTutoriel(t.getId());
                t.setContent(rs);
            }
        }
        return tutos;
    }

    /**
     * La méthode permet de retourner une liste contenant tout les éléments présent dans le Cursor qui ont pour
     * parentId, l'id passé en parametre.
     * @param liste : la liste dans laquelle on doit trouver les "enfants"
     * @param idParent : l'idParent auquel doit etre associé le(s) enfant(s) que l'ont cherche
     * @return
     */
    private List<Tutoriel> getSelectionByParentId(final List<Tutoriel> liste, final int idParent) {
        final List<Tutoriel> selections = new ArrayList<Tutoriel>();
        if(liste!=null) {
            for(Tutoriel t : liste){
                if (t.getParent() == idParent) {
                    selections.add(t);
                }
            }
        }
        return selections;
    }

    private Tutoriel findTutoByIdParent(List<Tutoriel> liste, long idParent) {
        for(Tutoriel t : liste) {
            if(t.getParent()== Tutoriel.NO_PARENT_VALUE && t.getId() == idParent) {
                return t;
            }
        }
        return null;
    }

    private String allChampsTutoriel() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id_T).append(",");
        sb.append(title_T).append(",");
        sb.append(type_T).append(",");
        sb.append(descriptif_T).append(",");
        sb.append(parent_T);

        return sb.toString();
    }

    @Override
    public Tutoriel getById(int id) {
        SQLiteDatabase db =  this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsTutoriel()).append(" FROM ").append(TABLE_TUTORIELS)
                .append(" WHERE ").append(id_T).append(" = ").append(id).append(";");
        Cursor c = db.rawQuery(qry.toString() , null);
        c.moveToFirst();
        int idT = c.getInt(0);
        String titleT = c.getString(1);
        Type typeT = Type.valueOf(c.getString(2));
        String descriptifT = c.getString(3);
        int parentT = c.getInt(4);

        final DBHandlerArticle dbha = new DBHandlerArticle(context);
        List<Article> listArticleFromTId =  dbha.getByIdTutoriel(id);
        List<Selection> contentT = new ArrayList<Selection>();
        for (Article p : listArticleFromTId){
            contentT.add(p);
        }
        db.close();
        return new Tutoriel(idT, titleT,typeT,descriptifT,contentT,parentT);
    }
}
