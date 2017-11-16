
package org.ramanugen.gifex.source.gifskey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GifskeyImageVariations {
    @SerializedName("fixed_height")
    @Expose
    private GifskeyImageDetails imageDetails;

    public GifskeyImageDetails getImageDetails() {
        return imageDetails;
    }

    public void setImageDetails(GifskeyImageDetails imageDetails) {
        this.imageDetails = imageDetails;
    }
}
