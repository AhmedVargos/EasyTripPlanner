package fallenleafapps.com.tripplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vargos on 25-Mar-18.
 */
public class NoteModel implements Parcelable {
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

    protected NoteModel(Parcel in) {
        id = in.readInt();
        body = in.readString();
        isFinished = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(body);
        dest.writeByte((byte) (isFinished ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NoteModel> CREATOR = new Parcelable.Creator<NoteModel>() {
        @Override
        public NoteModel createFromParcel(Parcel in) {
            return new NoteModel(in);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };
}