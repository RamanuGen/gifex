
package org.ramanugen.gifex.source.giphy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiphyImageVariations {
    @SerializedName("fixed_height_downsampled")
    @Expose
    private GiphyImageDetails imageDetails;

    public GiphyImageDetails getImageDetails() {
        return imageDetails;
    }

    public void setImageDetails(GiphyImageDetails imageDetails) {
        this.imageDetails = imageDetails;
    }
}
