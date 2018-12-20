package com.example.pc_0775.naugthyvideo.MyViewControl.MyLayout

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.pc_0775.naugthyvideo.R

/**
 * 自定义TextView，实现文本“展开”，“收起”功能
 * Created by PC-0775 on 2018/12/20.
 */
class LimitSpannableTextView(context: Context, attrs: AttributeSet?=null) : AppCompatTextView(context, attrs){

    init {
//        limitTextViewString(this.text.toString(), 140, this, object :OnClickListener{
//            override fun onClick(v: View?) {
//                //设置监听函数
//            }
//        })
    }

    fun setLimitText(text: CharSequence){
        limitTextViewString(text.toString(), 140, this, object :OnClickListener{
            override fun onClick(v: View?) {
                //设置监听函数
            }
        })
    }

    /**
     * get the last char index for max limit row,if not exceed the limit,return -1
     * @param textView
     * @param content
     * @param width
     * @param maxLine
     * @return
     */
    private fun getLastCharIndexForLimitTextView(textView: TextView, content:String, width:Int, maxLine:Int):Int{
        var textPaint:TextPaint = textView.paint
        var staticLayout:StaticLayout = StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
        if (staticLayout.lineCount > maxLine)
            return staticLayout.getLineStart(maxLine)-1
        else return -1
    }

    /**
     * 限制TextView显示字符字符，并且添加showMore和show more的点击事件
     * @param textString
     * @param textView
     * @param clickListener textView的点击监听器
     */
    private fun limitTextViewString(textString:String, maxFirstShowChartCount:Int, textView: TextView, listener: OnClickListener){
        //计算处理话费的时间
        var startTime = System.currentTimeMillis()
        if (null == textView) return;
        var width = textView.width //在recyclerView和ListView中，由于复用的原因，这个TextView可能以前就画好了，能获得宽度
        if (0 == width) width = 1000;//获取textView的实际宽度，这里可以用各种方式（一般是dp转px写死）填入TextView的宽度
        //返回-1表示没有达到maxLine
        var lastCharIndex = getLastCharIndexForLimitTextView(textView, textString, width, 5)
        //行数没有超，字符串长度没有超过限制
        if (lastCharIndex<0 && textString.length <= maxFirstShowChartCount){
            //行数没超过限制
            textView.setText(textString)
            return
        }
        //行数超出了限制
        //this will deprive the recyclerView's focus
        textView.movementMethod = LinkMovementMethod.getInstance()
        if (lastCharIndex>maxFirstShowChartCount || lastCharIndex<0){
            lastCharIndex = maxFirstShowChartCount;
        }
        //构造spannableString
        var explicitText = ""
        var explicitTextAll = ""
        if ('\n' == textString.get(lastCharIndex)){
            explicitText = textString.substring(0, lastCharIndex)
        }else if (lastCharIndex > 12){
            //如果最大行数限制的那一行到达12以后则直接显示"显示更多"
            explicitText = textString.substring(0, lastCharIndex-12)
        }
        var sourceLength = explicitText.length
        val showMore = "展开"
        explicitText = explicitText+"..."+showMore
        var mSpan = SpannableString(explicitText)

        val dismissMore = "收起"
        explicitTextAll = textString+dismissMore

        var mSpanAll = SpannableString(explicitTextAll)
        mSpanAll.setSpan(object :ClickableSpan(){

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.color = ContextCompat.getColor(textView.context, R.color.colorPrimary)
                ds?.isAntiAlias = true
                ds?.isUnderlineText = false
            }

            override fun onClick(widget: View?) {
                textView.text = mSpan
                textView.setOnClickListener(null)
                var handler = android.os.Handler()
                handler.postDelayed({
                    if (listener != null){
                        //防止点击两次
                        textView.setOnClickListener(listener)
                    }
                }, 20)
            }
        }, textString.length, explicitTextAll.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        mSpan.setSpan(object :ClickableSpan(){
            override fun onClick(widget: View?) {
                textView.text = mSpanAll
                textView.setOnClickListener(null)
                var handler = android.os.Handler()
                handler.postDelayed({
                    if (listener != null){
                        //防止点击两次
                        textView.setOnClickListener(listener)
                    }
                }, 20)
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.color = ContextCompat.getColor(textView.context, R.color.colorPrimary)
                ds?.isAntiAlias = true
                ds?.isUnderlineText = false
            }

        }, sourceLength, explicitText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //设置为“展开”状态下的TextVie
        textView.setText(mSpan)
        Log.i("info", "字符串处理耗时" + (System.currentTimeMillis() - startTime) + " ms");
    }
}