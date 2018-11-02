package com.example.pc_0775.naugthyvideo.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK

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

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                (data as Throwable).printStackTrace()
            }
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

    public override fun onDetach() {
        super.onDetach()

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
        btn_register_get_identifying_code.setOnClickListener(View.OnClickListener { })
    }
}// Required empty public constructor

