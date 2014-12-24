package data;

/**
 * Created by matelli on 23/12/2014.
 */
public class Presentation {

    String type, title, content;
    int id;

    public Presentation(){

    }

    public Presentation(int id, String title,String type, String content){
        this.type = type;
        this.id= id;
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "Presentation{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                '}';
    }
}
