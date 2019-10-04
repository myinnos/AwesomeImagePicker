package in.myinnos.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Media;
import in.myinnos.awesomeimagepicker.models.MediaStoreType;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 5;

    private ImageView imageView;
    private Button chooseImage;
    private Button chooseVideo;
    private Button chooseMixed;
    private TextView txImageSelects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txImageSelects = findViewById(R.id.txImageSelects);
        imageView = findViewById(R.id.imageView);
        chooseImage = findViewById(R.id.chooseImage);
        chooseVideo = findViewById(R.id.chooseVideo);
        chooseMixed = findViewById(R.id.chooseMixed);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose(MediaStoreType.IMAGES);
            }
        });

        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose(MediaStoreType.VIDEOS);
            }
        });

        chooseMixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose(MediaStoreType.MIXED);
            }
        });
    }

    private void choose(MediaStoreType type) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Helper.checkPermissionForExternalStorage(MainActivity.this)) {
                Helper.requestStoragePermission(MainActivity.this, READ_STORAGE_PERMISSION);
            } else {
                // opining custom gallery
                Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_MEDIASTORETYPE, type);
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        } else {
            Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
            intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
            intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_MEDIASTORETYPE, type);
            startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Media> media = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_MEDIA);

            txImageSelects.setText("");

            for (int i=0; i<media.size(); i++) {

                Uri uri = media.get(i).getUri();

                /*
                Glide.with(this).load(uri)
                        .placeholder(0xFFFF4081)
                        .override(400, 400)
                        .crossFade()
                        .centerCrop()
                        .into(imageView);
                        */
                Glide.with(this)
                        .load(uri)
                        .apply(RequestOptions.placeholderOf(new ColorDrawable(0xFFFF4081)))
                        .apply(RequestOptions.overrideOf(400, 400))
                        .apply(RequestOptions.centerCropTransform())
                        .transition(withCrossFade())
                        .into(imageView);

                txImageSelects.setText(txImageSelects.getText().toString().trim()
                        + "\n" +
                        String.valueOf(i + 1) + ". " + String.valueOf(uri));
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.github:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/myinnos")));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
