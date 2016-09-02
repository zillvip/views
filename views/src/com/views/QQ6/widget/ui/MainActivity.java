package com.views.QQ6.widget.ui;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.views.R;
import com.views.QQ6.widget.DragLayout;
import com.views.QQ6.widget.entity.ItemBean;
import com.views.QQ6.widget.utils.ItemDataUtils;
import com.views.multi_image_selector.MultiImageSelectorActivity;
import com.views.multi_image_selector.SelectorPhotosActivity;
import com.zbar.lib.utils.PermissionTool;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends BaseActivity {
	private DragLayout dl;
	private ListView lv;
	private ImageView ivIcon, ivBottom;
	private QuickAdapter<ItemBean> quickAdapter;
	private ImageButton ib_qrcode;

	private int REQUEST_CAMERA = 2001;

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
				case 1:
					startActivity(new Intent(MainActivity.this, MultiImageSelectorActivity.class));
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
				// 权限
				// http://blog.csdn.net/hudashi/article/details/50775180
				// 好多授权http://zhidao.baidu.com/link?url=5LWq3A3dcEurjO9nnkbYae8RWdQSfIHWPDBdRP3jWoYUnFxKXMYKvE23jf7S8vtRQQ8urKkYI4mDk739AldRYYRLiB0NbjPMPm7bA1Ixnc7

				// http://blog.csdn.net/hp910315/article/details/51174583
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
					if (PermissionTool.isCameraCanUse()) {
						// // 跳转到相关的拍照/扫描 页面
						startActivity(new Intent(MainActivity.this, SelectorPhotosActivity.class));

					} else {
						// // 当前APP没有摄像头权限弹层，或者其他相关提示
						Intent intent = new Intent();
						intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.setData(Uri.fromParts("package", getPackageName(), null));
						startActivity(intent);

					}
				} else {
					// Check if the Camera permission is already available.
					if (ActivityCompat.checkSelfPermission(MainActivity.this,
							Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
						requestCameraPermission();
					} else {
						// // 跳转到相关的拍照/扫描 页面
						startActivity(new Intent(MainActivity.this, SelectorPhotosActivity.class));
					}

				}
				// 需要注意的是安卓6.0后使用相机等涉及隐私的权限需要在代码内在申明，可通过以下方法：
				// http://blog.csdn.net/lw_90/article/details/51892054
				// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				// requestPermissions(
				// new String[] { Manifest.permission.CAMERA,
				// Manifest.permission.WRITE_EXTERNAL_STORAGE },
				// CAMERA_JAVA_REQUEST_CODE);
				// }

			}
		});
	}

	private void requestCameraPermission() {
		requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_CAMERA) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// 用户同意使用write
				// 跳转到相关的拍照/扫描 页面
				startActivity(new Intent(MainActivity.this, SelectorPhotosActivity.class));
			} else {
				// 用户不同意，向用户展示该权限作用
				showDailog();
			}
		}
	}

	private void showDailog() {
		AlertDialog dialog = new AlertDialog.Builder(this).setMessage("该摄像头需要赋予访问存储的权限，不开启将无法正常工作！")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// finish();
						// // 跳转到设置界面
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						startActivity(intent);

					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create();
		dialog.show();

	}

}
