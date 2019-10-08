
/*

Created by Robert Brewer on 7/9/2019

LotteryAsyncTask is a background thread that retrieves NY state lottery data from the lottery
website and stores the most popular selected numbers, defined by the user, in a list. The list
is passed back to the MAin Activity and used to produce new lottery numbers for the next drawing

The threads takes in a string array that include teo elements:
string[0] = website that contains the json data
string[1] = string that contains the user provider min,max ranger for the frequency of numbers
             selected from previous lottery drawings dor a 1 year calendar date.

Note that the New York State Lottery selects six numbers between 1 and 67. Since the app predicts
the numbers for the California State Lottery, all numbers above 47, will be discarded from selection
from the app

 */

package com.example.superlottopicker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LotteryAsyncTask extends AsyncTask<String, Void, List<Integer> >{


	int minRange , maxRange ;

	@Override
	protected List<Integer> doInBackground(String... strings) {
		//HttpURLConnection urlConnection;//define  HttpURLConnection(2)

		HttpURLConnection urlConnection = null;
		try {
			String result = "";
			URL url; //define URL variable(1)
			url = new URL(strings[0]);
			urlConnection = (HttpURLConnection) url.openConnection();//create a URLconnection(4)
			InputStream inputStream = urlConnection.getInputStream();//create an InputStream(5)
			//InputStreamReader reader = new InputStreamReader(inputStream);

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String lineData = ""; //get a variable to read each char in the file(7)

			while (lineData != null) { // -1 indicates no more data available on website content
				lineData = bufferedReader.readLine();
				result = result + lineData + "\n" ;

			}

			//creates a HashMap where the key are numbers between 1 and 47. The values are initialized to
			// zero and will be incremented to reflect the number of times the key number have been
			// drawn in the lottery( key = lottery number, value = # of times number has beeen drawn)
			HashMap<Integer, Integer> myHashLotteryNumbers = new HashMap<>();

			for(int i = 1; i < 48; i++){
				myHashLotteryNumbers.put(i, 0);
			}
/*
			for(int x = 0; x < myHashLotteryNumbers.size(); x++){
				System.out.println("MyNumbers1: " + (x+1) + "----" + myHashLotteryNumbers.get(x+1));
			}
*/
			MyJSONLotteryParser 	myJSONLotteryParser = new 	MyJSONLotteryParser(result);
			List<String> myResults = myJSONLotteryParser.getLotteryDataResults("winning_numbers");

			int numbers[];
			for(int j = 0; j < myResults.size(); j++) {

				numbers = getNumbersFromString(myResults.get(j));
				for(int i = 0; i < 6; i++) {
					System.out.print("testing: " + numbers[i] + ", ");
					upDateHashTable(myHashLotteryNumbers,numbers[i] );
				}
				System.out.println();
			}

			/*
			for(int y = 0; y < myHashLotteryNumbers.size(); y++){
				System.out.println("MyNumbers2: " + (y+1) + "----" + myHashLotteryNumbers.get(y+1));
			}
			*/

			try {
				String minNMaxValues[];
				minNMaxValues = strings[1].split(",");
				minRange = Integer.parseInt(minNMaxValues[0]);//minimun number of times a number has been selected
				maxRange = Integer.parseInt(minNMaxValues[1]);////maximun number of times a number has been selected
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<Integer> popularLotteryNumbers = getCommonLotteryNumbers(myHashLotteryNumbers);//the numbers that will be used to select the lottery numbers
			return popularLotteryNumbers ;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<Integer> result) {
		super.onPostExecute(result);
	}

	/*********************************************************************************
	 *  getNumbersFromString() takes in a string of lottery numbers, parse the string to
	 *  extract the 6 lottery numbers and place the numbers in an integer array
	 *
	 * @pre none
	 * @parameter String string
	 * @post returns a string array, containing the lottery numbers from a specific drawing
	 **********************************************************************************/
	static private int[] getNumbersFromString(String string) {

		String[] myString= string.split(" ");
		int[] num = new int[6];
		for(int i = 0; i < myString.length; i++) {

			num[i] = Integer.parseInt(myString[i]) ;
		}

		return num;
	}

	/*********************************************************************************
	 *  updateHashTable() updates the hashmap ny incrementing the number of times the lottery
	 *  number(key) has been drawn in previous lottery drawings
	 *
	 * @pre none
	 * @parameter HashMap myHashLotteryNumbers, Integer key
	 * @post
	 **********************************************************************************/
	private void upDateHashTable(HashMap<Integer, Integer> myHashLotteryNumbers, Integer key) {

		if(myHashLotteryNumbers.containsKey(key)){

			Integer oldValue = myHashLotteryNumbers.get(key);
			oldValue ++; //increments the hash key values

				myHashLotteryNumbers.replace(key, oldValue);


		}
	}

	/*********************************************************************************
	 *  getCommonLotteryNumbers() searched the HashMap and looks for keys where their values are
	 *  between the user defined min,max values. The method returns an list of integers with the
	 *  key(i.e. lottery numbers)
	 *
	 *
	 * @pre none
	 * @parameter HashMap<Integer, Integer> myHashLotteryNumbers
	 * @post List<Integer> : list of lottery numbers within the specified min,max ranges
	 **********************************************************************************/
	private List<Integer> getCommonLotteryNumbers(HashMap<Integer, Integer> myHashLotteryNumbers){

		List<Integer> commonNumbers = new ArrayList<>();
		int totalItems =  myHashLotteryNumbers.size()+ 1;//offset by 1 to include the last key
		                                                //in the hashMap for navigating the while
		                                                //loop
		int listIndex = 1;

		while(listIndex < totalItems) {

			// since HashMap minimum key is 1, list is offset to represent the first
			if (myHashLotteryNumbers.containsKey(listIndex )) {// since HashMap minimum value is 1, key value is offset to

				if (myHashLotteryNumbers.get(listIndex ) > minRange && myHashLotteryNumbers.get(listIndex ) < maxRange) {
					//System.out.println( "Numbers(" + listIndex + 1 + "), " + myHashLotteryNumbers.get(listIndex + 1 ) );
					commonNumbers.add(listIndex );
				}

				listIndex++;

			}
		}

		return commonNumbers;
	}





}
