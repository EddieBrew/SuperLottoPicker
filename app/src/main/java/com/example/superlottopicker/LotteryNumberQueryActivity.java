package com.example.superlottopicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superlottopicker.myCustomClasses.LotteryNumbersHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LotteryNumberQueryActivity extends CustomMenuActivity {

	TextView num1, num2, num3, num4, num5;
	TextView textViewFreq1, textViewFreq2, textViewFreq3, textViewFreq4, textViewFreq5;
	TextView frequencyNumberTitle;
	EditText editTextFrequency;
	ImageButton imageButton;
	LinearLayout layoutNum1, layoutNum2, layoutNum3, layoutNum4, layoutNum5;
	CheckBox chBxNumberType;
	Intent mIntent;
	boolean getMega = true;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_number_query);

		num1 = findViewById(R.id.num1);
		num2 = findViewById(R.id.num2);
		num3 = findViewById(R.id.num3);
		num4 = findViewById(R.id.num4);
		num5 = findViewById(R.id.num5);

		textViewFreq1 = findViewById(R.id.textViewFreq1);
		textViewFreq2 = findViewById(R.id.textViewFreq2);
		textViewFreq3 = findViewById(R.id.textViewFreq3);
		textViewFreq4 = findViewById(R.id.textViewFreq4);
		textViewFreq5 = findViewById(R.id.textViewFreq5);

		layoutNum1 = findViewById(R.id.layoutNum1);
		layoutNum2 = findViewById(R.id.layoutNum2);
		layoutNum3 = findViewById(R.id.layoutNum3);
		layoutNum4 = findViewById(R.id.layoutNum4);
		layoutNum5 = findViewById(R.id.layoutNum5);

		chBxNumberType = findViewById(R.id.chBxNumberType);
		editTextFrequency = findViewById(R.id.editTextFrequency);
		imageButton = findViewById(R.id.imageButton);

		mIntent = getIntent();
		final List<LotteryNumbersHolder> lottoNumbers = mIntent.getParcelableArrayListExtra("myLottoList");




		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				try {
					if (chBxNumberType.isChecked()) {
						getMega = true;
					} else {
						getMega = false;
					}

					String stringNumbers = editTextFrequency.getText().toString();
					String delimiter = ",";
					String[] numbers = stringNumbers.split(delimiter);

					Integer numType;
					if (getMega) {
						numType = 27;
					} else {
						numType = 47;
					}


					HashMap<Integer, Integer> myHashLotteryNumbers = new HashMap<>();

					for (int i = 1; i < numType + 1; i++) { //number is the max numbers that can be selected for lotto (47) and mega (27) numbers
						myHashLotteryNumbers.put(i, 0);
					}

					switch (numType) {
						case 27:
							if (isNumberWithinRange(numbers, numType)) {
								for (int i = 0; i < lottoNumbers.size(); i++) {
									LotteryNumberGeneratorActivity.upDateHashTable(myHashLotteryNumbers, lottoNumbers.get(i).getMegaNumber());
								}
								LotteryNumberGeneratorActivity.printHashMap("MEGA", myHashLotteryNumbers);
							} else {
								Toast.makeText(getApplication(), "Mega number(s) are greater than " + numType,
										Toast.LENGTH_LONG).show();
							}
							break;

						case 47:

							if (isNumberWithinRange(numbers, numType)) {
								Integer TOTAL_NUMBERS = 5;
								for (int j = 0; j < lottoNumbers.size(); j++) {
									for (int k = 0; k < TOTAL_NUMBERS; k++) {
										LotteryNumberGeneratorActivity.upDateHashTable(myHashLotteryNumbers, lottoNumbers.get(j).getLottoNumbers(k));
									}
								}

								LotteryNumberGeneratorActivity.printHashMap("LOTTO", myHashLotteryNumbers);
							}else{
								Toast.makeText(getApplication(), "Lotto number(s) are greater than " + numType,
										Toast.LENGTH_LONG).show();
							}
							break;

						default:
					}//end switch

					for (int i = 0; i < numbers.length; i++) {
						Integer value = getNumberFrequency(myHashLotteryNumbers, Integer.parseInt(numbers[i]));
						updateGUI(i, numbers[i], value);
					}
				}catch(Exception e){
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "INPUT ERROR: Verify numbers are separated by commas (etc 5,10,15,20,25)",
							Toast.LENGTH_LONG).show();
				}
			}//end onClick
		});

	}




	private boolean isNumberWithinRange(String[] num, Integer maxNum){

		for(int i = 0; i < num.length; i++){
			if(Integer.parseInt(num[i]) > maxNum){
				return false;
			}
		}
		return true;
	}

	public Integer getNumberFrequency( HashMap<Integer, Integer> map, Integer keyNum) {
		Iterator<Integer> itr = map.keySet().iterator();
		while (itr.hasNext()) {
			Integer key = itr.next();
			if (keyNum == key) {
				return map.get(key);
			}
		}
		return 0;

	}

	public void updateGUI(Integer row, String number, Integer frequency){

		switch(row){
			case 0:
				num1.setText(number);
				textViewFreq1.setText(frequency.toString() + "X");
				break;
			case 1:
				num2.setText(number);
				textViewFreq2.setText(frequency.toString()+ "X");
				break;
			case 2:
				num3.setText(number);
				textViewFreq3.setText(frequency.toString()+ "X");
				break;
			case 3:
				num4.setText(number);
				textViewFreq4.setText(frequency.toString()+ "X");
				break;
			case 4:
				num5.setText(number);
				textViewFreq5.setText(frequency.toString()+ "X");
				break;
			default:
		}
	}






}
