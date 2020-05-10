package com.example.superlottopicker;



/*

Created by Robert Brewer on 10/22/2019.

The SuperLottoPicker app  generates the California Lottery Super Lotto numbers.
The app uses the California State Lottery  and Mega numbers, up to the number 47 and 27 respectively. The app retrieves the past 52
using webscraping methods with the help of Volley and Jsoup libraries and  and stores the values. The apps stores previous lotto
numbers, in a Hashmap key-value pair, where the key is the number and
the value is the number of times the number has been drawn.

The apps allows the user to input the min and max frequency ranges the numbers have been drawn, and
from that pool of number, four(4) lottery ticket quick picts are generated and displayed on the UI

The apps consists of three(3) activities  that assists the user in generating
quickpick lottery numbers:

	    GeneratorFragment: Allows the user to input the min and max frequency ranges the lotto
                           and mega  numbers have been drawn, and from that pool of number, ten(10)
                           lottery ticket quick picts are generated and displayed on the UI

	    PastLottoNumbersFragment: Display the past 52 week drawn lottery numbers

	    FrequencyFragment: Display the number of time each SuperLotto and Mega numbers have been
	                       drawn the past 52 weeks.




 */





import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.superlottopicker.myCustomClasses.LotteryNumberFrequency;
import com.example.superlottopicker.myCustomClasses.LotteryNumbersHolder;
import com.example.superlottopicker.myFragments.GeneratorFragment;

import org.jetbrains.annotations.Contract;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.jsoup.Jsoup.parse;

