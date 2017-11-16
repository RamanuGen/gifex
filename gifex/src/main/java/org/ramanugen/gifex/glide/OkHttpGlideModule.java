package org.ramanugen.gifex.glide;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import org.ramanugen.gifex.Gifex;
import org.ramanugen.gifex.constants.Constants;

import java.io.InputStream;

public class OkHttpGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int clientDiskCacheLimit = Gifex.getDiskCacheLimit();
        int limit = (clientDiskCacheLimit > 0)?
                        clientDiskCacheLimit : Constants.DEFAULT_DISK_CACHE_SIZE_GLIDE_MB;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, limit));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(Gifex.getHttpClient()));
    }
}
