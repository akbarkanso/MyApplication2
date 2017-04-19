package com.example.kanso.myapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

	LinearLayout main;
	TextView dirName;

	int permission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permission);
		}

		main = (LinearLayout) findViewById(R.id.main);
		dirName = (TextView) findViewById(R.id.dirName);
		showInUI(new File("/"));

	}

	public void showInUI(final File dir) {
		if (dir == null) return;
		main.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(this);

		dirName.setText(dir.getPath());
		View root = inflater.inflate(R.layout.folder, null, false);
		((TextView) root.findViewById(R.id.textView)).setText("...");
		root.setOnClickListener(new Clicked(dir.getParentFile()));
		root.setOnLongClickListener(new LClicked(dir));
		main.addView(root);

		File[] files = dir.listFiles();
		if (files == null) return;

		for (final File f : files) {
			View inflatedLayout;
			if (f.isDirectory()) {
				inflatedLayout = inflater.inflate(R.layout.folder, null, false);
				inflatedLayout.setOnClickListener(new Clicked(f));
				inflatedLayout.setOnLongClickListener(new LClicked(f));
			} else {
				inflatedLayout = inflater.inflate(R.layout.file, null, false);
			}
			((TextView) inflatedLayout.findViewById(R.id.textView)).setText(f.getName());
			main.addView(inflatedLayout);
		}
	}

	private class Clicked implements View.OnClickListener {
		File f;

		public Clicked(File f) {
			this.f = f;
		}

		@Override
		public void onClick(View view) {
			showInUI(f);
		}
	}

	private class LClicked implements View.OnLongClickListener {
		File f;

		public LClicked(File f) {
			this.f = f;
		}

		@Override
		public boolean onLongClick(View view) {
			Intent intent = new Intent(MainActivity.this, LongPressedActivity.class);
			Bundle b = new Bundle();
			b.putString("path", f.getAbsolutePath()); //Your id
			intent.putExtras(b); //Put your id to your next Intent
			startActivity(intent);
			return true;
		}
	}
}
