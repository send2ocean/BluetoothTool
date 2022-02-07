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
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.5
 */
package com.fyts.bluetoothtool.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @ClassName AreaChart02View
 * @Description 平滑面积图例子
 */

public class AreaChart02View extends DemoView {

    private String TAG = "AreaChart02View";

    private AreaChart chart = new AreaChart();
    //标签集合
    private LinkedList<String> mLabels = new LinkedList<String>();
    //数据集合
    private LinkedList<AreaData> mDataset = new LinkedList<AreaData>();

    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();

    public AreaChart02View(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }

    public AreaChart02View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AreaChart02View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        chartLabels();
//        chartDataSet();
        chartRender();

        //綁定手势滑动事件
//        this.bindTouch(this, chart);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
    }

    private void chartRender() {
        try {
//            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
//            int[] ltrb = getBarLnDefaultSpadding();
//            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
//
//            //轴数据源
//            //标签轴
            chart.setCategories(mLabels);
//            //数据轴
            chart.setDataSource(mDataset);
//            //仅横向平移
//            chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
//
//            //数据轴最大值
            chart.getDataAxis().setAxisMax(125);
//            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(25);

//
//            //透明度
            chart.setAreaAlpha(180);
//            //显示图例
//            chart.getPlotLegend().show();
//
//            //激活点击监听
            chart.ActiveListenItemClick();
//            //为了让触发更灵敏，可以扩大5px的点击监听范围
            chart.extPointClickRange(5);

            CustomLineData line1 = new CustomLineData("高报值", 100d, Color.RED, 3);
//            line1.setCustomLineCap(XEnum.DotStyle.CROSS);
//            line1.setLabelHorizontalPostion(Align.CENTER);
            line1.setLabelOffset(5);
            line1.getLineLabelPaint().setColor(Color.RED);
            mCustomLineDataset.add(line1);

            CustomLineData line2 = new CustomLineData("低报值", 25d, Color.BLUE, 3);
//            line2.setCustomLineCap(XEnum.DotStyle.CROSS);
//            line2.setLabelHorizontalPostion(Align.CENTER);
            line2.setLabelOffset(5);
            line2.getLineLabelPaint().setColor(Color.BLUE);
            mCustomLineDataset.add(line2);
            chart.setCustomLines(mCustomLineDataset);

            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0] + DensityUtil.dip2px(this.getContext(), 10), ltrb[1],
                    ltrb[2] + DensityUtil.dip2px(this.getContext(), 20), ltrb[3]);

            //显示边框
            chart.showRoundBorder();

            chart.setCustomLines(mCustomLineDataset);

            //背景网格
            PlotGrid plot = chart.getPlotGrid();
            plot.hideHorizontalLines();
            plot.hideVerticalLines();
            chart.getDataAxis().getAxisPaint().setColor(Color.rgb(127, 204, 204));
            chart.getCategoryAxis().getAxisPaint().setColor(Color.rgb(127, 204, 204));

            chart.getDataAxis().getTickMarksPaint().setColor(Color.rgb(127, 204, 204));
            chart.getCategoryAxis().getTickMarksPaint().setColor(Color.rgb(127, 204, 204));

            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    int tmp = Integer.parseInt(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (value);
                }
            });

            //不使用精确计算，忽略Java计算误差,提高性能
            chart.disableHighPrecision();

            chart.disablePanMode();
            chart.hideBorder();
            chart.getPlotLegend().hide();

            //chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.ODD_EVEN);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private void chartDataSet() {

        List<Double> dataSeries5 = new LinkedList<Double>();
        dataSeries5.add((double) 36);
        dataSeries5.add((double) 20);
        dataSeries5.add((double) 130);
        dataSeries5.add((double) 30);
        dataSeries5.add((double) 110);

        AreaData line5 = new AreaData("line5", dataSeries5,
                Color.CYAN, Color.CYAN);
        //设置线上每点对应标签的颜色
        //line3.getDotLabelPaint().setColor(Color.YELLOW);
        line5.setLineStyle(XEnum.LineStyle.SOLID);
        line5.setDotStyle(XEnum.DotStyle.RING);

        mDataset.add(line5);

    }

    public void chartDataSet(List list) {
        AreaData line = new AreaData("数据", list, Color.CYAN, Color.CYAN);
        line.setLineStyle(XEnum.LineStyle.SOLID);
        line.setDotStyle(XEnum.DotStyle.RING);
        mDataset.add(line);
        postInvalidate();
    }


    private void chartLabels() {
//        mLabels.add("2010");
//        mLabels.add("2011");
//        mLabels.add("2012");
//        mLabels.add("2013");
//        mLabels.add("2014");
    }

    public void chartLabels(List<String> date) {
        if (mLabels.size() > 0) mLabels.clear();
        mLabels.addAll(date);
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
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            triggerClick(event.getX(), event.getY());
        }
        return true;
    }


    //触发监听
    private void triggerClick(float x, float y) {
        PointPosition record = chart.getPositionRecord(x, y);
        if (null == record) return;
        AreaData lData = mDataset.get(record.getDataID());
        Double lValue = lData.getLinePoint().get(record.getDataChildID());
        Toast.makeText(this.getContext(),
                record.getPointInfo() +
                        " Key:" + lData.getLineKey() +
                        " Label:" + lData.getLabel() +
                        " Current Value:" + Double.toString(lValue),
                Toast.LENGTH_SHORT).show();
    }
}

