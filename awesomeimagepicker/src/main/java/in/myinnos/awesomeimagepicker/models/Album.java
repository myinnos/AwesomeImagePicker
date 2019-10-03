package in.myinnos.awesomeimagepicker.models;

import android.net.Uri;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class Album {
    private long id;
    private String name;
    private Uri uri;

    public Album(long id, String name, Uri uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
