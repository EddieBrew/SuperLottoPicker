package com.example.superlottopicker.myFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.superlottopicker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment {

	public ListView listView;
	public ListViewFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment


		//name of the of the layout-->fragment_list_view
		View view = inflater.inflate(R.layout.fragment_list_view, container, false);

		//name of the listView(listview_lottoNumbers) inside the fragment_list_view layout
		listView = view.findViewById(R.id.listview_lottoNumbers);

		return view;

	}

}
