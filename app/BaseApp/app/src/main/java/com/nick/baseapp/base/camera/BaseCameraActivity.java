package com.nick.baseapp.base.camera;

import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nick.baseapp.BuildConfig;
import com.nick.baseapp.R;
import com.nick.baseapp.base.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaseCameraActivity extends AppCompatActivity implements View.OnClickListener, Camera.PictureCallback, TextureView.SurfaceTextureListener {
    private CameraParamList mListCameraPraramKey;
    private CameraParamList mListCameraPraramValue;

    private static String mPathDirTakePicture;
    private ImageView mIVAlbum;
    private ImageView mIVSettings;
    private static final String KEY_PATH_SHOT = "path_shot";
    private boolean mIsShowCameraParamList;
    private int mIndexCameraParam;
    private static final int INDEX_CAMERA_PREVIEW = 0;
    private static final int INDEX_CAMERA_PICTURE = 1;
    private static final int INDEX_WINDOW_SIZE = 2;

    private static final int INDEX_FOCOUS = 3;
    private static final int INDEX_FLASH = 4;
    private static final int INDEX_EXPOSURE = 5;
    private static final int INDEX_SCENE = 6;
    private static final int INDEX_ROTATION = 7;
    private static final int INDEX_WHITE_BALANCE = 8;
    private static final int INDEX_RATE = 9;
    private static final int INDEX_COLOR_EFFECT = 10;
    private static final int INDEX_ZOOM = 11;


    private BaseCameraView mCameraView;
    private int mIndexWindowSize;


    private static final String TAG = BaseCameraActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_camera);

        mCameraView = findViewById(R.id.mCameraView);
        findViewById(R.id.mFLBack).setOnClickListener(this);
        LinearLayout mLLToolBar = findViewById(R.id.mLLToolBar);
        ViewGroup.LayoutParams mLLToolBarP = mLLToolBar.getLayoutParams();
        mLLToolBarP.height = getResources().getDisplayMetrics().heightPixels / 4;
        mLLToolBar.setLayoutParams(mLLToolBarP);
        findViewById(R.id.mFLShot).setOnClickListener(this);
        findViewById(R.id.mFLAlbum).setOnClickListener(this);
        findViewById(R.id.mFLSettings).setOnClickListener(this);
        mIVSettings = findViewById(R.id.mIVSettings);
        mIVAlbum = findViewById(R.id.mIVAlbum);
        mListCameraPraramKey = findViewById(R.id.mListCameraPraramKey);
        mListCameraPraramValue = findViewById(R.id.mListCameraPraramValue);
        CameraParamList.MyData itemPreview = new CameraParamList.MyData(getResources().getString(R.string.preview_size), true);
        CameraParamList.MyData itemPicture = new CameraParamList.MyData(getResources().getString(R.string.picture_size), false);
        CameraParamList.MyData itemWindow = new CameraParamList.MyData(getResources().getString(R.string.window_size), false);

        CameraParamList.MyData itemFocus = new CameraParamList.MyData(getResources().getString(R.string.focus), false);
        CameraParamList.MyData itemFlash = new CameraParamList.MyData(getResources().getString(R.string.flash), false);
        CameraParamList.MyData itemExposure = new CameraParamList.MyData(getResources().getString(R.string.exposure), false);
        CameraParamList.MyData itemScence = new CameraParamList.MyData(getResources().getString(R.string.scence), false);
        CameraParamList.MyData itemRotation = new CameraParamList.MyData(getResources().getString(R.string.rotation), false);
        CameraParamList.MyData itemWhiteBalance = new CameraParamList.MyData(getResources().getString(R.string.white_balance), false);
        CameraParamList.MyData itemRate = new CameraParamList.MyData(getResources().getString(R.string.rate), false);
        CameraParamList.MyData itemColorEffect = new CameraParamList.MyData(getResources().getString(R.string.color_effect), false);
        CameraParamList.MyData itemZoom = new CameraParamList.MyData(getResources().getString(R.string.zoom), false);

        List<CameraParamList.MyData> datas = new ArrayList<>();
        datas.add(itemPreview);
        datas.add(itemPicture);
        datas.add(itemWindow);

        datas.add(itemFocus);
        datas.add(itemFlash);
        datas.add(itemExposure);
        datas.add(itemScence);
        datas.add(itemRotation);
        datas.add(itemWhiteBalance);
        datas.add(itemRate);
        datas.add(itemColorEffect);
        datas.add(itemZoom);

        mListCameraPraramKey.init(datas, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndexCameraParam = (Integer) v.getTag();
                initCameraParamValueList();
            }
        });

        BaseCameraView.setmSurfaceTextureListener(this);
        setAlbum();

    }

    private void setAlbum() {
        mPathDirTakePicture = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(), getString(R.string.app_name)).getPath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outHeight = Utils.dip2px(this, 48);
        options.outWidth = Utils.dip2px(this, 48);
        String pathLastAlbum = PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_PATH_SHOT, "");
        if (new File(pathLastAlbum).exists()) {
            mIVAlbum.setImageBitmap(BitmapFactory.decodeFile(pathLastAlbum));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mFLBack:
                finish();
                break;
            case R.id.mFLAlbum:
                break;
            case R.id.mFLSettings:
                mIsShowCameraParamList = !mIsShowCameraParamList;
                if (mIsShowCameraParamList) {
                    mListCameraPraramValue.setVisibility(View.VISIBLE);
                    mListCameraPraramKey.setVisibility(View.VISIBLE);
                    mIVSettings.setImageResource(R.drawable.ic_settings_on);
                } else {
                    mListCameraPraramValue.setVisibility(View.INVISIBLE);
                    mListCameraPraramKey.setVisibility(View.INVISIBLE);
                    mIVSettings.setImageResource(R.drawable.ic_settings);
                }
                break;
            case R.id.mFLShot:
                BaseCameraView.takePicture(this, this);
                break;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        initCameraParamValueList();
    }

    private void initCameraParamValueList() {
        switch (mIndexCameraParam) {
            case INDEX_CAMERA_PREVIEW:
                List<Camera.Size> listPreviewSize = BaseCameraView.getmListPreviewSize();
                int previewWidth = BaseCameraView.getmSizePreview().width;
                int previewHeight = BaseCameraView.getmSizePreview().height;
                List<CameraParamList.MyData> datasPreview = new ArrayList<>();
                for (int i = 0; i < listPreviewSize.size(); i++) {
                    int itemWidth = listPreviewSize.get(i).width;
                    int itemHeight = listPreviewSize.get(i).height;
                    boolean isCheck = false;
                    if (previewHeight == itemHeight && previewWidth == itemWidth) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(itemWidth + "x" + itemHeight, isCheck);
                    datasPreview.add(item);
                }
                mListCameraPraramValue.init(datasPreview, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Camera.Size> list = BaseCameraView.getmListPreviewSize();
                        int position = (Integer) v.getTag();
                        int width = list.get(position).width;
                        int height = list.get(position).height;
                        BaseCameraView.setPreviewSize(v.getContext(), width, height);
                    }
                });
                break;
            case INDEX_CAMERA_PICTURE:
                List<Camera.Size> listPictureSize = BaseCameraView.getmListPictureSize();

                int pictureWidth = BaseCameraView.getmSizePicture().width;
                int pictureHeight = BaseCameraView.getmSizePicture().height;
                List<CameraParamList.MyData> datasPicture = new ArrayList<>();
                for (int i = 0; i < listPictureSize.size(); i++) {
                    int itemWidth = listPictureSize.get(i).width;
                    int itemHeight = listPictureSize.get(i).height;
                    boolean isCheck = false;
                    if (pictureHeight == itemHeight && pictureWidth == itemWidth) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(itemWidth + "x" + itemHeight, isCheck);
                    datasPicture.add(item);
                }
                mListCameraPraramValue.init(datasPicture, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Camera.Size> list = BaseCameraView.getmListPictureSize();
                        int position = (Integer) v.getTag();
                        int width = list.get(position).width;
                        int height = list.get(position).height;
                        BaseCameraView.setPictureSize(v.getContext(), width, height);
                    }
                });

                break;
            case INDEX_WINDOW_SIZE:
                List<CameraParamList.MyData> listWindow = new ArrayList<>();
                CameraParamList.MyData item16_9 = null;
                CameraParamList.MyData item4_3 = null;
                switch (mIndexWindowSize) {
                    case 0:
                        item16_9 = new CameraParamList.MyData("16:9", true);
                        item4_3 = new CameraParamList.MyData("4:3", false);
                        break;
                    case 1:
                        item16_9 = new CameraParamList.MyData("16:9", false);
                        item4_3 = new CameraParamList.MyData("4:3", true);
                        break;
                }

                listWindow.add(item16_9);
                listWindow.add(item4_3);
                mListCameraPraramValue.init(listWindow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIndexWindowSize = (Integer) v.getTag();
                        ViewGroup.LayoutParams params = mCameraView.getLayoutParams();
                        switch (mIndexWindowSize) {
                            case 0:
                                params.height = getResources().getDisplayMetrics().heightPixels;
                                break;
                            case 1:
                                params.height = getResources().getDisplayMetrics().heightPixels / 4 * 3;
                                break;
                        }
                        mCameraView.setLayoutParams(params);

                    }
                });
                break;

            case INDEX_ZOOM:
                List<CameraParamList.MyData> listZoomData = new ArrayList<>();
                List<Integer> listZoom = BaseCameraView.getmListZoom();
                for (int i = 0; i < listZoom.size(); i++) {
                    String sItem = "" + listZoom.get(i);
                    boolean isCheck = false;
                    if (i == BaseCameraView.getmZoom()) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(sItem, isCheck);
                    listZoomData.add(item);
                }

                mListCameraPraramValue.init(listZoomData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setZoom(v.getContext(),position);
                    }
                });
                break;

            case INDEX_COLOR_EFFECT:
                List<CameraParamList.MyData> listColorEffectData = new ArrayList<>();
                List<String> listColorEffect = BaseCameraView.getmListColorEffect();
                for (int i = 0; i < listColorEffect.size(); i++) {
                    String sItem = listColorEffect.get(i);
                    boolean isCheck = false;
                    if (sItem.equals(BaseCameraView.getmColorEffect())) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(sItem, isCheck);
                    listColorEffectData.add(item);
                }

                mListCameraPraramValue.init(listColorEffectData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setColorEffect(v.getContext(), BaseCameraView.getmListColorEffect().get(position));
                    }
                });
                break;

            case INDEX_RATE:
                List<CameraParamList.MyData> listRateData = new ArrayList<>();
                List<String> listRate = BaseCameraView.getmListAntibanding();
                for (int i = 0; i < listRate.size(); i++) {
                    String sItem = listRate.get(i);
                    boolean isCheck = false;
                    if (sItem.equals(BaseCameraView.getmAntibanding())) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(sItem, isCheck);
                    listRateData.add(item);
                }

                mListCameraPraramValue.init(listRateData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setAntibanding(v.getContext(), BaseCameraView.getmListAntibanding().get(position));
                    }
                });

                break;
            case INDEX_WHITE_BALANCE:
                List<CameraParamList.MyData> listWhiteBalanceData = new ArrayList<>();
                List<String> listWhiteBalance = BaseCameraView.getmListWhiteBalance();
                for (int i = 0; i < listWhiteBalance.size(); i++) {
                    String sItem = listWhiteBalance.get(i);
                    boolean isCheck = false;
                    if (sItem.equals(BaseCameraView.getmWhiteBalance())) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(sItem, isCheck);
                    listWhiteBalanceData.add(item);
                }

                mListCameraPraramValue.init(listWhiteBalanceData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setWhiteBalance(v.getContext(), BaseCameraView.getmListWhiteBalance().get(position));
                    }
                });
                break;
            case INDEX_ROTATION:
                List<CameraParamList.MyData> listRotationData = new ArrayList<>();
                int rotation = BaseCameraView.getmRotation();
                CameraParamList.MyData item0 = new CameraParamList.MyData("" + 0, rotation == 0 ? true : false);
                CameraParamList.MyData item90 = new CameraParamList.MyData("" + 90, rotation == 90 ? true : false);
                CameraParamList.MyData item180 = new CameraParamList.MyData("" + 180, rotation == 180 ? true : false);
                CameraParamList.MyData item270 = new CameraParamList.MyData("" + 270, rotation == 270 ? true : false);
                listRotationData.add(item0);
                listRotationData.add(item90);
                listRotationData.add(item180);
                listRotationData.add(item270);

                mListCameraPraramValue.init(listRotationData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setRotation(v.getContext(), position * 90);
                    }
                });

                break;
            case INDEX_SCENE:
                List<CameraParamList.MyData> listSceneData = new ArrayList<>();
                List<String> listScene = BaseCameraView.getmListSceneMode();
                for (int i = 0; i < listScene.size(); i++) {
                    String sItem = listScene.get(i);
                    boolean isCheck = false;
                    if (sItem.equals(BaseCameraView.getmSceneMode())) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(sItem, isCheck);
                    listSceneData.add(item);
                }

                mListCameraPraramValue.init(listSceneData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setSceneMode(v.getContext(), BaseCameraView.getmListSceneMode().get(position));
                    }
                });
                break;
            case INDEX_FLASH:
                List<CameraParamList.MyData> listFlashData = new ArrayList<>();
                List<String> listFlash = BaseCameraView.getmListFlashMode();
                for (int i = 0; i < listFlash.size(); i++) {
                    String sItem = listFlash.get(i);
                    boolean isCheck = false;
                    if (sItem.equals(BaseCameraView.getmFlashMode())) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(sItem, isCheck);
                    listFlashData.add(item);
                }

                mListCameraPraramValue.init(listFlashData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setFlashMode(v.getContext(), BaseCameraView.getmListFlashMode().get(position));
                    }
                });
                break;
            case INDEX_FOCOUS:
                List<CameraParamList.MyData> listFocusData = new ArrayList<>();
                List<String> listFocus = BaseCameraView.getmListFocusMode();
                for (int i = 0; i < listFocus.size(); i++) {
                    String sItem = listFocus.get(i);
                    boolean isCheck = false;
                    if (sItem.equals(BaseCameraView.getmFocusMode())) {
                        isCheck = true;
                    }
                    CameraParamList.MyData item = new CameraParamList.MyData(sItem, isCheck);
                    listFocusData.add(item);
                }

                mListCameraPraramValue.init(listFocusData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        BaseCameraView.setFocusMode(v.getContext(), BaseCameraView.getmListFocusMode().get(position));

                    }
                });
                break;
            case INDEX_EXPOSURE:
                List<CameraParamList.MyData> listExposureData = new ArrayList<>();

                float exposureMax = BaseCameraView.getmExposureMax();
                float exposureMin = BaseCameraView.getmExposureMin();
                float exposureStep = 1;
                for (float i = 0; exposureMin + i <= exposureMax; i += exposureStep) {
                    float iItem = exposureMin + i;
                    boolean isCheck = false;
                    if (iItem == BaseCameraView.getmExposure())
                        isCheck = true;
                    CameraParamList.MyData item = new CameraParamList.MyData("" + iItem, isCheck);
                    listExposureData.add(item);
                }
                mListCameraPraramValue.init(listExposureData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        float exposureMin = BaseCameraView.getmExposureMin();
                        float exposureStep = 1;
                        int exposure = (int) (exposureMin + position * exposureStep);
                        BaseCameraView.setExposure(v.getContext(), exposure);

                    }
                });
                break;


        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        long time = System.currentTimeMillis();
        mPathDirTakePicture = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(), getString(R.string.app_name)).getPath();
        String fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + "_" + time + ".jpg";
        String path = new File(mPathDirTakePicture, fileName).getPath();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(KEY_PATH_SHOT, path).commit();
        Log.i(TAG, "onPictureTaken path: " + path);
        Utils.saveFile(data, path);
        BaseCameraView.startPreview(this);

        setAlbum();


    }
}


