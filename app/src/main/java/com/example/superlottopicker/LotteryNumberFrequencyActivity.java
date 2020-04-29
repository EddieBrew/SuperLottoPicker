package com.example.superlottopicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.superlottopicker.adapters.LottoFrequencyArrayAdapter;
import com.example.superlottopicker.myCustomClasses.LotteryNumberFrequency;
import com.example.superlottopicker.myCustomClasses.LotteryNumbersHolder;
import com.example.superlottopicker.myFragments.FrequencyFragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.superlottopicker.LotteryNumberGeneratorActivity.printHashMap;

public class LotteryNumberFrequencyActivity extends CustomMenuActivity {

	TextView num1, num2, num3, num4, num5;

	Intent mIntent;
	LottoFrequencyArrayAdapter lottoFrequencyArrayAdapter;
	ArrayAdapter<Integer> lottoFrequencyArrayAdapter1;
	ListView frequencyListView;
	private FrequencyFragment frequencyFragment;
	private FragmentTransaction fragmentTransaction;;
	private Button btnFrequentNumbers;
	CheckBox chbxTypeOfNumbers;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_number_frequency);
		chbxTypeOfNumbers = findViewById(R.id.chbxTypeOfNumbers);
		btnFrequentNumbers = findViewById(R.id.btnFrequentNumbers);
		frequencyListView = findViewById(R.id.frequency_fragment_listView);
		frequencyFragment = new FrequencyFragment();

		FragmentManager fragmentManager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.myQueryFrame, frequencyFragment);
		fragmentTransaction.commit();

		final int megaNumberTotal = 27;
		final int superLottoTotal = 47;
	    final int[] numSelector = new int[1];

		if(chbxTypeOfNumbers.isChecked()){
			numSelector[0] = megaNumberTotal;
		} else {

			numSelector[0] = superLottoTotal;
		}

		chbxTypeOfNumbers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(chbxTypeOfNumbers.isChecked()){
					chbxTypeOfNumbers.setChecked(true);
					numSelector[0] = megaNumberTotal;
					btnFrequentNumbers.post(new Runnable() {
						@Override
						public void run() {
							btnFrequentNumbers.performClick();
						}
					});
				} else{
					chbxTypeOfNumbers.setChecked(false)  ;
					numSelector[0] = superLottoTotal;
					btnFrequentNumbers.post(new Runnable() {
						@Override
						public void run() {
							btnFrequentNumbers.performClick();
						}
					});
				}
			}
		});

		btnFrequentNumbers.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayList(numSelector[0]);
			}
		});

		btnFrequentNumbers.post(new Runnable() {
			@Override
			public void run() {
				btnFrequentNumbers.performClick();
			}
		});


	}






	/*********************************************************************************
	 *  getLottoHashTable() populates a list that will hold the lotto and mega numbers, within the
	 *  user defined min,max frequency in which the number has been drawn.
	 *
	 * @pre none
	 * @parameter List<LotteryNumberHolder>  list : contains the past 52 weeks daily drwan lottry numbers
	 *             Integer number: determines whether the regular lotto numbers(5) or mega number(1)
	 *                              will be generated
	 *             String minMax: a string containing the user defined min and max frequency that will
	 *                            be used to vreate a list of numbers used to generate the new
	 *                            lottery tickets
	 *
	 * @post List<Integer> a list containing the the past lottery numbers occurances during the past
	 *                     52 weeks
	 * **********************************************************************************/
	public  HashMap<Integer, Integer> getLottoHashTable(List<LotteryNumbersHolder> list, Integer number) {

		//creates a HashMap where the keys are numbers between the lotto (1-47) and mega number(1-27) ranges. The values are initialized to
		// zero and will be incremented to reflect the number of times the key number have been
		// drawn in the lottery( key = lottery number, value = # of times number has beeen drawn)
		HashMap<Integer, Integer> myHashLotteryNumbers = new HashMap<>();
		for (int i = 1; i < number +1; i++) { //number is the max numbers that can be selected for lotto (47) and mega (27) numbers
			myHashLotteryNumbers.put(i, 0);
		}

		try {
			switch(number) {
				case 27:  //selects Mega number.
					//Integer megaMin = 5; // pre-defined min occurrence that the mega number has been drawn
					for (int j = 0; j < list.size(); j++) {//pre-defined min occurrence that the mega number has been drawn
						upDateHashTable(myHashLotteryNumbers, list.get(j).getMegaNumber());
					}
					//printHashMap(" HASH MEGA NUMBERS: :", myHashLotteryNumbers);
					break;

				case 47: //selects regular lotto numbers
					final int TOTAL_NUMBERS = 5;
					for (int j = 0; j < list.size(); j++) {
						for (int k = 0; k < TOTAL_NUMBERS; k++) {
							upDateHashTable(myHashLotteryNumbers, list.get(j).getLottoNumbers(k));
						}
					}
					//printHashMap(" HASH LOTTO NUMBERS: :", myHashLotteryNumbers);
					break;
				default:
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			Toast.makeText(this, "Min,Max INPUT ERROR: Verify min,max input( for ex. 5,10)", Toast.LENGTH_LONG).show();
		}
		//Log.i("POPULARLOTTERYNUMBERS = ", String.valueOf(popularLotteryNumbers.size()));
		return myHashLotteryNumbers;


	}

	/*********************************************************************************
	 *  updateHashTable() updates the hashmap by incrementing the number of times the lottery
	 *  number(key) has been drawn in previous lottery drawings
	 *
	 * @pre none
	 * @parameter HashMap myHashLotteryNumbers, Integer key
	 * @post
	 **********************************************************************************/
	static void upDateHashTable(HashMap<Integer, Integer> myHashLotteryNumbers, Integer key) {

		if (myHashLotteryNumbers.containsKey(key)) {

			Integer oldValue = myHashLotteryNumbers.get(key);
			oldValue++; //increments the hash key values

			myHashLotteryNumbers.put(key, oldValue);
		}
	}

	static List<LotteryNumberFrequency>  fillLotteyNumberFrequencyClass(HashMap<Integer, Integer> data){

		List<LotteryNumberFrequency> myData = new ArrayList<>();

		Iterator<Integer> itr = data.keySet().iterator();
		while(itr.hasNext()){
			Integer key = itr.next();
			Integer value = data.get(key);
			myData.add(new LotteryNumberFrequency(key, value ));
		}

		return myData;
	}




	private void displayList(final Integer numType){


		mIntent = getIntent();
		final List<LotteryNumbersHolder> lottoNumbers = mIntent.getParcelableArrayListExtra("myLottoList");
		HashMap<Integer, Integer>  	table  = getLottoHashTable(lottoNumbers, numType);;
		List<LotteryNumberFrequency> mylottoData = fillLotteyNumberFrequencyClass(table);
		sortListByDescendingFrequency(mylottoData);

		try {
			frequencyListView = this.findViewById(R.id.frequency_fragment_listView);
			lottoFrequencyArrayAdapter = new LottoFrequencyArrayAdapter(LotteryNumberFrequencyActivity.this, mylottoData);
			frequencyListView.setAdapter(lottoFrequencyArrayAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


/***********************************************************************************************
 * sortListByDescendingDate() sorts a List, by their dates, from lowest to highest
 * @pre none
 * @parameter List<> : a list of DailyInfoModel objects
 * @post none
 * ********************************************************************************************/
		private void sortListByDescendingFrequency(List<LotteryNumberFrequency> dataArray) {
			if (dataArray == null) {
				return;
			}

			boolean swap = true;
			int j = 0;
			while (swap) {
				swap = false;
				j++;
				for (int i = 0; i < dataArray.size() - j; i++) {
					int item1 = dataArray.get(i).getLottoNumber();
					int item2 = dataArray.get(i + 1).getLottoNumber();

					if (item1 > item2) {//swap list item
						LotteryNumberFrequency s = dataArray.get(i);
						dataArray.set(i, dataArray.get(i + 1));
						dataArray.set(i + 1, s);
						swap = true;
					}
				}
			}

		}


}
