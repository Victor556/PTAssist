package com.putao.ptx.assistant.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SizeF;

import com.sinovoice.hcicloud.model.OcrResult;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <br/>Description  :
 * <br/>
 * <br/>Author       : Victor<liuhe556@126.com>
 * <br/>
 * <br/>Created date : 2017-03-31</p>
 */
public class OcrImageView extends android.support.v7.widget.AppCompatImageView {
    private OcrResult mOcrResult;

    private Map<String, RectF> mPosReal = new HashMap<>();
    private Map<String, RectF> mPosShow = new HashMap<>();
    private Paint mPain;
    private Paint mPainSelect;
    private Paint mPainWhiteFrame;
    private Bitmap mBitmap;

    private RectF mSelectedRect = new RectF();
    private int COLOR_SLIDE = Color.argb(0x77, 0xff, 0xe2, 0x77);/*ffe277*/
    private int COLOR_FRONT = Color.argb(0x77, 0xff, 0xeff, 0xff);

    public OcrImageView(Context context) {
        this(context, null);
    }

    public OcrImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OcrImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPain = new Paint();
        mPain.setAntiAlias(true);
        mPain.setStrokeWidth(1);
        mPain.setStyle(Paint.Style.STROKE);
        mPain.setColor(Color.BLUE);

        mPainSelect = new Paint();
        mPainSelect.setAntiAlias(true);
        mPainSelect.setStyle(Paint.Style.FILL);
        mPainSelect.setColor(COLOR_SLIDE);


