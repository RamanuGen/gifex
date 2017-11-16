/* Copyright (C) 2012 The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.ramanugen.gifex.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import org.ramanugen.gifex.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom view to support easy movement from fresco to picasso or vise versa
 */
public class GifImageView extends android.support.v7.widget.AppCompatImageView {

    GlideDrawableImageViewTarget imageViewTarget =
            new GlideDrawableImageViewTarget(this) {
                @Override
                public void getSize(SizeReadyCallback cb) {
                    GifImageView.this.getSize(cb);
                }
            };

    private int viewWidth = -1, viewHeight = -1;
    private final List<SizeReadyCallback> cbs = new ArrayList<>();
    private int imageWidth;
    private int imageHeight;

    /**
     * Class constructor taking only a context. Use this constructor to create
     * {@link GifImageView} objects from your own code.
     *
     * @param context
     */
    public GifImageView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);

    }

    /**
     * Class constructor taking a context and an attribute set. This constructor
     * is used by the layout engine to construct a {@link GifImageView} from a set of
     * XML attributes.
     *
     * @param context
     * @param attrs   An attribute set which can contain attributes from
     *                {@link GifImageView} as well as attributes inherited
     *                from {@link View}.
     */
    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    private int findWidthForHeight(int imageWidth,int imageHeight,int viewHeight){
        float aspectRatio = ((float)imageWidth)/imageHeight;
        return (int) (aspectRatio * viewHeight);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        viewWidth = findWidthForHeight(imageWidth,imageHeight,viewHeight);
        setMeasuredDimension(viewWidth,viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        notifyCbs(viewWidth, viewHeight);
    }

    private void notifyCbs(int width, int height) {
        for (SizeReadyCallback cb : cbs) {
            cb.onSizeReady(width, height);
        }
        cbs.clear();
    }

    void getSize(SizeReadyCallback cb) {
        if (viewHeight > 0 && viewWidth > 0) {
            cb.onSizeReady(viewWidth, viewHeight);
        } else {
            if (!cbs.contains(cb)) {
                cbs.add(cb);
            }
        }
    }

    public GlideDrawableImageViewTarget getTarget() {
        return imageViewTarget;
    }

    public void loadImageCenterCrop(String imageUrl, int placeHolder) {
        setScaleType(ScaleType.CENTER_CROP);
        Glide.with(getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .error(R.drawable.error_placeholder)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.e("GiphyImageView","erro while loading image into gif image view",e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(getTarget());
    }

    public void setImageDims(int width, int height) {
        imageWidth = width;
        imageHeight = height;
    }
}
