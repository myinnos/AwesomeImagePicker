package in.myinnos.awesomeimagepicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.R;
import in.myinnos.awesomeimagepicker.models.Image;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by MyInnos on 03-11-2016.
 */
public class CustomImageSelectAdapter extends CustomGenericAdapter<Image> {

    public CustomImageSelectAdapter(Activity activity, Context context, ArrayList<Image> images) {
        super(activity, context, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_view_image_select, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_view_image_select);
            viewHolder.view = convertView.findViewById(R.id.view_alpha);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;

        viewHolder.view.getLayoutParams().width = size;
        viewHolder.view.getLayoutParams().height = size;

        if (arrayList.get(position).isSelected()) {
            viewHolder.view.setAlpha(0.5f);
            ((FrameLayout) convertView).setForeground(context.getResources().getDrawable(R.drawable.ic_done_white));

        } else {
            viewHolder.view.setAlpha(0.0f);
            ((FrameLayout) convertView).setForeground(null);
        }

        Uri uri = arrayList.get(position).getUri();

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

        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public View view;
    }

}
