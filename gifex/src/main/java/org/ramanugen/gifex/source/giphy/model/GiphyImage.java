
package org.ramanugen.gifex.source.giphy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiphyImage {
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("images")
    @Expose
    private GiphyImageVariations imageVariations;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GiphyImageVariations getImageVariations() {
        return imageVariations;
    }

    public void setImageVariations(GiphyImageVariations imageVariations) {
        this.imageVariations = imageVariations;
    }
}
