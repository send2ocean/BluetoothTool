package com.fyts.bluetoothtool.view.qrcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.other.ScreenUtil;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.CameraPreview;

import java.util.ArrayList;
import java.util.List;

/**
 * Message: 二维码
 * Author: 吕帅义
 * Date: 2021/4/15 18:58
 */
public class ViewfinderView extends View {
    protected static final String TAG = ViewfinderView.class.getSimpleName();


    protected final Paint paint;
    protected Bitmap resultBitmap;
    protected final int maskColor;
    protected final int resultColor;
    protected final int laserColor;
    protected final int resultPointColor;
    protected int scannerAlpha;
    protected List<ResultPoint> possibleResultPoints;
    protected List<ResultPoint> lastPossibleResultPoints;
    protected CameraPreview cameraPreview;

    // Cache the framingRect and previewFramingRect, so that we can still draw it after the preview
    // stopped.
    protected Rect framingRect;
    protected Rect previewFramingRect;
    private final Paint linePaint;
    private final Paint linePaints;
    private int lineSize = 20;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); //开启反锯齿
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#07D8FF"));
        linePaint.setStrokeWidth(5);

        linePaints = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaints.setColor(Color.parseColor("#1A3F81"));
        linePaints.setStrokeWidth(ScreenUtil.dip2px(context, 1));

        lineSize = ScreenUtil.dip2px(context, 10);
        Resources resources = getResources();

        // Get setted attributes on view
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.zxing_finder);

        this.maskColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_mask,
                resources.getColor(R.color.zxing_viewfinder_mask));
        this.resultColor = attributes.getColor(R.styleable.zxing_finder_zxing_result_view,
                resources.getColor(R.color.zxing_result_view));
        this.laserColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_laser,
                resources.getColor(R.color.zxing_viewfinder_laser));
        this.resultPointColor = attributes.getColor(R.styleable.zxing_finder_zxing_possible_result_points,
                resources.getColor(R.color.zxing_possible_result_points));

        attributes.recycle();

        scannerAlpha = 0;
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = null;
    }

    interface OnReSize {
        void onSize(int size);
    }

    OnReSize onReSize;

    public void setOnReSize(OnReSize onReSize) {
        this.onReSize = onReSize;
    }

    public void setCameraPreview(CameraPreview view) {
        this.cameraPreview = view;
        view.addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
                refreshSizes();
                invalidate();
            }

            @Override
            public void previewStarted() {

            }

            @Override
            public void previewStopped() {

            }

            @Override
            public void cameraError(Exception error) {

            }
        });
    }

    protected void refreshSizes() {
        if (cameraPreview == null) {
            return;
        }
        Rect framingRect = cameraPreview.getFramingRect();
        Rect previewFramingRect = cameraPreview.getPreviewFramingRect();
        if (framingRect != null && previewFramingRect != null) {
            this.framingRect = framingRect;
            this.previewFramingRect = previewFramingRect;
            onReSize.onSize(framingRect.bottom);
        }
    }


    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        Rect frame = framingRect;
        Rect previewFrame = previewFramingRect;

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        canvas.drawLine(frame.left, frame.top, frame.left + lineSize, frame.top, linePaint);
        canvas.drawLine(frame.left, frame.top, frame.left, frame.top + lineSize, linePaint);

        canvas.drawLine(frame.right - lineSize, frame.top, frame.right, frame.top, linePaint);
        canvas.drawLine(frame.right, frame.top, frame.right, frame.top + lineSize, linePaint);

        canvas.drawLine(frame.left, frame.bottom - lineSize, frame.left, frame.bottom, linePaint);
        canvas.drawLine(frame.left, frame.bottom, frame.left + lineSize, frame.bottom, linePaint);

        canvas.drawLine(frame.right - lineSize, frame.bottom, frame.right, frame.bottom, linePaint);
        canvas.drawLine(frame.right, frame.bottom - lineSize, frame.right, frame.bottom, linePaint);

        canvas.drawLine(frame.left + lineSize, frame.top, frame.right - lineSize, frame.top, linePaints);
        canvas.drawLine(frame.left + lineSize, frame.bottom, frame.right - lineSize, frame.bottom, linePaints);
        canvas.drawLine(frame.left, frame.top + lineSize, frame.left, frame.bottom - lineSize, linePaints);
        canvas.drawLine(frame.right, frame.top + lineSize, frame.right, frame.bottom - lineSize, linePaints);


    }

    /**
     * Only call from the UI thread.
     *
     * @param point a point to draw, relative to the preview frame
     */
    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        points.add(point);
    }

}
