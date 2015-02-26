package data;

import java.util.List;

/**
 * Created by matelli on 06/01/2015.
 */
public class Tutoriel<T> extends Selection {

    private String description;
    private List<Selection> content;
    //Null si c'est le tutoriel mère. Sinon, nous sommes un tutoriel "enfant" (un tutoriel de tutoriel) et on aura l'id de notre mère
    private long parent;

    public static final int NO_PARENT_VALUE = -1;



   /* public Tutoriel(int id, String title, Type type, String description,  List<Selection> content, long parent) {
        super(id,title,type);
        this.description = description;
        this.content = content;
        this.parent = parent;
    } */

    public Tutoriel(int id, String title, Type type, String description,  List<T> content, long parent) {
        super(id,title,type);
        this.description = description;
        this.content = (List<Selection>)content;
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Selection> getContent() {
        return content;
    }
    public void setContent(List<Selection> content) {
        this.content = content;
    }
    public void addContent(Selection content) {
        this.content.add(content);
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    /**
     * Méthode qui permet de savoir si nous somme un tutoriel principal, c-a-d, sans parent !
     * @return
     */
    public boolean isMainTutorial() {
        if(parent == NO_PARENT_VALUE && type.equals(Type.CATEGORIE))
           return true;
        return false;
    }

    @Override
    public String toString() {
        return "Tutoriel{" +
                "description='" + description + '\'' +
                ", parent=" + parent +
                ", content=" + content +
                "} " + super.toString();
    }
}
