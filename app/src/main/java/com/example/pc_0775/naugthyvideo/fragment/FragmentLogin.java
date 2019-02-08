package com.example.pc_0775.naugthyvideo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.Anno.annoUtil.ViewInjectUtils;
import com.example.pc_0775.naugthyvideo.Constants.Constants;
import com.example.pc_0775.naugthyvideo.R;
import com.example.pc_0775.naugthyvideo.bean.BaseResult;
import com.example.pc_0775.naugthyvideo.bean.UserBean;
import com.example.pc_0775.naugthyvideo.retrofit.UserLoginLoader;
import com.example.pc_0775.naugthyvideo.util.NetWorkUtil;
import com.example.pc_0775.naugthyvideo.util.SPUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.DeviceInfo;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment {
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
    @ViewInject(R.id.cb_remember_pass)
    private CheckBox cb_rememberPass;

    //handler
    private MyHandler handler = new MyHandler(this);

    //sharePreferences本地存储
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static class MyHandler extends Handler {
        WeakReference<FragmentLogin> weakReference;

        MyHandler(FragmentLogin fragmentLogin) {
            weakReference = new WeakReference<>(fragmentLogin);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentLogin fragmentLogin = weakReference.get();
            switch (msg.what) {
                case Constants.USER_LOGIN:
                    Type type = new TypeToken<BaseResult<UserBean>>(){}.getType();
                    BaseResult<UserBean> baseResult = NetWorkUtil.getGson().fromJson(msg.obj.toString(), type);
                    if(baseResult.getMessage().equals("success")){
                        Constants.user = baseResult.getResult().get(0);
                        fragmentLogin.activity.finish();
                    }else {
                        Constants.createAlertDialog(fragmentLogin.activity, baseResult.getMessage());
                    }

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

    private void init() {
        /**Description:获取记录在本地的登录账号以及密码*/
        takePassword();
    }

    private void setListener() {
        switch_loginIfShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //密码不可见
                    et_loginPassowrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //密码可见
                    et_loginPassowrd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
                et_loginPassowrd.setSelection(et_loginPassowrd.getText().length());
            }
        });

        btn_fragment_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = et_loginName.getText().toString();
                String password = et_loginPassowrd.getText().toString();
                rememberPassword(phone_number, password);
                if (phone_number.equals("")){
                    Constants.createAlertDialog(activity, "手机号不能为空");
                    return;
                }
                if (!Constants.isMobileNO(phone_number)){
                    Constants.createAlertDialog(activity, "手机号格式错误");
                    return;
                }
                if (password.equals("")){
                    Constants.createAlertDialog(activity, "密码不能为空");
                    return;
                }
//                sendPostRequest(phone_number, password);
                userLogin(activity, phone_number, password);
            }
        });
    }

    public static void userLogin(final Activity activity, String phone_number, String password){
        JMessageClient.login(phone_number, password, new RequestCallback<List<DeviceInfo>>() {
            @Override
            public void gotResult(int i, String s, List<DeviceInfo> deviceInfos) {
                /*Constants.createAlertDialog(activity, Constants.errorCodeTranfom(i));
                Constants.deviceInfoList = deviceInfos;
                for (DeviceInfo info : deviceInfos){
                    if (info.getPlatformType() == PlatformType.android){
                        Constants.androidDeviceInfo = info;
                    }
                }*/
                if (0 == i){
                    Constants.userInfo = JMessageClient.getMyInfo();
                    activity.finish();
                }
            }
        });
    }


    public void sendPostRequest(String phoneNumber, String password) {

        UserLoginLoader userLoginLoader = new UserLoginLoader();
        Map<String, String> map = new HashMap<>();
        map.put("phone_number", phoneNumber);
        map.put("password", password);
        userLoginLoader.postUserLogin(Constants.LOGIN_URL, phoneNumber, password)
        .subscribe(new Observer<BaseResult<UserBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseResult<UserBean> userBeanBaseResult) {
                if(userBeanBaseResult.getMessage().equals("success")){
                    Constants.user = userBeanBaseResult.getResult().get(0);
                    activity.finish();
                }else {
                    Constants.createAlertDialog(activity, userBeanBaseResult.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /*private void initPreferences(){
        if (null == preferences){
            preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            editor = preferences.edit();
        }
        if (null == editor){
            if (null == preferences){
                return;//获取不到
            }
            editor = preferences.edit();
        }
    }*/

    /**
     * 记住密码
     * @param phone_number
     * @param password
     */
    private void rememberPassword(String phone_number, String password){
        if (cb_rememberPass.isChecked()){
            SPUtils.Companion.put(activity, "isRemember", true);
            SPUtils.Companion.put(activity, "phone_number", phone_number);
            SPUtils.Companion.put(activity, "password", password);
        }
    }

    /**
     * 获取本地数据库中的账号密码
     */
    private void takePassword(){
        if((Boolean) SPUtils.Companion.get(activity, "isRemember", false)){
            et_loginName.setText((String)SPUtils.Companion.get(activity,"phone_number", ""));
            et_loginPassowrd.setText((String)SPUtils.Companion.get(activity,"password", ""));
            cb_rememberPass.setChecked(true);
        }
    }


}
