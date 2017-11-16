package org.ramanugen.gifex;

import org.ramanugen.gifex.factory.AdapterFactory;

import okhttp3.OkHttpClient;

/**
 * Created by rbojja on 10/17/2017.
 */
// Consider Pictor, Giftor, Gifhub, Giffera, Gifex, Gifspace, higallery, LiteGallery, parshvi
public class Gifex {
    public static class Configuration {
        public int diskCacheLimit;
        public OkHttpClient httpClient;
        public boolean prefetchImages;
        public boolean showSearchBar;

        public Configuration diskCacheLimit(int diskCacheLimit){
            this.diskCacheLimit = diskCacheLimit;
            return this;
        }

        public Configuration httpClient(OkHttpClient client){
            this.httpClient = client;
            return this;
        }

        public Configuration prefetchImages(boolean prefetchImages){
            this.prefetchImages = prefetchImages;
            return this;
        }

        public Configuration showSearchBar(boolean showSearchBar){
            this.showSearchBar = showSearchBar;
            return this;
        }
    }
    private static Configuration config;

    public static void init(Configuration configuration){
        config = configuration;
        AdapterFactory.init(config.httpClient);
    }

    public static OkHttpClient getHttpClient() {
        return config.httpClient;
    }
    public static int getDiskCacheLimit(){return config.diskCacheLimit;}
    public static boolean shouldPrefetchImages(){return config.prefetchImages;}
    public static boolean shouldShowSearchBar(){return config.showSearchBar;}
}
