package com.warm.finder_apt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作者：warm
 * 时间：2018-08-21 13:46
 * 描述：
 */
public class ViewFinder {

    public static void find(Object activity) {
        try {
            Class<?> viewFinder = activity.getClass().getClassLoader().loadClass(activity.getClass().getName() + "_ViewFinder");
            Method method = viewFinder.getMethod("find", activity.getClass());
            method.invoke(viewFinder.newInstance(), activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
