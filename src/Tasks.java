import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tasks {

    private int id;
    private String task;
    private Date date;

    public Tasks(int id, String task, Date date) {
        this.id = id;
        this.task = task;
        this.date = date;
    }

    public void getValues() {
        System.out.println(id + " " + task + " " + date);
    }

    public String getStringValues() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return task + " " + df.format(date);
    }

    public Integer getID() {
        return id;
    }
}
