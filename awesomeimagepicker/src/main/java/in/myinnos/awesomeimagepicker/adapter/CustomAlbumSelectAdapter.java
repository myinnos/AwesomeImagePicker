package in.myinnos.awesomeimagepicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.models.Album;
import in.myinnos.awesomeimagepicker.models.MediaStoreType;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class CustomAlbumSelectAdapter extends CustomGenericAdapter<Album> {

    private MediaStoreType mediaStoreType;
    private float radius = 1;

    public CustomAlbumSelectAdapter(Activity activity, Context context, ArrayList<Album> albums, MediaStoreType mediaStoreType) {
        super(activity, context, albums);

        this.mediaStoreType = mediaStoreType;

        radius = context.getResources().getDimension(R.dimen.dp10);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_view_item_album_select, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_album_image);
            viewHolder.iconPlayView = convertView.findViewById(R.id.image_view_icon_play);
            viewHolder.textView = convertView.findViewById(R.id.text_view_album_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (arrayList.get(position) != null) {

            Album album = arrayList.get(position);

            viewHolder.textView.setText(album.getName());

            viewHolder.iconPlayView.setVisibility(View.GONE);

            Glide.with(context)
                    .load(album.getUri())
                    .apply(RequestOptions.placeholderOf(new ColorDrawable(0xFFf2f2f2)))
                    .apply(RequestOptions.overrideOf(200, 200))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(withCrossFade())
                    .into(viewHolder.imageView);
        }

        viewHolder.imageView.setShapeAppearanceModel(viewHolder.imageView.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build());

        return convertView;
    }

    private static class ViewHolder {
        ShapeableImageView imageView;
        ImageView iconPlayView;
        TextView textView;
    }
}
