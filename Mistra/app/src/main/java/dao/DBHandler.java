package dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hdmytrow on 25/02/2015.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MistraDB.db";

    private String TABLE_NAME;
    private String CREATE_TABLE;

    public DBHandler(Context context,String creationTable, String tableName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.CREATE_TABLE = creationTable;
        this.TABLE_NAME = tableName;
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creation des tables
        if(CREATE_TABLE != null) {
            db.execSQL(CREATE_TABLE);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables
        if(this.TABLE_NAME != null) {
            db.execSQL("Drop table if exists " + this.TABLE_NAME);
            // Creation de nouvelles tables
        }
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    @Override
    public synchronized void close() {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db !=null && db.isOpen()){
            db.close();
        }
    }
    @Override
    public SQLiteDatabase getWritableDatabase() { return super.getWritableDatabase(); }
    @Override
    public SQLiteDatabase getReadableDatabase() {   return super.getReadableDatabase(); }
}
