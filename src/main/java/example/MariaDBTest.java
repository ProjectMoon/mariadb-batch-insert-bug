package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MariaDBTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String driverClass = args[0].equals("mysql") ? "com.mysql.jdbc.Driver" : "org.mariadb.jdbc.Driver";
        System.out.println("Using driver: " + driverClass);
        int iterations = Integer.parseInt(args[1]);

        //org.mariadb.jdbc.Driver or com.mysql.jdbc.Driver
        Class.forName(driverClass);
        String url = "jdbc:mysql://localhost/?serverTimezone=UTC&allowMultiQueries=true";
        Connection conn = DriverManager.getConnection(url, "root", "");

        String query1 = "create database example;";
        String query2 = "create table example.usage (id bigint not null auto_increment, event_uuid varchar(36), primary key (id));";

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query1);
        stmt.executeUpdate(query2);
        stmt.close();

        String query = "insert into example.usage (`event_uuid`) values (?);";

        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int c = 0; c < iterations; c++) {
                ps.setString(1, "abc");
                ps.addBatch();
            }

            ps.executeBatch();
            ResultSet rs = ps.getGeneratedKeys();
            List<Long> ids = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong(1);
                ids.add(id);
            }

            for (long id : ids) System.out.println("ID returned: " + id);

            ps.clearBatch();
            ps.close();
        }
        finally {
            if (conn != null) {
                if (!conn.isClosed()) {
                    stmt = conn.createStatement();
                    stmt.executeUpdate("drop database example;");
                }
                conn.close();
            }
        }
    }
}
