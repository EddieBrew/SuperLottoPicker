package com.example.superlottopicker;
/*

Created by Robert Brewer on 7/9/2019

 MyJSONLotteryParser is a class that takes the raw json data from the lottery website ,
 extracts the json array and objects that contains the daily lottery drawings and converts the daily
 lottery number in a list of strings

 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyJSONLotteryParser {

	String data;//raw JSON data from website
	MyJSONLotteryParser(String data){

		this.data = data;
	}


	/*********************************************************************************
	 *  getLotteryDataResults() extracts daily lottery numbers, using json data, in a string format
	 *  and places the daily info in a list, that will be used to later extract each the numbers
	 *  in integer format
	 *
	 *
	 * @pre none
	 * @parameter String queryField1: parameter used to extract json object from string data
	 * @post  List<String> string representation of each daily lottery drawing
	 **********************************************************************************/
	public List<String> getLotteryDataResults(String queryField1) {
		String singledParsed1 = "";
		JSONArray ja;
		JSONObject jo1;
		//System.out.println("Printing out " + queryField1 + " results");
		//System.out.println("--------------------------------------------");
		List<String> myLotteryNumbers =new ArrayList();
		try {
			ja = new JSONArray(data); //json array from data string

			for (int i = 0; i < ja.length(); i++){
				jo1 = (JSONObject) ja.get(i); //objext of the json Array
				singledParsed1 =  jo1.getString(queryField1);
				myLotteryNumbers.add(singledParsed1);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //json  array

		return myLotteryNumbers;
	}
}
