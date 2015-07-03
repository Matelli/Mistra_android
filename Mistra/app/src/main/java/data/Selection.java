package data;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by matelli on 07/01/2015.
 */
public class Selection implements Serializable {

    protected int id ;
    protected String title ;
    protected Type type ;

    public Selection() {
        this.id=-1;
        this.title=new String();
        this.type = null;
    }

    public Selection(int id, String title, Type type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public Selection(Cursor c) {
        this.id = c.getInt(0);
        this.title = c.getString(1);
        this.type = Type.valueOf(c.getString(2));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("Selection{");
        sb.append("id=").append(id);
        sb.append("title='").append(title).append("'");
        if(type != null)
            sb.append("type='").append(id).append("'");
        sb.append('}');
        return sb.toString();
    }
}
