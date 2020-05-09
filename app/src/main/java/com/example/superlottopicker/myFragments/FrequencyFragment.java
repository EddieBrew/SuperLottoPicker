package com.example.superlottopicker.myFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.superlottopicker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrequencyFragment extends Fragment {
	public ListView listView;
	public FrequencyFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		//name of the of the layout-->fragment_past
		//name of the of the layout-->fragment_past
		View view = inflater.inflate(R.layout.fragment_frequency, container, false);

		//name of the listView() inside the fragment_past layout
		listView = view.findViewById(R.id.frequency_fragment_listView);

		return view;
	}


}
