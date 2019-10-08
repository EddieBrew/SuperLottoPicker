package com.example.superlottopicker;



/*

Created by Robert Brewer on 2/15/2018.

The SuperLottoPicker app  generates the California Lottery Super Lotto numbers.
The app uses the Texas State Lottery numbers, up to the number 47. The app retrieves the past 52
weeks drawings using the site's api interface and stores the values. The apps stores previous lotto
numbers, in a Hashmap key-value pair, where the key is the number and
the value is the number of times the number has been drawn.

The apps allows the user to input the min.max frequency ranges the numbers have been drawn, and
from that pool of number, four(4) lottery ticket quick picts are displays on the UI

 */





import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NumberFragment.OnFragmentInteractionListener {
	private static HashMap<Integer, Integer> myHashLotteryNumbers;
	public static android.support.v4.app.FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private NumberFragment[] numberFragment; //UI component that displays each lottery ticket number
	FrameLayout lotteryNumberFrame, lotteryNumberFrame1, lotteryNumberFrame2, lotteryNumberFrame3;
	Button btnGenerateLotteryTickets; //pressed to generate lottery tickets
	EditText editTextMinMax;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final int LOTNUMBER = 4;//the number of lottery tickets displayed on UI

		lotteryNumberFrame = findViewById(R.id.lotteryNumberFrame);
		lotteryNumberFrame1 = findViewById(R.id.lotteryNumberFrame1);
		lotteryNumberFrame2 = findViewById(R.id.lotteryNumberFrame2);
		lotteryNumberFrame3 = findViewById(R.id.lotteryNumberFrame3);
		btnGenerateLotteryTickets =  findViewById(R.id.btnGenerateLotteryTickets);
		editTextMinMax = findViewById(R.id.editTextMinMax);




		btnGenerateLotteryTickets.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

						String[] dataToAstyncTask = new String[2];//string array that is passed to AsyncTask class
						dataToAstyncTask[0] = "https://data.ny.gov/resource/d6yy-54nr.json";
						dataToAstyncTask[1] = editTextMinMax.getText().toString();

						//List returned from the AsyncTask doInBackground method that includes lottery numbers that have been selected
					    //a speicif number of times within a 1 year period ( determined by the user's  min,max input range
						List<Integer> lotteryNumbers = new LotteryAsyncTask().execute(dataToAstyncTask).get();

					    if(lotteryNumbers.size() == 0) { //displays Error message if their are no results from the query. UI maintain
					    	                             //previous lottery number generated
					        Toast.makeText(MainActivity.this, "TRY AGAIN. No numbers within your designated range or verify " +
							        "min,max input format is accurate", Toast.LENGTH_LONG).show();
					    }
					    else{
						    for (int j = 0; j < LOTNUMBER; j++) {//displays new UI for lottery tickets
							    numberFragment[j].generateLotteryNumbers(lotteryNumbers);
						    }
						    Toast.makeText(MainActivity.this, "Lottery Numbers for Tickets completed", Toast.LENGTH_SHORT).show();
					    }
					} catch(Exception e){
						e.printStackTrace();
					}
			}
		});

		numberFragment = new NumberFragment[LOTNUMBER];

		for(int i =0; i < LOTNUMBER; i++){
			numberFragment[i] = new NumberFragment();
			setDefaultFragment(numberFragment[i], i);
		}
	}

	private void setDefaultFragment(Fragment defaultFragment, int fragmnentNum) {
		this.replaceFragment(defaultFragment, fragmnentNum);
	}

	// Replace current Fragment with the destination Fragment.
	public void replaceFragment(Fragment destFragment, int num) {
		// First get FragmentManager object.
		FragmentManager fragmentManager = this.getSupportFragmentManager();

		// Begin Fragment transaction.
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		// Replace the layout holder with the required Fragment object.
		switch(num){
			case 0: fragmentTransaction.add(R.id.lotteryNumberFrame, destFragment);
				break;
			case 1: fragmentTransaction.add(R.id.lotteryNumberFrame1, destFragment);
				break;
			case 2: fragmentTransaction.add(R.id.lotteryNumberFrame2, destFragment);
				break;
			case 3: fragmentTransaction.add(R.id.lotteryNumberFrame3, destFragment);
				break;
			default:

		}

		// Commit the Fragment replace action.
		fragmentTransaction.commit();
	}


	@Override
	public void onFragmentInteraction(String data) {

	}
}
