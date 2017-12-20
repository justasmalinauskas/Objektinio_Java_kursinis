import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Database {
    private Connection c = null;
    public void createDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Opened database successfully");
    }

    private void createTables() {
        createTasks();
    }

    private void createTasks() {
        String sql = "CREATE TABLE IF NOT EXISTS Tasks (\n"
                + "	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n"  // ID
                + "	name TEXT NOT NULL,\n" // Užduoties pavadinimas
                + "	date TEXT NOT NULL \n" // Formatas yra unix timestamp pvz: 1513629878
                + ");";
        try (Statement stmt = c.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table Tasks created successfully");
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void insertTask(String taskName, String taskDate) {
        String sql = "INSERT INTO Tasks(name, date) VALUES(?,?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setString(2, taskDate);
            pstmt.execute();
            System.out.println("Insert into Tasks was successful");
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void removeTask(int id) {
        String sql = "DELETE FROM Tasks "
                + "WHERE id = ?";

        try (PreparedStatement pstmt  = c.prepareStatement(sql)) {
            pstmt.setInt(1,  id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public ArrayList<Tasks> returnTasks(LocalDate date) {
        return getTasks(date);
    }

    public ArrayList<Tasks> returnTasks() {
        return getTasks(null);
    }

    private ArrayList<Tasks> getTasks(LocalDate date) {
        ArrayList<Tasks> tasks = new ArrayList<Tasks>();

        String sql = "SELECT id, name, date FROM Tasks "
                + "WHERE date LIKE ?";

        try (PreparedStatement pstmt  = c.prepareStatement(sql)) {


            if (date == null) {
                pstmt.setString(1,  "%" + "" + "%");
            } else {
                pstmt.setString(1,  "%" + date.toString() + "%");
            }


            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date datel = format.parse(rs.getString("date"));
                tasks.add(new Tasks(id, name, datel));
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("date"));
            }
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        } catch (ParseException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return tasks;
    }
}
