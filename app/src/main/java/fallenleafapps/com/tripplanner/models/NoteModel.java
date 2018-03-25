package fallenleafapps.com.tripplanner.models;

/**
 * Created by Vargos on 25-Mar-18.
 */

public class NoteModel {
    int id;
    String body;
    boolean isFinished;

    public NoteModel() {
    }

    public NoteModel(int id, String body, boolean isFinished) {
        this.id = id;
        this.body = body;
        this.isFinished = isFinished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
