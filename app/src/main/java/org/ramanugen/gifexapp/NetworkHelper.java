package org.ramanugen.gifexapp;

import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class NetworkHelper {
    private static OkHttpClient extOkHttpClient;

    public static OkHttpClient getExternalOkHttpClient() {
        if (extOkHttpClient == null) {
            OkHttpClient.Builder builder = getDefaultBuilder();
            extOkHttpClient = builder.build();
        }
        return extOkHttpClient;
    }

    public static OkHttpClient.Builder getDefaultBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // Forcing http 1.1
        builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        return builder;
    }


}
