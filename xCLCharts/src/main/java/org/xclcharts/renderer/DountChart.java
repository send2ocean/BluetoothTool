package org.xclcharts.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import org.xclcharts.chart.PieChart;
import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;

/**
 * @author XiongChuanLiang<br   />(xcl_168@aliyun.com)
 * @ClassName DountChart
 * @Description 环形图基类
 */
public class DountChart extends PieChart {

    //内环半径
    private int mFillRadius = 0;
    private float mInnerRadius = 0.8f;

    //内环填充颜色
    private Paint mPaintFill = null;

    private Paint mPaintCenterText = null;
    private String mCenterText = "";


    public DountChart() {
        initChart();
    }

    @Override
    public XEnum.ChartType getType() {
        return XEnum.ChartType.DOUNT;
    }

    private void initChart() {
        int fillColor = Color.BLACK;

        if (null != plotArea)
            fillColor = plotArea.getBackgroundPaint().getColor();

        if (null == mPaintFill) {
            mPaintFill = new Paint();
            mPaintFill.setColor(fillColor);
            mPaintFill.setAntiAlias(true);
        }

        this.setLabelStyle(XEnum.SliceLabelStyle.OUTSIDE);
    }

    private void initCenterTextPaint() {
        if (null == mPaintCenterText) {
            mPaintCenterText = new Paint();
            mPaintCenterText.setAntiAlias(true);
            mPaintCenterText.setTextSize(28);
            mPaintCenterText.setTextAlign(Paint.Align.CENTER);
        }
    }


    /**
     * 环内部填充画笔
     *
     * @return 画笔
     */
    public Paint getInnerPaint() {
        return mPaintFill;
    }

    /**
     * 设置环内部填充相对于环所占的比例
     *
     * @param precentage 环所占比例
     */
    public void setInnerRadius(float precentage) {
        mInnerRadius = precentage;
    }

    /**
     * 计算出环内部填充圆的半径
     *
     * @return 环的半径
     */
    public float calcInnerRadius() {
        mFillRadius = (int) MathHelper.getInstance().round(mul(getRadius(), mInnerRadius), 2);
        return mFillRadius;
    }

    /**
     * 开放绘制中心文字的画笔
     *
     * @return 画笔
     */
    public Paint getCenterTextPaint() {
        initCenterTextPaint();
        return mPaintCenterText;
    }

    /**
     * 设置中心点文字
     *
     * @param text 文字
     */
    public void setCenterText(String text) {
        mCenterText = text;
    }

    /**
     * 绘制中心点
     *
     * @param canvas 画布
     */
    private void renderCenterText(Canvas canvas) {
        if (mCenterText.length() > 0) {
            if (mCenterText.indexOf("\n") > 0) {
                float textY = plotArea.getCenterY();
                float textHeight = DrawHelper.getInstance().getPaintFontHeight(getCenterTextPaint());

                String[] arr = mCenterText.split("\n");
                for (int i = 0; i < arr.length; i++) {
                    canvas.drawText(arr[i],
                            plotArea.getCenterX(), textY, getCenterTextPaint());
                    textY += textHeight;
                }
            } else {
                canvas.drawText(mCenterText,
                        plotArea.getCenterX(), plotArea.getCenterY(), getCenterTextPaint());
            }

        }
    }

    @Override
    protected PointF renderLabelInside(Canvas canvas, String text, float itemAngle,
                                       float cirX, float cirY, float radius, float calcAngle, boolean showLabel) {
        if ("" == text) return null;

        //显示在扇形的中心
        float calcRadius = mFillRadius + (radius - mFillRadius) / 2;

        //计算百分比标签
        PointF point = MathHelper.getInstance().calcArcEndPointXY(
                cirX, cirY, calcRadius, calcAngle);
        //标识
        if (showLabel)
            DrawHelper.getInstance().drawRotateText(text, point.x, point.y, itemAngle,
                    canvas, getLabelPaint());
        return (new PointF(point.x, point.y));
    }

    protected void renderInnderCircle(Canvas canvas) {
        //中心点坐标
        float cirX = plotArea.getCenterX();
        float cirY = plotArea.getCenterY();
        canvas.drawCircle(cirX, cirY, mFillRadius, mPaintFill);

        //边框线
        if (null != mPaintArcBorder) {
            canvas.drawCircle(cirX, cirY, mFillRadius, mPaintArcBorder);
        }
    }

    protected void renderDount(Canvas canvas) {
        //内部
        renderInnderCircle(canvas);

    }

    /**
     * 绘制图 -- 环形图的标签处理待改进 ***
     */
    @Override
    protected boolean renderPlot(Canvas canvas) {
        calcInnerRadius();

        super.renderPlot(canvas);

        renderDount(canvas);
        return true;
    }

}
