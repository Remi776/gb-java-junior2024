package ru.gb.lesson1.homework;


import java.util.*;
import java.util.stream.Collectors;

public class Homework {
    private final static int MAX_PERSONS = 10;
    private final static int MAX_DEPARTMENTS = 3;

    public static void main(String[] args) {
        Random r = new Random();
        List<Person> personList = new ArrayList<>();
        List<Department> departmentList = new ArrayList<>();

        for (int i = 0; i < MAX_DEPARTMENTS; i++) {
            Department department = new Department();
            department.setName("Department #" + i);
            departmentList.add(department);
        }

        for (int i = 0; i < MAX_PERSONS; i++) {
            Person person = new Person();
            person.setName("Person #" + i);
            person.setAge(r.nextInt(18, 60));
            person.setSalary(r.nextInt(3000, 5000));
            person.setDepart(departmentList.get(r.nextInt(MAX_DEPARTMENTS)));
            personList.add(person);
        }

        personList.forEach(System.out::println);

        System.out.println(findMostYoungestPerson(personList));
        System.out.println(findMostExpensiveDepartment(personList));
        System.out.println(groupByDepartment(personList));
        System.out.println(groupByDepartmentName(personList));
        System.out.println(getDepartmentOldestPerson(personList));
        System.out.println(minSalaryInDepartment(personList));
    }


    private static class Department {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Department{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class Person {
        private String name;
        private int age;
        private double salary;
        private Department depart;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public Department getDepart() {
            return depart;
        }

        public void setDepart(Department depart) {
            this.depart = depart;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", depart=" + depart +
                    '\n' + '}';
        }
    }

    /**
     * Найти самого молодого сотрудника
     */
    static Optional<Person> findMostYoungestPerson(List<Person> people) {
        return people.stream()
                .filter(Objects::nonNull)
                .min(Comparator.comparing(Person::getAge));
    }

    /**
     * Найти департамент, в котором работает сотрудник с самой большой зарплатой
     */
    static Optional<Department> findMostExpensiveDepartment(List<Person> people) {
        return people.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(Person::getSalary))
                .map(Person::getDepart);
    }

    /**
     * Сгруппировать сотрудников по департаментам
     */
    static Map<Department, List<Person>> groupByDepartment(List<Person> people) {
        return people.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Person::getDepart));
    }

    /**
     * Сгруппировать сотрудников по названиям департаментов
     */
    static Map<String, List<Person>> groupByDepartmentName(List<Person> people) {
        return people.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(p -> p.getDepart().getName()));
    }

    /**
     * В каждом департаменте найти самого старшего сотрудника
     */
    static Map<String, Person> getDepartmentOldestPerson(List<Person> people) {
        return people.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        p -> p.getDepart().getName(),
                        p -> p,
                        (p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2
                ));
    }

    /**
     * *Найти сотрудников с минимальными зарплатами в своем отделе
     * (прим. можно реализовать в два запроса)
     */
    static List<Person> minSalaryInDepartment(List<Person> people) {
        return people.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Person::getDepart,
                        Collectors.minBy(Comparator.comparing(Person::getSalary))))
                .values().stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

    }
}
