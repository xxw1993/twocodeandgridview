package com.example.twocodeandgridview.gridview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.example.twocodeandgridview.R;
import com.example.twocodeandgridview.gridview.DragGridView.OnChanageListener;
import com.google.zxing.client.android.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @blog http://blog.csdn.net/xiaanming
 * 
 * @author xiaanming
 *
 */
public class MainActivity extends Activity implements OnItemClickListener {
	private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();
	private int[] ItemPosition = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, 28, 29, 30, 31, 32 };
	public static final int SCAN_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DragGridView mDragGridView = (DragGridView) findViewById(R.id.dragGridView);
		mDragGridView.setOnItemClickListener(this);// 监听点击事件
		for (int i = 0; i < 32; i++) {
			HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
			itemHashMap.put("item_image", R.drawable.com_tencent_open_notice_msg_icon_big);
			itemHashMap.put("item_text", "拖拽 " + Integer.toString(i));
			dataSourceList.add(itemHashMap);
		}

		final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, dataSourceList, R.layout.grid_item,
				new String[] { "item_image", "item_text" }, new int[] { R.id.item_image, R.id.item_text });

		mDragGridView.setAdapter(mSimpleAdapter);

		mDragGridView.setOnChangeListener(new OnChanageListener() {

			@Override
			public void onChange(int from, int to) {
				HashMap<String, Object> temp = dataSourceList.get(from);
				// 直接交互item
				// dataSourceList.set(from, dataSourceList.get(to));
				// dataSourceList.set(to, temp);
				// from 拖拽时选中的item position
				// to 拖拽所替换的item position
				// 这里的处理需要注意下
				if (from < to) {
					for (int i = from; i < to; i++) {
						Collections.swap(dataSourceList, i, i + 1);
					}
				} else if (from > to) {
					for (int i = from; i > to; i--) {
						Collections.swap(dataSourceList, i, i - 1);
					}
				}

				dataSourceList.set(to, temp);

				mSimpleAdapter.notifyDataSetChanged();

			}
		});

	}

	// 监听点击事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		HashMap<String, Object> obj = dataSourceList.get(position);
		// 根据位置获取map
		Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
		startActivityForResult(intent, SCAN_CODE);
		Toast.makeText(getApplicationContext(), "拖拽" + obj.get("item_text"), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SCAN_CODE:
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("scan_result");
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "无返回", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
