package com.ppg.bpmeasure.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ppg.bpmeasure.Calibration;
import com.ppg.bpmeasure.CameraActivity;
import com.ppg.bpmeasure.Details.UserInfo;
import com.ppg.bpmeasure.Login.Register;
import com.ppg.bpmeasure.R;
import com.ppg.bpmeasure.Tutotrial;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Measure#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Measure extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button measureBloodPressure,tutorial,calibrate;
    UserInfo userInfo;

    public Measure() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Measure.
     */
    // TODO: Rename and change types and number of parameters
    public static Measure newInstance(String param1, String param2) {
        Measure fragment = new Measure();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userInfo=(UserInfo) getActivity().getIntent().getSerializableExtra("UserInfo");
        tutorial = getView().findViewById(R.id.btn_tutorial);
        measureBloodPressure=getView().findViewById(R.id.btn_measure);
        calibrate=getView().findViewById(R.id.btn_calibration);
        tutorial.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle1));
        measureBloodPressure.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle1));
        calibrate.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle1));
        measureBloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                measureBloodPressure.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle));
                Calendar calendar=Calendar.getInstance();
                Date today = calendar.getTime();
                if(userInfo.timestampDate!=null && userInfo.timestampDate.before(today))
                {
                    Toast.makeText(getActivity(), "Calibration Period Expired", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra("UserInfo",userInfo);
                measureBloodPressure.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle1));
                startActivity(intent);
            }
        });
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorial.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle));
                Intent intent = new Intent(getActivity(), Tutotrial.class);
                intent.putExtra("UserInfo",userInfo);
                tutorial.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle1));
                startActivity(intent);
            }
        });
        calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calibrate.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle));

                Intent intent = new Intent(getActivity(), Calibration.class);
                intent.putExtra("UserInfo",userInfo);
                calibrate.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle1));

                startActivity(intent);
            }
        });
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
        return inflater.inflate(R.layout.fragment_measure, container, false);
    }
}