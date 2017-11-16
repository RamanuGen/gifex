
package org.ramanugen.gifex.source.giphy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiphyImageDetails {

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("mp4")
    @Expose
    private String mp4;

    @SerializedName("width")
    @Expose
    private int width;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("size")
    @Expose
    private int size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMp4() {
        return mp4;
    }

    public void setMp4(String mp4) {
        this.mp4 = mp4;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
