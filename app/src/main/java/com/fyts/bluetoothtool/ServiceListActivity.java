package com.fyts.bluetoothtool;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;

import com.fyts.bluetoothtool.adapter.ServiceListAdapter;
import com.fyts.bluetoothtool.bean.DetailItem;
import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.fyts.bluetoothtool.other.ClientManager;
import com.fyts.bluetoothtool.ui.BaseActivity;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务列表页
 */
public class ServiceListActivity extends BaseActivity {

    //服务列表
    private RecyclerView recycler;
    private ServiceListAdapter adapter;
    //选择的蓝牙标识
    private String address;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_list;
    }

    @Override
    protected void initView() {
        //选择的蓝牙标识
        address = getIntent().getStringExtra("address");
        //连接成功后获取到蓝牙的服务数据
        BleGattProfile data = getIntent().getParcelableExtra("data");

        //新建一个list存储
        List<DetailItem> items = new ArrayList<>();
        //获取服务list
        List<BleGattService> services = data.getServices();
        //循环遍历
        for (BleGattService service : services) {
            //外层service 添加到list并标记TYPE_SERVICE
            items.add(new DetailItem(DetailItem.TYPE_SERVICE, service.getUUID(), null));
            //通过service获取到characters
            List<BleGattCharacter> characters = service.getCharacters();
            for (BleGattCharacter character : characters) {
                //添加到list并标记TYPE_CHARACTER
                items.add(new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
            }
        }

        //初始化列表view
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ServiceListAdapter(activity, new OnAdapterClickListenerImpl() {
            @Override
            public void onItemClickListener(View v, int pos) {
                //列表的点击事件
                DetailItem choseData = adapter.getChoseData(pos);
                //如果点击的是characters则进入下一步 收发页面
                if (choseData.type == DetailItem.TYPE_CHARACTER) {
                    //跳转页面并传递数据
                    startActivity(new Intent(activity, MessageActivity.class)
                            .putExtra("address", address)
                            .putExtra("service", choseData.service)
                            .putExtra("character", choseData.uuid));
                }
            }
        });
        recycler.setAdapter(adapter);
        //设置数据
        adapter.setData(items);

        //返回按钮
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    @Override
    protected void getData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //断开连接
        ClientManager.getClient().disconnect(address);
    }
}