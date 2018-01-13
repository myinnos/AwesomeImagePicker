package in.myinnos.awesomeimagepicker.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class Video implements Parcelable {
    public long id;
    public String name;
    public String duration;
    public String path;
    public boolean isSelected;

    public Video(long id, String name, String duration, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.path = path;
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(duration);
        dest.writeString(path);
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    private Video(Parcel in) {
        id = in.readLong();
        name = in.readString();
        duration = in.readString();
        path = in.readString();
    }
}
