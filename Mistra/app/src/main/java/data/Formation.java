
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
    List<Article> articles;

    public Formation(int id, String title, Type type, String description, List<Article> articles) {
        super(id,title,type);
        this.description = description;
        this.articles = articles;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> content) {
        this.articles = content;
    }

    public void addArticle(Article article) {
        this.articles.add(article);
    }

    @Override
    public String toString() {
        return "Formation{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", articles=" + articles +
                '}';
    }

    @Override
    public int compareTo(Formation o) {
        if (this.getTitle().toLowerCase().equalsIgnoreCase(o.getTitle().toLowerCase())) {
            return this.getType().name().toLowerCase().compareToIgnoreCase(o.getType().name().toLowerCase());
        } else {
            return this.getTitle().toLowerCase().compareToIgnoreCase(o.getTitle().toLowerCase());
        }
    }

}

