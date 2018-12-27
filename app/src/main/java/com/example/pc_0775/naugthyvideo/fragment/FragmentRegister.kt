package com.example.pc_0775.naugthyvideo.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.example.pc_0775.naugthyvideo.Constants.Constants

import com.example.pc_0775.naugthyvideo.R
import kotlinx.android.synthetic.main.fragment_register.*
import android.os.Looper
import android.os.Message
import android.widget.*
import com.example.pc_0775.naugthyvideo.view.ActivityHome
import okhttp3.*
import java.io.IOException


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
    private var IS_VERIFICATION_CODE_RIGHT = false
    private var selectSex = "男"

    //login
    internal var eh: EventHandler = object : EventHandler() {

        override fun afterEvent(event: Int, result: Int, data: Any) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            val msg = Message()
            msg.arg1 = event
            msg.arg2 = result
            msg.obj = data
            Handler(Looper.getMainLooper(), Handler.Callback { msg ->
                val event = msg.arg1
                val result = msg.arg2
                val data = msg.obj
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理成功得到验证码的结果
                        // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    } else {
                        // TODO 处理错误的结果
                        (data as Throwable).printStackTrace()
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理验证码验证通过的结果
                        IS_VERIFICATION_CODE_RIGHT = true
                        iv_register_identifying_code_tip.setImageResource(R.drawable.circle_green)
                    } else {
                        // TODO 处理错误的结果
                        IS_VERIFICATION_CODE_RIGHT = true
                        iv_register_identifying_code_tip.setImageResource(R.drawable.circle_red)
                        (data as Throwable).printStackTrace()

                    }
                }
                // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                false
            }).sendMessage(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()!!.getString(ARG_PARAM1)
            mParam2 = getArguments()!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onAttach(context: Context?) {
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
        rb_sex_man.isChecked = true
    }

    private fun setListener(){
        //获取验证码
        btn_register_get_identifying_code.setOnClickListener {
            // 在尝试读取通信录时以弹窗提示用户（可选功能）
            SMSSDK.setAskPermisionOnReadContact(true);

            if (Constants.isMobileNO(et_register_phone_number.text.toString())){
                SMSSDK.getVerificationCode(Constants.country, et_register_phone_number.text.toString());
            }else{
                Toast.makeText(activity, "手机号错误", Toast.LENGTH_SHORT)
            }
        }

        //验证码校验
        et_register_identifying_code.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 4){
                    SMSSDK.submitVerificationCode(Constants.country, et_register_phone_number.text.toString(), s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        //对手机号进行验证,小红点提示
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
                val password = et_register_password.text.toString()
                val password_repeat = et_register_password_repeat.text.toString()
                if (password.equals(password_repeat)){
                    iv_register_password_tip.setImageResource(R.drawable.circle_green)
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_green)
                }else{
                    iv_register_password_tip.setImageResource(R.drawable.circle_red)
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_red)
                }
                if (password.equals("")){
                    iv_register_password_tip.setImageResource(R.drawable.circle_red)
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_red)
                }
            }
        })

        //检查两次密码是否一致
        et_register_password_repeat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = et_register_password.text.toString()
                val password_repeat = et_register_password_repeat.text.toString()
                if (password.equals(password_repeat)){
                    iv_register_password_tip.setImageResource(R.drawable.circle_green)
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_green)
                }else{
                    iv_register_password_tip.setImageResource(R.drawable.circle_red);
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_red);
                }
            }
        })

        //密码可见按钮
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

        //性别
        rg_register_sex.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == rb_sex_man.id){
                selectSex = rb_sex_man.text.toString()
            }else if (checkedId == rb_sex_woman.id){
                selectSex = rb_sex_woman.text.toString()
            }
        }

        //注册
        btn_fragment_register.setOnClickListener(View.OnClickListener {
            if (!IS_VERIFICATION_CODE_RIGHT){
                Constants.createAlertDialog(activity, "验证码错误！")
                return@OnClickListener
            }
            sendRegisterRequest()
        })
    }

    fun sendRegisterRequest(){
        val requestBody:RequestBody = FormBody.Builder()
                .add("phone_number", et_register_phone_number.text.toString())
                .add("nick_name", et_register_nickname.text.toString())
                .add("password", et_register_password.text.toString())
                .add("sex", selectSex)
                .build()
        val request = Request.Builder()
                .url(Constants.REGITER_URL)
                .post(requestBody)
                .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                e!!.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response!!.body().string()
                val intent = Intent(activity, ActivityHome::class.java)
                startActivity(intent)
                activity.finish()
            }
        })
    }
}

