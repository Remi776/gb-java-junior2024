package ru.gb.lesson2.homework;

import java.time.*;
import java.util.Date;

public class HomeworkMain {

    /**
     * В существующий класс ObjectCreator добавить поддержку аннотации RandomDate (по аналогии с Random):
     * 1. Аннотация должна обрабатываться только над полями типа java.util.Date
     * 2. Проверить, что min < max
     * 3. В поле, помеченной аннотацией, нужно вставлять рандомную дату,
     * UNIX-время которой находится в диапазоне [min, max)
     *
     * 4. *** Добавить поддержку для типов Instant, ...
     * 5. *** Добавить атрибут Zone и поддержку для типов LocalDate, LocalDateTime
     */

    /**
     * Примечание:
     * Unix-время - количество милисекунд, прошедших с 1 января 1970 года по UTC-0.
     */

    public static void main(String[] args) {
        DateClass dateClass = new ObjectCreator().createObj(DateClass.class);
        System.out.println("Date: " + dateClass.date);
        System.out.println("LocalDate: " + dateClass.localDate);
        System.out.println("Instant: " + dateClass.instant);
        System.out.println("LocalDateTime: " + dateClass.localDateTime);
    }

    static class DateClass {
        @RandomDate
        private Date date;
        @RandomDate(min = 1729538747000L, zone = "Asia/Jerusalem")
        private LocalDate localDate;
        @RandomDate(min = 1729538747000L, max = 1845689600000L, zone = "Australia/Sydney")
        private Instant instant;
        @RandomDate(zone = "Asia/Tokyo")
        private LocalDateTime localDateTime;
    }
}