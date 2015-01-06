package data;

import java.util.List;

/**
 * Created by matelli on 06/01/2015.
 */
public class Tutoriel extends Presentation {

    private String description;
    private List<Presentation> listSubTuto;

    public Tutoriel() {
        super.setContent(null);
    }

    public Tutoriel(int id, String title, String type, String description,  List<Presentation> listSubTuto) {
        super(id, title, type, null);
        this.description = description;
        this.listSubTuto = listSubTuto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Presentation> getListSubTuto() {
        return listSubTuto;
    }

    public void setListSubTuto(List<Presentation> listSubTuto) {
        this.listSubTuto = listSubTuto;
    }

    @Override
    public String toString() {
        return "Tutoriel{" +  super.toString() +
                "description='" + description + '\'' +
                ", listSubTuto=" + listSubTuto +
                "} " ;
    }
}
