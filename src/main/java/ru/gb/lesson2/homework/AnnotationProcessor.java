package ru.gb.lesson2.homework;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AnnotationProcessor {
    public static void validateAnnotation(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(RandomDate.class) && isValidDateType(field.getType())) {
                var annotation = field.getAnnotation(RandomDate.class);
                long min = annotation.min();
                long max = annotation.max();
                if (min >= max) {
                    throw new IllegalArgumentException("min value must be less than max.");
                }
                long randomTime = ThreadLocalRandom.current().nextLong(min, max);
                ZoneId zoneId = ZoneId.of(annotation.zone());
                try {
                    setFieldValue(field, obj, field.getType(), randomTime, zoneId);
                } catch (IllegalAccessException e) {
                    System.err.println("Unexpected value: " + e.getMessage());
                }
            }
        }
    }

    private static boolean isValidDateType(Class<?> fieldType) {
        return fieldType.equals(Date.class) || fieldType.equals(Instant.class) ||
                fieldType.equals(LocalDate.class) || fieldType.equals(LocalDateTime.class);
    }

    private static void setFieldValue(Field field, Object obj, Class<?> fieldType, long randomTime, ZoneId zoneId) throws IllegalAccessException {
        field.setAccessible(true);
        switch (fieldType.getSimpleName()) {
            case "Instant" -> field.set(obj, Instant.ofEpochMilli(randomTime));
            case "LocalDateTime" -> field.set(obj, Instant.ofEpochMilli(randomTime).atZone(zoneId).toLocalDateTime());
            case "LocalDate" -> field.set(obj, Instant.ofEpochMilli(randomTime).atZone(zoneId).toLocalDate());
            case "Date" -> field.set(obj, new Date(randomTime));
        }
    }
}
