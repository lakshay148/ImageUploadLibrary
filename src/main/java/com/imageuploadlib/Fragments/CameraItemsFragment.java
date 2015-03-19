package com.imageuploadlib.Fragments;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.imageuploadlib.Activity.CameraActivity;
import com.imageuploadlib.Activity.GalleryActivity;
import com.imageuploadlib.Adapters.PhotosGridAdapter;
import com.imageuploadlib.Interfaces.UpdateSelection;
import com.imageuploadlib.R;
import com.imageuploadlib.Service.ImageUploadService;
import com.imageuploadlib.Utils.ApplicationController;
import com.imageuploadlib.Utils.Constants;
import com.imageuploadlib.Utils.FileInfo;
import com.imageuploadlib.Utils.PhotoUploadParams;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;

/**
 * Created by Lakshay on 13-02-2015.
 */
public class CameraItemsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener, UpdateSelection {

    private static final String TAG = "CameraFragment";
    private static final int CODE_CAMERA = 148;
    private static final int CODE_GALLERY = 256;
    private static final String UPLOADED_IMAGEGS = "alreadyUploaded";
    private static final String SELECTED_IMAGES = "alreadySelected";
    private int maxPhotos = 20;
    public static final String ADD_PHOTOS = "addPhotos";
    public static final String PHOTO_PARAMS = "photoParams";
    TextView tvCount;

    DynamicGridView gvPhotos;
    PhotosGridAdapter photosGridAdapter;

    PhotoUploadParams params;

    private ImagesHandler imagesHandler;

    public interface ImagesHandler{
        public void outputImages(ArrayList<FileInfo> files);
    }

    public static CameraItemsFragment newInstance(PhotoUploadParams params, ImagesHandler imagesHandler,ArrayList<?> uploadedImages )
    {
        CameraItemsFragment fragment = new CameraItemsFragment();
        fragment.imagesHandler = imagesHandler;
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTO_PARAMS, params);
        bundle.putSerializable(SELECTED_IMAGES, uploadedImages);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.camera_fragment,container,false);

        rootView.findViewById(R.id.addPhotoCamera).setOnClickListener(this);
        rootView.findViewById(R.id.addPhotoGallary).setOnClickListener(this);
        rootView.findViewById(R.id.done).setOnClickListener(this);
        tvCount = (TextView) rootView.findViewById(R.id.photosCount);


        params = (PhotoUploadParams) getArguments().getSerializable(PHOTO_PARAMS);
        ArrayList<?> alreadySelected = (ArrayList<?>) getArguments().getSerializable(SELECTED_IMAGES);
//        ArrayList<String> uploadedImages = getArguments().getStringArrayList(UPLOADED_IMAGEGS);
        if(alreadySelected!=null)
        {
            ArrayList<FileInfo> uploadedFiles = (ArrayList<FileInfo>) createInfos(alreadySelected);
            ApplicationController.selectedImages.addAll(uploadedFiles);
        }
        setUpPhotosGrid(rootView);
        if(params.getNoOfPhotos()>0)
            maxPhotos = params.getNoOfPhotos();

        return rootView;
    }

    private ArrayList<?> createInfos(ArrayList<?> uploadedImages) {
        if(uploadedImages.get(0) instanceof FileInfo)
            return uploadedImages;
        ArrayList<FileInfo> files = new ArrayList<FileInfo>();
        for(int i =0 ; i< uploadedImages.size();i++)
        {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilePath((String)uploadedImages.get(i));
            fileInfo.setType(FileInfo.FILE_TYPE.IMAGE);
            fileInfo.setSelected(true);
            fileInfo.setFromServer(true);
            files.add(fileInfo);
        }
        return files;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvCount.setText(ApplicationController.selectedImages.size()+"");
    }

    private void setUpPhotosGrid(View rootView) {
        gvPhotos = (DynamicGridView) rootView.findViewById(R.id.gvPhotos);
        photosGridAdapter = new PhotosGridAdapter(getActivity(), ApplicationController.selectedImages , 2 , this);
        gvPhotos.setAdapter(photosGridAdapter);
        gvPhotos.setOnItemLongClickListener(this);
        gvPhotos.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                gvPhotos.stopEditMode();
            }
        });
        gvPhotos.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ApplicationController.selectedImages.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case CODE_CAMERA:
                if(data!=null)
                {
                    ArrayList<FileInfo> list = (ArrayList<FileInfo>) data.getSerializableExtra(CameraActivity.CAMERA_IMAGES);
                    updateGrid(list,ADD_PHOTOS);
                }
                break;

            case CODE_GALLERY:
                Log.e(TAG , "Size of Selected Images : " + ApplicationController.selectedImages.size());
                photosGridAdapter.set(ApplicationController.selectedImages);
                photosGridAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void updateGrid(ArrayList<FileInfo> list,String action)
    {
        if(action.equals(ADD_PHOTOS))
        {
            ApplicationController.selectedImages.addAll(list);
            photosGridAdapter.set(ApplicationController.selectedImages);
            photosGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.addPhotoCamera)
        {
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            intent.putExtra(PHOTO_PARAMS, params);
            startActivityForResult(intent, CODE_CAMERA);
        }
        else if(v.getId()==R.id.addPhotoGallary)
        {
            Intent intent1 = new Intent(getActivity(), GalleryActivity.class);
            startActivityForResult(intent1, CODE_GALLERY);
        }
        else if(v.getId()==R.id.done)
        {
            if(ApplicationController.selectedImages.size()>maxPhotos)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Maximum photos can be : "+ maxPhotos, Toast.LENGTH_SHORT).show();
                return;
            }
//            Intent intent = new Intent(getActivity() , ImageUploadService.class);
//            intent.putExtra(Constants.IMAGES_TO_UPLOAD, ApplicationController.selectedImages);
//            getActivity().startService(intent);

            imagesHandler.outputImages(ApplicationController.selectedImages);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        gvPhotos.startEditMode(position);
        return true;
    }


    @Override
    public void updateSelected(FileInfo fileInfo, Boolean selected) {
        if(!selected)
        {
            for (FileInfo fileInfo1 :ApplicationController.selectedImages)
            {
                if(fileInfo1.getFilePath().equals(fileInfo.getFilePath()))
                {
                    ApplicationController.selectedImages.remove(fileInfo1);
                    break;
                }
            }
        }
    }
}

