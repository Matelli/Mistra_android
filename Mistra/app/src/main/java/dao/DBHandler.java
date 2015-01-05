package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import data.Formation;
import data.Presentation;

/**
 * Created by matelli on 24/12/2014.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MistraDB.db";
    private static final String TABLE_FORMATION = "formations";
    private static final String TABLE_PRESENTATION = "presentations";
    // Column name of the formations table
    private static final String  id_F = "id_F";
    private static final String  type_F = "type_F";
    private static final String  title_F = "title_F";
    private static final String  descriptif_F = "descriptif_F";
    // Column name of the presentation table
    private static final String  id_P = "id_P";
    private static final String type_P= "type_P";
    private static final String  title_P = "title_P";
    private static final String  content_P = "content_P";

    // Requete de creation de la table Formation:
    private static String createF_table = "CREATE TABLE "
            + TABLE_FORMATION + "(" + id_F + " INTEGER PRIMARY KEY, " + title_F
            + " TEXT," + type_F
            + " TEXT," + descriptif_F + " TEXT" + " );";

    // Requet de creation de la table presentation
    private static String createP_table = "CREATE TABLE "
            + TABLE_PRESENTATION + "(" + id_P + " INTEGER, " + title_P
            + " TEXT," + type_P
            + " TEXT," + content_P + " TEXT," + id_F + " INTEGER, PRIMARY KEY ("+id_P +","+ id_F +" ), FOREIGN KEY ( "+id_F+" ) REFERENCES "+ TABLE_FORMATION +"("+id_F+")"+ ");";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creation des tables
        db.execSQL(createF_table);
        db.execSQL(createP_table);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables
        db.execSQL("Drop table if exists "+ TABLE_FORMATION);
        db.execSQL("Drop table if exists "+ TABLE_PRESENTATION);
        // Creation de nouvelles tables
        onCreate(db);

    }

    // Opend de la DB

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    // Close de la DB
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db !=null && db.isOpen()){
            /*// Drop tables
            db.execSQL("Drop table if exists "+ TABLE_FORMATION);
            db.execSQL("Drop table if exists "+ TABLE_PRESENTATION);*/
            db.close();
        }
    }


    // Creation d'une nouvelle formation

    public long createFormation( Formation f){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id_F, f.getId());
        Log.i("===> insertion de formation", " " + f.getId());
        values.put(title_F, f.getTitle());
        values.put(type_F, f.getType());
        values.put(descriptif_F, f.getDescription());

        Long cat_id = db.insert(TABLE_FORMATION, null, values);

        return cat_id;


    }

    // Creation d'une nouvelle presentation
    public long createPresentation( Formation f, Presentation p ){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("===> insertion de presentation", " " + p.getId());
        ContentValues values = new ContentValues();
        values.put(id_P, p.getId());
        values.put(title_P, p.getTitle());
        values.put(type_P, p.getType());
        values.put(content_P, p.getContent());
        values.put(id_F, f.getId());

        Long cat_id = db.insert(TABLE_PRESENTATION, null, values);

        return cat_id;
    }

    // Get d'une formation depuis son id

    public Formation getFormation( int formID){
        SQLiteDatabase db =  this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_FORMATION + " WHERE id_F = " + formID + ";";
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        int idF = c.getInt(0);
        String titleF = c.getString(1);
        String typeF = c.getString(2);
        String descriptifF = c.getString(3);
        List<Presentation> contentF = getPresentation( formID);
        return new Formation(idF, titleF,typeF,descriptifF,contentF);

    }

    // Get de toutes les formations

    public List<Formation> getAllFormations(){
        Log.i("===> Get all formation", " " + "");
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_FORMATION;
        List<Formation> listeForm = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            int idF = c.getInt(0);
            String titleF = c.getString(1);
            String typeF = c.getString(2);
            String descriptifF = c.getString(3);
            List<Presentation> contentF = getPresentation(idF);
            Formation cat = new Formation(idF, titleF,typeF,descriptifF,contentF);
            listeForm.add(cat);
            c.moveToNext();
        }
        c.close();
        return listeForm;
    }

    // Get d'une presentation depuis id d'une formation

    public List<Presentation> getPresentation( int formId){
        Log.i("===> Get all presentations for formation ", " " + ""+formId);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_PRESENTATION +" WHERE id_F = "+ formId ;
        Cursor c = db.rawQuery(query, null);
        List<Presentation> liste_p = new ArrayList<>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int idP = c.getInt(0);
            String titleP = c.getString(1);
            String typeP = c.getString(2);
            String contentP = c.getString(3);
            Presentation p = new Presentation(idP,titleP,typeP,contentP);
            liste_p.add(p);
            c.moveToNext();
        }
        c.close();
        return  liste_p;
    }

    // Get d'une seule presentation selon son id

    public Presentation getOnePresentation( int preID){
        SQLiteDatabase db =  this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_PRESENTATION + " WHERE id_P = " + preID + ";";
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        int idP = c.getInt(0);
        String titleP = c.getString(1);
        String typeP = c.getString(2);
        String contentP = c.getString(3);

        return new Presentation(idP, titleP,typeP,contentP);

    }


    // Update d'une formation

    public int updateFormation(Formation f){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_F, f.getTitle());
        values.put(type_F, f.getType());
        values.put(descriptif_F, f.getDescription());

        return db.update(TABLE_FORMATION,values,id_F + " = ?",new String[]{String.valueOf(f.getId())});

    }

    // Update d'une presentation

    public int updatePresentation(Presentation p){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_P, p.getTitle());
        values.put(type_P, p.getType());
        values.put(content_P, p.getContent());

        return db.update(TABLE_PRESENTATION,values,id_P + " = ?",new String[]{String.valueOf(p.getId())});
    }


    // Delete d'une formation

    public void deleteFormation(int idF){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FORMATION, id_F+ " = ?", new String[]{String.valueOf(idF)});

    }

    // Delete d'une presentation

    public void deletePresentation(int idP){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESENTATION, id_P+ " = ?", new String[]{String.valueOf(idP)});

    }

}
