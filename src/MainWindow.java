import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class MainWindow {

    public static JFrame frame;
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
                InsertTask.main(taskDate.getDate(), null);
                displayTasks();
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
                if (obj.length > 0) {
                    deleteTask.setEnabled(true);
                } else {
                    deleteTask.setEnabled(false);
                }
            }
        });
        deleteTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DeleteTasks.main(selectedTasks);
                displayTasks();
            }
        });
        tasksList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    InsertTask.main(taskDate.getDate(), selectedTasks.get(0));
                    displayTasks();
                }
            }
        });
    }

    public void start() {
        SwingUtilities.invokeLater(new Thread(this::guiStart));
    }


    public void guiStart() {
        try {
            // Sets UI as native, used in OS.
            if (System.getProperty("os.name").toLowerCase().contains("windows"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            else if (System.getProperty("os.name").toLowerCase().contains("linux"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            else
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.err.println(e.getMessage());
        }
        frame = new JFrame("PlanuotojasLite");
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.setResizable(true);
        frame.setVisible(true);
        db = new Database();
        db.createDB();
        taskDate.setDate(LocalDate.now());
        deleteTask.setEnabled(false);
        setIcons();
    }

    private void setIcons() {
        newTask.setIcon(new ImageIcon(getClass().getResource("/img/plus.png")));
        allTasks.setIcon(new ImageIcon(getClass().getResource("/img/list.png")));
        deleteTask.setIcon(new ImageIcon(getClass().getResource("/img/cancel.png")));
    }

    public void displayTasks() {
        tasks = db.returnTasks(taskDate.getDate());
        getTasks();
        if (taskDate.getText().equals("")) {
            tasksOf.setText("All tasks");
            allTasks.setEnabled(false);
        } else {
            tasksOf.setText("Tasks of: " + taskDate.getDate().toString());
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
