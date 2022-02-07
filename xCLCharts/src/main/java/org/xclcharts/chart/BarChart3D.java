/**
 * Copyright 2014  XCL-Charts
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br />(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */

package org.xclcharts.chart;

import android.graphics.Canvas;
import android.util.Log;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.bar.Bar;
import org.xclcharts.renderer.bar.Bar3D;
import org.xclcharts.renderer.info.PlotAxisTick;

import java.util.List;

/**
 * @ClassName Bar3DChart
 * @Description 3D柱形图的基类, 包含横向和竖向3D柱形图
 * @author XiongChuanLiang<br />(xcl_168@aliyun.com)
 *
 */

public class BarChart3D extends BarChart {

    private static final String TAG = "BarChart3D";

    //3D柱形绘制
    private Bar3D mBar3D = new Bar3D();

    public BarChart3D() {
        if (null != categoryAxis) categoryAxis.hideTickMarks();

        setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);
    }

    @Override
    public XEnum.ChartType getType() {
        return XEnum.ChartType.BAR3D;
    }

    /**
     * 设置坐标系底座厚度
     * @param thickness 底座厚度
     */
    public void setAxis3DBaseThickness(final int thickness) {
        mBar3D.setAxis3DBaseThickness(thickness);
    }

    /**
     * 返回坐标系底座厚度
     * @return 底座厚度
     */
    public double getAxis3DBaseThickness() {
        return mBar3D.getAxis3DBaseThickness();
    }

    /**
     * 设置柱形3D厚度
     * @param thickness 厚度
     */
    public void setBarThickness(final int thickness) {
        mBar3D.setThickness(thickness);
    }

    /**
     * 返回柱形3D厚度
     * @return 厚度
     */
    public double getBarThickness() {
        return mBar3D.getThickness();
    }

    /**
     * 设置3D偏转角度
     * @param angle 角度
     */
    public void setBarAngle(final int angle) {
        mBar3D.setAngle(angle);
    }
	/*public void setBarMyoffsetX(final int angle)
	{
		mBar3D.setMyoffsetX(angle);
	}
	public void setBarMyoffsetY(final int angle)
	{
		mBar3D.setMyOffsetY(angle);
	}*/

    /**
     * 返回3D偏转角度
     * @return 角度
     */
    public int getBarAngle() {
        return mBar3D.getAngle();
    }

    /**
     * 设透明度
     * @param alpha 透明度
     */
    public void setBarAlpha(final int alpha) {
        mBar3D.setAlpha(alpha);
    }

    /**
     *  坐标基座颜色
     * @param color 颜色
     */
    public void setAxis3DBaseColor(final int color) {
        mBar3D.setAxis3DBaseColor(color);
    }

    @Override
    public Bar getBar() {
        return mBar3D;
    }


    @Override
    protected boolean renderHorizontalBar(Canvas canvas) {
        //得到数据源
        List<BarData> chartDataSource = this.getDataSource();
        if (null == chartDataSource || chartDataSource.size() == 0) return false;

        //得到Y 轴分类横向间距高度
        float YSteps = getVerticalYSteps(getCateTickCount()); //getHorizontalYSteps();

        float barInitX = plotArea.getLeft() + this.mMoveX;
        float barInitY = plotArea.getBottom();// + this.mMoveY;


        //依柱形宽度，多柱形间的偏移值 与当前数据集的总数据个数得到当前分类柱形要占的高度
        int barNumber = getDatasetSize(chartDataSource);
        if (barNumber <= 0) return false;
        int currNumber = 0;

        float[] ret = mBar3D.getBarHeightAndMargin(YSteps, barNumber);
        if (null == ret || ret.length != 2) {
            Log.e(TAG, "分隔间距计算失败.");
            return false;
        }
        float barHeight = ret[0];
        float barInnerMargin = ret[1];
        float labelBarUseHeight = add(mul(barNumber, barHeight),
                mul((barNumber - 1), barInnerMargin));

        float scrWidth = plotArea.getPlotWidth(); //plotArea.getWidth();
        float valueWidth = dataAxis.getAxisRange();

        for (int i = 0; i < barNumber; i++) {
            //得到分类对应的值数据集
            BarData bd = chartDataSource.get(i);
            List<Double> barValues = bd.getDataSet();
            if (null == barValues || barValues.size() == 0) continue;
            List<Integer> barDataColor = bd.getDataColor();
            //设置成对应的颜色
            int barDefualtColor = bd.getColor();
            mBar3D.getBarPaint().setColor(barDefualtColor);

            //画同分类下的所有柱形
            for (int j = 0; j < barValues.size(); j++) {
                Double bv = barValues.get(j);
                setBarDataColor(mBar3D.getBarPaint(), barDataColor, j, barDefualtColor);

                float drawBarButtomY = add(sub(barInitY, mul((j + 1), YSteps)),
                        labelBarUseHeight / 2);
                drawBarButtomY = sub(drawBarButtomY, (barHeight + barInnerMargin) * currNumber);

                //参数值与最大值的比例  照搬到 y轴高度与矩形高度的比例上来
                double tlen = MathHelper.getInstance().sub(bv, dataAxis.getAxisMin());
                float valuePostion = mul(scrWidth, div((float) (tlen), valueWidth));

                //画出柱形
                float topY = sub(drawBarButtomY, barHeight);
                float rightX = add(barInitX, valuePostion);
                mBar3D.renderHorizontal3DBar(barInitX,
                        topY, rightX, drawBarButtomY,
                        mBar3D.getBarPaint().getColor(), canvas);

                //保存位置
                saveBarRectFRecord(i, j, barInitX + mMoveX,
                        topY + mMoveY, rightX + mMoveX, drawBarButtomY + mMoveY);

                float labelTopY = sub(drawBarButtomY, barHeight / 2);

                //在柱形的顶端显示上批注形状
                drawAnchor(this.getAnchorDataPoint(), i, j, canvas, rightX, labelTopY, 0.0f);

                //在柱形的顶端显示上柱形的当前值
                if (!mEqualAxisMin && Double.compare(dataAxis.getAxisMin(), bv) == 0) {
                } else {
                    mBar3D.renderBarItemLabel(getFormatterItemLabel(bv), rightX, labelTopY, canvas);
                }
            }
            currNumber++;
        }
        return true;
    }

    @Override
    protected float get3DOffsetX()// 分类
    {
        float tfx = (float) (mBar3D.getOffsetX() * 2);
        return tfx;
    }

    @Override
    protected float get3DBaseOffsetX()// 分类
    {
        double baseTickness = mBar3D.getAxis3DBaseThickness();
        double baseAngle = mBar3D.getAngle();
        double baseOffsetX = mBar3D.getOffsetX(baseTickness, baseAngle);

        return (float) baseOffsetX;
    }

    @Override
    protected float get3DBaseOffsetY()// 分类
    {
        //3D 偏移值
        double baseTickness = mBar3D.getAxis3DBaseThickness();
        double baseAngle = mBar3D.getAngle();
        //double baseOffsetX = mBar3D.getOffsetX(baseTickness,baseAngle);
        double baseOffsetY = mBar3D.getOffsetY(baseTickness, baseAngle);

        double labelHeight = DrawHelper.getInstance().getPaintFontHeight(
                categoryAxis.getTickLabelPaint());


        // 画上分类/刻度线
        double th = MathHelper.getInstance().add(
                MathHelper.getInstance().add(baseOffsetY, baseTickness), labelHeight);

        return (float) th;
    }

    @Override
    protected boolean renderVerticalBar(Canvas canvas) {


        //分类轴(X 轴) 且在这画柱形
        float barInitX = plotArea.getLeft();

        float barInitY = plotArea.getBottom();

        //得到分类轴数据集
        List<String> dataSet = categoryAxis.getDataSet();
        if (null == dataSet || dataSet.size() == 0) return false;

        // 依传入的分类个数与轴总宽度算出要画的分类间距数是多少
        // 总宽度 / 分类个数 = 间距长度
        float XSteps = div(plotArea.getPlotWidth(), (dataSet.size() + 1));


        //得到数据源
        List<BarData> chartDataSource = this.getDataSource();
        if (null == chartDataSource || chartDataSource.size() == 0) return false;
        int barNumber = getDatasetSize(chartDataSource);
        if (barNumber <= 0) return false;
        int currNumber = 0;

        float[] ret = mBar3D.getBarWidthAndMargin(XSteps, barNumber);
        if (null == ret || ret.length != 2) {
            Log.e(TAG, "分隔间距计算失败.");
            return false;
        }
        float barWidth = ret[0];
        float barInnerMargin = ret[1];
        float labelBarUseWidth = add(mul(barNumber, barWidth),
                mul((barNumber - 1), barInnerMargin));

        double axisRange = dataAxis.getAxisRange();
        double axisMin = dataAxis.getAxisMin();

        //开始处 X 轴 即分类轴
//		for(int i=0;i<barNumber;i++)
        for (int i = barNumber - 1; i >= 0; i--) {
            //依初超始X坐标与分类间距算出当前刻度的X坐标
            //float currentX = add(barInitX, mul((i+1), XSteps) );

            //得到分类对应的值数据集
            BarData bd = chartDataSource.get(i);
            List<Double> barValues = bd.getDataSet();

            if (null == barValues) continue;
            List<Integer> barDataColor = bd.getDataColor();
            //设成对应的颜色
            int barDefualtColor = bd.getColor();
            mBar3D.getBarPaint().setColor(barDefualtColor);

            //画出分类下的所有柱形
            for (int j = 0; j < barValues.size(); j++) {
                Double bv = barValues.get(j);
                setBarDataColor(mBar3D.getBarPaint(), barDataColor, j, barDefualtColor);

                //参数值与最大值的比例  照搬到 y轴高度与矩形高度的比例上来
                double tlen = MathHelper.getInstance().sub(bv, axisMin);
                float valuePostion = (float) (MathHelper.getInstance().div(tlen, axisRange));
                valuePostion = mul(plotArea.getPlotHeight(), valuePostion);

                //计算同分类多柱 形时，新柱形的起始X坐标
                //（自己改动）此处有改动，改变了绘制顺序；

                float drawBarStartX = sub(add(barInitX, mul((j + 1), XSteps)), labelBarUseWidth / 2);

                drawBarStartX = sub(drawBarStartX, (barWidth + barInnerMargin) * currNumber);
                //挪动位置；
                drawBarStartX = drawBarStartX + (barWidth * 2);
                //计算同分类多柱 形时，新柱形的结束X坐标
                float drawBarEndX = add(drawBarStartX, barWidth);

                //画出柱形
                float topY = sub(barInitY, valuePostion);

                mBar3D.renderVertical3DBar(drawBarStartX, topY,
                        drawBarEndX, barInitY,
                        mBar3D.getBarPaint().getColor(), canvas);

                //保存位置
                saveBarRectFRecord(i, j, drawBarStartX + mMoveX, topY + mMoveY,
                        drawBarEndX + mMoveX, plotArea.getBottom() + mMoveY);


                float labelTopX = add(drawBarStartX, barWidth / 2);

                //在柱形的顶端显示上批注形状
                drawAnchor(this.getAnchorDataPoint(), i, j, canvas, labelTopX, topY, 0.0f);

                //在柱形的顶端显示上柱形的当前值
                mBar3D.renderBarItemLabel(getFormatterItemLabel(bv), labelTopX, topY, canvas);

                //显示焦点框
                drawFocusRect(canvas, i, j, drawBarStartX, topY, drawBarEndX, barInitY);

            }
            currNumber++;
        }

        return true;
    }


    @Override
    protected void drawClipAxisLine(Canvas canvas) {
        switch (getChartDirection()) {
            case HORIZONTAL:
                //x轴 线 [要向里突]
                dataAxis.renderAxis(canvas, plotArea.getLeft(), plotArea.getBottom(),
                        plotArea.getPlotRight(), plotArea.getBottom());

                //Y轴线
                mBar3D.render3DYAxis(plotArea.getLeft(), plotArea.getTop(),
                        plotArea.getPlotRight(), plotArea.getBottom(), canvas);

                break;
            case VERTICAL:
                dataAxis.renderAxis(canvas, plotArea.getLeft(), plotArea.getTop(),
                        plotArea.getLeft(), plotArea.getBottom());

                //X轴 线
                mBar3D.render3DXAxis(plotArea.getLeft(), plotArea.getBottom(),
                        plotArea.getPlotRight(), plotArea.getBottom(), canvas);

                break;
        }
    }

    // TODO: 2016/9/4 0004

    @Override
    protected boolean drawClipVerticalPlot(Canvas canvas) {
        //显示绘图区rect
        float offsetX = mTranslateXY[0];
        float offsetY = mTranslateXY[1];
        initMoveXY();

        float yMargin = getClipYMargin();
        float xMargin = getClipXMargin();
        float gWidth = 0.0f;

        drawClipAxisClosed(canvas);

        //设置图显示范围
        canvas.save();
        //canvas.clipRect（）切割画布局部绘制；
        canvas.clipRect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());

        if (XEnum.PanMode.VERTICAL == this.getPlotPanMode()
                || XEnum.PanMode.FREE == this.getPlotPanMode()) {
            if (getPlotGrid().isShowVerticalLines())
                gWidth = this.getPlotGrid().getVerticalLinePaint().getStrokeWidth();
            //绘制Y轴tick和marks
            canvas.save();
            canvas.clipRect(plotArea.getLeft() - gWidth, plotArea.getTop() - gWidth,
                    plotArea.getRight() + gWidth, plotArea.getBottom() + gWidth);
            canvas.translate(0, offsetY);
            //绘制横向网格线
            drawClipDataAxisGridlines(canvas);
            canvas.restore();
        } else {
            drawClipDataAxisGridlines(canvas);
        }

        if (XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
                || XEnum.PanMode.FREE == this.getPlotPanMode()) {
            if (getPlotGrid().isShowHorizontalLines())
                gWidth = this.getPlotGrid().getHorizontalLinePaint().getStrokeWidth();
            //绘制X轴tick和marks
            canvas.save();

            canvas.clipRect(plotArea.getLeft() - gWidth, plotArea.getTop() - gWidth,
                    plotArea.getRight() + gWidth, plotArea.getBottom() + gWidth);
            canvas.translate(offsetX, 0);

            drawClipCategoryAxisGridlines(canvas);
            canvas.restore();
        } else {
            drawClipCategoryAxisGridlines(canvas);
        }

        //设置绘图区显示范围
        canvas.save();

        getClipExt().calc(getType());
        canvas.clipRect((float) MathHelper.getInstance().sub(plotArea.getLeft(), mBar3D.getOffsetX() * 2),
                plotArea.getTop() - getClipExt().getExtTop(),
                plotArea.getRight() + getClipExt().getExtRight(),
                (float) (MathHelper.getInstance().add(plotArea.getBottom(), mBar3D.getOffsetY()) + mBar3D.getOffsetY()) + 200);

        //canvas.clipRect(plotArea.getLeft()+ 0.5f , plotArea.getTop() + 0.5f,
        //		plotArea.getRight() + 0.5f, plotArea.getBottom() + 0.5f );
        //外轴 线
        dataAxis.renderAxis(canvas, (float) MathHelper.getInstance().sub(plotArea.getLeft(), mBar3D.getOffsetX() * 2), plotArea.getTop(),
                (float) MathHelper.getInstance().sub(plotArea.getLeft(), mBar3D.getOffsetX() * 2), (float) (MathHelper.getInstance().add(plotArea.getBottom(), mBar3D.getOffsetY()) + mBar3D.getOffsetY()));
        //内轴 线
        drawClipAxisLine(canvas);
        canvas.save();
        canvas.translate(mMoveX, mMoveY);
        //绘图
        drawClipPlot(canvas);
