package ru.gb.lesson4;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.gb.lesson4.entity.Author;

public class JPA {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.configure(); // !!! Иначе не прочитается hibernate.cfg.xml
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            // sessionFactory <-> connection
            withSession(sessionFactory);
        }
    }

    private static void withSession(SessionFactory sessionFactory) {

//        try (Session session = sessionFactory.openSession()) {
//            // session <-> statement
//            Author author = session.find(Author.class, 1L);
//            System.out.println(author);
//        }
//
//        try (Session session = sessionFactory.openSession()){
//            Transaction tx = session.beginTransaction();
//            Author author = new Author();
//            author.setId(22L);
//            author.setName("Author #22");
//
//            session.persist(author); // insert
//            tx.commit();
//        }

        try (Session session = sessionFactory.openSession()){
            Author toUpdate = session.find(Author.class, 22L);
            toUpdate.setName("UPDATED");

            Transaction tx = session.beginTransaction();
            session.merge(toUpdate);
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()){
            Author toDelete = session.find(Author.class, 1L);

            Transaction tx = session.beginTransaction();
            session.remove(toDelete);
            tx.commit();
        }
    }
}
