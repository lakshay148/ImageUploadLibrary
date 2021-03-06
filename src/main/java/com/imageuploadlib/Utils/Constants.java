package com.imageuploadlib.Utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Lakshay on 13-02-2015.
 */
public class Constants {

    public static final int TYPE_IMAGE = 1;
    private static final String ANALYTICS_FRAGMENTS = "AnalyticsFragment";
    public static final String OPEN_FRAGMENT = "openFragment";

    public static final String ANALYTICS = "Analytics";
    public static final String LISTVIEWS = "ListViews";
    public static final String CAMERA_ITEMS = "Camera";
    public static final String MAP_TYPE = "mapType";
    public static final String IMAGES_TO_UPLOAD = "imagesToUpload";
    public static String NAVIGATION_DRAWER = "Navigation Drawers";
    public static String NOTIFICATIONS = "Notifications";

    public static final String MAPS = "Maps";
    public static String[] allItems = new String[]{ANALYTICS,LISTVIEWS,NAVIGATION_DRAWER,MAPS,NOTIFICATIONS,CAMERA_ITEMS};

    public static HashMap<String,String> getItemsFragments()
    {
        HashMap<String,String> itemsFragments = new HashMap<>();
        itemsFragments.put(ANALYTICS,ANALYTICS_FRAGMENTS);
        return itemsFragments;
    }

    public static File getMediaOutputFile(int type)
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        if(type==TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        }
        else
            return null;
        return mediaFile;
    }

    public static String getImageUploadURL()
    {
        return "http://android.gaadi.com/imageUpload/image_upload.php";
    }
}
