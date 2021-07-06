package in.myinnos.awesomeimagepicker.helpers;

import android.net.Uri;
import android.provider.MediaStore;

import in.myinnos.awesomeimagepicker.models.MediaStoreType;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class ConstantsCustomGallery {
    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int PERMISSION_GRANTED = 1001;
    public static final int PERMISSION_DENIED = 1002;

    public static final int REQUEST_CODE = 2000;

    public static final int FETCH_STARTED = 2001;
    public static final int FETCH_COMPLETED = 2002;
    public static final int FETCH_UPDATED = 2003;
    public static final int ERROR = 2005;
    public static final int EMPTY_LIST = 2006;

    /**
     * Request code for permission has to be < (1 << 8)
     * Otherwise throws java.lang.IllegalArgumentException: Can only use lower 8 bits for requestCode
     */
    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 23;

    public static final String INTENT_EXTRA_ALBUM_ID = "albumId";
    public static final String INTENT_EXTRA_ALBUM = "album";
    public static final String INTENT_EXTRA_MEDIA = "media";
    public static final String INTENT_EXTRA_LIMIT = "limit";
    public static final String INTENT_EXTRA_MEDIASTORETYPE = "mediaStoreType";
    public static final int DEFAULT_LIMIT = 10;

    /*
     * Maximum number of media items that can be selected at a time
     */
    public static int limit = DEFAULT_LIMIT;

    //Type of media
    public static MediaStoreType mediaStoreType;

    public static Uri getQueryUri() {

        Uri queryUri;
        switch (mediaStoreType) {
            case MIXED:
                queryUri = MediaStore.Files.getContentUri("external");
                break;
            case VIDEOS:
                queryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                break;
            default:
                queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                break;
        }
        return queryUri;
    }
}
