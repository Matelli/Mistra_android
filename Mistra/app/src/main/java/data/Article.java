package data;

import android.database.Cursor;

/**
 * Created by matelli on 23/12/2014.
 */
public class Article extends Selection implements Comparable<Article> {

    String  description;
    long itemParent;
    Categorie categorie;

    public static final int NO_PARENT_VALUE = -1;

    public Article(){

    }

    public Article(int id, String title, Type type, String description,long itemParent,Categorie categorie){
        super(id,title,type);
        this.description = description;
        this.itemParent = itemParent;
        this.categorie = categorie;

    }

    /*public Article(final Cursor c, final Categorie cat) {
        super(c.getInt(0),c.getString(1),Type.valueOf(c.getString(2)));

        this.description = c.getString(3);
        this.itemParent= c.getInt(4);
        this.categorie = cat;
    }*/


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getItemParent() {
        return itemParent;
    }

    public void setItemParent(long itemParent) {
        this.itemParent = itemParent;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Presentation{" +
                "id=" + id +
                ", type='" + type.name() + '\'' +
                ", title='" + title + '\'' +
                ", categorie='" + categorie + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Article p) {
        if (this.getTitle().toLowerCase().equalsIgnoreCase(p.getTitle().toLowerCase())) {
            return this.getType().name().toLowerCase().compareToIgnoreCase(p.getType().name().toLowerCase());
        } else {
            return this.getTitle().toLowerCase().compareToIgnoreCase(p.getTitle().toLowerCase());
        }
    }

}
