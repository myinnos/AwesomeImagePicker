package in.myinnos.awesomeimagepicker.activities;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import java.util.ArrayList;
import java.util.HashSet;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.adapter.CustomAlbumSelectAdapter;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Album;
import in.myinnos.awesomeimagepicker.models.MediaStoreType;

import static in.myinnos.awesomeimagepicker.R.anim.abc_fade_in;
import static in.myinnos.awesomeimagepicker.R.anim.abc_fade_out;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class AlbumSelectActivity extends HelperActivity {
    private ArrayList<Album> albums;

    private TextView errorDisplay, tvProfile;
    private LinearLayout liFinish;

    private ProgressBar loader;
    private GridView gridView;
    private CustomAlbumSelectAdapter adapter;

    private ActionBar actionBar;

    private ContentObserver observer;
    private Handler handler;
    private Thread thread;

    private final String[] albumProjection = new String[]{
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME
    };

    private final String[] mediaProjection = new String[] {
            MediaStore.MediaColumns._ID,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_album_select);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        ConstantsCustomGallery.limit = intent.getIntExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, ConstantsCustomGallery.DEFAULT_LIMIT);

        ConstantsCustomGallery.mediaStoreType = MediaStoreType.MIXED;
        if (intent.hasExtra(ConstantsCustomGallery.INTENT_EXTRA_MEDIASTORETYPE)) {
            MediaStoreType mediaStoreType = (MediaStoreType) intent.getSerializableExtra(ConstantsCustomGallery.INTENT_EXTRA_MEDIASTORETYPE);
            if (mediaStoreType != null) {
                ConstantsCustomGallery.mediaStoreType = mediaStoreType;
            }
        }

        errorDisplay = findViewById(R.id.text_view_error);
        errorDisplay.setVisibility(View.INVISIBLE);

        tvProfile = findViewById(R.id.tvProfile);
        tvProfile.setText(R.string.album_view);
        liFinish = findViewById(R.id.liFinish);

        loader = findViewById(R.id.loader);
        gridView = findViewById(R.id.grid_view_album_select);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MediaSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_ALBUM, albums.get(position).getName());
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_ALBUM_ID, albums.get(position).getId());
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        });

        liFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(abc_fade_in, abc_fade_out);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ConstantsCustomGallery.PERMISSION_GRANTED: {
                        loadAlbums();
                        break;
                    }

                    case ConstantsCustomGallery.FETCH_STARTED: {
                        loader.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.INVISIBLE);
                        break;
                    }

                    case ConstantsCustomGallery.FETCH_UPDATED: {
                        if (adapter == null) {
                            adapter = new CustomAlbumSelectAdapter(AlbumSelectActivity.this, getApplicationContext(),
                                    albums, ConstantsCustomGallery.mediaStoreType);
                            gridView.setAdapter(adapter);

                            loader.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            orientationBasedUI(getResources().getConfiguration().orientation);

                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    }

                    case ConstantsCustomGallery.ERROR: {
                        loader.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                return true;
            }
        });
        observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                loadAlbums();
            }
        };

        getContentResolver().registerContentObserver(ConstantsCustomGallery.getQueryUri(), false, observer);

        checkPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopThread();

        getContentResolver().unregisterContentObserver(observer);
        observer = null;

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(null);
        }
        albums = null;
        if (adapter != null) {
            adapter.releaseResources();
        }
        gridView.setOnItemClickListener(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);
    }

    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

//        if (adapter != null) {
//            int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 2 : metrics.widthPixels / 4;
//            adapter.setLayoutParams(size);
//        }
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        overridePendingTransition(abc_fade_in, abc_fade_out);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            default: {
                return false;
            }
        }
    }

    private void loadAlbums() {
        startThread(new AlbumLoaderRunnable());
    }

    private class AlbumLoaderRunnable implements Runnable {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            if (adapter == null) {
                sendMessage(ConstantsCustomGallery.FETCH_STARTED);
            }

            Uri externalContentUri = ConstantsCustomGallery.getQueryUri();

            String selection = null;
            if (ConstantsCustomGallery.mediaStoreType == MediaStoreType.MIXED) {
                selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                        + " OR "
                        + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
            }

            Cursor cursor = getApplicationContext().getContentResolver().query(externalContentUri, albumProjection,
                            selection, null, MediaStore.Video.Media.DATE_MODIFIED);

            if (cursor == null) {
                sendMessage(ConstantsCustomGallery.ERROR);
                return;
            }

            int itemCount = 0;
            int pageCount = 50;

            if (albums == null) {
                albums = new ArrayList<>();
            }
            albums.clear();

            HashSet<Long> albumSet = new HashSet<>();
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }

                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID));
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME));

                    if (!albumSet.contains(id)) {

                        Uri uri = getThumbnail(externalContentUri, id);

                        albums.add(new Album(id, name, uri));

                        /*if (!album.equals("Hiding particular folder")) {
                            temp.add(new Album(album, image));
                        }*/
                        albumSet.add(id);
                    }

                    itemCount++;

                    /*
                     * This will show thumbnails every time 50 items are loaded
                     */
                    if (cursor.isFirst() || itemCount > pageCount) {
                        sendMessage(ConstantsCustomGallery.FETCH_UPDATED);
                        itemCount = 0;
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();

            // adding taking photo from camera option!
            /*albums.add(new Album(getString(R.string.capture_photo),
                    "https://image.freepik.com/free-vector/flat-white-camera_23-2147490625.jpg"));*/

            sendMessage(ConstantsCustomGallery.FETCH_COMPLETED);
        }
    }

    private Uri getThumbnail(Uri externalContentUri, long albumId) {

        Uri uri = null;

        Cursor cursor = getContentResolver().query(externalContentUri, mediaProjection,
                MediaStore.MediaColumns.BUCKET_ID + " =?", new String[]{""+albumId}, MediaStore.MediaColumns.DATE_ADDED);

        if (cursor == null) {
            sendMessage(ConstantsCustomGallery.ERROR);
            return null;
        }

        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return null;
                }

                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));

                uri = ContentUris.withAppendedId(externalContentUri, id);

                try {
                    getContentResolver().openFileDescriptor(uri, "r");
                } catch (Exception e) {
                    // File doesn't actually exist
                    continue;
                }

                // We only need one thumbnail
                break;

            } while (cursor.moveToPrevious());
        }
        cursor.close();

        return uri;
    }

    private void startThread(Runnable runnable) {
        stopThread();
        thread = new Thread(runnable);
        thread.start();
    }

    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;
        }

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what) {
        if (handler == null) {
            return;
        }

        Message message = handler.obtainMessage();
        message.what = what;
        message.sendToTarget();
    }

    @Override
    protected void permissionGranted() {
        Message message = handler.obtainMessage();
        message.what = ConstantsCustomGallery.PERMISSION_GRANTED;
        message.sendToTarget();
    }

    @Override
    protected void hideViews() {
        loader.setVisibility(View.GONE);
        gridView.setVisibility(View.INVISIBLE);
    }
}
