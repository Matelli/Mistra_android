package data;

/**
 * Created by matelli on 23/12/2014.
 */
public class Presentation extends Selection implements Comparable<Presentation> {

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

    @Override
    public int compareTo(Presentation p) {
        if (this.getTitle().toLowerCase().equalsIgnoreCase(p.getTitle().toLowerCase())) {
            return this.getType().toLowerCase().compareToIgnoreCase(p.getType().toLowerCase());
        } else {
            return this.getTitle().toLowerCase().compareToIgnoreCase(p.getTitle().toLowerCase());
        }
    }

}
