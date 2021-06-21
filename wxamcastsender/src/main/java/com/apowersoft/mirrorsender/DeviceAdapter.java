package com.apowersoft.mirrorsender;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apowersoft.mirrorcast.api.DeviceBean;
import com.apowersoft.mirrorcast.api.DeviceType;


import java.util.List;

/**
 * author : Terry.Tao
 * date   : 2019/11/6
 * desc   :
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<DeviceBean> deviceBeans;
    private String connectIp;

    private OnItemClickListener onItemClickListener;
    private OnStopClickListener onStopClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnStopClickListener(OnStopClickListener onStopClickListener) {
        this.onStopClickListener = onStopClickListener;
    }

    public DeviceAdapter(List<DeviceBean> deviceBeans) {
        this.deviceBeans = deviceBeans;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_device, viewGroup, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder deviceViewHolder, final int i) {
        DeviceBean item = deviceBeans.get(i);
        switch (item.getDeviceType()) {
            case DeviceType.ANDROID:
                deviceViewHolder.ivDevice.setImageResource(R.mipmap.ic_device_android);
                break;
            case DeviceType.IOS:
                deviceViewHolder.ivDevice.setImageResource(R.mipmap.ic_device_ios);
                break;
            case DeviceType.MAC:
                deviceViewHolder.ivDevice.setImageResource(R.mipmap.ic_device_mac);
                break;
            case DeviceType.PC:
                deviceViewHolder.ivDevice.setImageResource(R.mipmap.ic_device_windows);
                break;
            case DeviceType.ANDROID_TV:
                deviceViewHolder.ivDevice.setImageResource(R.mipmap.ic_device_tv);
                break;
            default:
                break;
        }
        deviceViewHolder.tvDevice.setText(item.getDeviceName());
        if (!TextUtils.isEmpty(connectIp)) {
            if (connectIp.equals(item.getIpAddress())) {
                deviceViewHolder.llContainer.setSelected(true);
                deviceViewHolder.ivDeviceConnect.setVisibility(View.VISIBLE);
            } else {
                deviceViewHolder.llContainer.setSelected(false);
                deviceViewHolder.ivDeviceConnect.setVisibility(View.GONE);
            }
        } else {
            deviceViewHolder.llContainer.setSelected(false);
            deviceViewHolder.ivDeviceConnect.setVisibility(View.GONE);
        }

        deviceViewHolder.ivDeviceConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStopClickListener != null) {
                    onStopClickListener.stopClick(v, i);
                }
            }
        });

        deviceViewHolder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClick(v, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceBeans.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDevice;
        private TextView tvDevice;
        private ImageView ivDeviceConnect;
        private LinearLayout llContainer;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDevice = itemView.findViewById(R.id.iv_device);
            tvDevice = itemView.findViewById(R.id.tv_device);
            ivDeviceConnect = itemView.findViewById(R.id.iv_device_connect);
            llContainer = itemView.findViewById(R.id.ll_container);
        }
    }

    public String getConnectIp() {
        return connectIp;
    }

    public void setConnectIp(String connectIp) {
        this.connectIp = connectIp;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void itemClick(View view, int position);
    }

    public interface OnStopClickListener {
        void stopClick(View view, int position);
    }
}
