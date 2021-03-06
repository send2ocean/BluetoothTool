/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package org.xclcharts.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.PlotArcLabelInfo;

import java.util.List;

/**
 * @ClassName Pie3DChart
 * @Description  3D饼图基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class PieChart3D extends PieChart {
	
	private static final String TAG="PieChart3D";
	
	//渲染层数
	private final int mRender3DLevel = 15;
	
		
	public PieChart3D() {
		// TODO Auto-generated constructor stub
			 		
	}
	
	@Override
	public XEnum.ChartType getType()

	{
		return XEnum.ChartType.PIE3D;
	}
	
	private boolean render3D(Canvas canvas,
							float initOffsetAngle,
							List<PieData> chartDataSource,
							float cirX,float cirY,float radius)
	{
		float subY = sub(cirY, radius);
		float addY = add(cirY, radius);
		float v = (addY - subY)/4;
		float left = sub(cirX, radius);
		float top = subY + v;
		float right = add(cirX, radius);
		float bottom = addY - v;


		if(null == chartDataSource) return false;
 		float offsetAngle = initOffsetAngle;		
        float currentAngle = 0.0f;	              
        float newRadius = 0.0f;	
        int size = 0;

		for(int i=0;i < mRender3DLevel;i++)
		{
//              canvas.save(Canvas.MATRIX_SAVE_FLAG);
              canvas.translate(0,(mRender3DLevel-i)*radius/60 );
              size = chartDataSource.size();
			  for(int j=0;j< size;j++)
			  {			  
				    PieData cData =  chartDataSource.get(j);
				    if(null == cData) continue;
					//currentAngle = cData.getSliceAngle();		
					currentAngle = MathHelper.getInstance().getSliceAngle(getTotalAngle(), (float) cData.getPercentage());
					if(!validateAngle(currentAngle)) continue;
				  Paint paint = geArcPaint();
				  paint.setAntiAlias(true);
				  paint.setColor(cData.getSliceColor());
					
				    if(cData.getSelected()) //指定突出哪个块
		            {				    			    	
				    	//偏移圆心点位置(默认偏移半径的1/10)
				    	newRadius = div(radius , getSelectedOffset());
				    	 //计算百分比标签
				    	PointF point = MathHelper.getInstance().calcArcEndPointXY(
				    								cirX,cirY,newRadius,
				    								add(offsetAngle, div(currentAngle,2f)));

				       /* initRectF(sub(point.x, radius) ,sub(point.y , radius),
				        		   add(point.x , radius),add(point.y , radius));*/
						float subPY = sub(point.y, radius);
						float addPY = add(point.y, radius);
						float Pv = (addY - subY)/4;
						initRectF(sub(point.x, radius) ,subPY+Pv,
								add(point.x, radius),addPY-Pv);

	                    canvas.drawArc(mRectF, offsetAngle, currentAngle, true,geArcPaint());
		            }else{		       
		                //确定饼图范围       
//		                initRectF(sub(cirX, radius), sub(cirY, radius),
//								add(cirX, radius), add(cirY, radius));
						initRectF(left, top,
								right,bottom);

//	                    canvas.drawArc(mRectF, offsetAngle, currentAngle, true, geArcPaint());
						canvas.drawArc(mRectF, offsetAngle, currentAngle, true, geArcPaint());
		            }			    			    
		            //下次的起始角度

				    offsetAngle = add(offsetAngle,currentAngle);
				}
			//把当前画布返回（调整）到上一个save()状态之前
	            canvas.restore();
	            offsetAngle = initOffsetAngle;
		}
		
		return true;
	}
	
	private boolean renderFlatArcAndLegend(Canvas canvas,
										float initOffsetAngle,
										List<PieData> chartDataSource,
										float cirX,float cirY,float radius)

	{
		float subY = sub(cirY, radius);
		float addY = add(cirY, radius);
		float v = (addY - subY)/4;
		float left = sub(cirX, radius);
		float top = subY + v;
		float right = add(cirX, radius);
		float bottom = addY - v;

		if(null == chartDataSource) return false;
 		float offsetAngle = initOffsetAngle;
        float currentAngle = 0.0f;	              
        float newRadius = 0.0f;	
        int size = chartDataSource.size();
        mLstLabels.clear();

		for(int j=0;j< size;j++)
		{
		 	PieData cData = chartDataSource.get(j);
		 	if(null == cData) continue;
		 	//currentAngle = cData.getSliceAngle();
		 	currentAngle = MathHelper.getInstance().getSliceAngle(getTotalAngle(), (float) cData.getPercentage());
		 	if(!validateAngle(currentAngle)) continue;		  
		 	geArcPaint().setColor( DrawHelper.getInstance().getDarkerColor(
		 												cData.getSliceColor()) ); 						
		  	
		    if(cData.getSelected()) //指定突出哪个块
            {					    					    	
		    	//偏移圆心点位置(默认偏移半径的1/10)
		    	newRadius = div(radius , getSelectedOffset());
		    	 //计算百分比标签
		    	PointF point = MathHelper.getInstance().calcArcEndPointXY(
		    					cirX,cirY,newRadius,add(offsetAngle , div(currentAngle,2f))); 	
		          		        
		       /* initRectF(sub(point.x , radius) ,sub(point.y , radius ),
					add(point.x , radius ),add(point.y , radius));*/
				float subPY = sub(point.y, radius);
				float addPY = add(point.y, radius);
				float Pv = (addY - subY)/4;
				initRectF(sub(point.x, radius) ,subPY+Pv,
						add(point.x, radius),addPY-Pv);

                canvas.drawArc(mRectF, offsetAngle, currentAngle, true,geArcPaint());
                
		        mLstLabels.add(new PlotArcLabelInfo(j,point.x, point.y,radius,offsetAngle, currentAngle));
            }else{
            	//确定饼图范围

				initRectF(left, top,
						right,bottom);

                canvas.drawArc(mRectF, offsetAngle, currentAngle, true, geArcPaint()); 
                
     	       mLstLabels.add(new PlotArcLabelInfo(j,cirX,cirY,radius,offsetAngle, currentAngle));
     	    }		
		
		    //保存角度
		    saveArcRecord(j,cirX+ this.mTranslateXY[0],cirY+ this.mTranslateXY[1],
		    					radius,offsetAngle,currentAngle,
		    					getSelectedOffset(),this.getOffsetAngle());
		    
           //下次的起始角度  
		    offsetAngle = add(offsetAngle,currentAngle);		        		  
		}		
		
		//绘制Label
		renderLabels(canvas);			
		
		//图例
		plotLegend.renderPieKey(canvas,chartDataSource);	
		
		return true;
	}


	@Override
	protected boolean renderPlot(Canvas canvas)
	{				
		//数据源
 		List<PieData> chartDataSource = this.getDataSource();
 		if(null == chartDataSource)
		{
 			Log.e(TAG, "数据源为空.");
 			return false;
		}
 	 		
		//计算中心点坐标		
		float cirX = plotArea.getCenterX();
	    float cirY = plotArea.getCenterY();	     
        float radius = getRadius();
       
		if(render3D(canvas,mOffsetAngle,chartDataSource,cirX, cirY, radius))
		{
			return renderFlatArcAndLegend(canvas,mOffsetAngle,chartDataSource,
					cirX, cirY, radius);
		}else{
			return false;
		}

	}

}
