package com.as.atlas.teaorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDrinkOrderListener} interface
 * to handle interaction events.
 * Use the {@link DrinkOrderDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkOrderDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;   // Keep to know fragment can bring multi argments

    // UI Component
    NumberPicker numberPickerMedium;
    NumberPicker numberPickerLarge;
    RadioGroup radioGroupIce;
    RadioGroup radioGroupSugar;
    EditText editTextNote;

    private DrinkOrder drinkOrder;
    private OnDrinkOrderListener mListener;

    public DrinkOrderDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param drinkOrder Use Json Format as argument.
     * @return A new instance of fragment DrinkOrderDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrinkOrderDialogFragment newInstance(DrinkOrder drinkOrder) {
        DrinkOrderDialogFragment fragment = new DrinkOrderDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, String.valueOf(drinkOrder.getJsonData()));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {   // = onCreate + onCreateView
        if (getArguments() != null) {
            String data = getArguments().getString(ARG_PARAM1);
            drinkOrder = DrinkOrder.newInstanceWithData(data);
        }
        
        
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View root = layoutInflater.inflate(R.layout.fragment_drink_order_dialog, null);
        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(root)
                .setTitle(drinkOrder.drinkName)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drinkOrder.mediumCupNum = numberPickerMedium.getValue();
                        drinkOrder.largeCupNum = numberPickerLarge.getValue();
                        drinkOrder.ice = getSelectedItemFromRadioGroup(radioGroupIce);
                        drinkOrder.sugar = getSelectedItemFromRadioGroup(radioGroupSugar);
                        drinkOrder.note = editTextNote.getText().toString();
                        if (mListener != null) {
                            mListener.onDrinkOrderFinished(drinkOrder);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        
         
        numberPickerMedium = (NumberPicker) root.findViewById(R.id.numberPickerMediumCup);
        numberPickerMedium.setMaxValue(100);
        numberPickerMedium.setMinValue(0);
        numberPickerMedium.setValue(drinkOrder.mediumCupNum);

        numberPickerLarge = (NumberPicker) root.findViewById(R.id.numberPickerLargeCup);
        numberPickerLarge.setMaxValue(100);
        numberPickerLarge.setMinValue(0);
        numberPickerLarge.setValue(drinkOrder.largeCupNum);

        radioGroupIce = (RadioGroup) root.findViewById(R.id.radioGroupIce);
        radioGroupSugar = (RadioGroup) root.findViewById(R.id.radioGroupSugar);
        editTextNote = (EditText) root.findViewById(R.id.editTextNote);
        
        return builder.create();
        //return super.onCreateDialog(savedInstanceState);
    }
    
    private String getSelectedItemFromRadioGroup(RadioGroup rg) {
        int id = rg.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) rg.findViewById(id);
        return rb.getText().toString();
    }

    // 不長出 OK, CANCEL
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            drinkOrder = DrinkOrder.newInstanceWithData(mParam1);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_drink_order_dialog, container, false);
//    }

    // TODO: Rename method, update argument and hook method into UI event

    // For build pass
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrinkOrderListener) {
            mListener = (OnDrinkOrderListener) context;  // 透過 mListener 對  Activity 做溝通
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDrinkOrderListener");
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
    public interface OnDrinkOrderListener {  //如果Fragment 觸發事情 會透過什麼 interface 發給  ActivityMa
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);   // For build pass
        void onDrinkOrderFinished(DrinkOrder drinkOrder);
    }
}
