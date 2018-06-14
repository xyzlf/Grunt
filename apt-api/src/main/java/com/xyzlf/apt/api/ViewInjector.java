package com.xyzlf.apt.api;

import android.app.Activity;
import android.view.View;

public class ViewInjector {

    private static final String SUFFIX = "$$ViewInject";

    public static void injectView(Activity activity) {
        ViewInject proxyActivity = findProxyActivity(activity);
        proxyActivity.inject(activity, activity);
    }

    public static void injectView(Object o, View view) {
        ViewInject proxyActivity = findProxyActivity(o);
        proxyActivity.inject(o, view);
    }

    private static ViewInject findProxyActivity (Object o) {

        try {
            Class clazz = o.getClass();
            Class injectClazz = Class.forName(clazz.getName() + SUFFIX);
            return (ViewInject) injectClazz.newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        throw new RuntimeException(String.format("can not find %s , maybe has something wrong when compiler.", o.getClass().getSimpleName() + SUFFIX));
    }

}
