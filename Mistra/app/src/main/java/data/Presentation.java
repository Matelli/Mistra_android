package data;

/**
 * Created by matelli on 23/12/2014.
 */
public class Presentation extends Selection {

    String  content;

    public Presentation(){

    }

    public Presentation(int id, String title,String type, String content){
        super(id,title,type);
        this.content = content;

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
