package com.example.pc_0775.naugthyvideo.fragment

import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo
import cn.jpush.im.api.BasicCallback
import com.example.pc_0775.naugthyvideo.bean.BaseResult
import com.example.pc_0775.naugthyvideo.bean.UserBean
import com.example.pc_0775.naugthyvideo.retrofit.UserLoginLoader
import com.example.pc_0775.naugthyvideo.view.ActivityHome
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


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
    private var isVerificationCodeOk = false
    private var isPasswordConsistentOk = false
    private var selectSex = "男"

    //login
    private var eh: EventHandler = object : EventHandler() {

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
                        var smart = data as Boolean
                        if(smart) {
                               //通过Mob云验证
                                isVerificationCodeOk = true
                                iv_register_identifying_code_tip.setImageResource(R.drawable.circle_green)
                                et_register_identifying_code.setText("云验证通过")
                            } else {
                               //依然走短信验证
                            }
                    } else {
                        // TODO 处理错误的结果
                        (data as Throwable).printStackTrace()
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // TODO 处理验证码验证通过的结果
                        isVerificationCodeOk = true
                        iv_register_identifying_code_tip.setImageResource(R.drawable.circle_green)
                    } else {
                        // TODO 处理错误的结果
                        isVerificationCodeOk = true
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
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setListener()
    }

    override fun onDetach() {
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
            fragment.arguments = args
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
            SMSSDK.setAskPermisionOnReadContact(true)

            if (Constants.isMobileNO(et_register_phone_number.text.toString())){
                SMSSDK.getVerificationCode(Constants.country, et_register_phone_number.text.toString())
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
                    iv_register_phone_number_tip.setImageResource(R.drawable.circle_green)
                }else{
                    iv_register_phone_number_tip.setImageResource(R.drawable.circle_red)
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
                    isPasswordConsistentOk = true
                }else{
                    iv_register_password_tip.setImageResource(R.drawable.circle_red)
                    iv_register_password_repeat_tip.setImageResource(R.drawable.circle_red)
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
            et_register_password.setSelection(et_register_password.text.length)
            et_register_password_repeat.setSelection(et_register_password_repeat.text.length)
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
            //昵称检查
            if (et_register_nickname.text.isEmpty()){
                Constants.createAlertDialog(activity, "昵称不能为空！")
                return@OnClickListener
            }
            //验证码检查
            if (!isVerificationCodeOk){
                Constants.createAlertDialog(activity, "验证码错误！")
                return@OnClickListener
            }
            //密码一致检查
            if (!isPasswordConsistentOk){
                Constants.createAlertDialog(activity, "两次密码不一样！")
                return@OnClickListener
            }
            //密码为空检查
            if (et_register_password_repeat.text.isEmpty()){
                Constants.createAlertDialog(activity, "密码不能为空！")
                return@OnClickListener
            }

            var userInfo = RegisterOptionalUserInfo()
            userInfo.nickname = et_register_nickname.text.toString()
            if("男" == selectSex){
                userInfo.gender = UserInfo.Gender.male
            }else if ("女" == selectSex){
                userInfo.gender = UserInfo.Gender.female
            }
            var extraParameters = mutableMapOf<String, String>()
            extraParameters.put("isVIP", "false")
            userInfo.extras = extraParameters
            //发送注册请求
            JMessageClient.register(et_register_phone_number.text.toString(), et_register_password.text.toString(), userInfo, object : BasicCallback(){
                override fun gotResult(p0: Int, p1: String?) {

                    //如果注册失败，直接返回
                    if (0 == p0){
                        Constants.createAlertDialog(activity, "", "注册成功", "确定", object :DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                FragmentLogin.userLogin(activity, et_register_phone_number.text.toString(), et_register_password.text.toString())
                            }
                        })
                    }else{
                        Constants.createAlertDialog(activity, p1)
                    }
                }
            })

//            sendRegisterRequest()
        })
    }

    private fun sendRegisterRequest(){

        //用retrofit实现注册
        var userLoginLoader = UserLoginLoader()
        userLoginLoader.postUserRegister(Constants.REGITER_URL, et_register_phone_number.text.toString(),
                et_register_nickname.text.toString(), et_register_password.text.toString(), selectSex)
                .subscribe(object : Observer<BaseResult<Objects>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(userBeanBaseResult: BaseResult<Objects>) {
                        var msg = userBeanBaseResult.message
                        activity.runOnUiThread { Constants.createAlertDialog(activity, msg) }
                        if (msg == "注册成功"){
                            sendPostRequest(et_register_phone_number.text.toString(), et_register_password.text.toString())
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {

                    }
                })
    }

    fun sendPostRequest(phoneNumber: String, password: String) {
        val requestBody = FormBody.Builder()
                .add("phone_number", phoneNumber)
                .add("password", password)
                .build()
        val request = Request.Builder()
                .url(Constants.LOGIN_URL)
                .post(requestBody)
                .build()

        val client = OkHttpClient()
        Thread(Runnable {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()?.string()
                    val msg = JSONObject(json).getString("message")
                    if (msg == "success"){
                        activity.finish()
                    }
                }
            })
        }).start()
    }
}




