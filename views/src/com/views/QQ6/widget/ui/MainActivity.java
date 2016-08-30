package com.views.QQ6.widget.ui;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.views.R;
import com.views.QQ6.widget.DragLayout;
import com.views.QQ6.widget.entity.ItemBean;
import com.views.QQ6.widget.utils.ItemDataUtils;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.utils.PermissionTool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private DragLayout dl;
	private ListView lv;
	private ImageView ivIcon, ivBottom;
	private QuickAdapter<ItemBean> quickAdapter;
	private ImageButton ib_qrcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setStatusBar();
		initDragLayout();
		initView();

	}

	private void initDragLayout() {
		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragLayout.DragListener() {
			// 界面打开的时候
			@Override
			public void onOpen() {
			}

			// 界面关闭的时候
			@Override
			public void onClose() {
			}

			// 界面滑动的时候
			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(ivIcon, 1 - percent);
			}
		});
	}

	private void initView() {
		ivIcon = (ImageView) findViewById(R.id.iv_icon);
		ivBottom = (ImageView) findViewById(R.id.iv_bottom);
		ib_qrcode = (ImageButton) findViewById(R.id.ib_qrcode);

		lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(quickAdapter = new QuickAdapter<ItemBean>(this, R.layout.item_left_layout,
				ItemDataUtils.getItemBeans()) {
			@Override
			protected void convert(BaseAdapterHelper helper, ItemBean item) {
				helper.setImageResource(R.id.item_img, item.getImg()).setText(R.id.item_tv, item.getTitle());
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// Toast.makeText(MainActivity.this, "Click Item " + position,
				// Toast.LENGTH_SHORT).show();
				switch (position) {
				case 0:
					startActivity(new Intent(MainActivity.this, com.views.pulltorefresh.ui.MainActivity.class));
					break;

				default:
					break;
				}
			}
		});
		ivIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dl.open();
			}
		});
		ib_qrcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (PermissionTool.isCameraCanUse()) {

					// 跳转到相关的拍照/扫描 页面
					startActivity(new Intent(MainActivity.this, CaptureActivity.class));

				} else {
					// 好多授权http://zhidao.baidu.com/link?url=5LWq3A3dcEurjO9nnkbYae8RWdQSfIHWPDBdRP3jWoYUnFxKXMYKvE23jf7S8vtRQQ8urKkYI4mDk739AldRYYRLiB0NbjPMPm7bA1Ixnc7

					// 当前APP没有摄像头权限弹层，或者其他相关提示
					Intent intent = new Intent();
					intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					intent.setData(Uri.fromParts("package", getPackageName(), null));
					startActivity(intent);

				}

			}
		});
	}

}
