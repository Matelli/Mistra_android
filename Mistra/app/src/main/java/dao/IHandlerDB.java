package dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import data.Article;
import data.Formation;
import data.Presentation;

/**
 * Created by hdmytrow on 25/02/2015.
 */
public interface IHandlerDB<T> {
    /**
     * @param handler : Prend en parametre soit un {@link dao.DBHandlerFormation} ou un {@link dao.DBHandlerTutoriel}
     */
    public long insert(T handler);
    /**
     * @param handler : Prend en parametre soit un {@link dao.DBHandlerFormation} ou un {@link dao.DBHandlerTutoriel}
     */
    public int update(T handler);
    public void deleteById(int id);
   // public void onOpen(SQLiteDatabase db);
    //public void closeDB();
    //**** GETTERs ****//
    public List getAll();
    public T getById(int id);//correspond au getFormation
}
