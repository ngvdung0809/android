package com.example.thoitiet.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thoitiet.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Hourly.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Hourly#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Hourly extends Fragment {
    LineChart lineChart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Hourly() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Hourly.
     */
    // TODO: Rename and change types and number of parameters
    public static Hourly newInstance(String param1, String param2) {
        Hourly fragment = new Hourly();
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
        View view= inflater.inflate(R.layout.fragment_hourly, container, false);
        lineChart=(LineChart)view.findViewById(R.id.chart);
        hourly(lineChart);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
        void onFragmentInteraction(Uri uri);
    }

    public String ConvertTime(long day){
        Date date=new Date(day*1000L);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        String Day=simpleDateFormat.format(date);
        return Day;
    }

    public void hourly(final LineChart lineChart){
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url="http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/353412?apikey=KtXroA6bnEx3YVNpAzeSCBqusF6Gstqb&language=vi&metric=true";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d("kq",response);
                        try{
                            JSONArray jsonArray= new JSONArray(response);
                            ArrayList<Entry> entries=new ArrayList<>();
                            for(int i=0;i<12;i++){
                                JSONObject hourly=jsonArray.getJSONObject(i);
                                Long time=hourly.getLong("EpochDateTime");
                                String hour=ConvertTime(time);

                                JSONObject Temperature=hourly.getJSONObject("Temperature");
                                Double temp=Temperature.getDouble("Value");

                                entries.add(new Entry(Integer.parseInt(hour),temp.floatValue()));
                            }
                            LineDataSet lineDataSet=new LineDataSet(entries,"nhiệt độ");
                            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                            lineDataSet.setValueTextSize(10f);
                            lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                            lineDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
                            lineDataSet.setLineWidth(5f);



                            // Controlling X axis( goi ra truc x)
                            XAxis xAxis = lineChart.getXAxis();
                            // Set the xAxis position to bottom. Default is top
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setTextSize(15f);
                            xAxis.setTextColor(Color.WHITE);
                            //Customizing x axis value

                            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1

                            // Controlling right side of y axis
                            YAxis yAxisRight = lineChart.getAxisRight();
                            yAxisRight.setEnabled(false);

                            //***
                            // Controlling left side of y axis
                            YAxis yAxisLeft = lineChart.getAxisLeft();
                            yAxisLeft.setGranularity(8f);
                            yAxisLeft.setTextColor(Color.WHITE);
                            yAxisLeft.setTextSize(15f);

                            // Setting Data
                            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(lineDataSet);
                            LineData data = new LineData(dataSets);
                            lineChart.setData(data);
                            lineChart.invalidate();



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
                });
        requestQueue.add(stringRequest);
    }

}
