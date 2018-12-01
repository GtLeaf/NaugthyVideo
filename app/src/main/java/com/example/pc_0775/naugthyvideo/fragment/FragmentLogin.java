package com.example.pc_0775.naugthyvideo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.view.ActivityHome;
import com.example.pc_0775.naugthyvideo.view.ActivityLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //context
    private View mView;

    private Activity activity;

    //mView
    @ViewInject(R.id.switch_login_if_show)
    private Switch switch_loginIfShow;
    @ViewInject(R.id.et_login_name)
    private EditText et_loginName;
    @ViewInject(R.id.et_login_passowrd)
    private EditText et_loginPassowrd;
    @ViewInject(R.id.btn_fragment_login)
    private Button btn_fragment_login;

    //handler
    private MyHandler handler = new MyHandler(this);
    public static class MyHandler extends Handler{
        WeakReference<FragmentLogin> weakReference;

        MyHandler(FragmentLogin fragmentLogin){
            weakReference = new WeakReference<>(fragmentLogin);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentLogin fragmentLogin = weakReference.get();
            switch (msg.what){
                case Constants.LOGIN_SUCCESS:
                    break;
                default:
                    break;
            }
        }


    }

    public FragmentLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
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
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewInjectUtils.fragmentInject(this);
        activity = getActivity();
        init();
        setListener();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }

    private void init(){
    }

    private void setListener(){
        switch_loginIfShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //密码不可见
                    et_loginPassowrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    //密码可见
                    et_loginPassowrd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
                et_loginPassowrd.setSelection(et_loginPassowrd.getText().length());
            }
        });

        btn_fragment_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityHome.class);
                startActivity(intent);
                activity.finish();
//                sendPostRequest(et_loginName.getText().toString(), et_loginPassowrd.getText().toString());
            }
        });
    }

    public void sendPostRequest(String phoneNumber, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("phone_number", phoneNumber)
                .add("password", password)
                .build();
        final Request request = new Request.Builder()
                .url(Constants.LOGIN_URL)
                .post(requestBody)
                .build();

        final OkHttpClient client = new OkHttpClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Message message = Message.obtain();
                message.what = Constants.LOGIN_SUCCESS;
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        boolean success = false;
                        try {
                            success = new JSONObject(response.body().string()).getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (success) {
//                            Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), ActivityHome.class);
                            startActivity(intent);
                            activity.finish();
                        }else {
//                            Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                            Log.d("login", "onResponse: 登录失败");
                        }

                    }
                });
            }
        }).start();
    }


}
