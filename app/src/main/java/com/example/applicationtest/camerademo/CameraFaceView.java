package com.example.applicationtest.camerademo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import com.example.applicationtest.R;

/**
 * TODO:画面内没有人时，将方框和图标取消；
 */
public class CameraFaceView extends androidx.appcompat.widget.AppCompatImageView {
    private Context mContext;
    private Camera.Face[] mFaces;
    private Matrix mMatrix = new Matrix();
    private boolean mirror;
    private Paint mLinePaint;

    private RectF rectF = new RectF();
    private Drawable mFaceIndicator = null;
    private final String TAG = CameraFaceView.class.getSimpleName().toString();

    public CameraFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        this.mContext = context;
        mFaceIndicator = mContext.getResources().getDrawable(R.drawable.ic_action_info);
    }

    public void setFaces(Camera.Face[] faces) {
        this.mFaces = faces;
        invalidate();
        Log.d(TAG, "invalidate ; mFaces = " + mFaces + "; length = " + mFaces.length);
    }

    public void clearFaces() {
        mFaces = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFaces == null || mFaces.length < 1) {
            return;
        }
        if (mFaces != null) {
            int id = CameraUtils.getCameraId();
            mirror = (id == Camera.CameraInfo.CAMERA_FACING_FRONT);
            canvas.save();
            prepareMatrix();
            mMatrix.postRotate(0); //Matrix.postRotate默认是顺时针
            canvas.rotate(-0);   //Canvas.rotate()默认是逆时针
            for (int i = 0; i < mFaces.length; i++) {
                rectF.set(mFaces[i].rect);
                mMatrix.mapRect(rectF);
                Rect newRect = new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
                mFaceIndicator.setBounds(newRect);
                Log.i(TAG, "onDraw: rectF = " + Math.round(rectF.left) + " ; " + Math.round(rectF.top) + " ; " + Math.round(rectF.right) + " ; " + Math.round(rectF.bottom));
                canvas.drawRect(newRect,mLinePaint);
                mFaceIndicator.draw(canvas);
            }
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    private void prepareMatrix() {
        mMatrix.setScale(mirror ? -1 : 1, 1);
        mMatrix.postRotate(9);
        mMatrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
        mMatrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = Color.rgb(98, 212, 68);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(180);
    }

}
