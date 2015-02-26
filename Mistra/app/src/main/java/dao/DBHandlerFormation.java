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
import data.Formation;

/**
 * Created by hdmytrow on 25/02/2015.
 */
public class DBHandlerFormation extends DBHandler implements IHandlerDB<Formation> {

    private Context context;

    public static final String TABLE_FORMATION = "formations";
    // Column name of the formations table
    private static final String  id_F = "id_F";
    private static final String  type_F = "type_F";
    private static final String  title_F = "title_F";
    private static final String  descriptif_F = "descriptif_F";
    //private static final String  parent_F = "parent_F";//crée un double niveau. Null si 1er niveau sinon ça contient l'id de la formation parente


    // Requete de creation de la table Formation s'il n'existe pas déjà
    private static String creationFormationTable = "CREATE TABLE IF NOT EXISTS "
            + TABLE_FORMATION + "(" + id_F + " INTEGER PRIMARY KEY, "
            + title_F + " TEXT,"
            + type_F  + " TEXT,"
            + descriptif_F + " TEXT"
            +" );";


    public DBHandlerFormation(Context context) {
        super(context,creationFormationTable, TABLE_FORMATION);
        this.context = context;
    }

    public DBHandlerFormation(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }

    public DBHandlerFormation(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public long insert(Formation f) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id_F, f.getId());
        Log.i("DBHandlerFormation", " ===> insertion de formation" + f.getId());
        values.put(title_F, f.getTitle());
        values.put(type_F, f.getType());
        values.put(descriptif_F, f.getDescription());

        Long cat_id = db.insert(TABLE_FORMATION, null, values);

        return cat_id;
    }

    @Override
    public int update(Formation f) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_F, f.getTitle());
        values.put(type_F, f.getType());
        values.put(descriptif_F, f.getDescription());

        return db.update(TABLE_FORMATION,values,id_F + " = ?",new String[]{String.valueOf(f.getId())});
    }

    @Override
    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FORMATION, id_F+ " = ?", new String[]{String.valueOf(id)});
    }

    private String allChampsFormation() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id_F).append(",");
        sb.append(title_F).append(",");
        sb.append(type_F).append(",");
        sb.append(descriptif_F).append(",");
        return sb.toString();
    }

    @Override
    public List<Formation> getAll() {
        Log.i("DBHandlerFormation", " ===> Get all formation");
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsFormation()).append(" FROM ").append(TABLE_FORMATION).append(" ORDER BY ").append(title_F).append(";");
        List<Formation> listeForm = new ArrayList<Formation>();
        Cursor c = db.rawQuery(qry.toString(), null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            int idF = c.getInt(0);
            String titleF = c.getString(1);
            String typeF = c.getString(2);
            String descriptifF = c.getString(3);
            final DBHandlerArticle dbha = new DBHandlerArticle(context);
            List<Article> contentF = dbha.getByIdFormation(idF);
            Formation cat = new Formation(idF, titleF,typeF,descriptifF,contentF);
            listeForm.add(cat);
            c.moveToNext();
        }
        c.close();
        return listeForm;
    }

    @Override
    public Formation getById(int id) {
        SQLiteDatabase db =  this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsFormation()).append(" FROM ").append(TABLE_FORMATION).append(" WHERE ").append(id_F).append(" = ").append(id).append(";");
        Cursor c = db.rawQuery(qry.toString() , null);
        c.moveToFirst();
        int idF = c.getInt(0);
        String titleF = c.getString(1);
        String typeF = c.getString(2);
        String descriptifF = c.getString(3);
        final DBHandlerArticle dbha = new DBHandlerArticle(context);
        List<Article> contentF = dbha.getByIdFormation(id);
        return new Formation(idF, titleF,typeF,descriptifF,contentF);
    }

}
