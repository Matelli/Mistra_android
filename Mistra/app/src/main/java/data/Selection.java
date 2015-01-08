package data;

/**
 * Created by matelli on 07/01/2015.
 */
public class Selection {

    protected int id ;
    protected String title ;
    protected String type ;

    public Selection() {

    }

    public Selection(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Selection{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
