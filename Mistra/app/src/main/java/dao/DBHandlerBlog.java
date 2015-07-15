package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import data.Blog;
import data.Type;

/**
 * Created by hdmytrow on 13/07/2015.
 */
public class DBHandlerBlog extends DBHandler implements IHandlerDB<Blog>  {
    private Context context;

    public static final String TABLE_BLOG = "blogs";

    // Column name of the presentation table
    private static final String id_B = "id_B";
    private static final String title_B = "title_B";
    private static final String type_B= "type_B";
    private static final String url_B = "url_B";
    private static final String guid_B = "guid_B";
    private static final String datePublication_B = "datePublication_B";
    private static final String description_B = "description_B";
    private static final String content_B = "content_B";
    private static final String image_B = "image_B";

    // Requete de creation de la table presentation s'il n'�xiste pas d�j�
    //private static String createTable = "CREATE TABLE IF NOT EXISTS "
    public static String creationBlog_table = "CREATE TABLE "
            + TABLE_BLOG + "(" + id_B + " INTEGER PRIMARY KEY, "
            + title_B + " TEXT,"
            + type_B + " TEXT,"
            + url_B + " TEXT,"
            + guid_B + " TEXT,"
            + datePublication_B + " DATE,"
            + description_B + " TEXT,"
            + content_B + " TEXT,"
            + image_B + " TEXT );";

    public DBHandlerBlog(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public long insert(Blog b) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(!existOnDb(db, b)) {
            Log.i("DBHandlerFormation", " ===> insertion d'un Blog" + b.getId());
            ContentValues values = new ContentValues();
            values.put(title_B, b.getTitle());
            values.put(type_B, b.getType().name());
            values.put(url_B, b.getUrl());
            values.put(guid_B, b.getGuid());
            values.put(datePublication_B, Blog.sdf.format(b.getDatePublication()));
            values.put(description_B, b.getDescription());
            values.put(content_B, b.getContent());
            values.put(image_B, b.getImage());

            Long cat_id = db.insert(TABLE_BLOG, null, values);

            db.close();
            return cat_id;
        } else {
            final int rs = update(b);
            db.close();
            return rs;

        }
    }

    public boolean existOnDb(final SQLiteDatabase db, final Blog b) {
        boolean rs = false;
        StringBuilder qry = new StringBuilder("SELECT ").append(id_B).append(" FROM ").append(TABLE_BLOG);
        qry.append(" WHERE ").append(title_B).append(" = \"").append(b.getTitle()).append("\"");
        qry.append(" AND ").append(guid_B).append(" = \"").append(b.getGuid()).append("\"");
        qry.append(";");
        Cursor c = db.rawQuery(qry.toString() , null);
        if(c.getCount() > 0) {
            rs = true;
        }
        //db.close();
        c.close();
        return rs;
    }

    @Override
    public int update(Blog b) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(title_B, b.getTitle());
        values.put(type_B, b.getType().name());
        values.put(url_B, b.getUrl());
        values.put(guid_B, b.getGuid());
        values.put(datePublication_B, Blog.sdf.format(b.getDatePublication()));
        values.put(description_B, b.getDescription());
        values.put(content_B, b.getContent());
        values.put(image_B, b.getImage());

        final int id = db.update(TABLE_BLOG,values,id_B + " = ?",new String[]{String.valueOf(b.getId())});
        db.close();
        return b.getId();
    }

    @Override
    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BLOG, id_B+ " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    private String allChampsArticle() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id_B).append(",");
        sb.append(title_B).append(",");
        sb.append(type_B).append(",");
        sb.append(url_B).append(",");
        sb.append(guid_B).append(",");
        sb.append(datePublication_B).append(",");
        sb.append(description_B).append(",");
        sb.append(content_B).append(",");
        sb.append(image_B);

        return sb.toString();
    }

    @Override
    public List getAll() {
        Log.i("DBHandlerBlog", " ===> Get all Blogs");
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsArticle()).append(" FROM ").append(TABLE_BLOG).append(";");
        Cursor c = db.rawQuery(qry.toString(), null);
        List<Blog> liste_B = new ArrayList<Blog>();
        c.moveToFirst();

        Date date = null;
        while(!c.isAfterLast()){
            int idB = c.getInt(0);
            String titleB = c.getString(1);
            Type typeB = Type.valueOf(c.getString(2));
            String urlB = c.getString(3);
            String guidB = c.getString(4);
            String datePubB = c.getString(5);//utilser un sdf.parse
            String descriptionB = c.getString(6);
            String contentB = c.getString(7);
            String imageB = c.getString(8);

            try {

                date = Blog.sdf.parse(datePubB);

            } catch (ParseException e) {
                //e.printStackTrace();
                date = null;
            }

            Blog b = new Blog(idB,titleB,typeB,urlB,guidB,date,descriptionB,contentB,imageB);
            liste_B.add(b);
            c.moveToNext();
        }
        c.close();
        db.close();
        return  liste_B;
    }

    @Override
    public Blog getById(int id) {
        SQLiteDatabase db =  this.getReadableDatabase();
        StringBuilder qry = new StringBuilder("SELECT ").append(allChampsArticle()).append(" FROM ").append(TABLE_BLOG)
                .append(" WHERE ").append(id_B).append(" = ").append(id).append(";");
        Cursor c = db.rawQuery(qry.toString(), null);
        c.moveToFirst();

        int idB = c.getInt(0);
        String titleB = c.getString(1);
        Type typeB = Type.valueOf(c.getString(2));
        String urlB = c.getString(3);
        String guidB = c.getString(4);
        String datePubB = c.getString(5);//utilser un sdf.parse
        String descriptionB = c.getString(6);
        String contentB = c.getString(7);
        String imageB = c.getString(8);

        Date date = null;
        try {

            date = Blog.sdf.parse(datePubB);

        } catch (ParseException e) {
            //e.printStackTrace();
            date = null;
        }

        db.close();
        return new Blog(idB,titleB,typeB,urlB,guidB,date,descriptionB,contentB,imageB);
    }
}
