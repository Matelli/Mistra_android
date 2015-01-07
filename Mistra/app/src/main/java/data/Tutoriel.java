package data;

import java.util.List;

/**
 * Created by matelli on 06/01/2015.
 */
public class Tutoriel extends Selection {

    private String description;
    private List<Selection> content;



    public Tutoriel(int id, String title, String type, String description,  List<Selection> content) {
        
        this.description = description;
        this.content = content;
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

    @Override
    public String toString() {
        return "Tutoriel{" +
                "description='" + description + '\'' +
                ", content=" + content +
                "} " + super.toString();
    }
}
