package org.ramanugen.gifex.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.ramanugen.gifex.Gifex;
import org.ramanugen.gifex.R;
import org.ramanugen.gifex.adapter.GalleryAdapter;
import org.ramanugen.gifex.controller.GifFetcher;
import org.ramanugen.gifex.itemdecoration.SpacesItemDecoration;
import org.ramanugen.gifex.model.GifInternalRequest;
import org.ramanugen.gifex.model.GifRequest;
import org.ramanugen.gifex.model.ImageObject;
import org.ramanugen.gifex.utils.ResourceUtils;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by rbojja on 9/27/2017.
 */

public class GifGalleryView extends LinearLayout {
    private static final int MAX_RETRY_COUNT = 2;

    private FrameLayout galleryContainer;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private TextView noResultTextView;

    private LinearLayoutManager layoutManager;

    private ArrayList<ImageObject> items;

    private GifRequest request;

    private int indexToLoadFrom;
    private int retryCount;
    private boolean isRequestInProgress;
    private int placeHolderResId;
    private int padding;
    private int searchBarHeight;


    public GifGalleryView(Context context) {
        super(context);
        init(context,null);
    }

    public GifGalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public GifGalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private GalleryAdapter.ItemSelectionListener selectionListener;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        readValues();
    }

    private void readValues() {
        padding = (int) getResources().getDimension(R.dimen.gallery_view_padding);
        searchBarHeight = (int)getResources().getDimension(R.dimen.gifs_search_bar_height);
    }

    // TODO Min height should 148dp to accommodate a fixed height of 40dp and 4dp,4dp paddings
    // Remaining height will be used for the gallery list
	// TODO provide a config for image load option - center_crop, fit_center etc
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int requestedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int requestedHeight = MeasureSpec.getSize(heightMeasureSpec);

        int heightRemaining = requestedHeight;
        int heightOccupied = 0;

        if(searchBar.getVisibility() != GONE){
            int widthMSpecSearchBar = MeasureSpec.makeMeasureSpec(requestedWidth,MeasureSpec.EXACTLY);
            int heightMSpecSearchBar = MeasureSpec.makeMeasureSpec(searchBarHeight,MeasureSpec.EXACTLY);

            searchBar.measure(widthMSpecSearchBar,heightMSpecSearchBar);

            heightRemaining -= searchBarHeight;
            heightOccupied += searchBarHeight;
        }

        if(galleryContainer.getVisibility() != GONE) {
            int widthMSpecGalleryContainer = MeasureSpec.makeMeasureSpec(requestedWidth, MeasureSpec.EXACTLY);
            int heightMSpecGalleryContainer = MeasureSpec.makeMeasureSpec(
                    heightRemaining - 2 * ResourceUtils.dpToPx(getContext(), padding),
                    MeasureSpec.EXACTLY);

            galleryContainer.measure(widthMSpecGalleryContainer, heightMSpecGalleryContainer);
            heightOccupied += heightRemaining;
        }

        setMeasuredDimension(requestedWidth,heightOccupied);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        int parentWidth = getWidth();
