package com.example.pc_0775.naugthyvideo.MyView

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView
import com.example.pc_0775.naugthyvideo.R

/**
 * 自定义TextView，实现文本“展开”，“收起”功能
 * 原博客地址：https://blog.csdn.net/qq_28695619/article/details/72857907
 * Created by PC-0775 on 2018/12/20.
 */
class LimitSpannableTextView(context: Context, attrs: AttributeSet?=null) : AppCompatTextView(context, attrs){

    private var mLastActionDownTime:Long = -1

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
        var staticLayout = StaticLayout(content, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
        return if (staticLayout.lineCount > maxLine)
            staticLayout.getLineStart(maxLine)
        else -1
    }

    /**
     * 限制TextView显示字符字符，并且添加showMore和show more的点击事件
     * @param textString
     * @param textView
     * @param clickListener textView的点击监听器
     */
    private fun setLimitString(textString:String, listener: OnClickListener){
        //计算处理话费的时间
        var startTime = System.currentTimeMillis()
        if (null == this) return
        var width = this.width //在recyclerView和ListView中，由于复用的原因，这个TextView可能以前就画好了，能获得宽度
        if (0 == width) width = this.paint.measureText(textString).toInt()//获取textView的实际宽度，这里可以用各种方式（一般是dp转px写死）填入TextView的宽度
        //返回-1表示没有达到maxLine
        var lastCharIndex = getLastCharIndexForLimitTextView(this, textString, width, 4)
        //行数没有超，字符串长度没有超过限制
        if (lastCharIndex<0){
            //行数没超过限制
            this.setText(textString)
            return
        }
        //行数超出了限制
        //this will deprive the recyclerView's focus
        this.movementMethod = LinkMovementMethod.getInstance()

        //构造spannableString
        var explicitText = ""
        var explicitTextAll = ""
        //最后一个字符不能是换行
        if ('\n' == textString.get(lastCharIndex)){
            explicitText = textString.substring(0, lastCharIndex)
        }else{
            explicitText = textString.substring(0, lastCharIndex+1)
        }
        /*
        * sourceLength：收起时Text中字符串的总长度
        * */
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
                ds?.color = ContextCompat.getColor(this@LimitSpannableTextView.context, R.color.colorPrimary)
                ds?.isAntiAlias = true
                ds?.isUnderlineText = false
            }

            override fun onClick(widget: View?) {
                this@LimitSpannableTextView.text = mSpan
                this@LimitSpannableTextView.setOnClickListener(null)
                var handler = android.os.Handler()
                handler.postDelayed({
                    if (listener != null){
                        //防止点击两次
                        this@LimitSpannableTextView.setOnClickListener(listener)
                    }
                }, 20)
            }
        }, textString.length, explicitTextAll.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        mSpan.setSpan(object :ClickableSpan(){
            override fun onClick(widget: View?) {
                this@LimitSpannableTextView.text = mSpanAll
                this@LimitSpannableTextView.setOnClickListener(null)
                android.os.Handler().postDelayed({
                    if (listener != null){
                        //防止点击两次
                        this@LimitSpannableTextView.setOnClickListener(listener)
                    }
                }, 20)
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.color = ContextCompat.getColor(this@LimitSpannableTextView.context, R.color.colorPrimary)
                ds?.isAntiAlias = true
                ds?.isUnderlineText = false
            }

        }, sourceLength, explicitText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //默认设置为“展开”状态下的TextVie
        this.setText(mSpan)
        Log.i("info", "字符串处理耗时" + (System.currentTimeMillis() - startTime) + " ms");
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        //处理ClicekSpan与textIsSelectable属性冲突，暂时不起作用
        /*if(this.text != null && text is Spannable){
            handleLinkMovementMethod(this, text as Spannable, event)
        }*/


        return super.onTouchEvent(event)
    }

    private fun handleLinkMovementMethod(widget: TextView, buffer: Spannable, event: MotionEvent?):Boolean{
        val action = event?.action
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN){
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            var layout = widget.layout
            var line = layout.getLineForVertical(y)
            var off = layout.getOffsetForHorizontal(line, x.toFloat())

            var link = buffer.getSpans(off, off, ClickableSpan::class.java)

            if (!link.isNotEmpty()){
                if (action == MotionEvent.ACTION_UP){
                    var actionUpTime = System.currentTimeMillis()
                    if (actionUpTime - mLastActionDownTime > ViewConfiguration.getLongPressTimeout()){
                        //长按事件，取消LinkMovementMethod处理，即不处理ClickableSpan点击事件
                        return false
                    }
                }
                link[0].onClick(widget)
                Selection.removeSelection(buffer)
            }else if (action == MotionEvent.ACTION_DOWN){
                Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]))
                mLastActionDownTime = System.currentTimeMillis()
            }
        }
        return false
    }

    /*
    * 为textView添加文本长度限制
    * @param textView       需要操作的TextView
    * @param text           textView需要设置的文本
    * @param limitNumber    最大允许显示的字符数
    * @param listener       点击事件监听
    * */
    fun setLimitText(text:String, limitNumber:Int, listener:View.OnClickListener?, textView: TextView = this){
        //如果文本长度没有超过限制，则直接setText
        if(text.length < limitNumber){
            textView.text = text
            return
        }
        //允许TextView中文本改变,发生移动
        textView.movementMethod = LinkMovementMethod.getInstance()
        //构造spannableString
        var explicitText = ""
        var explicitTextAll = ""
        //最后一个字符是换行，就不读取
        explicitText = if ('\n' == text.get(limitNumber)){
            text.substring(0, limitNumber)
        }else{
            text.substring(0, limitNumber+1)
        }
        /*
        * sourceLength：收起时Text中字符串的总长度
        * */
        var sourceLength = explicitText.length
        val showMore = "展开"
        explicitText = explicitText+"..."+showMore
        var mSpan = SpannableString(explicitText)

        val dismissMore = "收起"
        explicitTextAll = text+dismissMore
        var mSpanAll = SpannableString(explicitTextAll)

        //设置“展开”的点击事件
        mSpan.setSpan(object :ClickableSpan(){
            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.color = ContextCompat.getColor(textView.context, R.color.colorPrimary)
                ds?.isAntiAlias = true
                ds?.isUnderlineText = false
            }

            override fun onClick(widget: View?) {
                textView.text = mSpanAll
                textView.setOnClickListener(null)
                android.os.Handler().postDelayed({
                    //防止触发TextView的点击事件
                    textView.setOnClickListener(listener)
                }, 20)
            }
        }, sourceLength, explicitText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        //设置“收起”的点击事件
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
                android.os.Handler().postDelayed({
                    //防止触TextView的发点击事件
                    textView.setOnClickListener(listener)
                }, 20)
            }
        }, text.length, explicitTextAll.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //默认为需要展开状态
        textView.setText(mSpan)
    }

}