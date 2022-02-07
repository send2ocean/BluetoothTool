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
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.fyts.bluetoothtool.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.other.ScreenUtil;
import com.fyts.bluetoothtool.other.ToolUtils;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineData;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author XiongChuanLiang<br />(xcl_168@aliyun.com)
 * @ClassName LineChart01View
 * @Description 折线图的例子
 */
public class LineChart01View extends DemoView {

    private String TAG = "LineChart01View";
    private LineChart chart = new LineChart();

    //标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<LineData> chartData = new LinkedList<LineData>();

    private Paint mPaintTooltips = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();

    public LineChart01View(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public LineChart01View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LineChart01View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        chartRender();
        //綁定手势滑动事件
        this.bindTouch(this, chart);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
    }

    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //限制Tickmarks可滑动偏移范围
            chart.setXTickMarksOffsetMargin(ltrb[2] - 20.f);
            chart.setYTickMarksOffsetMargin(ltrb[3] - 20.f);

            //设定数据源
            chart.setCategories(labels);
            chart.setDataSource(chartData);

            //数据轴最大值
            chart.getDataAxis().setAxisMax(30);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(5);

//            CustomLineData line1 = new CustomLineData("高报值", 20d, Color.RED, 3);
//            line1.setLabelOffset(5);
//            line1.setLineStyle(XEnum.LineStyle.DASH);
//            line1.getLineLabelPaint().setColor(Color.RED);
//            mCustomLineDataset.add(line1);
//
//            CustomLineData line2 = new CustomLineData("低报值", 10d, Color.BLUE, 3);
//            line2.setLabelOffset(5);
//            line2.setLineStyle(XEnum.LineStyle.DASH);
//            line2.getLineLabelPaint().setColor(Color.BLUE);
//            mCustomLineDataset.add(line2);
//            chart.setCustomLines(mCustomLineDataset);

            //激活点击监听
            chart.ActiveListenItemClick();
            //为了让触发更灵敏，可以扩大5px的点击监听范围
            chart.extPointClickRange(5);
            chart.showClikedFocus();

            //绘制十字交叉线
            chart.showDyLine();
            chart.getDyLine().setDyLineStyle(XEnum.DyLineStyle.Vertical);
            chart.getPlotArea().extWidth(100.f);

            //调整轴显示位置
            chart.setDataAxisLocation(XEnum.AxisLocation.LEFT);
            chart.setCategoryAxisLocation(XEnum.AxisLocation.BOTTOM);
            //收缩绘图区右边分割的范围，让绘图区的线不显示出来
            chart.getClipExt().setExtRight(0.f);

            //背景网格
            PlotGrid plot = chart.getPlotGrid();
            plot.showVerticalLines();
            plot.showHorizontalLines();
            plot.getHorizontalLinePaint().setColor(ToolUtils.showColor(getContext(), R.color.color_283C6E));
            plot.getVerticalLinePaint().setColor(ToolUtils.showColor(getContext(), R.color.color_283C6E));

