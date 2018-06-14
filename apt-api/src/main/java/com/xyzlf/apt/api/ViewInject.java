package com.xyzlf.apt.api;

public interface ViewInject<T> {

    void inject(T t, Object source);

}
