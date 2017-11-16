package org.ramanugen.gifex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ramanugen.gifex.R;
import org.ramanugen.gifex.model.ImageObject;
import org.ramanugen.gifex.view.GifImageView;

import java.util.List;

/**
 * Created by rbojja on 9/28/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private int placeHolderResId;

    public interface ItemSelectionListener{
        void onItemSelected(ImageObject imageObject);
    }
    private ItemSelectionListener selectionListener;

    private View.OnClickListener clickListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer)view.getTag(R.id.gifs_recycler_view);
            if(selectionListener != null){
                selectionListener.onItemSelected(items.get(position));
            }
        }
    });

    private final List<ImageObject> items;

    public GalleryAdapter(final List<ImageObject> items,
                          ItemSelectionListener selectionListener,
                          int placeHolderResId) {
        this.items = items;
        this.selectionListener = selectionListener;
        this.placeHolderResId = placeHolderResId;
    }

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(clickListener);
        return viewHolder;
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ImageObject image = items.get(position);
        holder.imageView.setImageDims(image.getWidth(),image.getHeight());

        if(placeHolderResId == 0){
            placeHolderResId = R.drawable.placeholder_picture_type;
        }

        holder.imageView.loadImageCenterCrop(image.getUrl(),placeHolderResId);
        holder.itemView.setTag(R.id.gifs_recycler_view,position);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public List<ImageObject> getItems() {
        return items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected GifImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (GifImageView) itemView.findViewById(R.id.gif_image_view);
        }
    }
}