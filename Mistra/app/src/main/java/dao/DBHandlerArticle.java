package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import data.Article;
import data.Categorie;
import data.Formation;
import data.Type;

/**
 * Created by hdmytrow on 25/02/2015.
 */
public class DBHandlerArticle extends DBHandler implements IHandlerDB<Article>  {

    private Context context;

    public static final String TABLE_ARTICLE = "articles";

    // Column name of the presentation table
    private static final String id_A = "id_A";
    private static final String type_A= "type_A";
    private static final String title_A = "title_A";
    private static final String description_A = "description_A";
    private static final String id_parent = "id_parent";
    private static final String categorie_A = "categorie_A";//valeur d'énumeration de la CATEGORIE pour savoir si l'id parent est pour formation ou tutoriel

    // Requete de creation de la table presentation s'il n'éxiste pas déjà
    //private static String createTable = "CREATE TABLE IF NOT EXISTS "
    public static String createArticle_table = "CREATE TABLE "
            + TABLE_ARTICLE + "(" + id_A + " INTEGER PRIMARY KEY, "
            + title_A + " TEXT,"
            + type_A + " TEXT,"
            + description_A + " TEXT,"
            + id_parent + " INTEGER,"
            + categorie_A + " TEXT );";
            //+ " PRIMARY KEY ("+id_A +","+ id_parent +" ),"
            //+ " FOREIGN KEY ( "+id_parent+" ) REFERENCES "+ DBHandlerFormation.TABLE_FORMATION +"("+DBHandlerFormation.id_F+")"+ ");";

    public DBHandlerArticle(Context context) {
        //super(context, createTable, TABLE_ARTICLE);
        super(context);
        this.context = context;
    }

    /*public DBHandlerArticle(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public DBHandlerArticle(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }*/

