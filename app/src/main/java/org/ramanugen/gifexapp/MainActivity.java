package org.ramanugen.gifexapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.ramanugen.gifex.adapter.GalleryAdapter;
import org.ramanugen.gifex.constants.GifSource;
import org.ramanugen.gifex.model.GifRequest;
import org.ramanugen.gifex.model.ImageObject;
import org.ramanugen.gifex.view.GifGalleryView;

public class MainActivity extends AppCompatActivity {
    private GifGalleryView giphySpace;
    private Button loadGifsBtn;
    private GalleryAdapter.ItemSelectionListener selectionListener = new GalleryAdapter.ItemSelectionListener() {
        @Override
        public void onItemSelected(ImageObject imageObject) {
            Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("text/plain");
            targetedShareIntent.putExtra(Intent.EXTRA_TEXT,"share gif "+"\n"+ imageObject.getUrl() +" ");
            Intent chooserIntent = Intent.createChooser(
                    targetedShareIntent,"Share Chooser");
            chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(chooserIntent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        giphySpace = (GifGalleryView) findViewById(R.id.giphy_space);
        loadGifsBtn = (Button) findViewById(R.id.load_gifs_btn);
        loadGifsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGifs();
            }
        });
//        loadGifsBtn.setVisibility(View.GONE); // uncomment to use only search gif functionality
        giphySpace.setDefaultRequest(makeGifRequest());
    }

    private GifRequest makeGifRequest(){
        GifRequest request = new GifRequest();
        request.maxLimit = 100;
        request.eachRequestLimit = 5;
        request.source = GifSource.GIPHY;
        request.apiKey = ""; // include API key
        return request;
    }

    private void loadGifs(){
        GifRequest request = new GifRequest();
        request.maxLimit = 100;
        request.keyword = "Funny";
        request.eachRequestLimit = 5;
        request.source = GifSource.GIFSKEY;
        request.apiKey = ""; // include API key
        giphySpace.loadInitialData(request,selectionListener);
    }
}