            //数据轴
            chart.getDataAxis().getAxisPaint().setColor(ToolUtils.showColor(getContext(), R.color.color_1E90FF));
            chart.getDataAxis().getTickMarksPaint().setColor(ToolUtils.showColor(getContext(), R.color.color_1E90FF));
            chart.getDataAxis().getTickLabelPaint().setColor(ToolUtils.showColor(getContext(), R.color.color_333333));
            chart.getDataAxis().getTickLabelPaint().setTextSize(ScreenUtil.dip2px(getContext(),11));
            //标签轴
            chart.getCategoryAxis().getAxisPaint().setColor(ToolUtils.showColor(getContext(), R.color.color_1E90FF));
            chart.getCategoryAxis().getTickMarksPaint().setColor(ToolUtils.showColor(getContext(), R.color.color_1E90FF));
            chart.getCategoryAxis().getTickLabelPaint().setColor(ToolUtils.showColor(getContext(), R.color.color_333333));
            chart.getCategoryAxis().getTickLabelPaint().setTextSize(ScreenUtil.dip2px(getContext(),11));
            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(value -> {
                Double tmp = Double.parseDouble(value);
                DecimalFormat df = new DecimalFormat("#0");
                return (df.format(tmp));
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void setLinear(double height, double low) {
        if (mCustomLineDataset.size() > 0) {
            mCustomLineDataset.removeAll(mCustomLineDataset);
        }
        CustomLineData line1 = new CustomLineData("高报值", height, Color.RED, 3);
//            line1.setCustomLineCap(XEnum.DotStyle.CROSS);
//            line1.setLabelHorizontalPostion(Align.CENTER);
        line1.setLabelOffset(5);
        line1.setLineStyle(XEnum.LineStyle.DASH);
        line1.getLineLabelPaint().setColor(Color.RED);
        mCustomLineDataset.add(line1);

        CustomLineData line2 = new CustomLineData("低报值", low, Color.BLUE, 3);
//            line2.setCustomLineCap(XEnum.DotStyle.CROSS);
//            line2.setLabelHorizontalPostion(Align.CENTER);
        line2.setLabelOffset(5);
        line2.setLineStyle(XEnum.LineStyle.DASH);
        line2.getLineLabelPaint().setColor(Color.BLUE);
        mCustomLineDataset.add(line2);
    }


    public void setChartYData(int max, int step) {
        chart.getDataAxis().setAxisMax(max);
        //数据轴刻度间隔
        chart.getDataAxis().setAxisSteps(step);
    }

    public void setChartData(LinkedList<Double> list) {

        if (chartData.size() > 0) {
            chartData.remove();
        }
        LineData lineData1 = new LineData("", list, ToolUtils.showColor(getContext(), R.color.color_01CFD2));
        //设置小圆点的颜色
        lineData1.getPlotLine().getPlotDot().setRingInnerColor(ToolUtils.showColor(getContext(), R.color.color_01CFD2));
        //线的画笔
        lineData1.getLinePaint().setStrokeWidth(7);
        //标签画笔
        lineData1.setLabelVisible(false);
        //点画笔
        lineData1.setDotStyle(XEnum.DotStyle.RING);
//        lineData1.getDotLabelPaint().setTextSize(22);
//        lineData1.getDotLabelPaint().setTextAlign(Align.LEFT);
//        lineData1.setItemLabelRotateAngle(45.f);
//        lineData1.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.TEXT);
        chartData.add(lineData1);
    }

    public void setLabelsDate(LinkedList<String> list) {
        labels.clear();
        chart.setDataSize(list.size());
        labels.addAll(list);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_UP) {
            triggerClick(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (chart.getDyLineVisible()) chart.getDyLine().setCurrentXY(0, 0);
        }
        super.onTouchEvent(event);
        return true;
    }


    //触发监听
    private void triggerClick(float x, float y) {

        //交叉线
//        if (chart.getDyLineVisible()) chart.getDyLine().setCurrentXY(0, 0);

        if (!chart.getListenItemClickStatus()) {
            //交叉线
            if (chart.getDyLineVisible()) this.invalidate();
        } else {
            PointPosition record = chart.getPositionRecord(x, y);
            if (null == record) {
                if (chart.getDyLineVisible()) this.invalidate();
                return;
            }

            LineData lData = chartData.get(record.getDataID());
            Double lValue = lData.getLinePoint().get(record.getDataChildID());

//            float r = record.getRadius();
//            chart.showFocusPointF(record.getPosition(), r + r * 0.5f);
//            chart.getFocusPaint().setStyle(Style.STROKE);
//            chart.getFocusPaint().setStrokeWidth(3);
//            if (record.getDataID() >= 3) {
//                chart.getFocusPaint().setColor(Color.BLUE);
//            } else {
//                chart.getFocusPaint().setColor(Color.RED);
//            }

            //在点击处显示tooltip
            mPaintTooltips.setColor(Color.WHITE);
            chart.getToolTip().setCurrentXY(record.getPosition().x, record.getPosition().y);
            chart.getToolTip().addToolTip("日期: " + labels.get(record.getDataChildID()), mPaintTooltips);
            chart.getToolTip().addToolTip("\n", mPaintTooltips);
            chart.getToolTip().addToolTip("报值: " + Double.toString(lValue), mPaintTooltips);

            //当前标签对应的其它点的值
            int cid = record.getDataChildID();
            String xLabels = "";
            for (LineData data : chartData) {
                if (cid < data.getLinePoint().size()) {
                    xLabels = Double.toString(data.getLinePoint().get(cid));
                    if (chart.getDyLineVisible()) chart.getDyLine().setCurrentXY(x, y);
                }
            }
            this.invalidate();
        }
    }
}
