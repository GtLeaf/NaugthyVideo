package com.example.pc_0775.naugthyvideo.Anno.annoUtil;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.pc_0775.naugthyvideo.Anno.ContentView;
import com.example.pc_0775.naugthyvideo.Anno.DynamicHandler;
import com.example.pc_0775.naugthyvideo.Anno.EventBase;
import com.example.pc_0775.naugthyvideo.Anno.ViewInject;
import com.example.pc_0775.naugthyvideo.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by PC-0775 on 2018/9/22.
 */

public class ViewInjectUtils {

    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    public static void inject(Activity activity){
        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);
    }

    public static void fragmentInject(Fragment fragment){
        fragmentInjectViews(fragment);
    }

    /**注入布局文件
     * @param activity
     */
    private static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        // 查询类上是否存在ContentView的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {//存在
            int contentViewLayoutId = contentView.value();

            try {
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入所有的控件
     *
     * @param activity
     */
    private static void injectViews(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        //遍历所有成员对象
        for(Field field : fields){
            ViewInject viewInjectAnnotation = field.getAnnotation(ViewInject.class);
            if (viewInjectAnnotation != null) {
                int viewId = viewInjectAnnotation.value();
                if(viewId != -1){
                    Log.e("TAG", "injectViews: "+viewId);
                    //初始化View
                    try {
                        Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        Object resView = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        field.set(activity, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 为fragment注入所有的控件
     *
     * @param fragment
     */
    private static void fragmentInjectViews(Fragment fragment){
        Class<? extends View> viewClass = fragment.getView().getClass();
        Class<? extends Fragment> fragmentClass = fragment.getClass();
        Field[] fields = fragmentClass.getDeclaredFields();
        //遍历所有成员对象
        for(Field field : fields){
            ViewInject viewInjectAnnotation = field.getAnnotation(ViewInject.class);
            if (viewInjectAnnotation != null) {
                int viewId = viewInjectAnnotation.value();
                if(viewId != -1){
                    Log.e("TAG", "injectViews: "+viewId);
                    //初始化View
                    try {
                        Method method = viewClass.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        Object resView = method.invoke(fragment.getView(), viewId);
                        field.setAccessible(true);
                        field.set(fragment, resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注入所有的事件
     * @param activity
     */
    private static void injectEvents(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getMethods();
        //遍历所有的方法
        for (Method method : methods){
            Annotation[] annotations = method.getAnnotations();
            //拿到方法上所有的注解
            for (Annotation annotation : annotations){
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //拿到注解上的注解
                EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
                //如果设置为EventBase
                if (eventBaseAnnotation != null) {
                    //取出设置监听器的名称，监听器的类型，调用的方法名
                    String listenerSetter = eventBaseAnnotation.listenerSetter();
                    Class<?> listenerType = eventBaseAnnotation.listenerType();
                    String methodName = eventBaseAnnotation.methodName();

                    try {
                        //拿到Onclick注解中的value方法
                        Method aMethod = annotationType.getDeclaredMethod("value");
                        //取出所有的viewId
                        int[] viewIds = (int[]) aMethod.invoke(annotation, new Object());
                        //通过InvocationHandler设置代理
                        DynamicHandler handler = new DynamicHandler(activity);
                        handler.addMethod(methodName, method);
                        Object listener = Proxy.newProxyInstance(
                                listenerType.getClassLoader(),
                                new Class[]{ listenerType }, handler);
                        //遍历所有的View，设置事件
                        for (int viewId : viewIds){
                            View view = activity.findViewById(viewId);
                            Method setEventListenerMethod = view.getClass()
                                    .getMethod(listenerSetter, listenerType);
                            setEventListenerMethod.invoke(view, listener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
