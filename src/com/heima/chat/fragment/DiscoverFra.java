package com.heima.chat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heima.chat.R;
import com.heima.chat.base.BaseFragment;
import com.heima.chat.widget.NormalTopBar;

public class DiscoverFra extends BaseFragment {
	private NormalTopBar mTopBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_discover, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mTopBar = (NormalTopBar) view.findViewById(R.id.discover_top_bar);

		mTopBar.setBackVisibility(false);
		mTopBar.setTitle("发现");
	}

}
