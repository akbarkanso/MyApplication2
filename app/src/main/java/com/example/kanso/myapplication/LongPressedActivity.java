package com.example.kanso.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;

public class LongPressedActivity extends AppCompatActivity {

	HashSet<String> set;
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_long_pressed);

		set = new HashSet<>();
		list = (ListView) findViewById(R.id.list);


		Bundle b = getIntent().getExtras();
		String path;
		if (b != null) {
			path = b.getString("path");
			((TextView) findViewById(R.id.dirName)).setText("Unique words in " + path);
			File dir = new File(path);
			search(dir, 5);
		}

		ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.word, set.toArray(new String[set.size()]));
		list.setAdapter(adapter);
	}

	private void search(File dir, int depth) {
		if (depth == 0) return;

		File[] files = dir.listFiles();
		if (files != null && files.length != 0) {
			for (File f : files) {
				String name = f.getName();
				String[] words = name.split("(\\.|-|\\s|-|_)");
				for (String word : words) {
					set.add(word);
				}
				search(f, depth - 1);
			}
		}
	}
}
