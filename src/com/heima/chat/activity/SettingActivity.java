package com.heima.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.heima.chat.ChatApplication;
import com.heima.chat.R;
import com.heima.chat.base.BaseActivity;
import com.heima.chat.db.AccountDao;
import com.heima.chat.domain.Account;
import com.heima.chat.widget.DialogLogout;
import com.heima.chat.widget.NormalTopBar;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private NormalTopBar mTopBar;
	private Button mBtnLogout;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.act_setting);

		initView();
		initEvent();
	}

	private void initView() {
		mTopBar = (NormalTopBar) findViewById(R.id.setting_top_bar);
		mBtnLogout = (Button) findViewById(R.id.setting_logout);

		mTopBar.setTitle("设置");
	}

	private void initEvent() {
		mTopBar.setOnBackListener(this);
		mBtnLogout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == mTopBar.getBackView()) {
			finish();
		} else if (v == mBtnLogout) {
			clickLogout();
		}
	}

	private void clickLogout() {
		final DialogLogout dialog = new DialogLogout(this);
		dialog.show();
		dialog.setClickLogoutListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				logout();
			}
		});
	}

	private void logout() {
		AccountDao dao = new AccountDao(this);
		Account account = dao.getCurrentAccount();
		account.setCurrent(false);
		account.setToken(null);
		dao.updateAccount(account);

		((ChatApplication) getApplication()).closeApplication();

		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(LoginActivity.ENTER_KEY, LoginActivity.ENTER_SIGN_IN);
		startActivity(intent);
	}
}
