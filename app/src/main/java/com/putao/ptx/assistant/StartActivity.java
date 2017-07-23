package com.putao.ptx.assistant;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.putao.ptx.assistant.haicientity.HaiciBean;
import com.putao.ptx.assistant.utils.BitmapUtils;
import com.putao.ptx.assistant.utils.Constance;
import com.putao.ptx.assistant.utils.WebUtils;
import com.putao.ptx.assistant.views.OcrImageView;
import com.sinovoice.hcicloud.analyze.Ocr;
import com.sinovoice.hcicloud.model.OcrData;
import com.sinovoice.hcicloud.model.OcrResult;

import java.io.File;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;


public class StartActivity extends AppCompatActivity {

    public static final String ACTION_EXIT = "com.putao.assist.DETAIL";
    private static final String TAG = "OcrMainActivity";
    private static final int FILE_SELECT_CODE = 10001;
    public static final int TIME_SCAN = 1500;

    @Bind(R.id.ivDisplay)
    OcrImageView mDisplay;

    @Bind(R.id.ivPreview)
    ImageView ivPreview;

    @Bind(R.id.tvLog)
    TextView mLogView;

    @Bind(R.id.scan)
    View mScan;


    @Bind(R.id.tvScan)
    View tvScan;

    @Bind(R.id.select)
    View btnSelect;

    @Bind(R.id.detect)
    View btnDetect;

    @Bind(R.id.ivExit)
    ImageView ivDel;


    @Bind(R.id.ocrRst)
    EditText ocrRst;


    @Bind(R.id.translate)
    View translate;

    ValueAnimator mAni = ValueAnimator.ofFloat(0, 1);

