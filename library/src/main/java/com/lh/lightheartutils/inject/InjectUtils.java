package com.lh.lightheartutils.inject;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.lightheartutils.anno.InjectLayout;
import com.lh.lightheartutils.anno.InjectView;
import com.lh.lightheartutils.anno.OnClick;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liaohui
 * @date 2018/4/17
 */
public final class InjectUtils {

    //延迟加载单例
    private static class Holder {
        private static final LHInjector INJECTOR = new MyInjector();
    }

    private InjectUtils() {
    }

    public static LHInjector get() {
        return Holder.INJECTOR;
    }

    private static class MyInjector implements LHInjector {

        private static final Set<String> INNORE = new HashSet<String>() {{
            add(AppCompatActivity.class.getName());
            add(Fragment.class.getName());
            add(Object.class.getName());
        }};
        private static final String TAG = MyInjector.class.getSimpleName();

        private MyInjector() {
        }

        /* ==================================注入========================================*/

        @Override
        public void inject(Activity activity) {
            Class<? extends Activity> aClass = activity.getClass();
            int layoutId = getLayoutId(aClass);
            if (layoutId != 0) {
                try {
                    Method setContentView = aClass.getMethod("setContentView", int.class);
                    setContentView.invoke(activity, layoutId);
                    findViews(activity, aClass);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }

        @Override
        public View inject(Fragment fragment, LayoutInflater inflater, ViewGroup container) {
            Class<? extends Fragment> aClass = fragment.getClass();
            int layoutId = getLayoutId(aClass);
            if (layoutId != 0) {
                View parentView = inflater.inflate(layoutId, container, false);
                if (parentView != null) {
                    findViews(fragment, aClass, parentView);
                }
                return parentView;
            }
            return null;
        }

        @Override
        public void inject(Object holder, View view) {
            if (holder == null) {
                return;
            }
            findViews(holder, holder.getClass().getDeclaredFields(), view);
            setOnclicks(holder, holder.getClass().getDeclaredMethods(), view);
        }

        /* ==================================注入========================================*/

        private void findViews(Fragment fragment, Class clazz, View view) {
            if (clazz == null) {
                return;
            }
            if (!INNORE.contains(clazz.getName())) {
                findViews(fragment, clazz.getSuperclass(), view);
            }
            findViews(fragment, clazz.getDeclaredFields(), view);
            setOnclicks(fragment, clazz.getDeclaredMethods(), view);
        }


        private void setOnclicks(final Object holder, Method[] methods, View view) {
            for (int i = 0; i < methods.length; i++) {
                final Method method = methods[i];
                if (Modifier.isStatic(method.getModifiers()) || Modifier.isPrivate(method.getModifiers())) {
                    continue;
                }
                OnClick onClick = method.getAnnotation(OnClick.class);
                if (onClick != null) {
                    final int[] value = onClick.value();
                    for (int j = 0; j < value.length; j++) {
                        final int finalJ = j;
                        view.findViewById(value[j]).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    method.invoke(holder, value[finalJ]);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }


        private void findViews(Activity activity, Class clazz) {
            View parentView = activity.getWindow().getDecorView();
            Log.e(TAG, "parentView" + parentView.toString());
//            View parentView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            if (clazz != null) {
                if (!INNORE.contains(clazz.getName())) {
                    findViews(activity, clazz.getSuperclass());
                }
                Field[] fields = clazz.getDeclaredFields();
                Method[] methods = clazz.getDeclaredMethods();
                findViews(activity, fields, parentView);
                setOnclicks(activity,methods,parentView);
            }
        }

        /**
         * 找到子View
         */
        private void findViews(Object holder, Field[] fields, View parentView) {
            Log.e(TAG, "fields.length" + fields.length);
            for (int i = 0; i < fields.length; i++) {
                //静态和final、数组、基本数据类型的都不注入
                Field field = fields[i];
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) &&
                        !Modifier.isFinal(modifiers) &&
                        !field.getType().isArray() &&
                        !field.getType().isPrimitive()) {
                    InjectView injectView = field.getAnnotation(InjectView.class);
                    if (injectView != null) {
                        int viewId = injectView.value();
                        if (viewId == 0) {
                            continue;
                        }
                        View realView = parentView.findViewById(viewId);
                        Log.e(TAG, "parentView.findViewById" + viewId);
                        if (realView != null) {
                            field.setAccessible(true);
                            try {
                                field.set(holder, realView);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        /**
         * 获取当前类注入的LayoutId
         */
        private int getLayoutId(Class<?> aClass) {
            int layoutId = 0;
            if (aClass != null) {
                InjectLayout injectLayout = aClass.getAnnotation(InjectLayout.class);
                if (injectLayout == null) {
                    getLayoutId(aClass.getSuperclass());
                } else {
                    layoutId = injectLayout.value();
                }
            }
            return layoutId;
        }


    }
}
