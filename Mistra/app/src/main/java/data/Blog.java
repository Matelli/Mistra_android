package data;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by hdmytrow on 13/07/2015.
 */
public class Blog extends Selection implements Comparable<Blog>  {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss '+0000'");

    static public int ID_DEFAULT = -1;
    private String url;
    private String guid;
    private Date datePublication;
    private String description;
    private String content;
    private String image;

    public Blog(final int id,final String titre, final Type type, final String url, final String guid, final Date datePublication, final String description, final String content, final String image) {
        super(id,titre,type);
        this.url=url;
        this.guid=guid;
        this.datePublication=datePublication;
        this.description=description;
        this.content=content;
        this.image=image;
    }



    public Blog(final String titre, final Type type, final String url, final String guid, final Date datePublication, final String description, final String content, final String image) {
        super(ID_DEFAULT,titre,type);
        this.url=url;
        this.guid=guid;
        this.datePublication=datePublication;
        this.description=description;
        this.content=content;
        this.image=image;
    }




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(Blog a) {
        if (this.getTitle().toLowerCase().equalsIgnoreCase(a.getTitle().toLowerCase())) {
            return this.getType().name().toLowerCase().compareToIgnoreCase(a.getType().name().toLowerCase());
        } else {
            return this.getTitle().toLowerCase().compareToIgnoreCase(a.getTitle().toLowerCase());
        }
    }
}
