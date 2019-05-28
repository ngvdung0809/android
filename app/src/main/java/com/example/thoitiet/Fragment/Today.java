package com.example.thoitiet.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thoitiet.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Today.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Today#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Today extends Fragment {

    TextView txtTemp,txtStatus,txtdoam,txtgio,txtmay,txtapxuat,txtuv,txttamnhin,txtmucdouv;
    TextView txtcsonhiem,txtmucdonguyhiem,txtupdatetime;
    LinearLayout bg;
    ImageView imgicon;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Today() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Today.
     */
    // TODO: Rename and change types and number of parameters
    public static Today newInstance(String param1, String param2) {
        Today fragment = new Today();
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
        GetCurrentWeatherData();
        GetPolution();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_today, container, false);
        //setData();
        txtdoam=(TextView) view.findViewById(R.id.tvdoam);
        txtgio=(TextView) view.findViewById(R.id.tvtocdogio);
        txtmay=(TextView)view.findViewById(R.id.tvmay);
        txtapxuat=(TextView)view.findViewById(R.id.tvapxuat);
        txtuv=(TextView)view.findViewById(R.id.tvtiauv);
        txttamnhin=(TextView)view.findViewById(R.id.tvtamnhin);
//        txtCity=(TextView)view.findViewById(R.id.tvcity);
//        txtDay=(TextView)view.findViewById(R.id.tvday);
        txtStatus=(TextView)view.findViewById(R.id.tvstatus);
        txtTemp=(TextView)view.findViewById(R.id.tvtemp);
        imgicon=(ImageView) view.findViewById(R.id.icon);
        txtmucdouv=(TextView) view.findViewById(R.id.mucdouv);
        txtcsonhiem=(TextView) view.findViewById(R.id.csonhiem);
        txtmucdonguyhiem=(TextView)view.findViewById(R.id.mucdo);
        bg=(LinearLayout) view.findViewById(R.id.bgmau);
        txtupdatetime=(TextView)view.findViewById(R.id.updatetime);

        return view ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //@Override
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
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm:ss");
        String Day=simpleDateFormat.format(date);
        return Day;
    }

    private void GetPolution(){
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url="http://api.waqi.info/search/?keyword=hanoi&token=207d67b4b253dfd518da8c32809c81c53ba20f46";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d("kq",response);
                        try{
                            JSONObject jsonObject= new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            JSONObject  data=jsonArray.getJSONObject(0);
                            String chiso=data.getString("aqi");
                            txtcsonhiem.setText(chiso);
                            int mau= Integer.parseInt(chiso);
                            if(mau>0&&mau<50){
                                bg.setBackgroundColor(getResources().getColor(R.color.green));
                                txtmucdonguyhiem.setText("Tốt");
                                txtmucdonguyhiem.setTextColor(getResources().getColor(R.color.green));
                            }
                            else if (mau<100){
                                bg.setBackgroundColor(getResources().getColor(R.color.yellow));
                                txtmucdonguyhiem.setText("Trung Bình");
                                txtmucdonguyhiem.setTextColor(getResources().getColor(R.color.yellow));
                            }
                            else if (mau<150){
                                bg.setBackgroundColor(getResources().getColor(R.color.orange));
                                txtmucdonguyhiem.setText("Không tốt cho người nhạy cảm");
                                txtmucdonguyhiem.setTextColor(getResources().getColor(R.color.orange));
                            }
                            else if (mau<200){
                                bg.setBackgroundColor(getResources().getColor(R.color.red));
                                txtmucdonguyhiem.setText("Có hại cho sức khỏe");
                                txtmucdonguyhiem.setTextColor(getResources().getColor(R.color.red));
                            }
                            else if (mau<300){
                                bg.setBackgroundColor(getResources().getColor(R.color.violet));
                                txtmucdonguyhiem.setText("Rất có hại cho sức khỏe");
                                txtmucdonguyhiem.setTextColor(getResources().getColor(R.color.violet));
                            }
                            else{
                                bg.setBackgroundColor(getResources().getColor(R.color.purple));
                                txtmucdonguyhiem.setText("Nguy Hiểm");
                                txtmucdonguyhiem.setTextColor(getResources().getColor(R.color.purple));
                            }


                            JSONObject time=data.getJSONObject("time");
                            Long ngay=time.getLong("vtime");
                            String setday=ConvertTime(ngay);
                            txtupdatetime.setText("Update: "+setday);



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

    private void  GetCurrentWeatherData(){
        //Toast.makeText(Activity_Today.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
        // thực thi các request sẽ gửi đi
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        String url="http://dataservice.accuweather.com/currentconditions/v1/353412?apikey=KtXroA6bnEx3YVNpAzeSCBqusF6Gstqb&language=vi&details=true";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("ketqua",response);
                        try{

                            JSONArray jsonArray=new JSONArray(response);
                            JSONObject jsonObject=jsonArray.getJSONObject(0);


                            // trạng thái thời tiết
                            String statusweather=jsonObject.getString("WeatherText");
                            txtStatus.setText(statusweather);

                            Integer a=jsonObject.getInt("WeatherIcon");
                            String icon;
                            if(a<10){
                                icon="0"+a.toString();
                            }
                            else{
                                icon=a.toString();
                            }
                            Picasso.with(getContext()).load("https://developer.accuweather.com/sites/default/files/"+icon+"-s.png").into(imgicon);

                            // lấy nhiệt độ
                            JSONObject jsonObjecttemp=jsonObject.getJSONObject("Temperature");
                            JSONObject jsonObjectmetric=jsonObjecttemp.getJSONObject("Metric");
                            Double temp=jsonObjectmetric.getDouble("Value");
                            String nhietdo=String.valueOf(temp.intValue())+"°";
                            txtTemp.setText(nhietdo);

                            // lấy thông tin về độ ẩm
                            Integer do_am=jsonObject.getInt("RelativeHumidity");
                            txtdoam.setText(do_am.toString()+"%");

                            // lấy thông tin gió
                            JSONObject jsonObjectwind=jsonObject.getJSONObject("Wind");
                            JSONObject jsonObjectDirection=jsonObjectwind.getJSONObject("Direction");
                            JSONObject jsonObjectSpeed=jsonObjectwind.getJSONObject("Speed");
                            String huong_gio=jsonObjectDirection.getString("English");
                            JSONObject jsonObjectSpeedMetric=jsonObjectSpeed.getJSONObject("Metric");
                            Double wind_speed=jsonObjectSpeedMetric.getDouble("Value");
                            txtgio.setText(huong_gio+" "+wind_speed.toString());

                            // lấy giá trị UV
                            Integer UVindex=jsonObject.getInt("UVIndex");
                            String UVindextext=jsonObject.getString("UVIndexText");
                            txtuv.setText(UVindex.toString());
                            txtmucdouv.setText(UVindextext);


                            // lấy thông tin  tầm nhìn
                            JSONObject jsonObjectVisibility=jsonObject.getJSONObject("Visibility");
                            JSONObject jsonObjectVisibilityMetric=jsonObjectVisibility.getJSONObject("Metric");
                            Double tam_nhin=jsonObjectVisibilityMetric.getDouble("Value");
                            String visibility_unit=jsonObjectVisibilityMetric.getString("Unit");
                            txttamnhin.setText(tam_nhin.toString()+""+visibility_unit);

                            // lấy thông tin áp xuất
                            JSONObject jsonObjectPressure =jsonObject.getJSONObject("Pressure");
                            JSONObject jsonObjectPressureMetric=jsonObjectPressure.getJSONObject("Metric");
                            Double ap_xuat=jsonObjectPressureMetric.getDouble("Value");
                            txtapxuat.setText(ap_xuat.toString());

                            //lấy % mây
                            Integer ptmay=jsonObject.getInt("CloudCover");
                            txtmay.setText(String.valueOf(ptmay)+"%");


                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);

    }


}

