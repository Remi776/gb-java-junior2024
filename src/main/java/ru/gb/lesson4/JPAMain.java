package ru.gb.lesson4;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.gb.lesson4.entity.Author;
import ru.gb.lesson4.entity.Book;

public class JPAMain {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.configure(); // !!! Иначе не прочитается hibernate.cfg.xml
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            // sessionFactory <-> connection
            withSessionCRUD(sessionFactory);
        }
    }

    private static void withSessionCRUD(SessionFactory sessionFactory) {

        try (Session session = sessionFactory.openSession()) {
            // session <-> statement
            Author author = session.find(Author.class, 1L);
            System.out.println(author);
        }

        try (Session session = sessionFactory.openSession()) {
            // session <-> statement
            Book book = session.find(Book.class, 1L);
            System.out.println(book);
        }
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


//
//        try (Session session = sessionFactory.openSession()){
//            Author toUpdate = session.find(Author.class, 22L);
//            toUpdate.setName("UPDATED");
//
//            Transaction tx = session.beginTransaction();
//            session.merge(toUpdate);  // update
//            tx.commit();
//        }
//
//
//        try (Session session = sessionFactory.openSession()){
//            Author toDelete = session.find(Author.class, 1L);
//
//            Transaction tx = session.beginTransaction();
//            session.remove(toDelete); // delete
//            tx.commit();
//        }
    }
}
