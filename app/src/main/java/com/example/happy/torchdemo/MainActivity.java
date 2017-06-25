package com.example.happy.torchdemo;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * 开启手电筒实例
 * 实例网址参考资料：http://blog.csdn.net/mynameishuangshuai/article/details/53214763
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnOpen;
    private Button btnClose;
    private CameraManager manager;// 声明CameraManager对象
    private Camera m_Camera = null;// 声明Camera对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);//摄像头管理类

        btnOpen = (Button) findViewById(R.id.btn_open);
        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_open:
                lightSwitch(false);
                break;
            case R.id.btn_close:
                lightSwitch(true);
                break;
        }
    }
    /**
     * 手电筒控制方法
     *
     * @param lightStatus
     * @return
     */
    private void lightSwitch(final boolean lightStatus) {
        if (lightStatus) { // 关闭手电筒
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//手机android版本大于6.0的 运行这个方法
                try {
                    manager.setTorchMode("0", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {//手机小于6.0运行该方法
                if (m_Camera != null) {
                    m_Camera.stopPreview();//关闭手电筒
                    m_Camera.release();
                    m_Camera = null;
                }
            }
        } else { // 打开手电筒
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//手机android版本大于6.0的 运行这个方法
                try {
                    manager.setTorchMode("0", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                final PackageManager pm = getPackageManager();
                final FeatureInfo[] features = pm.getSystemAvailableFeatures();
                for (final FeatureInfo f : features) {
                    if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
                        if (null == m_Camera) {
                            m_Camera = Camera.open();
                        }
                        final Camera.Parameters parameters = m_Camera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        m_Camera.setParameters(parameters);
                        m_Camera.startPreview();//开启手电筒
                    }
                }
            }
        }
    }
}
