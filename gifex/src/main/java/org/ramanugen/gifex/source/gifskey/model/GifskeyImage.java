
package org.ramanugen.gifex.source.gifskey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GifskeyImage {
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("images")
    @Expose
    private GifskeyImageVariations imageVariations;

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

    public GifskeyImageVariations getImageVariations() {
        return imageVariations;
    }

    public void setImageVariations(GifskeyImageVariations imageVariations) {
        this.imageVariations = imageVariations;
    }
}
