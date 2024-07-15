package src.main.java.ru.gb.homework3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class JDBC {
    /**
     * С помощью JDBC, выполнить следующие пункты:
     * 1. Создать таблицу Person (скопировать код с семниара)
     * 2. Создать таблицу Department (id bigint primary key, name varchar(128) not null)
     * 3. Добавить в таблицу Person поле department_id типа bigint (внешний ключ)
     * 4. Написать метод, который загружает Имя department по Идентификатору person
     * 5. * Написать метод, который загружает Map<String, String>, в которой маппинг person.name -> department.name
     * Пример: [{"person #1", "department #1"}, {"person #2", "department #3}]
     * 6. ** Написать метод, который загружает Map<String, List<String>>, в которой маппинг department.name -> <person.name>
     * Пример:
     * [
     * {"department #1", ["person #1", "person #2"]},
     * {"department #2", ["person #3", "person #4"]}
     * ]
     * <p>
     * 7. *** Создать классы-обертки над таблицами, и в пунктах 4, 5, 6 возвращать объекты.
     */

    public static void main(String[] args) {
        // JDBC Java DataBase Connectivity - набор классов и интерфейсов (в JDK), с помощью которых
        // можно организовать взаимодействие с реляционными БД

        // java.sql.Driver - интерфейс, в котором прописано взаимодействие с БД
        // org.postgresql.Driver, oracle.Driver, org.h2.Driver, ...mysql.Driver, ...

        // DriverManager - класс, в котором зарегистрированы все драйверы

        // Connection - интерфейс, отвечающий за соединение
        // Statement - интерфейс, отвечающий за запрос в сторону БД
        // PreparedStatement - расширяет возможности Statement и позволяет избежать sql-инъекций

        // jdbc:postgres:user@password:host:port///
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            createTables(connection);
            insertData(connection);
            String age = "55";
            System.out.println("Person с возрастом 55: " + selectNamesByAge(connection, age));

            updateData(connection);
            selectData(connection);
        } catch (SQLException e) {
            System.err.println("Во время подключения произошла ошибка: " + e.getMessage());
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createPersonTable = """
                    create table person (
                      id bigint primary key,
                      name varchar(256),
                      age integer,
                      active boolean,
                      department_id bigint
                    )
                    """;

            String createDepartmentTable = """
                    create table department (
                    id bigint primary key,
                    name varchar(128) not null
                    )
                    """;

            statement.executeUpdate(createPersonTable);
            statement.executeUpdate(createDepartmentTable);

            String addForeignKeyConstraint = """
                    alter table person
                    add constraint fk_department
                    foreign key (department_id)
                    references department(id)
                    """;

            statement.executeUpdate(addForeignKeyConstraint);

        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertQuery = new StringBuilder("insert into person(id, name, age, active) values\n");
            for (int i = 1; i <= 10; i++) {
                int age = ThreadLocalRandom.current().nextInt(20, 60);
                boolean active = ThreadLocalRandom.current().nextBoolean();
                insertQuery.append(String.format("(%s, '%s', %s, %s)", i, "Person #" + i, age, active));

                if (i != 10) {
                    insertQuery.append(",\n");
                }
            }

            int insertCount = statement.executeUpdate(insertQuery.toString());
            System.out.println("Вставлено строк: " + insertCount);
        }
    }

    private static void updateData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int updateCount = statement.executeUpdate("update person set active = true where id > 5");
            System.out.println("Обновлено строк: " + updateCount);
        }
    }

    // static Optional<String> selectNameById(long id) {
    //   ...
    // }

    private static List<String> selectNamesByAge(Connection connection, String age) throws SQLException {
//    try (Statement statement = connection.createStatement()) {
//      statement.executeQuery("select name from person where age = " + age);
//      // where age = 1 or 1=1
//    }

        try (PreparedStatement statement =
                     connection.prepareStatement("select name from person where age = ?")) {
            statement.setInt(1, Integer.parseInt(age));
            ResultSet resultSet = statement.executeQuery();

            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
            return names;
        }
    }

    private static void selectData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, name, age
                    from person
                    where active is true""");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                // persons.add(new Person(id, name, age))
                System.out.println("Найдена строка: [id = " + id + ", name = " + name + ", age = " + age + "]");
            }
        }
    }

    /**
     * Пункт 4
     */
    private static String getPersonDepartmentName(Connection connection, long personId) throws SQLException {
        // FIXME: Ваш код тут
        throw new UnsupportedOperationException();
    }

    /**
     * Пункт 5
     */
    private static Map<String, String> getPersonDepartments(Connection connection) throws SQLException {
        // FIXME: Ваш код тут
        throw new UnsupportedOperationException();
    }

    /**
     * Пункт 6
     */
    private static Map<String, List<String>> getDepartmentPersons(Connection connection) throws SQLException {
        // FIXME: Ваш код тут
        throw new UnsupportedOperationException();
    }

}