//		mLstCateTick.add(new PlotAxisTick(100, 200, dataSet.get(i), labelX, labelY, showTicks));
        if (XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
                || XEnum.PanMode.FREE == this.getPlotPanMode()) {
            //绘制X轴tick和marks


//			canvas.clipRect(this.getLeft() + xMargin, this.getTop(),
//					this.getRight() - xMargin,this.getBottom()); //this.getRight() + xMargin

//			canvas.translate(mMoveX, mMoveY);
            drawClipCategoryAxisTickMarks(canvas);

        } else {
            drawClipCategoryAxisTickMarks(canvas);
        }

        //还原绘图区绘制
        canvas.restore(); //clip
        canvas.restore(); //clip
        canvas.restore(); //clip
		/*//轴 线
		drawClipAxisLine(canvas);	*/
        /////////////////////////////////////////

        //轴刻度
        if (XEnum.PanMode.VERTICAL == this.getPlotPanMode()
                || XEnum.PanMode.FREE == this.getPlotPanMode()) {
            //绘制Y轴tick和marks
            canvas.save();
            canvas.clipRect(this.getLeft(), this.getTop() + yMargin,
                    this.getRight(), this.getBottom() - yMargin);
            canvas.translate(0, offsetY);
            drawClipDataAxisTickMarks(canvas);
            canvas.restore();
        } else {
            drawClipDataAxisTickMarks(canvas);
        }


		/*if( XEnum.PanMode.HORIZONTAL == this.getPlotPanMode()
				|| XEnum.PanMode.FREE == this.getPlotPanMode() )
		{
			//绘制X轴tick和marks
			canvas.save();

			canvas.clipRect(this.getLeft() + xMargin, this.getTop(),
					this.getRight() - xMargin,this.getBottom()); //this.getRight() + xMargin

			canvas.translate(offsetX,0);
			drawClipCategoryAxisTickMarks(canvas);
			canvas.restore();
		}else{
			drawClipCategoryAxisTickMarks(canvas);
		}*/

        /////////////////////////////////////////

        //图例
        drawClipLegend(canvas);
        return true;
    }

    @Override
    protected void drawClipDataAxisGridlines(Canvas canvas) {
        // 与柱形图不同，无须多弄一个
        float XSteps = 0.0f, YSteps = 0.0f;

        // 数据轴数据刻度总个数
        int tickCount = dataAxis.getAixTickCount();
        int labeltickCount = tickCount + 1;
//		int labeltickCount = tickCount;
        if (0 == tickCount) {
            Log.w(TAG, "数据轴数据源为0!");
            return;
        }
        //}else if (1 == tickCount)  //label仅一个时右移
        //	    labeltickCount = tickCount - 1 ;


        // 标签轴(X 轴)
        float axisX = 0.0f, axisY = 0.0f, currentX = 0.0f, currentY = 0.0f;
        // 标签
        double currentTickLabel = 0d;
        // 轴位置
        XEnum.AxisLocation pos = getDataAxisLocation();

        //步长
        switch (pos) {
            case LEFT: //Y
            case RIGHT:
            case VERTICAL_CENTER:
                YSteps = getVerticalYSteps(tickCount);

                currentX = axisX = getAxisXPos(pos);
                currentY = axisY = plotArea.getBottom();
                break;
            case TOP: //X
            case BOTTOM:
            case HORIZONTAL_CENTER:
                XSteps = getVerticalXSteps(tickCount);

                currentY = axisY = getAxisYPos(pos);
                currentX = axisX = plotArea.getLeft();
                break;
        }

        mLstDataTick.clear();
        //绘制
        for (int i = 0; i < labeltickCount - 1; i++) {
            switch (pos) {
                case LEFT: //Y
                case RIGHT:
                case VERTICAL_CENTER:
                    // 依起始数据坐标与数据刻度间距算出上移高度
                    currentY = sub(plotArea.getBottom(), mul(i, YSteps));

                    // 从左到右的横向网格线
                    drawHorizontalGridLines(canvas, plotArea.getLeft(), plotArea.getRight(),
                            i, tickCount, YSteps, currentY);
//					baseBottom2+offsetY
                    drawHorizontalGridbias(canvas, (float) MathHelper.getInstance().sub(plotArea.getLeft(), mBar3D.getOffsetX() * 2), plotArea.getLeft(),
                            i, tickCount, YSteps, MathHelper.getInstance().add(currentY, (float) mBar3D.getOffsetY()) + (float) mBar3D.getOffsetY(), currentY);

                    Log.i("---------", currentY + "");

                    //if(!dataAxis.isShowAxisLabels())continue;

                    // 标签
                    currentTickLabel = MathHelper.getInstance().add(
                            dataAxis.getAxisMin(), i * dataAxis.getAxisSteps());

                    //sub(axisX ,get3DOffsetX())
                    /*MathHelper.getInstance().add(currentY ,(float)mBar3D.getOffsetY())+(float)mBar3D.getOffsetY()*/
                    mLstDataTick.add(new PlotAxisTick(i, (float) MathHelper.getInstance().sub(axisX, mBar3D.getOffsetX() * 2),
                            MathHelper.getInstance().add(currentY, (float) mBar3D.getOffsetY()) + (float) mBar3D.getOffsetY(), Double.toString(currentTickLabel)));
					/*mLstDataTick.add(new PlotAxisTick(i, axisX,
							currentY, Double.toString(currentTickLabel)));*/
                    break;
                case TOP: //X
                case BOTTOM:
                case HORIZONTAL_CENTER:
                    //bar
                    // 依起始数据坐标与数据刻度间距算出上移高度
                    currentX = add(axisX, mul(i, XSteps));

                    //绘制竖向网格线
                    drawVerticalGridLines(canvas, plotArea.getTop(), plotArea.getBottom(),
                            i, tickCount, XSteps, currentX);

                    // if(!dataAxis.isShowAxisLabels())continue;

                    // 画上标签/刻度线
                    currentTickLabel = MathHelper.getInstance().add(
                            dataAxis.getAxisMin(), i * dataAxis.getAxisSteps());

                    mLstDataTick.add(new PlotAxisTick(i, currentX, axisY,
                            Double.toString(currentTickLabel)));

                    break;
            } //switch end
        } //end for
    }
}
