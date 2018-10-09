package com.example.pc_0775.naugthyvideo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.bean.VideoInfo;
import com.example.pc_0775.naugthyvideo.view.ActivityFunctionVideo;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.example.pc_0775.naugthyvideo.view.ActivityVideoPlay;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentFunctionInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFunction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFunction extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentFunctionInteractionListener mListener;

    //view
    private View view;
    private Context mContext;
    private Button btn_classOne;
    private Button btn_classTwo;
    private Button btn_classThree;
    private Button btn_functionTest;

    //data

    //handler
    private MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<FragmentFunction> weakReference;

        public MyHandler(FragmentFunction fragmentFunction){
            weakReference = new WeakReference<FragmentFunction>(fragmentFunction);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentFunction fragment = weakReference.get();
            Context context = fragment.getActivity().getApplicationContext();
            if(null == msg.obj){
                Toast.makeText(context, "获取数据失败，请检查网络", Toast.LENGTH_SHORT).show();
                return;
            }
            List<VideoInfo> videoInfoList = NetWorkUtil.parseJsonArray(msg.obj.toString(), VideoInfo.class);
            if(videoInfoList.size() == 0){
                Toast.makeText(context, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (msg.what){
                case Constants.CLASS_ONE_REQUEST:
                    HashMap parameters = new HashMap();
                    parameters.put("leixing", "1");
                    Uri uri = NetWorkUtil.createUri(Constants.CLASS_ONE_VIDEO_URL, parameters);
                    ActivityFunctionVideo.actionStart(context, uri, videoInfoList);
                    break;
                case Constants.CLASS_TWO_REQUEST:
                    HashMap classTowparameters = new HashMap();
                    classTowparameters.put("leixing", "movielist1");
                    Uri classTowUri = NetWorkUtil.createUri(Constants.CLASS_TWO_VIDEO_URL, classTowparameters);
                    ActivityFunctionVideo.actionStart(context, classTowUri, videoInfoList);
                    break;
                case Constants.CLASS_THREE_REQUEST:
                    HashMap classThreeparameters = new HashMap();
                    classThreeparameters.put("leixing", "toupaizipai");
                    Uri classThreeUri = NetWorkUtil.createUri(Constants.CLASS_THREE_VIDEO_URL, classThreeparameters);
                    ActivityFunctionVideo.actionStart(context, classThreeUri, videoInfoList);
                    break;
            }
        }
    }

    public FragmentFunction() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFunction.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFunction newInstance(String param1, String param2) {
        FragmentFunction fragment = new FragmentFunction();
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
        view = inflater.inflate(R.layout.fragment_function, container, false);

        initView();
        setLinstener();
        return view;
    }

    private <T extends View> T $(int resId){
        return (T) view.findViewById(resId);
    }

    private void initView(){
        btn_classOne = $(R.id.btn_classOne);
        btn_classTwo = $(R.id.btn_classTwo);
        btn_classThree = $(R.id.btn_classThree);
        btn_functionTest = $(R.id.btn_function_test);
    }

    private void setLinstener(){
        btn_classOne.setOnClickListener(this);
        btn_classTwo.setOnClickListener(this);
        btn_classThree.setOnClickListener(this);
        btn_functionTest.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentFunctionInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_classOne:
                HashMap parameters = new HashMap();
                parameters.put("leixing", "se55");
                parameters.put("yeshu", "1");
                Uri uri = NetWorkUtil.createUri(Constants.CLASS_ONE_VIDEO_URL, parameters);
                NetWorkUtil.sendRequestWithOkHttp(uri.toString(), Constants.CLASS_ONE_REQUEST, handler );
                break;
            case R.id.btn_classTwo:
                HashMap classTowparameters = new HashMap();
                classTowparameters.put("leixing", "movielist1");
                classTowparameters.put("yeshu", "1");
                Uri classTowuri = NetWorkUtil.createUri(Constants.CLASS_TWO_VIDEO_URL, classTowparameters);
                NetWorkUtil.sendRequestWithOkHttp(classTowuri.toString(), Constants.CLASS_TWO_REQUEST, handler );
                break;
            case R.id.btn_classThree:
                HashMap classThreeparameters = new HashMap();
                classThreeparameters.put("leixing", "toupaizipai");
                classThreeparameters.put("yeshu", "1");
                Uri classThreeUri = NetWorkUtil.createUri(Constants.CLASS_THREE_VIDEO_URL, classThreeparameters);
                NetWorkUtil.sendRequestWithOkHttp(classThreeUri.toString(), Constants.CLASS_THREE_REQUEST, handler );
                break;
            case R.id.btn_function_test:
                ActivityVideoPlay.actionStart(getActivity().getApplicationContext(), Constants.TEST_VIDEO_URL, true);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentFunctionInteractionListener) {
            mListener = (OnFragmentFunctionInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentFunctionInteractionListener");
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
    public interface OnFragmentFunctionInteractionListener {
        // TODO: Update argument type and name
        void onFragmentFunctionInteraction(Uri uri);
    }
}
