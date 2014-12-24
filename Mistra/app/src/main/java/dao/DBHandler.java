package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import data.Categorie;
import data.Presentation;

/**
 * Created by matelli on 24/12/2014.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MistraDB.db";
    private static final String TABLE_CATEGORIE = "categorie";
    private static final String TABLE_PRESENTATION = "presentation";
    // Column name of the categorie table
    private static final String  id_C = "id_C";
    private static final String  type_C = "type_C";
    private static final String  title_C = "title_C";
    private static final String  descriptif_C = "descriptif_C";
    // Column name of the presentation table
    private static final String  id_P = "id_P";
    private static final String type_P= "type_P";
    private static final String  title_P = "title_P";
    private static final String  content_P = "content_P";


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
        String createC_table = "CREATE TABLE "
                + TABLE_CATEGORIE + "(" + id_C + " INTEGER PRIMARY KEY," + title_C
                + " TEXT," + type_C
                + " TEXT," + descriptif_C + "TEXT," + ")";

        String createP_table = "CREATE TABLE "
                + TABLE_PRESENTATION + "(" + id_P + " INTEGER PRIMARY KEY," + title_P
                + " TEXT," + type_P
                + " TEXT," + content_P + "TEXT," + id_C + " INTEGER "+ ")";

        // Creation des tables
        db.execSQL(createC_table);
        db.execSQL(createP_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables
        db.execSQL("Drop table if exists"+ TABLE_CATEGORIE);
        db.execSQL("Drop table if exists"+ TABLE_PRESENTATION);
        // Creation de nouvelles tables
        onCreate(db);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    // Create a categorie

    public long createCategorie( Categorie c){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id_C, c.getId());
        values.put(title_C, c.getTitle());
        values.put(type_C, c.getType());
        values.put(descriptif_C, c.getDescription());

        Long cat_id = db.insert(TABLE_CATEGORIE, null, values);

        return cat_id;


    }

    public long createPresentation( Categorie c, Presentation p ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(id_P, p.getId());
        values.put(title_P, p.getTitle());
        values.put(type_P, p.getType());
        values.put(content_P, p.getContent());
        values.put(id_C, c.getId());

        Long cat_id = db.insert(TABLE_PRESENTATION, null, values);

        return cat_id;
    }

    public Categorie getCategorie( int catID){
        SQLiteDatabase db =  this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_CATEGORIE + "WHERE id_c = "+catID;
        Cursor c = db.rawQuery(query , null);
        c.moveToFirst();
        int idc = c.getInt(0);
        String titlec = c.getString(1);
        String typec = c.getString(2);
        String descriptifC = c.getString(3);
        List<Presentation> contentC = getPresentation( catID);
        return new Categorie(idc, titlec,typec,descriptifC,contentC);

    }

    public List<Categorie> getAllCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_CATEGORIE;
        List<Categorie> listeCat = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            c.moveToFirst();
            int idc = c.getInt(0);
            String titlec = c.getString(1);
            String typec = c.getString(2);
            String descriptifC = c.getString(3);
            List<Presentation> contentC = getPresentation(idc);
            Categorie cat = new Categorie(idc, titlec,typec,descriptifC,contentC);
            listeCat.add(cat);
        }
        return listeCat;
    }

    public List<Presentation> getPresentation( int catId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_PRESENTATION +"WHERE id_C = "+ catId ;
        Cursor c = db.rawQuery(query, null);
        List<Presentation> liste_p = new ArrayList<>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int idp = c.getInt(0);
            String titlep = c.getString(1);
            String typep = c.getString(2);
            String contentp = c.getString(3);
            Presentation p = new Presentation(idp,titlep,typep,contentp);
            liste_p.add(p);
        }
        c.close();
        return  liste_p;
    }

}
