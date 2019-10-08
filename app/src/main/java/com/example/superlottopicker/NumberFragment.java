package com.example.superlottopicker;


/*
NumberFragment configures the layout and sets the view
attributes for displaying the 5 lottery numbers and the mega number

*/





import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.os.SystemClock.sleep;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumberFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
    TextView num1, num2, num3, num4, num5, numMega;
	private OnFragmentInteractionListener mListener;

	public NumberFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment NumberFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static NumberFragment newInstance(String param1, String param2) {
		NumberFragment fragment = new NumberFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View view = inflater.inflate(R.layout.fragment_number, container, false);
		num1 = view.findViewById(R.id.num1);
		num2 = view.findViewById(R.id.num2);
		num3 = view.findViewById(R.id.num3);
		num4 = view.findViewById(R.id.num4);
		num5 = view.findViewById(R.id.num5);
		numMega = view.findViewById(R.id.numMega);
		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(String data) {
		if (mListener != null) {
			mListener.onFragmentInteraction(data);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(String data);
	}

	/*********************************************************************************
	 *  generateLottryNumbers() generates lottery numbers using the user's defined min,max
	 *  values. The values are stored in a list. A random number between 1 and the list size
	 *  is generated and the valus is used as the list item index. The value at that index will
	 *  be chosen as the number
	 *
	 * @pre none
	 * @parameter List<Integer> myList: list containing the user defined min,max values
	 * @post
	 **********************************************************************************/
	void generateLotteryNumbers(List<Integer> myList){

		Random rand = new Random();
		int[] lotteryNumbers = new int[5];
		int loopIterator = 50;//the algorithm generates the numbers after the 100 loop
		int minNum = 1; //
		int maxNum = myList.size() -1;
		//int megaMaxNum = 27;//mega number highest number

		for(int y=0; y < myList.size(); y++){
			//System.out.println("MyNumbers4: " + y + "------" + myList.get(y));
		}


		for(int i =0; i < loopIterator; i++) {
			int num =5;
			int index = 0;
			while (index < num) {
				int newNum = rand.nextInt((maxNum - minNum) + 1) + minNum;
                 if(index == 0){
	                 lotteryNumbers[index] = myList.get(newNum);
                 }
                 else {
					while (isDuplicateNum(myList.get(newNum), lotteryNumbers, index)) {
						newNum = rand.nextInt((maxNum - minNum) + 1) + minNum;
					}
	                 lotteryNumbers[index] = myList.get(newNum);
				}
				index++;
			}// end while
		}//end for
		sortAscending(lotteryNumbers);
		//displayLotteryNumber(lotteryNumbers, number);
		displayLotteryNumber(lotteryNumbers);
	}


	/*********************************************************************************
	 *  sortAscending() sorts the generated lottery  numbers in ascending order in the array
	 *
	 * @pre none
	 * @parameter int array
	 * @post  updates the array
	 **********************************************************************************/
	void sortAscending(int[] array){
		boolean swap = true;
		int j = 0;
		while (swap) {
			swap = false;
			j++;
			for (int i = 0; i < array.length - j; i++) {
				if (array[i] > array[i+1]) {//swap code

					int temp = array[i];
					array[i] = array[i+1];
					array[i+1] = temp;
					swap = true;
				}
			}
		}//end while
	}


	/*********************************************************************************
	 *  displayLotteryNumber() displays the lottery number in the UI numberFragment
	 *
	 * @pre none
	 * @parameter  int[] numbers: array containing the generated lottery numbers
	 *             int
	 * @post
	 **********************************************************************************/
	void displayLotteryNumber(int[] numbers){


		for (int index = 0; index < numbers.length; index ++){

			switch(index){
				case 0: num1.setText(String.valueOf(numbers[index]));
				break;
				case 1: num2.setText(String.valueOf(numbers[index]));
				break;
				case 2: num3.setText(String.valueOf(numbers[index]));
				break;
				case 3: num4.setText(String.valueOf(numbers[index]));
				break;
				case 4: num5.setText(String.valueOf(numbers[index]));
				break;
				default:
			}
		}

		numMega.setText(String.valueOf(generateMegaNumber()));

	}

	/*********************************************************************************
	 *  isDuplicateNum() checks to see if the current generated number has already been selected
	 *
	 * @pre none
	 * @parameter int newNum: random generated num
	 *            int []lotteryNumbers: numbers currently stored in the array
	 *            int index current number of generated numbers in the array
	 * @post return true if number exist in the arry, false if the number does not exist in the array
	 **********************************************************************************/
	boolean isDuplicateNum(int newNum, int[]lotteryNumbers, int index){

		boolean duplicate = false;
		for (int i = 0; i < index; i++){

			if (newNum == lotteryNumbers[i]){
				duplicate = true;
				break;
				//return duplicate;
			}
		}
		return duplicate;
	}

	/*********************************************************************************
	 *  generateMegaNumber() generated the SuperLotto mega number
	 *  the EditTextPayroll Message in the fragment_query fragment layout
	 *
	 * @pre none
	 * @parameter none
	 * @post returns an int and places it on the UI display
	 **********************************************************************************/
	int generateMegaNumber(){
		Random rand = new Random();

		int minNum = 1;
		int maxNum = 27;
		return  rand.nextInt( (maxNum - minNum)+1) +minNum;
	}


}