        mPainWhiteFrame = new Paint();
        mPainWhiteFrame.setAntiAlias(true);
        mPainWhiteFrame.setStrokeWidth(5);
        mPainWhiteFrame.setStyle(Paint.Style.STROKE);
        mPainWhiteFrame.setColor(Color.WHITE);
    }

    public void setOcrResult(OcrResult result) {
        mOcrResult = result;
        mPosReal = mOcrResult.getPos();
        for (Map.Entry<String, RectF> t : mPosReal.entrySet()) {
            mPosShow.put(t.getKey(), toViewRectF(t.getValue()));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        updateImgDisplaySize();
        Log.d(TAG, "onLayout:  realImgShowHeight:" + realImgShowHeight + "  realImgShowHeight:" + realImgShowHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (realImgShowWidth < 0 || realImgShowHeight < 0 /*|| Math.abs(mFrameF.width() - realImgShowWidth) > 10*/) {
            //postDelayed(new Runnable() {
            //    @Override
            //    public void run() {
            //        updateImgDisplaySize();
            //    }
            //}, 20);
            updateImgDisplaySize();
        } else if (Math.abs(mFrameF.width() - realImgShowWidth) < 10) {
            canvas.drawRect(mFrameF, mPainWhiteFrame);
        }

        for (RectF r : mPosShow.values()) {
            canvas.drawRect(r, mPain);
        }
        if (getForeground() == null) {
            if (mSelectedRect != null) {
                mPainSelect.setColor(COLOR_SLIDE);
                canvas.drawRect(mSelectedRect, /*mPain*/mPainSelect);
            }
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        recycleBitmap();
        mBitmap = bm;
        super.setImageBitmap(bm);
        updateImgDisplaySize();
    }

    int realImgShowWidth = -1;
    int realImgShowHeight = -1;
    int realImgWidth = -1;
    int realImgHeight = -1;
    int realViewWidth = -1;
    int realViewHeight = -1;

    float mScaleX;
    float mScaleY;
    final RectF mFrameF = new RectF();

    public SizeF updateImgDisplaySize() {
        Drawable imgDrawable = getDrawable();
        if (imgDrawable != null) {
            //获得ImageView中Image的真实宽高，
            int dw = imgDrawable.getBounds().width();
            int dh = imgDrawable.getBounds().height();
            realImgWidth = dw;
            realImgHeight = dh;

            //获得ImageView中Image的变换矩阵
            Matrix m = getImageMatrix();
            float[] values = new float[10];
            m.getValues(values);

            //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数
            float sx = values[0];
            float sy = values[4];
            mScaleX = sx;
            mScaleY = sy;

            //计算Image在屏幕上实际绘制的宽高
            realImgShowWidth = (int) (dw * sx);
            realImgShowHeight = (int) (dh * sy);
            realViewWidth = getWidth();
            realViewHeight = getHeight();
            int left = (realViewWidth - realImgShowWidth) / 2;
            int top = (realViewHeight - realImgShowHeight) / 2;
            mFrameF.set(left, top, left + realImgShowWidth, top + realImgShowHeight);
            //mFrameF.set(0, 0, getWidth(), getHeight());
            Log.d(TAG, "updateImgDisplaySize: mFrameF:" + mFrameF);
            invalidate();
        }
        return new SizeF(realImgShowWidth, realImgShowHeight);
    }

    private static final String TAG = "OcrImageView";

    private float realXToViewX(float realx) {
        return mScaleX * realx + (realViewWidth - realImgShowWidth) / 2;
    }


    private float realYToViewY(float realy) {
        return mScaleY * realy + (realViewHeight - realImgShowHeight) / 2;
    }


    private RectF toViewRectF(RectF realF) {
        RectF f = new RectF();
        f.left = realXToViewX(realF.left);
        f.right = realXToViewX(realF.right);
        f.top = realYToViewY(realF.top);
        f.bottom = realYToViewY(realF.bottom);
        return f;
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        Drawable foreground = getForeground();
        if (foreground == null || mSelectedRect == null) {
            super.onDrawForeground(canvas);
            return;
        }
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                        | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                        | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        super.onDrawForeground(canvas);
        mPainSelect.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        if (mSelectedRect != null) {
            mPainSelect.setColor(COLOR_FRONT);
            canvas.drawRect(mSelectedRect, /*mPain*/mPainSelect);
        }
        mPainSelect.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    private RectF toRealRectF(RectF realF) {
        RectF f = new RectF();
        f.left = viewXTorealX(realF.left);
        f.right = viewXTorealX(realF.right);
        f.top = viewYToRealY(realF.top);
        f.bottom = viewYToRealY(realF.bottom);
        return f;
    }


    private float viewXTorealX(float vx) {
        return (vx - (realViewWidth - realImgShowWidth) / 2) / mScaleX;
    }

    private float viewYToRealY(float vy) {
        return (vy - (realViewHeight - realImgShowHeight) / 2) / mScaleY;
    }


    public Bitmap getSectorBitmap(RectF r) {
        if (r == null) {
            return null;
        }
        RectF tmp = new RectF(r);

        float offsetV = (getWidth() - realImgShowWidth) / 2.0f;
        float offsetH = (getHeight() - realImgShowHeight) / 2.0f;
        RectF display = new RectF(offsetH, offsetV, offsetH + realImgShowWidth, offsetH + realImgShowHeight);
        boolean bIntersect = tmp.intersect(display);
        if (!bIntersect) {
            return null;
        }
        tmp.offset(-offsetH, -offsetV);

        tmp.left = Math.max(tmp.left, 0);
        tmp.top = Math.max(tmp.top, 0);
        tmp.right = Math.min(tmp.right, getWidth());
        tmp.bottom = Math.min(tmp.bottom, getHeight());

        RectF ret = toRealRectF(r);
        int width = (int) ret.width();
        int height = (int) ret.height();
        int left = (int) ret.left;
        int top = (int) ret.top;

        if (mBitmap == null) {
            setDrawingCacheEnabled(true);
            mBitmap = getDrawingCache();
            setDrawingCacheEnabled(false);
        }
        if (!mBitmap.isRecycled() && mBitmap != null &&
                left >= 0 && top >= 0 && width > 0 && height > 0
                && left + width <= mBitmap.getWidth() && top + height <= mBitmap.getHeight()) {
            return Bitmap.createBitmap(mBitmap, left, top, width, height, null, false);
        }
        return null;
    }

    public void setSelectedRect(RectF selectedRect) {
        if (selectedRect != null) {
            mSelectedRect.set(selectedRect);
        } else {
            mSelectedRect.set(0, 0, 0, 0);
        }
        postInvalidate();
    }

    public int getRealImgShowHeight() {
        return realImgShowHeight;
    }


    public int getRealImgShowWidth() {
        return realImgShowWidth;
    }

    public RectF getFrameF() {
        return mFrameF;
    }


    public RectF getSelectedRect() {
        return mSelectedRect;
    }

    public void recycleBitmap() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
