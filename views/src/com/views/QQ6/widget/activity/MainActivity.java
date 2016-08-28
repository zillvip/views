package com.views.QQ6.widget.activity;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.views.R;
import com.views.QQ6.widget.DragLayout;
import com.views.QQ6.widget.entity.ItemBean;
import com.views.QQ6.widget.utils.ItemDataUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private DragLayout dl;
	private ListView lv;
	private ImageView ivIcon, ivBottom;
	private QuickAdapter<ItemBean> quickAdapter;

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
				Toast.makeText(MainActivity.this, "Click Item " + position, Toast.LENGTH_SHORT).show();
			}
		});
		ivIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dl.open();
			}
		});
	}

}
