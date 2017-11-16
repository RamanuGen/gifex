package org.ramanugen.gifexapp;

import android.app.Application;

import org.ramanugen.gifex.Gifex;

/**
 * Created by rbojja on 9/28/2017.
 */
// Refer
// using https://github.com/pwittchen/InfiniteScroll/
// explored https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
public class GiphiesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Gifex.init(new Gifex.Configuration()
                .httpClient(NetworkHelper.getExternalOkHttpClient())
                .diskCacheLimit(300*1024*1024/*Read it from build config or some constant*/)
                .prefetchImages(true)
                .showSearchBar(true)); // make it part of gif gallery view
    }
}

