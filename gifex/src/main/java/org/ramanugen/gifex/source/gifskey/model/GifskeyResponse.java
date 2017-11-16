
package org.ramanugen.gifex.source.gifskey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GifskeyResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<GifskeyImage> listOfImages = new ArrayList<>();

    public ArrayList<GifskeyImage> getListOfImages() {
        return listOfImages;
    }

    public void setListOfImages(ArrayList<GifskeyImage> listOfImages) {
        this.listOfImages = listOfImages;
    }
}
