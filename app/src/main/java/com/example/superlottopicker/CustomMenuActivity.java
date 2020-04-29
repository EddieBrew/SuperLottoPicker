package com.example.superlottopicker;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class CustomMenuActivity extends AppCompatActivity {
	private 	Intent mIntent;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mMenuInflater = getMenuInflater();
		mMenuInflater.inflate(R.menu.menu_activity, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int fragmentSelector;


		switch (item.getItemId()) {
			case (R.id.menu_home)://


				mIntent = new Intent(getApplicationContext(), LotteryNumberGeneratorActivity.class);
				startActivity(mIntent);
				return true;


			case (R.id.menu_pastLottoNumbers)://When the Delete Database menu item is clicked, the event below is performed:

				mIntent = new Intent(getApplicationContext(), LotteryNumberPastActivity.class);
				mIntent.putParcelableArrayListExtra("myLottoList", (ArrayList<? extends Parcelable>) LotteryNumberGeneratorActivity.lottoNumberList);
				startActivity(mIntent);
				return true;

			case (R.id.menu_numberQuery)://
				mIntent = new Intent(getApplicationContext(), LotteryNumberFrequencyActivity.class);
				mIntent.putParcelableArrayListExtra("myLottoList", (ArrayList<? extends Parcelable>) LotteryNumberGeneratorActivity.lottoNumberList);
				startActivity(mIntent);

				return true;



			default:
		}//end switch
		return true;
	}

}
