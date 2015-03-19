package com.imageuploadlib.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.imageuploadlib.R;
import com.imageuploadlib.Utils.CommonUtils;
import com.imageuploadlib.Utils.Constants;
import com.imageuploadlib.Utils.FileInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Lakshay on 16-03-2015.
 */
public class ImageUploadService extends IntentService {
    private static final String TAG = "ImageUploadService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private ArrayList<FileInfo> imagesList ;
    public ImageUploadService() {
        super("Image Upload Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Toast.makeText(getApplicationContext(), "Image Upload Started", Toast.LENGTH_SHORT).show();
        imagesList = (ArrayList<FileInfo>) intent.getSerializableExtra(Constants.IMAGES_TO_UPLOAD);
        for (int i = 0; i<imagesList.size(); i++)
        {
            Log.e(TAG , "image being uploaded : "+ i );
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext httpContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(Constants.getImageUploadURL());
            MultipartEntity multipartContent = new MultipartEntity();
            FileBody fileBody = new FileBody(new File(imagesList.get(i).getFilePath()));
            try {
                multipartContent.addPart("source", new StringBody("Android GCloud App"));
                String extension = imagesList.get(i).getFilePath().substring(imagesList.get(i).getFilePath().lastIndexOf("."));
                multipartContent.addPart("file_name", new StringBody("used_car_" + i + "_"  + extension)); // ,"image/jpeg"
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            multipartContent.addPart("stockImg", fileBody);

            Log.e("MultiPartcontent ", multipartContent.toString());
            httpPost.setEntity(multipartContent);
            CommonUtils.createNotification(getApplicationContext(), R.drawable.image_load_default_small,"Image Upload Lib" ,"Image being uploaded", new Intent(), 1);
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost, httpContext);
                String responses = EntityUtils.toString(response.getEntity());

                Log.e(TAG , responses.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
