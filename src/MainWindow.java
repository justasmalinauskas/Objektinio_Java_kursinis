import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class MainWindow {

    private JFrame frame;
    private JPanel mainPanel;
    private JButton newTask;
    private DatePicker taskDate;
    private JList tasksList;
    private JLabel tasksOf;
    private JButton allTasks;
    private JButton deleteTask;
    private Database db;
    private ArrayList<Tasks> tasks;
    private ArrayList<Tasks> selectedTasks = new ArrayList<>();

    public MainWindow() {
        newTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                InsertTask.main(taskDate.getDate());
            }
        });

        taskDate.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent dateChangeEvent) {
                displayTasks();
            }
        });
        allTasks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                taskDate.setDate(null);
            }
        });
        tasksList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                selectedTasks.clear();
                int[] obj = tasksList.getSelectedIndices();
                for (int i = 0; i < obj.length; i++) {
                    selectedTasks.add(tasks.get(obj[i]));
                }
                System.out.println("SELECTION BEGIN");
                for (int i = 0; i < selectedTasks.size(); i++) {
                    selectedTasks.get(i).getValues();
                }
                System.out.println("SELECTION OVER");
            }
        });
        deleteTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DeleteTasks.main(selectedTasks);
                displayTasks();
            }
        });
    }

    public void start() {
        SwingUtilities.invokeLater(new Thread(this::guiStart));
    }


    public void guiStart() {
        frame = new JFrame("PlanuotojasLite");
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.setResizable(true);
        frame.setVisible(true);
        //////////
        db = new Database();
        db.createDB();
        taskDate.setDate(LocalDate.now());

    }

    public void displayTasks() {
        tasks = db.returnTasks(taskDate.getDate());
        getTasks();
        if (taskDate.getText().equals("")) {
            tasksOf.setText("All tasks");
            allTasks.setEnabled(false);
        } else {
            tasksOf.setText("Tasks of: " + taskDate.getText());
            allTasks.setEnabled(true);
        }
    }



    private void getTasks() {
        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).getValues();
            model.addElement(tasks.get(i).getStringValues());
        }

        tasksList.setModel(model);
    }
}
