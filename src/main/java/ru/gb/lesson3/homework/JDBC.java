package ru.gb.lesson3.homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class JDBC {

    private static StringBuilder insertQueryPerson;

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
            createTable(connection);
            insertData(connection);
//            String age = "55";
//            System.out.println("Person с возрастом 55: " + selectNamesByAge(connection, age));
            selectData(connection);
            System.out.println("=".repeat(10));

            long id = ThreadLocalRandom.current().nextInt(1, 11);
            System.out.printf("Department name(personId = %s): '%s'%n", id, getDepartmentNameByPersonId(connection, id));
            System.out.println("=".repeat(10));

            getPersonDepartmentMap(connection)
                    .forEach((person, department) -> System.out.println(person + " -> " + department));

            System.out.println("=".repeat(10));

            getDepartmentPersonsMap(connection)
                    .forEach((person, department) -> System.out.println(person + " -> " + department));

        } catch (SQLException e) {
            System.err.println("Во время подключения произошла ошибка: " + e.getMessage());
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            String createDepartmentTable = """
                    create table department (
                    id bigint primary key,
                    name varchar(128) not null
                    )
                    """;

            String createPersonTable = """
                    create table person (
                      id bigint primary key,
                      name varchar(256),
                      age integer,
                      active boolean,
                      department_id bigint,
                      foreign key (department_id) references department(id)
                    )
                    """;

            statement.executeUpdate(createDepartmentTable);
            statement.executeUpdate(createPersonTable);

        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            StringBuilder insertQueryDepartment = new StringBuilder("insert into department(id, name) values\n");
            for (int i = 1; i <= 5; i++) {
                insertQueryDepartment.append(String.format("(%s, '%s')", i, "Department #" + i));

                if (i != 5) {
                    insertQueryDepartment.append(",\n");
                }
            }
            statement.executeUpdate(insertQueryDepartment.toString());

            StringBuilder insertQueryPerson = new StringBuilder("insert into person(id, name, age, active, department_id) values\n");
            for (int i = 1; i <= 10; i++) {
                int age = ThreadLocalRandom.current().nextInt(20, 60);
                boolean active = ThreadLocalRandom.current().nextBoolean();
                int department_id = ThreadLocalRandom.current().nextInt(1, 6);
                insertQueryPerson.append(String.format("(%s, '%s', %s, %s, %s)", i, "Person #" + i, age, active, department_id));

                if (i != 10) {
                    insertQueryPerson.append(",\n");
                }
            }
            statement.executeUpdate(insertQueryPerson.toString());
        }
    }

//    private static void updateData(Connection connection) throws SQLException {
//        try (Statement statement = connection.createStatement()) {
//            int updateCount = statement.executeUpdate("update person set active = true where id > 5");
//            System.out.println("Обновлено строк: " + updateCount);
//        }
//    }

    // static Optional<String> selectNameById(long id) {
    //   ...
    // }

    private static void selectData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, name, age, department_id
                    from person
                    """);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                int department_id = resultSet.getInt("department_id");
                // persons.add(new Person(id, name, age))
                System.out.println("Найдена строка: [id = " + id + ", name = " + name + ", age = " + age + ", department_id = " + department_id + "]");
            }

            System.out.println("=".repeat(10));

            ResultSet resultSetDepartment = statement.executeQuery("""
                    select id, name
                    from department
                    """);

            while (resultSetDepartment.next()) {
                long id = resultSetDepartment.getLong("id");
                String name = resultSetDepartment.getString("name");
                // persons.add(new Person(id, name, age))
                System.out.println("Найдена строка: [id = " + id + ", name = " + name + "]");
            }
        }
    }

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

    /**
     * Пункт 4
     */
    private static String getDepartmentNameByPersonId(Connection connection, long personId) throws SQLException {
        String query = """
                select d.name from department d
                join person p on d.id = p.department_id
                where p.id = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, personId);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next() ? resultSet.getString("name") : "Department not found";
        }
    }

    /**
     * Пункт 5
     */
    private static Map<String, String> getPersonDepartmentMap(Connection connection) throws SQLException {

        String query = """
                select p.name as p_name, d.name as d_name from department d
                join person p on d.id = p.department_id
                """;
        Map<String, String> map = new HashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                map.put(resultSet.getString("p_name"), resultSet.getString("d_name"));
            }
            return map;
        }
    }

    /**
     * Пункт 6
     */
    private static Map<String, List<String>> getDepartmentPersonsMap(Connection connection) throws SQLException {
//        * [
//     * {"department #1", ["person #1", "person #2"]},
//     * {"department #2", ["person #3", "person #4"]}
//     * ]
        String query = """
                select d.name as d_name, p.name as p_name from department d
                join person p on d.id = p.department_id
                """;
        Map<String, List<String>> map = new HashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String departmentName = resultSet.getString("d_name");
                String personName = resultSet.getString("p_name");
                map.computeIfAbsent(departmentName, k -> new ArrayList<>()).add(personName);
            }
            return map;
        }
    }

}
