package ru.gb.lesson2.homework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectCreator {
    public <T> T createObj(Class<T> clazz) {
        try {
            Constructor<T> tConstructor = clazz.getDeclaredConstructor();
            T obj = tConstructor.newInstance();
            AnnotationProcessor.validateAnnotation(obj);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
