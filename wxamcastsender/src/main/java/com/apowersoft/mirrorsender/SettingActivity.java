package com.apowersoft.mirrorsender;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apowersoft.mirrorcast.api.CastModel;
import com.apowersoft.mirrorcast.api.CastSharpness;
import com.apowersoft.mirrorcast.api.MirrorCastSender;
import com.apowersoft.mirrorcast.api.Resolution;
import com.apowersoft.mirrorsender.dialog.ChoiceAdapter;
import com.apowersoft.mirrorsender.dialog.ChoiceDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Terry.Tao
 * date   : 2019/11/6
 * desc   :
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llResolution;
    private LinearLayout llSharpness;
    private LinearLayout llMirrorModel;

    private TextView tvResolution;
    private TextView tvSharpness;
    private TextView tvMirrorModel;

    private MirrorCastSender mirrorCastSender;

    private List<String> resolutions;
    private List<String> mirrorModels;
    private List<String> sharpnessList;

    private ChoiceDialog resolutionDialog;
    private ChoiceDialog mirrorModeDialog;
    private ChoiceDialog sharpnessDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initData();
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.action_settings);

        llResolution = findViewById(R.id.ll_resolution);
        llSharpness = findViewById(R.id.ll_bit);
        llMirrorModel = findViewById(R.id.ll_mirror_model);
        tvResolution = findViewById(R.id.tv_resolution);
        tvSharpness = findViewById(R.id.tv_bit);
        tvMirrorModel = findViewById(R.id.tv_mirror_mode);


        llResolution.setOnClickListener(this);
        llSharpness.setOnClickListener(this);
        llMirrorModel.setOnClickListener(this);

        mirrorCastSender = MirrorCastSender.getInstance();

        setCastWidthStr();
        setSharpnessStr();
        setCastModel();
    }

    /**
     * 投屏模式
     */
    private void setCastModel() {
        if (mirrorCastSender.getCastModel() == null) {
            return;
        }
        switch (mirrorCastSender.getCastModel()) {
            case SMOOTH_FIRST:
                tvMirrorModel.setText(mirrorModels.get(0));
                break;
            case QUALITY_FIRST:
                tvMirrorModel.setText(mirrorModels.get(1));
                break;
            case HIGH_QUALITY:
                tvMirrorModel.setText(mirrorModels.get(2));
                break;
        }
    }

    /**
     * 投屏清晰度
     */
    private void setSharpnessStr() {
        if (mirrorCastSender.getCastSharpness() == null) {
            return;
        }
        switch (mirrorCastSender.getCastSharpness()) {
            case LD:
                tvSharpness.setText(sharpnessList.get(0));
                break;
            case SD:
                tvSharpness.setText(sharpnessList.get(1));
                break;
            case HD:
                tvSharpness.setText(sharpnessList.get(2));
                break;
            case FHD:
                tvSharpness.setText(sharpnessList.get(3));
                break;
        }
    }

    /**
     * 投屏分辨率
     */
    private void setCastWidthStr() {
        if (mirrorCastSender.getCastWidth() == null) {
            return;
        }
        switch (mirrorCastSender.getCastWidth()) {
            case CAST_WIDTH_480:
                tvResolution.setText(resolutions.get(0));
                break;
            case CAST_WIDTH_544:
                tvResolution.setText(resolutions.get(1));
                break;
            case CAST_WIDTH_720:
                tvResolution.setText(resolutions.get(2));
                break;
            case CAST_WIDTH_1080:
                tvResolution.setText(resolutions.get(3));
                break;
            case CAST_WIDTH_1440:
                tvResolution.setText(resolutions.get(4));
                break;
        }
    }

    private void initData() {
        resolutions = new ArrayList<>();
        resolutions.add("480P");
        resolutions.add("544P");
        resolutions.add("720P");
        resolutions.add("1080P");
        resolutions.add("1440P");

        mirrorModels = new ArrayList<>();
        mirrorModels.add(getResources().getString(R.string.setting_mirror_model_one));
        mirrorModels.add(getResources().getString(R.string.setting_mirror_model_two));
        mirrorModels.add(getResources().getString(R.string.setting_mirror_model_three));

        sharpnessList = new ArrayList<>();
        sharpnessList.add(getResources().getString(R.string.cast_quality_low));
        sharpnessList.add(getResources().getString(R.string.cast_quality_middle));
        sharpnessList.add(getResources().getString(R.string.cast_quality_high));
        sharpnessList.add(getResources().getString(R.string.cast_quality_than_high));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_resolution:
                showResolutionDialog();
                break;
            case R.id.ll_bit:
                showSharpnessDialog();
                break;
            case R.id.ll_mirror_model:
                showMirrorModeDialog();
                break;
        }
    }

    /**
     * 设置投屏模式
     */
    private void showMirrorModeDialog() {
        if (mirrorModeDialog == null) {
            mirrorModeDialog = new ChoiceDialog(SettingActivity.this, mirrorModels);
            mirrorModeDialog.setOnItemClickListener(new ChoiceDialog.OnItemClickListener() {
                @Override
                public void onItemClick(ChoiceAdapter adapter, View view, int position) {
                    boolean isSupport = false;
                    switch (position) {
                        case 0:
                            isSupport = mirrorCastSender.setCastModel(CastModel.SMOOTH_FIRST);
                            break;
                        case 1:
                            isSupport = mirrorCastSender.setCastModel(CastModel.QUALITY_FIRST);
                            break;
                        case 2:
                            isSupport = mirrorCastSender.setCastModel(CastModel.HIGH_QUALITY);
                            break;
                    }
                    //检测 模式不支持 提示
                    if (!isSupport) {
                        Toast.makeText(SettingActivity.this, getString(R.string.not_support_model), Toast.LENGTH_SHORT).show();
                    } else {
                        tvMirrorModel.setText(mirrorModels.get(position));
                    }
                    mirrorModeDialog.dismiss();
                }
            });
        }
        mirrorModeDialog.show();
    }

    /**
     * 设置投屏分辨率
     */
    private void showResolutionDialog() {
        if (resolutionDialog == null) {
            resolutionDialog = new ChoiceDialog(SettingActivity.this, resolutions);
            resolutionDialog.setOnItemClickListener(new ChoiceDialog.OnItemClickListener() {
                @Override
                public void onItemClick(ChoiceAdapter adapter, View view, int position) {
                    boolean isSupport = false;
                    switch (position) {
                        case 0:
                            isSupport = mirrorCastSender.setCastWidth(Resolution.CAST_WIDTH_480);
                            break;
                        case 1:
                            isSupport = mirrorCastSender.setCastWidth(Resolution.CAST_WIDTH_544);
                            break;
                        case 2:
                            isSupport = mirrorCastSender.setCastWidth(Resolution.CAST_WIDTH_720);
                            break;
                        case 3:
                            isSupport = mirrorCastSender.setCastWidth(Resolution.CAST_WIDTH_1080);
                            break;
                        case 4:
                            isSupport = mirrorCastSender.setCastWidth(Resolution.CAST_WIDTH_1440);
                            break;
                    }
                    //检测分辨率不支持
                    if (!isSupport) {
                        Toast.makeText(SettingActivity.this, getString(R.string.not_support_resolution), Toast.LENGTH_SHORT).show();
                    } else {
                        tvResolution.setText(resolutions.get(position));
                    }
                    resolutionDialog.dismiss();
                }
            });
        }
        resolutionDialog.show();
    }

    /**
     * 设置投屏分辨率
     */
    private void showSharpnessDialog() {
        if (sharpnessDialog == null) {
            sharpnessDialog = new ChoiceDialog(SettingActivity.this, sharpnessList);
            sharpnessDialog.setOnItemClickListener(new ChoiceDialog.OnItemClickListener() {
                @Override
                public void onItemClick(ChoiceAdapter adapter, View view, int position) {
                    switch (position) {
                        case 0:
                            mirrorCastSender.setCastSharpness(CastSharpness.LD);
                            break;
                        case 1:
                            mirrorCastSender.setCastSharpness(CastSharpness.SD);
                            break;
                        case 2:
                            mirrorCastSender.setCastSharpness(CastSharpness.HD);
                            break;
                        case 3:
                            mirrorCastSender.setCastSharpness(CastSharpness.FHD);
                            break;

                    }
                    tvSharpness.setText(sharpnessList.get(position));
                    sharpnessDialog.dismiss();
                }
            });
        }
        sharpnessDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