    @Override
    public long insert(Article a) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(!existOnDb(db, a)) {
            Log.i("DBHandlerFormation", " ===> insertion d'Article" + a.getId());
            ContentValues values = new ContentValues();
            values.put(id_A, a.getId());
            values.put(title_A, a.getTitle());
            values.put(type_A, a.getType().name());
            values.put(description_A, a.getDescription());
            values.put(id_parent, a.getItemParent());
            values.put(categorie_A, a.getCategorie().name());

            Long cat_id = db.insert(TABLE_ARTICLE, null, values);

            db.close();
            return cat_id;
        } else {


            final int rs = update(a);
            db.close();
            return rs;
        }

    }

    public boolean existOnDb(final SQLiteDatabase db, final Article a) {
        boolean b = false;
        StringBuilder qry = new StringBuilder("SELECT ").append(id_A).append(" FROM ").append(TABLE_ARTICLE).append(" WHERE ").append(id_A).append(" = ").append(a.getId()).append(";");
        Cursor c = db.rawQuery(qry.toString() , null);
        if(c.getCount() > 0) {
            b = true;
        }
        //db.close();
        c.close();
        return b;
    }

    @Override
    public int update(Article a) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_A, a.getTitle());
        values.put(type_A, a.getType().name());
        values.put(description_A, a.getDescription());
        values.put(id_parent, a.getItemParent());
        values.put(categorie_A, a.getCategorie().name());

        final int id = db.update(TABLE_ARTICLE,values,id_A + " = ?",new String[]{String.valueOf(a.getId())});
        db.close();
        return a.getId();
    }

    @Override
    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLE, id_A+ " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    private String allChampsArticle() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id_A).append(",");
        sb.append(title_A).append(",");
        sb.append(type_A).append(",");
        sb.append(description_A).append(",");
        sb.append(id_parent).append(",");
        sb.append(categorie_A);
        return sb.toString();
    }

    @Override
    public List getAll() {
        Log.i("DBHandlerFormation", " ===> Get all Articles");
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsArticle()).append(" FROM ").append(TABLE_ARTICLE).append(";");
        Cursor c = db.rawQuery(qry.toString(), null);
        List<Article> liste_A = new ArrayList<Article>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int idA = c.getInt(0);
            String titleA = c.getString(1);
            Type typeA = Type.valueOf(c.getString(2));
            String descriptionA = c.getString(3);
            int id_parent = c.getInt(4);
            Categorie categorieA = Categorie.valueOf(c.getString(5));
            Article p = new Article(idA,titleA,typeA,descriptionA,id_parent,categorieA);
            liste_A.add(p);
            c.moveToNext();
        }
        c.close();
        db.close();
        return  liste_A;
    }

    public List getAllByCategorie(Categorie categorie) {
        if(categorie == null) return new ArrayList<Article>();

        Log.i("DBHandlerFormation", " ===> Get all Articles");
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsArticle()).append(" FROM ").append(TABLE_ARTICLE)
                .append(" WHERE ").append(categorie_A).append(" = '").append(categorie.name()).append("';");
        Cursor c = db.rawQuery(qry.toString(), null);
        List<Article> liste_A = new ArrayList<Article>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int idA = c.getInt(0);
            String titleA = c.getString(1);
            Type typeA = Type.valueOf(c.getString(2));
            String descriptionA = c.getString(3);
            int id_parent = c.getInt(4);
            Categorie categorieA = Categorie.valueOf(c.getString(5));
            Article p = new Article(idA,titleA,typeA,descriptionA,id_parent,categorieA);
            liste_A.add(p);
            c.moveToNext();
        }
        c.close();
        db.close();
        return  liste_A;
    }

    public List getByIdFormation(int idFormation) {
        Log.i("DBHandlerFormation", " ===> Get all Article for formation "+idFormation);
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsArticle()).append(" FROM ").append(TABLE_ARTICLE)
                .append(" WHERE ").append(id_parent).append(" = ").append(idFormation)
                .append(" AND ").append(categorie_A).append(" = '").append(Categorie.FORMATION.name()).append("';");
        Cursor c = db.rawQuery(qry.toString(), null);
        List<Article> liste_A = new ArrayList<Article>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int idA = c.getInt(0);
            String titleA = c.getString(1);
            Type typeA = Type.valueOf(c.getString(2));
            String descriptionA = c.getString(3);
            int id_parent = c.getInt(4);
            Categorie categorieA = Categorie.valueOf(c.getString(5));
            Article p = new Article(idA,titleA,typeA,descriptionA,id_parent,categorieA);
            liste_A.add(p);
            c.moveToNext();
        }
        c.close();
        db.close();
        return  liste_A;
    }

    public List getByIdTutoriel(int idTutoriel) {
        Log.i("DBHandlerFormation", " ===> Get all Article for Tutoriel "+idTutoriel);
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsArticle()).append(" FROM ").append(TABLE_ARTICLE)
                .append(" WHERE ").append(id_parent).append(" = ").append(idTutoriel)
                .append(" AND ").append(categorie_A).append(" = '").append(Categorie.TUTORIEL.name()).append("';");
        Cursor c = db.rawQuery(qry.toString(), null);
        List<Article> liste_A = new ArrayList<Article>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int idA = c.getInt(0);
            String titleA = c.getString(1);
            Type typeA = Type.valueOf(c.getString(2));
            String descriptionA = c.getString(3);
            int id_parent = c.getInt(4);
            Categorie categorieA = Categorie.valueOf(c.getString(5));
            Article p = new Article(idA,titleA,typeA,descriptionA,id_parent,categorieA);
            liste_A.add(p);
            c.moveToNext();
        }
        c.close();
        db.close();
        return  liste_A;
    }


    @Override
    public Article getById(int id) {
        SQLiteDatabase db =  this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsArticle()).append(" FROM ").append(TABLE_ARTICLE)
                .append(" WHERE ").append(id_A).append(" = ").append(id).append(";");
        Cursor c = db.rawQuery(qry.toString(), null);
        c.moveToFirst();
        int idA = c.getInt(0);
        String titleA = c.getString(1);
        Type typeA = Type.valueOf(c.getString(2));
        String descriptionA = c.getString(3);
        int id_parent = c.getInt(4);
        Categorie categorieA = Categorie.valueOf(c.getString(5));

        db.close();
        return new Article(idA, titleA,typeA,descriptionA,id_parent,categorieA);
    }
}
