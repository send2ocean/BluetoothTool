package com.fyts.bluetoothtool;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fyts.bluetoothtool.adapter.DataListAdapter;
import com.fyts.bluetoothtool.adapter.MessageListAdapter;
import com.fyts.bluetoothtool.bean.DataBean;
import com.fyts.bluetoothtool.bean.MessageBean;
import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.fyts.bluetoothtool.other.PermissionUtils;
import com.fyts.bluetoothtool.other.ToastUtils;
import com.fyts.bluetoothtool.other.ClientManager;
import com.fyts.bluetoothtool.other.TimeUtil;
import com.fyts.bluetoothtool.ui.BaseActivity;
import com.fyts.bluetoothtool.view.MaxHeightRecyclerView;
import com.fyts.bluetoothtool.view.chart.LineChart01View;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * 消息发送接收
 */
@SuppressLint("ClickableViewAccessibility")
public class MessageActivity extends BaseActivity {

    //蓝牙相关参数
    private String address;
    private UUID service;
    private UUID character;
    //顶部输入和发送
    private EditText ed_content;
    private TextView tv_send;
    //接收列表
    private MaxHeightRecyclerView recycler;
    private MessageListAdapter adapter;
    private List<MessageBean> list;
    //数据解析
    private TextView tv_voltage; //电压
    private TextView tv_concentration; //浓度
    private TextView tv_temperature; //温度
    //折线图
    private LineChart01View chart_voltage; //电压
    private LineChart01View chart_concentration; //浓度
    private LineChart01View chart_temperature; //温度
    //列表
    private RecyclerView recycler_voltage; //电压
    private DataListAdapter voltageAdapter;
    private RecyclerView recycler_concentration; //浓度
    private DataListAdapter concentrationAdapter;
    private RecyclerView recycler_temperature; //温度
    private DataListAdapter temperatureAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        service = (UUID) intent.getSerializableExtra("service");
        character = (UUID) intent.getSerializableExtra("character");
        //消息列表
        list = new ArrayList<>();
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MessageListAdapter(activity, new OnAdapterClickListenerImpl());
        recycler.setAdapter(adapter);
        //发送
        ed_content = findViewById(R.id.ed_content);
        tv_send = findViewById(R.id.tv_send);
        //数据解析
        tv_voltage = findViewById(R.id.tv_voltage);
        tv_concentration = findViewById(R.id.tv_concentration);
        tv_temperature = findViewById(R.id.tv_temperature);
        //折线图控件初始化
        chart_voltage = findViewById(R.id.chart_voltage);
        chart_concentration = findViewById(R.id.chart_concentration);
        chart_temperature = findViewById(R.id.chart_temperature);
        //列表
        //电压
        recycler_voltage = findViewById(R.id.recycler_voltage);
        recycler_voltage.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false; // 直接禁止垂直滑动
            }
        });
        voltageAdapter = new DataListAdapter(activity, new OnAdapterClickListenerImpl() {
        });
        recycler_voltage.setAdapter(voltageAdapter);
        //浓度
        recycler_concentration = findViewById(R.id.recycler_concentration);
        recycler_concentration.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false; // 直接禁止垂直滑动
            }
        });
        concentrationAdapter = new DataListAdapter(activity, new OnAdapterClickListenerImpl());
        recycler_concentration.setAdapter(concentrationAdapter);
        //温度
        recycler_temperature = findViewById(R.id.recycler_temperature);
        recycler_temperature.setLayoutManager(new LinearLayoutManager(activity) {
            @Override
            public boolean canScrollVertically() {
                return false; // 直接禁止垂直滑动
            }
        });
        temperatureAdapter = new DataListAdapter(activity, new OnAdapterClickListenerImpl());
        recycler_temperature.setAdapter(temperatureAdapter);

        //监听通知
        ClientManager.getClient().notify(address, service, character, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                String res = bytesToHexString(value);
                Log.d(TAG,"接收到数据 "+res);
                list.add(0, new MessageBean(res, TimeUtil.getTime(TimeUtil.NORMAL_PAT)));
                adapter.setData(list);
                paraseByte(value);
            }

            @Override
            public void onResponse(int code) {

            }
        });

        tv_send.setOnClickListener(v -> send());
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.tv_read).setOnClickListener(v -> read());
        findViewById(R.id.tv_write).setOnClickListener(v -> finish());
    }

    /**
     * 解析蓝牙传输过来的数据
     * @param bytes
     */
    private void paraseByte(byte[] bytes) {
        /**
         * 第一字节		数据长度
         * 第二字节		功能码
         * 第三字节		蓝牙连接状态位
         * 第四字节		传感器工作模式
         * 第五字节		按键计数
         * 第六字节		气体浓度值     小数点前
         * 第七字节		气体浓度值     小数点后
         * 第八字节		信号电压值      小数点前
         * 第九字节		信号电压值      小数点后
         * 第十字节		温度值    	      小数点前
         * 第十一字节	温度值	      小数点后
         * 第十二字节	空
         * 第十三字节	浓度报警等级
         * 第十四字节	气体浓度电压值  小数点前
         * 第十五字节	气体浓度电压值  小数点后
         * 第十六字节	温度电压值         小数点前
         * 第十七字节	温度电压值         小数点后
         * 第十八字节	温度系数	         小数点前
         * 第十九字节	温度系数	         小数点后
         */
        //数据长度 固定值
        int dLen = 0x13;
        //功能码  固定值
        int dFlag = 0x11;
        Log.d(TAG, "begin paraseByte" );
        int bSize = bytes.length;
        if(bSize<19){
            Log.e(TAG,"数据长度不够，数据长度="+bSize);
            return;
        }
        //第一字节		数据长度
        int bLen = Integer.valueOf(bytes[0]);
        //第二字节		功能码
        int bFlag = Integer.valueOf(bytes[1]);
        if(dLen==bLen && dFlag==bFlag){
//            第三字节		蓝牙连接状态位
            int bBlueState = Integer.valueOf(bytes[2]);
//            第四字节		传感器工作模式
            int bSensorWorkMode = Integer.valueOf(bytes[3]);
//            第五字节		按键计数
            int bCount = Integer.valueOf(bytes[4]);
//            第六字节		气体浓度值     小数点前
            int bGasConcentration1 = Integer.valueOf(bytes[5]);
//            第七字节		气体浓度值     小数点后
            int bGasConcentration2 = Integer.valueOf(bytes[6]);

            tv_concentration.setText("浓度："+bGasConcentration1+"."+bGasConcentration2);

//            第八字节		信号电压值      小数点前
            int bSignalVoltage1 = Integer.valueOf(bytes[7]);
//            第九字节		信号电压值      小数点后
            int bSignalVoltage2 = Integer.valueOf(bytes[8]);

            tv_voltage.setText("信号电压值："+bSignalVoltage1+"."+bSignalVoltage2);

//            第十字节		温度值    	      小数点前
            int bTemperature1 = Integer.valueOf(bytes[9]);
//            第十一字节	温度值	      小数点后
            int bTemperature2 = Integer.valueOf(bytes[10]);

            tv_temperature.setText("温度："+bTemperature1+"."+bTemperature2);

//            第十二字节	空
//            第十三字节	浓度报警等级
            int bConcentrationAlarmLevel = Integer.valueOf(bytes[12]);
//            第十四字节	气体浓度电压值  小数点前
            int bGasConcentrationVoltage1 = Integer.valueOf(bytes[13]);
//            第十五字节	气体浓度电压值  小数点后
            int bGasConcentrationVoltage2 = Integer.valueOf(bytes[14]);
//            第十六字节	温度电压值         小数点前
            int bTemperatureVoltage1 = Integer.valueOf(bytes[15]);
//            第十七字节	温度电压值         小数点后
            int bTemperatureVoltage2  = Integer.valueOf(bytes[16]);
//            第十八字节	温度系数	         小数点前
            int bTemperatureCoefficient1 = Integer.valueOf(bytes[17]);
//            第十九字节	温度系数	         小数点后
            int bTemperatureCoefficient2 = Integer.valueOf(bytes[18]);
        }else{
            Log.e(TAG,"数据格式不对");
        }
        Log.d(TAG, "end  paraseByte" );
    }

    //读取消息
    private void read() {
        //读数据
        ClientManager.getClient().read(address, service, character, new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                if (code == REQUEST_SUCCESS) {
                    String res = bytesToHexString(data);
                    ToastUtils.showShort(activity, res);
                }
            }
        });
    }

    /**
     * 字节 转换成  16进制字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }

    /**
     *  字符串   转换成 16进制字节数组
     * @param message
     * @return
     */
    public byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    //发送消息
    private void send() {
        //发送数据
        String content = ed_content.getText().toString();
        Log.d(TAG,"发送数据 "+content);
//        String test= "131100000011000000000000014000000000a1";
//        paraseByte( getHexBytes(test) );
        if (TextUtils.isEmpty(content)) return;
        //content为输入的内容，转为bytes发送
//        byte[] bytes = content.getBytes();
        byte[] bytes = getHexBytes(content);
        Log.d(TAG,"发送数据1 "+bytesToHexString(bytes));
        ToastUtils.showShort(activity, "发送数据  "+bytesToHexString(bytes));
        ClientManager.getClient().writeNoRsp(address, service, character, bytes, code -> {
            if (code == REQUEST_SUCCESS) {
                ToastUtils.showShort(activity, "发送成功");
            } else {
                ToastUtils.showShort(activity, "failed");
            }
        });
    }

    @Override
    protected void getData() {
        tv_voltage.setText("信号电压值：");
        tv_concentration.setText("浓度：");
        tv_temperature.setText("温度：");
        //模拟数据
        //x轴
        LinkedList<Double> xList = new LinkedList<>();
        xList.add(5d);
        xList.add(17d);
        xList.add(13d);
        xList.add(27d);
        xList.add(25.5d);
        //y轴
        LinkedList<String> yList = new LinkedList<>();
        yList.add("0.1");
        yList.add("0.2");
        yList.add("0.3");
        yList.add("0.4");
        yList.add("0.5");
        yList.add("0.6");
        yList.add("0.7");
        yList.add("0.8");

        //电压折线图设置数据
        chart_voltage.setChartYData(30, 5); //数据轴刻度间隔
        chart_voltage.setLabelsDate(yList); //y轴数据
        chart_voltage.setChartData(xList); //x轴数据
        chart_voltage.invalidate(); //刷新折线图
        //浓度折线图设置数据
        chart_concentration.setChartYData(30, 5);
        chart_concentration.setLabelsDate(yList);
        chart_concentration.setChartData(xList);
        chart_concentration.invalidate();
        //温度折线图设置数据
        chart_temperature.setChartYData(30, 5);
        chart_temperature.setLabelsDate(yList);
        chart_temperature.setChartData(xList);
        chart_temperature.invalidate();

        //电压模拟数据
        List<DataBean> voltageList = new ArrayList<>();
        voltageList.add(new DataBean("电压一", 0.0));
        voltageList.add(new DataBean("电压二", 0.0));
        voltageList.add(new DataBean("电压三", 0.0));
        voltageList.add(new DataBean("电压四", 0.0));
        voltageList.add(new DataBean("电压五", 0.0));
        voltageList.add(new DataBean("电压六", 0.0));
        voltageList.add(new DataBean("电压七", 0.0));
        voltageList.add(new DataBean("电压八", 0.0));
        voltageList.add(new DataBean("电压九", 0.0));
        voltageList.add(new DataBean("电压十", 0.0));
        voltageAdapter.setData(voltageList);

        //浓度模拟数据
        List<DataBean> concentrationList = new ArrayList<>();
        concentrationList.add(new DataBean("浓度参数数量", 0.0));
        concentrationList.add(new DataBean("高浓度报警一", 0.0));
        concentrationList.add(new DataBean("高浓度报警二", 0.0));
        concentrationList.add(new DataBean("低浓度报警一", 0.0));
        concentrationList.add(new DataBean("低浓度报警二", 0.0));
        concentrationList.add(new DataBean("高浓度报警一", 0.0));
        concentrationList.add(new DataBean("浓度一", 0.0));
        concentrationList.add(new DataBean("浓度二", 0.0));
        concentrationList.add(new DataBean("浓度三", 0.0));
        concentrationList.add(new DataBean("浓度四", 0.0));
        concentrationList.add(new DataBean("浓度五", 0.0));
        concentrationList.add(new DataBean("浓度六", 0.0));
        concentrationList.add(new DataBean("浓度七", 0.0));
        concentrationList.add(new DataBean("浓度八", 0.0));
        concentrationList.add(new DataBean("浓度九", 0.0));
        concentrationList.add(new DataBean("浓度十", 0.0));
        concentrationAdapter.setData(concentrationList);

        //温度模拟数据
        List<DataBean> temperatureList = new ArrayList<>();
        temperatureList.add(new DataBean("温度补偿数量", 0.0));
        temperatureList.add(new DataBean("温度点一", 0.0));
        temperatureList.add(new DataBean("零飘点一", 0.0));
        temperatureList.add(new DataBean("比例系数一", 0.0));
        temperatureList.add(new DataBean("温度点二", 0.0));
        temperatureList.add(new DataBean("零飘点二", 0.0));
        temperatureList.add(new DataBean("比例系数二", 0.0));
        temperatureList.add(new DataBean("温度点三", 0.0));
        temperatureList.add(new DataBean("零飘点三", 0.0));
        temperatureList.add(new DataBean("比例系数三", 0.0));
        temperatureList.add(new DataBean("温度点四", 0.0));
        temperatureList.add(new DataBean("零飘点四", 0.0));
        temperatureList.add(new DataBean("比例系数四", 0.0));
        temperatureAdapter.setData(temperatureList);
    }
}