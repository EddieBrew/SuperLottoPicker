package com.example.superlottopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.superlottopicker.adapters.LottoArrayAdapter;
import com.example.superlottopicker.myCustomClasses.LotteryNumbersHolder;
import com.example.superlottopicker.myFragments.ListViewFragment;

import java.util.List;

public class LotteryNumberPastActivity extends CustomMenuActivity {


	private Button btnPastNumbers;
	Bundle bundle;
	Intent intent;
	LottoArrayAdapter lottoArrayAdapter;
	ListView lotteryListView;
	private ListViewFragment listViewFragment;
	private FragmentTransaction fragmentTransaction;;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_numbers_past);
		intent  = getIntent();

		btnPastNumbers = findViewById(R.id.btnPastNumbers);
		listViewFragment = new ListViewFragment();
		lotteryListView = this.findViewById(R.id.listview_lottoNumbers);
//

		FragmentManager fragmentManager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.myDisplayFrame, listViewFragment);
		fragmentTransaction.commit();

		btnPastNumbers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayList();
			}
		});


		btnPastNumbers .post(new Runnable() {//allows the past lotto numbers to be displayed when starting the activity No burtton click required to UI
			@Override
			public void run() {
				btnPastNumbers.performClick();
			}
		});

	}





	private void displayList( ){

		List<LotteryNumbersHolder> lottoNumbers = intent.getParcelableArrayListExtra("myLottoList");

		try {

			lottoArrayAdapter = new LottoArrayAdapter(LotteryNumberPastActivity.this, lottoNumbers);
			lotteryListView = LotteryNumberPastActivity.this.findViewById(R.id.listview_lottoNumbers);
			lotteryListView.setAdapter(lottoArrayAdapter);


		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show();
			Log.i("MYERROR", e.toString());
		}
	}

}