public class LotteryNumberGeneratorActivity extends CustomMenuActivity implements GeneratorFragment.OnFragmentInteractionListener {
	public static android.support.v4.app.FragmentManager fragmentManager;
	private GeneratorFragment[] numberFragment; //UI component that displays each lottery ticket number
	FrameLayout lotteryNumberFrame, lotteryNumberFrame1, lotteryNumberFrame2, lotteryNumberFrame3;
	Button btnGenerateLotteryTickets; //pressed to generate lottery tickets
	EditText editTextMinMax;
	private RequestQueue mRequestQueue; //requestqueue varaiable from Vollei library
	private StringRequest stringRequest;   //the variable type of the requestqueue
	public static List<LotteryNumbersHolder> lottoNumberList;//holds daily lotton numbers extracted from website


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery_number_generator);
		final int LOTNUMBER = 4;//the number of lottery tickets displayed on UI

		lotteryNumberFrame = findViewById(R.id.lotteryNumberFrame);
		lotteryNumberFrame1 = findViewById(R.id.lotteryNumberFrame1);
		lotteryNumberFrame2 = findViewById(R.id.lotteryNumberFrame2);
		lotteryNumberFrame3 = findViewById(R.id.lotteryNumberFrame3);
		btnGenerateLotteryTickets =  findViewById(R.id.btnGenerateLotteryTickets);
		editTextMinMax = findViewById(R.id.editTextMinMax);

		sendRequestAndPrintResponse();// generates a List of strings that contains the daily lottery numbers
		btnGenerateLotteryTickets.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

					final int LOTNUMBER = 4;//the number of lottery tickets displayed on UI
					final int LOTTO_MAX = 47;
					final int MEGA_MAX = 27;
					List<Integer> lottoNumbers = null; //
					List<Integer> megaNumbers = null; //
					String minNMax = editTextMinMax.getText().toString();

					//Code to create lists with the most lotto and mega numbers
					lottoNumbers = getListForNumbers(lottoNumberList, LOTTO_MAX, minNMax);
					megaNumbers = getListForNumbers(lottoNumberList, MEGA_MAX, minNMax);

					for (int j = 0; j < LOTNUMBER; j++) {//displays new UI for lottery tickets
						numberFragment[j].generateLotteryNumbers(lottoNumbers, megaNumbers);
					}

					if(lottoNumbers.size() > 0 && megaNumbers.size() > 0) {
						Toast.makeText(LotteryNumberGeneratorActivity.this, "Lottery Numbers Generated for Tickets Completed", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(LotteryNumberGeneratorActivity.this, "TRY AGAIN. ", Toast.LENGTH_LONG).show();
				}
			}
		});


		numberFragment = new GeneratorFragment[LOTNUMBER];

		for(int i =0; i < LOTNUMBER; i++) {
			numberFragment[i] = new GeneratorFragment();
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


	/*********************************************************************************
	 *  sendRequestAndPrintResponse() is the main function that produces the lottery numbers by
	 *  abstracting the data from the network using Volley for networking and Jsoup for webscraping.
	 *  Upon analazing the the 52 weeks of the CA State Lottery drawings, lotto and mega numbers
	 *  are populated into the UI
	 *
	 * @pre none
	 * @parameter none
	 * @post Populates the UI lottery number displays
	 **********************************************************************************/
	private void sendRequestAndPrintResponse(){

		String website = "https://www.lotteryusa.com/california/super-lotto-plus/year";
		mRequestQueue = Volley.newRequestQueue(this); //used for simple queue requests
		stringRequest = new StringRequest(Request.Method.GET, website, new Response.Listener<String>() {
			@Override
			public void onResponse(final String response) {

							produceLottoNMegaNumbers(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(LotteryNumberGeneratorActivity.this, "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
			}
		});

		mRequestQueue.add(stringRequest);
	}



	/*********************************************************************************
	 *  produceLottoNMegaNumbers() produced a list containing LotteryNumberHolder objects.
	 *  The LotteryHolderNumber class contains the daily drawings of the CA SuperLotto
	 *  numbers
	 *
	 * @pre String response: the HTML transmitted data received from the CA Lottery webpagepage.
	 *
	 * @parameter none
	 * @post Populates the UI lottery number displays
	 **********************************************************************************/
	private void produceLottoNMegaNumbers(final String response){

		if( response == "")return ;
			parseHTMLPage(response);// List of the daily lottery numbers
		}





	/*********************************************************************************
	 *  parseHTMLPage() parses the CA Lottery HTML website data and extract the daily lotto numbers
	 *                  and populates a List with the lotto numbers
	 *
	 * @pre String response: the HTML transmitted data received from the CA Lottery webpagepage.
	 *
	 * @parameter none
	 * @post List<LotteryNumberHolder> updates global variable "lottoNumberList" which holds the daily
	 *                                  lottoe numbers for the last 52 weeks
	 **********************************************************************************/
	@Contract("null -> null")
	private void parseHTMLPage(final String response) {

		if (response == "") return;


		//StringBuilder myLottoInfo =  new StringBuilder();
		//StringBuilder myDate = new StringBuilder();
		Document doc;

		doc = parse(response);


		List<LotteryNumbersHolder> myDailyLotto = new ArrayList<>();
		try {
			List<String> lottoDrawingDates = new ArrayList<>(); //holds the lotto drawing dates
			Elements lotteryDatesInfo = doc.select("time");

			for (Element lottoDates : lotteryDatesInfo) {
				Elements date = lottoDates.getElementsByAttribute("datetime");
				lottoDrawingDates.add(date.text());
			}


			List<String> weeklyLottoNumbers = new ArrayList<>(); //holds the lotto numbers
			Elements rows = doc.select("tr.c-game-table__item");
			for (Element row : rows) {
				Elements cells = row.children();//get all elements in the result cell
				for (Element cell : cells) {
					Elements numListings = cell.getElementsByAttributeValue("class", "c-result c-result--in-card c-result--has-extra");
					for (Element numListing : numListings) {
						Elements numbers = numListing.getElementsByTag("li ");
						String lottoNumbers = "";
						for (Element number : numbers) {
							String lotteryNumber = number.text();
							lottoNumbers += lotteryNumber + ", ";
						}
						String updatedLottoNumbers = lottoNumbers.replace("MN: ", "");////removes mega Number designator from lotto number string
						String updatedLottoNumbers1 = updatedLottoNumbers.replace (",", "");
						//Log.i("ERROR1", updatedLottoNumbers1 );
						weeklyLottoNumbers.add(updatedLottoNumbers1);
					}
				}

			}





			//List<String> dailyLottoNumbersString = new ArrayList<>();//fill
			for (int i = 0; i < weeklyLottoNumbers.size(); i++) {
				//Log.i("ERROR1", lottoDrawingDates.get(i + 1) + "    " + weeklyLottoNumbers.get(i) );
				myDailyLotto.add(new LotteryNumbersHolder(lottoDrawingDates.get(i + 1), weeklyLottoNumbers.get(i)));
			}
			//Log.i("Size2 = ", String.valueOf(myDailyLotto.size()));

		} catch (Exception e) {
			e.printStackTrace();

		}

		lottoNumberList = myDailyLotto;


	}

	/*********************************************************************************
	 *  getListForNumbers() populates a list that will hold the lotto and mega numbers, within the
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
	public List<Integer> getListForNumbers(List<LotteryNumbersHolder> list, Integer number, String minMax) {

		//creates a HashMap where the keys are numbers between the lotto (1-47) and mega number(1-27) ranges. The values are initialized to
		// zero and will be incremented to reflect the number of times the key number have been
		// drawn in the lottery( key = lottery number, value = # of times number has beeen drawn)
		HashMap<Integer, Integer> myHashLotteryNumbers = new HashMap<>();
		for (int i = 1; i < number +1; i++) { //number is the max numbers that can be selected for lotto (47) and mega (27) numbers
			myHashLotteryNumbers.put(i, 0);
		}

		List<Integer> popularLotteryNumbers = new ArrayList<>();
		//Integer minRange = null;//minimun number of times a number has been selected
		//Integer maxRange = null;////maximun number of times a number has been selected

		try {
			String minNMaxValues[] = minMax.split(",");
			Integer minRangeForLotto = Integer.parseInt(minNMaxValues[0]);
			Integer maxRangeForLotto = Integer.parseInt(minNMaxValues[1]);
			Integer minRangeForMega = Integer.parseInt(minNMaxValues[2]);
			Integer maxRangeForMega = Integer.parseInt(minNMaxValues[3]);
			final int TOTAL_NUMBERS = 5;
			Boolean minNumberRequiredInPool = false;

			switch(number) {
				case 27:  //selects Mega number.
					//Integer megaMin = 5; // pre-defined min occurrence that the mega number has been drawn
					final Integer megaMax = 10;//
					for (int j = 0; j < list.size(); j++) {//pre-defined min occurrence that the mega number has been drawn
						upDateHashTable(myHashLotteryNumbers, list.get(j).getMegaNumber());
					}
					//printHashMap(" HASH MEGA NUMBERS: :", myHashLotteryNumbers);
					while (!minNumberRequiredInPool) {
						//printHashMapUsingLoop(" HASH MEGA NUMBERS: :", myHashLotteryNumbers);
						popularLotteryNumbers = getCommonLotteryNumbers(myHashLotteryNumbers, minRangeForMega, maxRangeForMega);
						if (popularLotteryNumbers.size() < 5) {// Adjusts the min Range if the popularLotteryNumberlist is less than the five numbers
							//required to make up the five lottery number.
							minRangeForMega--;
							minNumberRequiredInPool = false;
						} else {
							minNumberRequiredInPool = true;
						}
					}
					//Log.i("MLOTTERYNUMBERS = ", String.valueOf(popularLotteryNumbers.size()));
					break;

				case 47: //selects regular lotto numbers
					for (int j = 0; j < list.size(); j++) {
						for (int k = 0; k < TOTAL_NUMBERS; k++) {
							upDateHashTable(myHashLotteryNumbers, list.get(j).getLottoNumbers(k));
						}
					}
					//printHashMapUsingLoop("LottoNumbers", myHashLotteryNumbers);
					while (!minNumberRequiredInPool) { // Adjusts the min Range if the popularLotteryNumberlist is less than the five numbers
														//required to make up the five lottery number.
						//printHashMapUsingLoop("HASH LOTTO NUMBERS: :", myHashLotteryNumbers);
						popularLotteryNumbers = getCommonLotteryNumbers(myHashLotteryNumbers, minRangeForLotto, maxRangeForLotto);
						if (popularLotteryNumbers.size() <= 5) {
							minRangeForLotto--;
							if (minRangeForLotto < 0) {// Adjusts the max Range if the min range is already at 0
								minRangeForLotto = 0;
								maxRangeForLotto++;
								//Log.i("POPULARLOTTERYNUMBERS = ", String.valueOf(popularLotteryNumbers.size()));
								minNumberRequiredInPool = false;
							}
						} else {
							//Log.i("POPULARLOTTERYNUMBERS = ", String.valueOf(popularLotteryNumbers.size()));
							minNumberRequiredInPool = true;
						}
					}
					break;
				default:
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			Toast.makeText(this, "Min,Max INPUT ERROR: Verify min,max input( for ex. 5,10,3,8)", Toast.LENGTH_LONG).show();
		}
		//Log.i("POPULARLOTTERYNUMBERS = ", String.valueOf(popularLotteryNumbers.size()));
		return popularLotteryNumbers;


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


	/*********************************************************************************
	 *  getCommonLotteryNumbers() searched the HashMap and search for values
	 *  between the user defined min,max values. The method returns a list key integers(i.e. lottery numbers)
	 *
	 *
	 * @pre none
	 * @parameter HashMap<Integer, Integer> myHashLotteryNumbers
	 * @post List<Integer> : list of lottery numbers within the specified min,max ranges
	 **********************************************************************************/
	private List<Integer> getCommonLotteryNumbers(HashMap<Integer, Integer> myHashLotteryNumbers, Integer minRange, Integer maxRange) {

		List<Integer> commonNumbers = new ArrayList<>();
		int totalItems = myHashLotteryNumbers.size() + 1;//offset by 1 to include the last key

		int listIndex = 1;

		while (listIndex < totalItems) {

			// since HashMap minimum key is 1, list is offset to represent the first item in map
			if (myHashLotteryNumbers.containsKey(listIndex)) {

				if (myHashLotteryNumbers.get(listIndex) >= minRange && myHashLotteryNumbers.get(listIndex) <= maxRange) {
					commonNumbers.add(listIndex);
				}
				listIndex++;
			}
		}

		return commonNumbers;
	}


	/*********************************************************************************
	 *  printHashMap() prints out the hashmap key,value pai, using an iterator to traverse
	 *  through the hashmap.
	 * @pre none
	 * @parameter String title: Designates whther the hash table contains Lotto or Mega number info
	 *            HashMap<Integer, Integer> myHashLotteryNumbers
	 * @post none
	 **********************************************************************************/
	static void printHashMap(String title,	HashMap<Integer, Integer> map){
		//System.out.println(title + " HASH OUTPUT");
		Iterator<Integer> itr = map.keySet().iterator();
		while(itr.hasNext())
		{
			Integer key = itr.next();
			Integer value = map.get(key);
			System.out.println("HASHMAP: Key = " + key + ", Value = " + value);
		}
	}


	/*********************************************************************************
	 *  printHashMapUsingLoop() prints out the hashmap key,value pai, using a For Loop to traverse
	 *  through the hashmap.
	 * @pre none
	 * @parameter String title: Designates whther the hash table contains Lotto or Mega number info
	 *            HashMap<Integer, Integer> myHashLotteryNumbers
	 * @post none
	 **********************************************************************************/
	private void printHashMapUsiCustngLoop(String title, HashMap<Integer, Integer> map){
		System.out.println(title + " HASH OUTPUT");
		for(int i = 1; i < map.size() +1 ; i++){
			Log.i("HASHMAP:", "Key = " + i + ", Value = " + map.get(i).toString());
		}
	}

}
