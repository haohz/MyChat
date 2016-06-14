package com.heima.chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.heima.chat.ChatApplication;
import com.heima.chat.R;
import com.heima.chat.activity.MessageActivity;
import com.heima.chat.base.BaseFragment;
import com.heima.chat.db.HMDB;
import com.heima.chat.db.MessageDao;
import com.heima.chat.domain.Account;
import com.heima.chat.widget.NormalTopBar;

public class ChatFra extends BaseFragment implements OnItemClickListener {
	private NormalTopBar mTopBar;

	private ListView listView;
	private ConversationAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_chat, container, false);
		initView(view);
		initEvent();


		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	private void initView(View view) {
		mTopBar = (NormalTopBar) view.findViewById(R.id.chat_top_bar);
		listView = (ListView) view.findViewById(R.id.chat_list_view);

		mTopBar.setBackVisibility(false);
		mTopBar.setTitle("消息");

	}

	private void initEvent() {
		listView.setOnItemClickListener(this);
	}

	private void loadData() {
		if (getActivity() == null) {
			return;
		}
		ChatApplication application = (ChatApplication) getActivity()
				.getApplication();
		Account account = application.getCurrentAccount();

		MessageDao dao = new MessageDao(getActivity());
		Cursor cursor = dao.queryConversation(account.getAccount());

		adapter = new ConversationAdapter(getActivity(), cursor);
		listView.setAdapter(adapter);

	}

	private class ConversationAdapter extends CursorAdapter {

		public ConversationAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return View.inflate(context, R.layout.item_conversation, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView tvUnread = (TextView) view
					.findViewById(R.id.item_converation_tv_unread);
			TextView tvName = (TextView) view
					.findViewById(R.id.item_converation_name);
			TextView tvContent = (TextView) view
					.findViewById(R.id.item_converation_content);
			String name = cursor.getString(cursor
					.getColumnIndex(HMDB.Conversation.COLUMN_ACCOUNT));
			String content = cursor.getString(cursor
					.getColumnIndex(HMDB.Conversation.COLUMN_CONTENT));
			int unread = cursor.getInt(cursor
					.getColumnIndex(HMDB.Conversation.COLUMN_UNREAD));

			if (unread <= 0) {
				tvUnread.setVisibility(View.GONE);
				tvUnread.setText("");
			} else {
				if (unread >= 99) {
					tvUnread.setText("99");
				} else {
					tvUnread.setText("" + unread);
				}
				tvUnread.setVisibility(View.VISIBLE);
			}
			tvName.setText(name);
			tvContent.setText(content);

			view.setTag(name);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), MessageActivity.class);
		intent.putExtra("messager", (String) view.getTag());
		startActivity(intent);
	}
}
