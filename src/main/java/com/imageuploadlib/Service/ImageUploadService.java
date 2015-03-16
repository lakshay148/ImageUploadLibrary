package com.imageuploadlib.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
    public ImageUploadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

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
//            Log.e("Source , file_name , car_id ,api_key , ucdid , username ,SERVICE_EXECUTIVE_LOGIN , APP_VERSION", new StringBody(String.valueOf(CommonUtils.getIntSharedPreference(this, Constants.UC_DEALER_ID, -1))) + "," + new StringBody(CommonUtils.getStringSharedPreference(this, Constants.UC_DEALER_USERNAME, "")) + "," + new StringBody(String.valueOf(CommonUtils.getBooleanSharedPreference(this, Constants.SERVICE_EXECUTIVE_LOGIN, false))) + "," + new StringBody(CommonUtils.getStringSharedPreference(this, Constants.APP_VERSION_CODE, "")));
            httpPost.setEntity(multipartContent);
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
