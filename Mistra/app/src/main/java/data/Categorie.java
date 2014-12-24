
package data;


import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by matelli on 22/12/2014.
 */

public class Categorie {
    String type, title,description ;
    int id;
    List<Presentation> content;

    public Categorie(int id, String title, String type, String description, List<Presentation> content) {
        this.description = description;
        this.type = type;
        this.title = title;
        this.id = id;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Presentation> getContent() {
        return content;
    }

    public void setContent(List<Presentation> content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Categorie{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", content=" + content +
                '}';
    }


}

