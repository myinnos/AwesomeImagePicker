package in.myinnos.awesomeimagepicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
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

    public CustomAlbumSelectAdapter(Activity activity, Context context, ArrayList<Album> albums, MediaStoreType mediaStoreType) {
        super(activity, context, albums);

        this.mediaStoreType = mediaStoreType;
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

        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;

        viewHolder.textView.setText(arrayList.get(position).name);

        if (mediaStoreType == MediaStoreType.VIDEOS) {
            viewHolder.iconPlayView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iconPlayView.setVisibility(View.GONE);
        }

        if (arrayList.get(position).name.equals("Take Photo")) {

            /*
            Glide.with(context).load(arrayList.get(position).cover)
                    .placeholder(0xFFFF4081)
                    .override(200, 200)
                    .crossFade()
                    .centerCrop()
                    .into(viewHolder.imageView);
                    */
            Glide.with(context)
                    .load(arrayList.get(position).cover)
                    .apply(RequestOptions.placeholderOf(new ColorDrawable(0xFFFF4081)))
                    .apply(RequestOptions.overrideOf(200, 200))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(withCrossFade())
                    .into(viewHolder.imageView);
        } else {
            final Uri uri = Uri.fromFile(new File(arrayList.get(position).cover));
            /*
            Glide.with(context).load(uri)
                    .placeholder(0xFFFF4081)
                    .override(200, 200)
                    .crossFade()
                    .centerCrop()
                    .into(viewHolder.imageView);
                    */
            Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions.placeholderOf(new ColorDrawable(0xFFFF4081)))
                    .apply(RequestOptions.overrideOf(200, 200))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(withCrossFade())
                    .into(viewHolder.imageView);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        ImageView iconPlayView;
        TextView textView;
    }
}
