package com.heima.chat.service;

import java.util.HashMap;
import java.util.Map;

import com.heima.chat.ChatApplication;
import com.heima.chat.db.BackTaskDao;
import com.heima.chat.db.HMDB;
import com.heima.chat.domain.Account;
import com.heima.chat.domain.NetTask;
import com.heima.chat.lib.HMHttpManaer;
import com.heima.chat.utils.CommonUtil;
import com.heima.chat.utils.SerializableUtil;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

public class BackgroundService extends IntentService {

	// 1.构造函数应该注意的
	public BackgroundService() {
		super("background");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		// 2. 此方法执行在子线程中
		// 3. service start多次，此方法会排队执行
		Account account = ((ChatApplication) getApplication())
				.getCurrentAccount();
		if (account == null) {
			return;
		}

		if (!CommonUtil.isNetConnected(this)) {
			return;
		}

		BackTaskDao dao = new BackTaskDao(this);
		Cursor cursor = dao.query(account.getAccount(), 0);

		// 存储到 map中
		Map<Long, String> map = new HashMap<Long, String>();

		if (cursor != null) {

			while (cursor.moveToNext()) {
				final long id = cursor.getLong(cursor
						.getColumnIndex(HMDB.BackTask.COLUMN_ID));
				String filePath = cursor.getString(cursor
						.getColumnIndex(HMDB.BackTask.COLUMN_PATH));

				map.put(id, filePath);
			}
			cursor.close();
		}

		for (Map.Entry<Long, String> me : map.entrySet()) {
			try {
				final Long id = me.getKey();
				String filePath = me.getValue();

				NetTask task = (NetTask) SerializableUtil.read(filePath);
				// TODO: 发送请求
				// 改变状态值
				dao.updateState(id, 1);

				String url = task.getUrl();
				Map<String, String> headers = task.getHeaders();
				Map<String, String> paramaters = task.getParameters();
				boolean result = HMHttpManaer.getInstance().post(url, headers,
						paramaters);

				if (result) {

					System.out.println("#########9");

					dao.updateState(id, 2);
				} else {
					System.out.println("#########10");

					dao.updateState(id, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
