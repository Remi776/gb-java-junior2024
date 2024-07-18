package ru.gb.lesson4.homework4;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.gb.lesson4.homework4.entity.Post;
import ru.gb.lesson4.homework4.entity.PostComment;


public class Main {
    /**
     * Используя hibernate, создать таблицы:
     * 1. Post (публикация) (id, title)
     * 2. PostComment (комментарий к публикации) (id, text, post_id)
     *
     * Написать стандартные CRUD-методы: создание, загрузка, удаление.
     *
     * Доп. задания:
     * 1. * В сущностях post и postComment добавить поля timestamp с датами.
     * 2. * Создать таблицу users(id, name) и в сущностях post и postComment добавить ссылку на юзера.
     * 3. * Реализовать методы:
     * 3.1 Загрузить все комментарии публикации
     * 3.2 Загрузить все публикации по идентификатору юзера
     * 3.3 ** Загрузить все комментарии по идентификатору юзера
     * 3.4 **** По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты.
     * // userId -> List<User>
     *
     *
     * Замечание:
     * 1. Можно использовать ЛЮБУЮ базу данных (например, h2)
     * 2. Если запутаетесь, приходите в группу в телеграме или пишите мне @inchestnov в личку.
     */

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        try(SessionFactory sessionFactory = configuration.buildSessionFactory()){
//            sessionCreate(sessionFactory);
//            sessionUpdate(sessionFactory);
//            sessionDelete(sessionFactory);
            withSession(sessionFactory);
        }
    }

private static void withSession(SessionFactory sessionFactory){
        try (Session session = sessionFactory.openSession()){
            PostComment postComment = session.find(PostComment.class, 1L);
            System.out.println(postComment);

        }
}

    private static void sessionCreate(SessionFactory sessionFactory) {

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Post post = new Post();
            post.setId(17L);
            post.setTitle("Post #17");
            session.persist(post);
            tx.commit();
        }
    }

private static void sessionUpdate(SessionFactory sessionFactory) {
    try (Session session = sessionFactory.openSession()) {
        Post toUpdate = session.find(Post.class, 17L);
        toUpdate.setTitle("TITLE UPDATED");

        Transaction tx = session.beginTransaction();
        session.merge(toUpdate);  // update
        tx.commit();
    }
}

private static void sessionDelete(SessionFactory sessionFactory){
        try (Session session = sessionFactory.openSession()){
            PostComment toDelete = session.find(PostComment.class, 1L);

            Transaction tx = session.beginTransaction();
            session.remove(toDelete); // delete
            tx.commit();
        }
    }
}
