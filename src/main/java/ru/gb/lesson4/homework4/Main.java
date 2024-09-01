package ru.gb.lesson4.homework4;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.gb.lesson4.homework4.entity.Post;
import ru.gb.lesson4.homework4.entity.PostComment;
import ru.gb.lesson4.homework4.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {
    /**
     * Используя hibernate, создать таблицы:
     * 1. Post (публикация) (id, title)
     * 2. PostComment (комментарий к публикации) (id, text, post_id)
     * <p>
     * Написать стандартные CRUD-методы: создание, загрузка, удаление.
     * <p>
     * Доп. задания:
     * 1. * В сущностях post и postComment добавить поля timestamp с датами.
     * 2. * Создать таблицу users(id, name) и в сущностях post и postComment добавить ссылку на юзера.
     * 3. * Реализовать методы:
     * 3.1 Загрузить все комментарии публикации
     * 3.2 Загрузить все публикации по идентификатору юзера
     * 3.3 ** Загрузить все комментарии по идентификатору юзера
     * 3.4 **** По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты.
     * // userId -> List<User>
     * <p>
     * <p>
     * Замечание:
     * 1. Можно использовать ЛЮБУЮ базу данных (например, h2)
     * 2. Если запутаетесь, приходите в группу в телеграме или пишите мне @inchestnov в личку.
     */

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
//            sessionCreateTables(sessionFactory);

            getAllCommentsOfPost(sessionFactory);
            System.out.println();
            getAllUserPosts(sessionFactory);
            System.out.println();
            getAllPostCommentsByUserId(sessionFactory);
            System.out.println();
            getAllUsersFromTheOwnerOfPost(sessionFactory);

        }
    }

    private static void getAllUsersFromTheOwnerOfPost(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, 1L);
            System.out.println(user);

            user.getPostComments().stream()
                    .map(PostComment::getPost)
                    .collect(Collectors.groupingBy(Post::getUser))
                    .forEach((userKey, posts) -> System.out.println(userKey + ": " + posts));
        }
    }

    private static void getAllPostCommentsByUserId(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, 1L);
            System.out.println(user);
            user.getPostComments()
                    .forEach(System.out::println);
        }
    }

    private static void getAllCommentsOfPost(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.find(Post.class, 3L);
            System.out.println(post);

            post.getPostComments()
                    .forEach(System.out::println);
        }
    }

    private static void getAllUserPosts(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, 2L);
            System.out.println(user);

            user.getPosts()
                    .forEach(System.out::println);
        }
    }


    private static void sessionCreateTables(SessionFactory sessionFactory) {

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            User user1 = new User();
            user1.setId(1L);
            user1.setName("John");

            User user2 = new User();
            user2.setId(2L);
            user2.setName("Bob");

            User user3 = new User();
            user3.setId(3L);
            user3.setName("Ron");

            Post post1 = new Post();
            post1.setId(1L);
            post1.setTitle("Post #1");
            post1.setUser(user1);

            Post post2 = new Post();
            post2.setId(2L);
            post2.setTitle("Post #2");
            post2.setUser(user2);

            Post post3 = new Post();
            post3.setId(3L);
            post3.setTitle("Post #3");
            post3.setUser(user3);

            Post post4 = new Post();
            post4.setId(4L);
            post4.setTitle("Post #4");
            post4.setUser(user2);

            PostComment postComment1 = new PostComment();
            postComment1.setId(1L);
            postComment1.setText("awesome");
            postComment1.setUser(user1);
            postComment1.setPost(post1);

            PostComment postComment2 = new PostComment();
            postComment2.setId(2L);
            postComment2.setText("cool");
            postComment2.setUser(user2);
            postComment2.setPost(post2);

            PostComment postComment3 = new PostComment();
            postComment3.setId(3L);
            postComment3.setText("not bad");
            postComment3.setUser(user3);
            postComment3.setPost(post3);

            PostComment postComment4 = new PostComment();
            postComment4.setId(4L);
            postComment4.setText("could be better...");
            postComment4.setUser(user1);
            postComment4.setPost(post3);

            PostComment postComment5 = new PostComment();
            postComment5.setId(5L);
            postComment5.setText("good");
            postComment5.setUser(user1);
            postComment5.setPost(post3);

            session.persist(user1);
            session.persist(user2);
            session.persist(user3);

            session.persist(post1);
            session.persist(post2);
            session.persist(post3);
            session.persist(post4);

            session.persist(postComment1);
            session.persist(postComment2);
            session.persist(postComment3);
            session.persist(postComment4);
            session.persist(postComment5);

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

    private static void sessionDelete(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            PostComment toDelete = session.find(PostComment.class, 1L);

            Transaction tx = session.beginTransaction();
            session.remove(toDelete); // delete
            tx.commit();
        }
    }
}
