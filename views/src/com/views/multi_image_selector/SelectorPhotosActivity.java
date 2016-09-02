package com.views.multi_image_selector;

import java.util.ArrayList;

import com.views.R;
import com.views.QQ6.widget.ui.MainActivity;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.utils.PermissionTool;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SelectorPhotosActivity extends Activity {
	private static final int REQUEST_CAMERA = 1;
	private static final int REQUEST_IMAGE = 2;
	protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
	protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

	private TextView mResultText;
	private RadioGroup mChoiceMode, mShowCamera;
	private EditText mRequestNum;

	private ArrayList<String> mSelectPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mis_select_photos);
		mResultText = (TextView) findViewById(R.id.result);
		mChoiceMode = (RadioGroup) findViewById(R.id.choice_mode);
		mShowCamera = (RadioGroup) findViewById(R.id.show_camera);
		mRequestNum = (EditText) findViewById(R.id.request_num);

		mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
				if (checkedId == R.id.multi) {
					mRequestNum.setEnabled(true);
				} else {
					mRequestNum.setEnabled(false);
					mRequestNum.setText("");
				}
			}
		});

		View button = findViewById(R.id.button);
		if (button != null) {
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// 拍照权限
					// 权限
					// http://blog.csdn.net/hudashi/article/details/50775180
					// 好多授权http://zhidao.baidu.com/link?url=5LWq3A3dcEurjO9nnkbYae8RWdQSfIHWPDBdRP3jWoYUnFxKXMYKvE23jf7S8vtRQQ8urKkYI4mDk739AldRYYRLiB0NbjPMPm7bA1Ixnc7

					// http://blog.csdn.net/hp910315/article/details/51174583
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
						if (PermissionTool.isCameraCanUse()) {
							// // 跳转到相关的拍照/扫描 页面
							pickImage();
						} else {
							// // 当前APP没有摄像头权限弹层，或者其他相关提示
							Intent intent = new Intent();
							intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
							intent.setData(Uri.fromParts("package", getPackageName(), null));
							startActivity(intent);

						}
					} else {
						// Check if the Camera permission is already available.
						if (ActivityCompat.checkSelfPermission(SelectorPhotosActivity.this,
								Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
							requestCameraPermission();
						} else {
							// // 跳转到相关的拍照/扫描 页面
							pickImage();
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

	}

	private void requestCameraPermission() {
		requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA);

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

	private void pickImage() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission
																	// was added
																	// in API
																	// Level 16
				&& ActivityCompat.checkSelfPermission(this,
						Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.mis_permission_rationale),
					REQUEST_STORAGE_READ_ACCESS_PERMISSION);
		} else {
			boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;
			int maxNum = 9;

			if (!TextUtils.isEmpty(mRequestNum.getText())) {
				try {
					maxNum = Integer.valueOf(mRequestNum.getText().toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			MultiImageSelector selector = MultiImageSelector.create(SelectorPhotosActivity.this);
			selector.showCamera(showCamera);
			selector.count(maxNum);
			if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
				selector.single();
			} else {
				selector.multi();
			}
			selector.origin(mSelectPath);
			selector.start(SelectorPhotosActivity.this, REQUEST_IMAGE);

		}
	}

	private void requestPermission(final String permission, String rationale, final int requestCode) {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
			new AlertDialog.Builder(this).setTitle(R.string.mis_permission_dialog_title).setMessage(rationale)
					.setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions(SelectorPhotosActivity.this, new String[] { permission },
									requestCode);
						}
					}).setNegativeButton(R.string.mis_permission_dialog_cancel, null).create().show();
		} else {
			ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				pickImage();
			}
		} else if (requestCode == REQUEST_CAMERA) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// 用户同意使用write
				// 跳转到相关的拍照/扫描 页面
				pickImage();
			} else {
				// 用户不同意，向用户展示该权限作用
				showDailog();
			}
		}

		else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
				StringBuilder sb = new StringBuilder();
				for (String p : mSelectPath) {
					sb.append(p);
					sb.append("\n");
				}
				mResultText.setText(sb.toString());
			}
		}
	}

}
