package com.simcoder.bimbo.instagram.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Profile.AccountSettingsActivity;
import com.simcoder.bimbo.instagram.Utils.FilePaths;
import com.simcoder.bimbo.instagram.Utils.FileSearch;
import com.simcoder.bimbo.instagram.Utils.GridImageAdapter;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";


    //constants
    private static final int NUM_GRID_COLUMNS = 3;


    //widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    //vars
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
        gridView = (GridView) view.findViewById(R.id.gridView);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
         if (mProgressBar != null) {
             mProgressBar.setVisibility(View.GONE);
             directories = new ArrayList<>();
             Log.d(TAG, "onCreateView: started.");

             ImageView shareClose = (ImageView) view.findViewById(R.id.ivCloseShare);
             if (shareClose != null) {
                 shareClose.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Log.d(TAG, "onClick: closing the gallery fragment.");
                 if (getActivity() != null){
                         getActivity().finish();
                     }}
                 });
             }
         }
        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);
         if (nextScreen != null) {
             nextScreen.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Log.d(TAG, "onClick: navigating to the final share screen.");

                     if (isRootTask()) {
                         if (getActivity() != null){
                         Intent intent = new Intent(getActivity(), NextActivity.class);
                        if (mSelectedImage != null){
                         intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                         startActivity(intent);
                     }} else {
                         Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                        if (mSelectedImage != null){
                         intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                         intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
                         startActivity(intent);
                         if (getActivity() != null) {
                             getActivity().finish();
                         }                     } }}

                 }
             });
         }
        init();

        return view;
    }

    private boolean isRootTask() {

            if (((ShareActivity) getActivity()).getTask() == 0) {
                return true;
            } else {
                return false;
            }
        }

    private void init() {
        FilePaths filePaths = new FilePaths();

        //check for other folders indide "/storage/emulated/0/pictures"
        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }
        if (filePaths.CAMERA != null) {
            directories.add(filePaths.CAMERA);
        }
        ArrayList<String> directoryNames = new ArrayList<>();
        if (directories != null) {
            for (int i = 0; i < directories.size(); i++) {
                Log.d(TAG, "init: directory: " + directories.get(i));
                if (directories.get(i) != null) {
                    int index = directories.get(i).lastIndexOf("/");
                    String string = directories.get(i).substring(index);
                    directoryNames.add(string);
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        if (adapter != null) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (directorySpinner != null){
            directorySpinner.setAdapter(adapter);
                   if (directorySpinner != null){
            directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      if (directories.get(position) !=null){
                    Log.d(TAG, "onItemClick: selected: " + directories.get(position));

                    //setup our image grid for the directory chosen
                    setupGridView(directories.get(position));
                }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }}}

    private void setupGridView(String selectedDirectory) {
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);

        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //set the grid column width
        if (getResources().getDisplayMetrics() != null) {
            int gridWidth = getResources().getDisplayMetrics().widthPixels;
           if (gridWidth != 0){
               if (NUM_GRID_COLUMNS !=0){
            int imageWidth = gridWidth / NUM_GRID_COLUMNS;
             if (gridView != null){
        if (imageWidth != 0){
            gridView.setColumnWidth(imageWidth);
             if (getActivity() != null){
          if (imgURLs != null){
            //use the grid adapter to adapter the images to gridview
            GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
            if (gridView != null){
                if (adapter != null) {
                    gridView.setAdapter(adapter);
                }
            //set the first image to be displayed when the activity fragment view is inflated
            try {
                if (imgURLs.get(0) != null) {
                    if (galleryImage != null) {
                        if (mAppend != null){
                        setImage(imgURLs.get(0), galleryImage, mAppend);
                         if (imgURLs.get(0) != null){
                        mSelectedImage = imgURLs.get(0);

                        }        }}}
                } catch(ArrayIndexOutOfBoundsException e){
                if (e.getMessage() != null){
                    Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " + e.getMessage());
                }}
            if (gridView != null){
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (imgURLs.get(position) != null){
                    Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));
                     if (galleryImage != null){
                         if (mAppend != null){
                    setImage(imgURLs.get(position), galleryImage, mAppend);
                    mSelectedImage = imgURLs.get(position);
                }}}}
            });

        }}
    }}}}}}}}

    private void setImage(String imgURL, ImageView image, String append){
        Log.d(TAG, "setImage: setting image");
     if (ImageLoader.getInstance() != null){
        ImageLoader imageLoader = ImageLoader.getInstance();
             if (imageLoader != null){
              if (append != null){
                  if (imgURL != null){
                      if (image != null){
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
              if(mProgressBar != null){
                mProgressBar.setVisibility(View.INVISIBLE);
            }}

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
               if (mProgressBar != null){
                mProgressBar.setVisibility(View.INVISIBLE);
            }}

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
               if (mProgressBar != null){
                mProgressBar.setVisibility(View.INVISIBLE);
            }}
        });
    }
}}}}}




}