//        int parentHeight = getHeight();
//
//        Rect parentRect = new Rect(0,0,parentWidth,parentHeight);
//        Rect childRect = new Rect();
//        Gravity.apply(Gravity.CENTER_VERTICAL,parentWidth,parentHeight,parentRect,childRect);
//
//        recyclerView.layout(childRect.left,childRect.top,childRect.right,childRect.bottom);
    }

    private void init(Context context, AttributeSet attrs) {
        readAttributes(attrs);
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.gif_gallery_view, this, true);
        setOrientation(VERTICAL);

        galleryContainer = (FrameLayout)findViewById(R.id.gifs_gallery_container);
        recyclerView = (RecyclerView)findViewById(R.id.gifs_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.gifs_progress_bar);
        searchBar = (EditText)findViewById(R.id.gifs_search_bar);
        noResultTextView = (TextView)findViewById(R.id.gifs_no_result_view);

        if(!Gifex.shouldShowSearchBar()){
            searchBar.setVisibility(GONE);
        }else{
            searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    performSearch();
                    return true;
                }
            });
        }

        items = new ArrayList<>();
        initRecyclerView(context);
    }

    private void readAttributes(AttributeSet attrs) {
        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GifGalleryView,
                0, 0
        );

        try {
            placeHolderResId =
                    attributes.getResourceId(
                            R.styleable.GifGalleryView_gif_place_holder, 0);
        } finally {
            // release the TypedArray so that it can be reused.
            attributes.recycle();
        }

    }

    private void initRecyclerView(Context context) {
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(ResourceUtils.dpToPx(getContext(),10)));
        recyclerView.setAdapter(new GalleryAdapter(items,selectionListener,placeHolderResId));
    }

    @NonNull
    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(request.eachRequestLimit, layoutManager) {
            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                if(!isRequestInProgress){
                    if (items.size() >= request.maxLimit) {
                        progressBar.setVisibility(View.GONE);
                    }else {
                        requestAndLoadData(firstVisibleItemPosition);
                    }
                }
            }
        };
    }

    public void performSearch(){
        request.keyword = searchBar.getText().toString();
        loadInitialData(request,this.selectionListener);
    }

    public void setDefaultRequest(GifRequest request){
        this.request = new GifRequest(request);
    }

    public void loadInitialData(GifRequest request, GalleryAdapter.ItemSelectionListener selectionListener){
        this.request = new GifRequest(request);
        this.selectionListener = selectionListener;
        indexToLoadFrom = 0;
        isRequestInProgress = false;
        recyclerView.addOnScrollListener(createInfiniteScrollListener());
        items.clear();
        recyclerView.setAdapter(new GalleryAdapter(this.items,selectionListener,placeHolderResId));
        requestAndLoadData(0);
    }

    private void refreshRecyclerView(RecyclerView view, RecyclerView.Adapter adapter, int position) {
        view.setAdapter(adapter);
        view.invalidate();
        view.scrollToPosition(position);
    }

    private void prefetchImages(ArrayList<ImageObject> imageObjects){
        for(ImageObject object: imageObjects){
            int sizeOriginal = com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;
            Glide.with(getContext().getApplicationContext())
                    .load(object.getUrl())
                    .downloadOnly(sizeOriginal,sizeOriginal);
        }
    }

    private void requestAndLoadData(final int firstVisibleItemPosition){
        if(request == null){
            throw new IllegalArgumentException("Please set request: use setDefaultRequest" +
                    " in case of search or loadInitialData for normal use");
        }
        if(request.apiKey == null){
            throw new IllegalArgumentException("Please provide api key as part of the request");
        }
        if(request.keyword == null){
            throw new IllegalArgumentException("Please provide a keyword as part of the request");
        }

        isRequestInProgress = true;
        progressBar.setVisibility(View.VISIBLE);
        GifInternalRequest internalRequest = new GifInternalRequest(request);
        internalRequest.offset = indexToLoadFrom;
        GifFetcher.getGifs(internalRequest)
                .map(new Func1<ArrayList<ImageObject>, ArrayList<ImageObject>>() {
                    @Override
                    public ArrayList<ImageObject> call(ArrayList<ImageObject> imageObjects) {
                        if(Gifex.shouldPrefetchImages()){
                            prefetchImages(imageObjects);
                        }
                        return imageObjects;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<ImageObject>>() {
                    @Override
                    public void call(ArrayList<ImageObject> detailsList) {
                        progressBar.setVisibility(View.GONE);
                        if((detailsList != null) && !detailsList.isEmpty()) {
                            Log.d("Gifex", "size of urls " + detailsList.size());
                            noResultTextView.setVisibility(GONE);
                            int lastItemPosition = items.size() - 1;
                            items.addAll(detailsList);
                            indexToLoadFrom = items.size();
                            refreshRecyclerView(recyclerView,
                                    new GalleryAdapter(items, selectionListener, placeHolderResId), lastItemPosition);
                            retryCount = 0;
                            isRequestInProgress = false;
                        }else{
                            Log.e("Gifex","no results found with keyword : "+request.keyword);
                            noResultTextView.setVisibility(VISIBLE);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("Gifex", "requestAndLoadData error", throwable);
                        if(retryCount <= MAX_RETRY_COUNT){
                            retryCount++;
                            requestAndLoadData(firstVisibleItemPosition);
                        }else {
                            retryCount = 0;
                            progressBar.setVisibility(View.GONE);
                            isRequestInProgress = false;
                        }
                    }
                });
    }
}