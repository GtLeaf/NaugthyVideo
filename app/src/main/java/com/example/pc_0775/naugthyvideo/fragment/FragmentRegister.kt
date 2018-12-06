package com.example.pc_0775.naugthyvideo.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.example.pc_0775.naugthyvideo.Constants.Constants

import com.example.pc_0775.naugthyvideo.R
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FragmentRegister.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FragmentRegister.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRegister : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    //login
    internal var eh: EventHandler = object : EventHandler() {

        override fun afterEvent(event: Int, result: Int, data: Any?) {
            var i:Int = 0
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    i = 1
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    i = 2
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    i = 3
                }
            } else {
                i = 4
                (data as Throwable).printStackTrace()
            }
            System.out.println(i)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()!!.getString(ARG_PARAM1)
            mParam2 = getArguments()!!.getString(ARG_PARAM2)
        }
    }

    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    public override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init();
        setListener();
    }

    public override fun onDetach() {
        super.onDetach()
        SMSSDK.unregisterEventHandler(eh)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentRegister.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): FragmentRegister {
            val fragment = FragmentRegister()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }

    private fun init(){
        SMSSDK.registerEventHandler(eh) //注册短信回调
    }

    private fun setListener(){
        btn_register_get_identifying_code.setOnClickListener(View.OnClickListener {
            if (Constants.isMobileNO(et_register_phone_number.text.toString())){
                SMSSDK.getVerificationCode("86", et_register_phone_number.text.toString());
            }else{
                Toast.makeText(activity, "手机号错误", Toast.LENGTH_SHORT)
            }
        })

        et_register_phone_number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (Constants.isMobileNO(s.toString())){
                    iv_register_phone_number_tip.setImageResource(R.drawable.circle_green);
                }else{
                    iv_register_phone_number_tip.setImageResource(R.drawable.circle_red);
                }
            }
        })

        //检查两次密码是否一致
        et_register_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var password = et_register_password.text.toString()
                var password_repeat = et_register_password_repeat.text.toString()
                if (password.equals(password_repeat)){
                    iv_register_password_tip.setImageResource(R.drawable.circle_green)
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_green)
                }else{
                    iv_register_password_tip.setImageResource(R.drawable.circle_red);
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_red);
                }
            }
        })

        //检查两次密码是否一致
        et_register_password_repeat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var password = et_register_password.text.toString()
                var password_repeat = et_register_password_repeat.text.toString()
                if (password.equals(password_repeat)){
                    iv_register_password_tip.setImageResource(R.drawable.circle_green)
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_green)
                }else{
                    iv_register_password_tip.setImageResource(R.drawable.circle_red);
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_red);
                }
            }
        })

        switch_register_if_show.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //密码不可见
                et_register_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                et_register_password_repeat.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                //密码可见
                et_register_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                et_register_password_repeat.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
            et_register_password.setSelection(et_register_password.getText().length)
            et_register_password_repeat.setSelection(et_register_password_repeat.getText().length)
        })

    }

    /*fun isMobileNO(mobileNums:String):Boolean {
        *//**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         *//*
        var telRegex = Regex("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$")// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return telRegex.matches(mobileNums);
    }*/


}// Required empty public constructor

