package io.github.seyud.weave;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import io.github.seyud.weave.dummy.DummyProvider;
import io.github.seyud.weave.dummy.DummyReceiver;
import io.github.seyud.weave.dummy.DummyService;

@SuppressLint("NewApi")
public class DelegateComponentFactory extends AppComponentFactory {

    AppComponentFactory receiver;

    public DelegateComponentFactory() {
        DynLoad.componentFactory = this;
    }

    @Override
    public ClassLoader instantiateClassLoader(ClassLoader cl, ApplicationInfo info) {
        return new DelegateClassLoader();
    }

    @Override
    public Application instantiateApplication(ClassLoader cl, String className) {
        return new StubApplication();
    }

    @Override
    public Activity instantiateActivity(ClassLoader cl, String className, Intent intent)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (receiver != null)
            return receiver.instantiateActivity(DynLoad.activeClassLoader, className, intent);
        return create(className, DownloadActivity.class);
    }

    @Override
    public BroadcastReceiver instantiateReceiver(ClassLoader cl, String className, Intent intent)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (receiver != null)
            return receiver.instantiateReceiver(DynLoad.activeClassLoader, className, intent);
        return create(className, DummyReceiver.class);
    }

    @Override
    public Service instantiateService(ClassLoader cl, String className, Intent intent)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (receiver != null)
            return receiver.instantiateService(DynLoad.activeClassLoader, className, intent);
        return create(className, DummyService.class);
    }

    @Override
    public ContentProvider instantiateProvider(ClassLoader cl, String className)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (receiver != null)
            return receiver.instantiateProvider(DynLoad.activeClassLoader, className);
        return create(className, DummyProvider.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T create(String name, Class<T> fallback)
            throws IllegalAccessException, InstantiationException {
        try {
            return (T) DynLoad.activeClassLoader.loadClass(name)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException e) {
            try {
                return fallback.getDeclaredConstructor().newInstance();
            } catch (java.lang.reflect.InvocationTargetException
                     | NoSuchMethodException ex) {
                InstantiationException ie = new InstantiationException();
                ie.initCause(ex);
                throw ie;
            }
        } catch (java.lang.reflect.InvocationTargetException
                 | NoSuchMethodException ex) {
            InstantiationException ie = new InstantiationException();
            ie.initCause(ex);
            throw ie;
        }
    }

}
