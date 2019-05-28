package com.example.thoitiet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thoitiet.CustomAdapter;
import com.example.thoitiet.R;
import com.example.thoitiet.ThoiTiet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Next5Day.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Next5Day#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Next5Day extends Fragment {

    ArrayList<ThoiTiet> mangthoitiet =new ArrayList<ThoiTiet>();
    CustomAdapter customAdapter;
    ListView listweather;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Next5Day() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Next5Day.
     */
    // TODO: Rename and change types and number of parameters
    public static Next5Day newInstance(String param1, String param2) {
        Next5Day fragment = new Next5Day();
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

        Get5DayData();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_next5_day, container, false);

        listweather = (ListView) view.findViewById(R.id.listdaily);
        customAdapter=new CustomAdapter(getContext(),mangthoitiet);
        listweather.setAdapter(customAdapter);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    // @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }



    public String ConvertTime(long day){
        Date date=new Date(day*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE");
        String Day=simpleDateFormat.format(date);
        return Day;
    }

    public String Dayformat(long day){
        Date date=new Date(day*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String Dayfm=simpleDateFormat.format(date);
        return Dayfm;
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
        void onFragmentInteraction(Uri uri);
    }

    public void Get5DayData(){
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url="http://dataservice.accuweather.com/forecasts/v1/daily/5day/353412?apikey=RP37Tuafp3SCZro7STIzeIxgKpm2sPEF&language=vi&metric=true";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("ketqua",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray DailyForecasts=jsonObject.getJSONArray("DailyForecasts");
                            for(int i=0;i<5;i++){
                                JSONObject data=DailyForecasts.getJSONObject(i);


                                Long ngay=data.getLong("EpochDate");
                                String setday=ConvertTime(ngay);

                                String dayformat=Dayformat(ngay );



                                JSONObject Temperature=data.getJSONObject("Temperature");
                                JSONObject minTemp=Temperature.getJSONObject("Minimum");
                                JSONObject maxTemp=Temperature.getJSONObject("Maximum");

                                Double min=minTemp.getDouble("Value");
                                Double max=maxTemp.getDouble("Value");
                                // lấy nhiệt độ nhỏ nhất và nhiệt độ lớn nhất
                                String  temp=String.valueOf(min.intValue())+"-"+String.valueOf(max.intValue())+"°";

                                // lấy icon và trạng thái thời tiết

                                JSONObject Day=data.getJSONObject("Day");
                                Integer icon=Day.getInt("Icon");
                                String status=Day.getString("IconPhrase");


                                mangthoitiet.add(new ThoiTiet(setday,status,temp,icon,dayformat));

                            }
                            customAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(stringRequest);
    }
}
