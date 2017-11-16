package org.ramanugen.gifex.source.gifskey.transport;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.ramanugen.gifex.source.gifskey.model.GifskeyImage;
import org.ramanugen.gifex.source.gifskey.model.GifskeyResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GifskeyResponseDeserializer implements JsonDeserializer<GifskeyResponse> {
    @Override
    public GifskeyResponse deserialize(JsonElement json,
                                       Type typeOfT,
                                       JsonDeserializationContext context)
            throws JsonParseException {

        GifskeyResponse response = new GifskeyResponse();
        JsonObject responseObject = json.getAsJsonObject();

        JsonArray gifskiesArray = responseObject.getAsJsonArray("data");
        if((gifskiesArray != null) && (gifskiesArray.size() > 0)){
            ArrayList<GifskeyImage> gifskiesList = new ArrayList<>(gifskiesArray.size());
            for (JsonElement element : gifskiesArray ) {
                GifskeyImage giphy =  context.deserialize(element, GifskeyImage.class);
                gifskiesList.add(giphy);
            }
            response.setListOfImages(gifskiesList);
        }
        return response;
    }
}
