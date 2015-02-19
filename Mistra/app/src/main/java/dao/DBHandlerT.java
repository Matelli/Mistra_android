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

import data.Selection;
import data.Tutoriel;
import data.Presentation;

/**
 * Created by matelli on 06/01/2015.
 */
public class DBHandlerT extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MistraDB_Tuto.db";
    private static final String TABLE_TUTORIELS = "tutoriels";
    private static final String TABLE_PRESENTATION_T = "presentationT";
    // Column name of the tutoriels table
    private static final String  id_T = "id_T";
    private static final String  type_T = "type_T";
    private static final String  title_T = "title_T";
    private static final String  descriptif_T = "descriptif_T";
    // Column name of the presentationT table
    private static final String  id_PT = "id_PT";
    private static final String type_PT = "type_PT";
    private static final String  title_PT = "title_PT";
    private static final String  content_PT = "content_PT";

    // Requete de creation de la table Tutoriels
    private static String createT_table = "CREATE TABLE "
            + TABLE_TUTORIELS + "(" + id_T + " INTEGER PRIMARY KEY, " + title_T
            + " TEXT," + type_T
            + " TEXT," + descriptif_T + " TEXT" + " );";

    // Requet de creation de la table presentationT
    private static String createPT_table = "CREATE TABLE "
            + TABLE_PRESENTATION_T + "(" + id_PT + " INTEGER, " + title_PT
            + " TEXT," + type_PT
            + " TEXT," + content_PT + " TEXT," + id_T + " INTEGER, PRIMARY KEY ("+id_PT +","+ id_T +" ), FOREIGN KEY ( "+id_T+" ) REFERENCES "+ TABLE_TUTORIELS +"("+id_T+")"+ ");";


    public DBHandlerT(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    public DBHandlerT(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHandlerT(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
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
        Log.i("===> Creation des tables", "tutoriel et presentationT");
        // Creation des tables
        db.execSQL(createT_table);
        db.execSQL(createPT_table);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables
        db.execSQL("Drop table if exists "+ TABLE_TUTORIELS);
        db.execSQL("Drop table if exists "+ TABLE_PRESENTATION_T);
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
            db.execSQL("Drop table if exists "+ TABLE_TUTORIELS);
            db.execSQL("Drop table if exists "+ TABLE_PRESENTATION_T);*/
            db.close();
        }
    }


    // Creation d'un nouveau tutoriel

    public long createTutoriel( Tutoriel f){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id_T, f.getId());
        Log.i("===> insertion de tutoriel", " " + f.getId());
        values.put(title_T, f.getTitle());
        values.put(type_T, f.getType());
        values.put(descriptif_T, f.getDescription());

        Long cat_id = db.insert(TABLE_TUTORIELS, null, values);

        return cat_id;


    }

    // Creation d'une nouvelle presentationT
    public long createPresentationT( Tutoriel f, Presentation p ){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("===> insertion de presentationT", " " + p.getId());
        ContentValues values = new ContentValues();
        values.put(id_PT, p.getId());
        values.put(title_PT, p.getTitle());
        values.put(type_PT, p.getType());
        values.put(content_PT, p.getContent());
        values.put(id_T, f.getId());

        Long cat_id = db.insert(TABLE_PRESENTATION_T, null, values);

        return cat_id;
    }

    // Get d'une tutoriel depuis son id

    public Tutoriel getTutoriel( int formID){
        SQLiteDatabase db =  this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_TUTORIELS + " WHERE id_T = " + formID + ";";
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        int idT = c.getInt(0);
        String titleT = c.getString(1);
        String typeT = c.getString(2);
        String descriptifT = c.getString(3);

        List<Presentation> listPreFromTId =  getPresentationT( formID);
        List<Selection> contentT = new ArrayList<Selection>();
        for (Presentation p : listPreFromTId){
            contentT.add(p);
        }
        return new Tutoriel(idT, titleT,typeT,descriptifT,contentT);

    }

    // Get de toutes les tutoriels

    public List<Tutoriel> getAllTutoriels(){
        Log.i("===> Get all tutoriel", " " + "");
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_TUTORIELS+" ORDER BY "+title_T;
        List<Tutoriel> listeForm = new ArrayList<Tutoriel>();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            int idT = c.getInt(0);
            String titleT = c.getString(1);
            String typeT = c.getString(2);
            String descriptifT = c.getString(3);
            List<Presentation> listPreFromT = getPresentationT(idT);
            List<Selection> contentT = new ArrayList<Selection>();
            for (Presentation p : listPreFromT){
                contentT.add(p);
            }
            Tutoriel cat = new Tutoriel(idT, titleT,typeT,descriptifT,contentT);
            listeForm.add(cat);
            c.moveToNext();
        }
        c.close();
        return listeForm;
    }

    // Get d'une presentationT depuis id d'une tutoriel

    public List<Presentation> getPresentationT( int formId){
        Log.i("===> Get all presentationTs for tutoriel ", " " + ""+formId);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_PRESENTATION_T +" WHERE id_T = "+ formId ;
        Cursor c = db.rawQuery(query, null);
        List<Presentation> liste_p = new ArrayList<Presentation>();
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

    // Get d'une seule presentationT selon son id

    public Presentation getOnePresentationT( int preID){
        SQLiteDatabase db =  this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_PRESENTATION_T + " WHERE id_PT = " + preID + ";";
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        int idP = c.getInt(0);
        String titleP = c.getString(1);
        String typeP = c.getString(2);
        String contentP = c.getString(3);

        return new Presentation(idP, titleP,typeP,contentP);

    }


    // Update d'une tutoriel

    public int updateTutoriel(Tutoriel f){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_T, f.getTitle());
        values.put(type_T, f.getType());
        values.put(descriptif_T, f.getDescription());

        return db.update(TABLE_TUTORIELS,values,id_T + " = ?",new String[]{String.valueOf(f.getId())});

    }

    // Update d'une presentationT

    public int updatePresentationT(Presentation p){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_PT, p.getTitle());
        values.put(type_PT, p.getType());
        values.put(content_PT, p.getContent());

        return db.update(TABLE_PRESENTATION_T,values,id_PT + " = ?",new String[]{String.valueOf(p.getId())});
    }


    // Delete d'une tutoriel

    public void deleteTutoriel(int idF){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TUTORIELS, id_T+ " = ?", new String[]{String.valueOf(idF)});

    }

    // Delete d'une presentationT

    public void deletePresentationT(int idP){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESENTATION_T, id_PT+ " = ?", new String[]{String.valueOf(idP)});

    }



}
