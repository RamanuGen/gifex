
package org.ramanugen.gifex.source.giphy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GiphyResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<GiphyImage> listOfImages = new ArrayList<>();

    public ArrayList<GiphyImage> getListOfImages() {
        return listOfImages;
    }

    public void setListOfImages(ArrayList<GiphyImage> listOfImages) {
        this.listOfImages = listOfImages;
    }
}