    private String mSelectedPath = Constance.OCR_PATH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        mDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //       ocrRecg();
            }
        });
        verifyStoragePermissions(StartActivity.this);
    }


    private void initView() {
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisplay.setSelectedRect(null);
                showFileChooser();
            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSelectedPath) || !new File(mSelectedPath).exists()) {
                    Toast.makeText(StartActivity.this, "请选择有效的图片！", Toast.LENGTH_LONG).show();
                    return;
                }
                mLogView.setText("");
                Runnable runnable = new Runnable() {
                    public void run() {
                        long tm = SystemClock.elapsedRealtime();
                        boolean bUseReal = true/*true*/;//TODO
                        final OcrResult ret;
                        if (bUseReal) {
                            ret = new Ocr().recogChineseText(/*mSelectedPath*/Constance.OCR_PATH);
                        } else {
                            ret = new OcrResult(0, "成功", new OcrData());
                            try {
                                Thread.sleep((long) (new Random().nextFloat() * 10000) + 6000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.e(TAG, "run: " + e);
                            }
                        }
                        long wasteTime = SystemClock.elapsedRealtime() - tm;
                        Log.d(TAG, "run: ocr waste Time:" + wasteTime);

                        if (ret.getError() != 0) {//识别失败
                            //TODO
                        }

                        final String text = ret.result();
                        /*HaiCiReturn*/
                        HaiciBean haiCiReturn;
                        try {
                            //String translateRst = WebUtils.doPost(text/*"长"*/);
                            long tm2 = SystemClock.elapsedRealtime();
                            String translateRst = WebUtils.doPost("长");
                            Log.d(TAG, "run: doPost waste time" + (SystemClock.elapsedRealtime() - tm2));
                            printLog(translateRst);
                            translateRst = WebUtils.doPost("国家");
                            printLog(translateRst);
                            translateRst = WebUtils.doPost("刻舟求剑");
                            printLog(translateRst);
                            translateRst = WebUtils.doPost("国");
                            printLog(translateRst);
                            translateRst = WebUtils.doPost("good");
                            printLog(translateRst);
                            translateRst = WebUtils.doPost("android");
                            printLog(translateRst);

                            Log.i(TAG, "run: translateRst:\n" + translateRst.replaceAll("\\}", "}\n"));
                            haiCiReturn = new Gson().fromJson(translateRst, /*HaiCiReturn*/HaiciBean.class);
                            Log.i(TAG, "WebUtils.doPost: " + translateRst + "  haiCiReturn:" + haiCiReturn.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "run: " + e);
                        }
                        mLogView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLogView.setVisibility(View.VISIBLE);
                                ocrRst.setText(text);
                                mLogView.setText(text);
                                mDisplay.setOcrResult(ret);
                                Log.d(TAG, "run: initView:" + text);
                                stopScan();

                            }
                        }, TIME_SCAN - wasteTime % TIME_SCAN);
                    }
                };
                startScan();
                new Thread(runnable).start();
            }
        });
        ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDisplay.setOnTouchListener(new View.OnTouchListener() {
            public long tmLastPointUp;
            public float startX, startY;
            public Bitmap bitmap;
            RectF select = new RectF();
            PointF pLast = new PointF();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX() - 30;
                        startY = event.getY() - 20;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount() == 1) {
                            updateSelectedRect(event);
                        } else {
                            float dx = event.getX() - pLast.x;
                            float dy = event.getY() - pLast.y;
                            startX += dx;
                            startY += dy;
                            select.offset(dx, dy);
                        }
                        bitmap = mDisplay.getSectorBitmap(select);
                        if (bitmap != null) {
                            ivPreview.setImageBitmap(bitmap);
                            mDisplay.setSelectedRect(select);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (SystemClock.elapsedRealtime() - tmLastPointUp > 500) {
                            updateSelectedRect(event);
                        }
                        bitmap = mDisplay.getSectorBitmap(select);
                        if (bitmap != null) {
                            ivPreview.setImageBitmap(bitmap);
                            BitmapUtils.saveBitmap(bitmap, Constance.OCR_PATH);
                            mDisplay.setSelectedRect(select);
                        }
                    case MotionEvent.ACTION_POINTER_UP:
                        tmLastPointUp = SystemClock.elapsedRealtime();
                        break;
                }
                pLast.x = event.getX();
                pLast.y = event.getY();
                return true/*false*/;
            }

            private void updateSelectedRect(MotionEvent event) {
                select.left = startX;
                select.top = startY;
                select.right = event.getX();
                select.bottom = event.getY();
                if (select.right < select.left) {
                    float left = select.left;
                    select.left = select.right;
                    select.right = left;
                }
                if (select.bottom < select.top) {
                    float top = select.top;
                    select.top = select.bottom;
                    select.bottom = top;
                }
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate(ocrRst.getText().toString().replaceAll("[,|.|!|?|，|。|！|？]", ""));
            }
        });

        try {
            ivPreview.setImageBitmap(BitmapFactory.decodeFile(Constance.OCR_PATH));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initView: " + e);
        }
    }

    private HaiciBean printLog(String translateRst) {
        HaiciBean haiCiReturn;
        Gson gson = new Gson();
        haiCiReturn = gson.fromJson(translateRst, /*HaiCiReturn*/HaiciBean.class);
        String beanSrc = gson.toJson(haiCiReturn);
        String src = translateRst.replaceAll("\\}", "}\n");
        String bean = beanSrc.replaceAll("\\}", "}\n");
        Log.i(TAG, "getHaiciBean: translateRst\n" + src + "\n\n\n\n\n\n  haiCiBean:\n" + bean);
        return haiCiReturn;
    }

    private void translate(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.putao.ptx.ipspirits", "com.putao.ptx.ipspirits.activities.MainActivity");
        /**
         * @return type 类型
         * 1-成语
         * 2-英语
         * 3-诗词
         * 4-百科
         * 5-识字
         * 6-看图识英语*/
        boolean isEnglish = s.length() == s.getBytes().length;
        intent.putExtra("pai_widget_type", isEnglish ? 2 : 1);
        //intent.putExtra("pai_widget_data", data);


    }

    private void stopScan() {
        //mScan.setVisibility(View.INVISIBLE);
        //tvScan.setVisibility(View.INVISIBLE);
        mScan.animate().alpha(0f).setDuration(500).start();
        tvScan.animate().alpha(0f).setDuration(400).start();
        if (mAni != null) {
            mAni.cancel();
        }
    }

    private void startScan() {
        mScan.setAlpha(0);
        mScan.setVisibility(View.VISIBLE);
        mScan.animate().alpha(0.9f).setDuration(800).start();
        int width = updateImgDisplaySize(mDisplay);
        tvScan.getLayoutParams().width = width;
        mScan.getLayoutParams().width = width - 20;
        tvScan.setAlpha(0);
        tvScan.setVisibility(View.VISIBLE);
        tvScan.animate().alpha(0.9f).setDuration(800).start();
        View p = (View) mScan.getParent();
        int height = p.getHeight();
        int offset = 20;
        mAni.setFloatValues(offset, height - offset - mScan.getHeight());
        mAni.setRepeatMode(ValueAnimator.REVERSE);
        mAni.setDuration(TIME_SCAN);
        mAni.setRepeatCount(ValueAnimator.INFINITE);
        mAni.removeAllUpdateListeners();
        mAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScan.setTranslationY(((Number) animation.getAnimatedValue()).intValue());
            }
        });
        mAni.start();
    }


    /***
     group:android.permission-group.STORAGE
     permission:android.permission.READ_EXTERNAL_STORAGE
     permission:android.permission.WRITE_EXTERNAL_STORAGE
     ***/
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private boolean verifyStoragePermissions(Activity activity) {

        if (Build.VERSION.SDK_INT < 23) /*******below android 6.0*******/ {
            return true;
        }

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*do request function(into apk)*/
                } else {

                    Toast.makeText(getApplicationContext(),
                            "storage permission denied,it will exit apk",
                            Toast.LENGTH_LONG).show();

                 /*do request function(exitapk)*/
                    finish();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent./*ACTION_GET_CONTENT*/ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("*/*");
        //intent.setType("image/jpeg");//选择图片
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
            startActivityForResult(/*chooser*/intent, FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e(TAG, "showFileChooser: ", ex);
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = getPath(this, uri);//uri.getPath();
                    if (path != null && (path.endsWith(".png") || path.endsWith(".jpg"))) {
                        mSelectedPath = path;
                        mDisplay.setImageBitmap(BitmapFactory.decodeFile(mSelectedPath));
                        mLogView.setText("");
                        //btnDetect.performClick();

                        Intent it = new Intent(StartActivity.this, OcrResultActivity.class);
                        it.putExtra(Constance.KEY_IMG, mSelectedPath);
                        it.putExtra(Constance.KEY_SELECT, mSelectedPath);
                        startActivity(it);
                    } else {
                        Toast.makeText(this, "请选择.jpg文件！", Toast.LENGTH_LONG).show();
                    }
                    Log.d(TAG, "onActivityResult: mSelectedPath:" + path);


//                    Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
//                    String[] proj = {MediaStore.Images.Media.DATA};
//                    Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
//                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    actualimagecursor.moveToFirst();
//                    String img_path = actualimagecursor.getString(actual_image_column_index);
//                    File file = new File(img_path);
//                    Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();


                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    private int updateImgDisplaySize(@NonNull ImageView iv) {
        int realImgShowWidth = iv.getWidth(), realImgShowHeight;
        Drawable imgDrawable = iv.getDrawable();
        if (imgDrawable != null) {
            //获得ImageView中Image的真实宽高，
            int dw = imgDrawable.getBounds().width();
            int dh = imgDrawable.getBounds().height();

            //获得ImageView中Image的变换矩阵
            Matrix m = iv.getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];

            //计算Image在屏幕上实际绘制的宽高
            realImgShowWidth = (int) (dw * sx);
            realImgShowHeight = (int) (dh * sy);
        }
        return realImgShowWidth;
    }


}
