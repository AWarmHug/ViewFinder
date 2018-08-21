package com.warm.finder;

import android.app.Activity;
import android.support.v4.util.ArrayMap;
import android.view.View;

import com.warm.finder.annotations.Id;
import com.warm.finder.annotations.OnClick;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 作者：warm
 * 时间：2018-08-20 18:18
 * 描述：
 */
public class ViewFinder {

    private static Map<Integer, Object> sOnClickListenerMap = new ArrayMap<>();
    private static Map<String, Method> sMethodMap = new ArrayMap<>();

    public static void inject(final Activity activity) {

        Class activityClass = activity.getClass();

        Field[] fields = activityClass.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isAbstract(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || field.getType().isArray() || field.getType().isEnum() || field.getType().isPrimitive()) {
                continue;
            }
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                int value = id.value();
                View view = activity.findViewById(value);

                field.setAccessible(true);
                try {
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        Method[] methods = activityClass.getDeclaredMethods();

        for (final Method methodItem : methods) {
            if (filterMethod(methodItem)) {
                continue;
            }
            OnClick onClick = methodItem.getAnnotation(OnClick.class);

            if (onClick != null) {
                int[] ids = onClick.values();
                for (int i = 0; i < ids.length; i++) {
                    methodItem.setAccessible(true);
                    final View view = activity.findViewById(ids[i]);
                   Object onClickListener = sOnClickListenerMap.get(view.getId());
                    if (onClickListener == null) {
                        onClickListener = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader()
                                , new Class<?>[]{View.OnClickListener.class}
                                , new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                        return methodItem.invoke(activity, args);
                                    }
                                });
                        sOnClickListenerMap.put(view.getId(), onClickListener);
                    }

                    try {
                        Method setOnClickListener = sMethodMap.get("setOnClickListener");
                        if (setOnClickListener == null) {
                            setOnClickListener = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                            sMethodMap.put("setOnClickListener", setOnClickListener);
                        }
                        setOnClickListener.invoke(view, onClickListener);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    static boolean filterMethod(Method method) {
        return Modifier.isAbstract(method.getModifiers())/*||Modifier.isPrivate(method.getModifiers())*/;
    }


}
