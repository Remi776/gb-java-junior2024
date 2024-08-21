package ru.gb.lesson2.homework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface RandomDate {
    long min() default 1704067200000L; // 1 january 2024 UTC0
    long max() default 1735689600000L; // 1 january 2025 UTC0
    String zone() default "Europe/Moscow";
}

