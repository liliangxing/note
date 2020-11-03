package com.example.zhl.notedemo.task;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;

public class CheckUpdateTasker extends AsyncTask<String, Integer, Integer> {

	
	private Context mContext;
	public CheckUpdateTasker(Context context) {
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (isCancelled())
			return 1;

		return 1;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
	}
}
