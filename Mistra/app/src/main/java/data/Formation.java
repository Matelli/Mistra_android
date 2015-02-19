
package data;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by matelli on 22/12/2014.
 */

public class Formation extends Selection implements Comparable<Formation> {
    String description ;
    List<Presentation> content;

    public Formation(int id, String title, String type, String description, List<Presentation> content) {
        this.description = description;
        this.type = type;
        this.title = title;
        this.id = id;
        this.content = content;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Presentation> getContent() {
        return content;
    }

    public void setContent(List<Presentation> content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Formation{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", content=" + content +
                '}';
    }

    @Override
    public int compareTo(Formation o) {
        if (this.getTitle().toLowerCase().equalsIgnoreCase(o.getTitle().toLowerCase())) {
            return this.getType().toLowerCase().compareToIgnoreCase(o.getType().toLowerCase());
        } else {
            return this.getTitle().toLowerCase().compareToIgnoreCase(o.getTitle().toLowerCase());
        }
    }

}

