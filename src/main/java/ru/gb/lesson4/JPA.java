package ru.gb.lesson4;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class JPA {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().buildSessionFactory();
    }
}
