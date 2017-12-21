package com.justas.planuotojaslite;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainWindow {

    public static JFrame frame;
    private ArrayList<Tasks> selectedTasks = new ArrayList<>();
    private JPanel mainPanel;
    private JButton newTask;
    private DatePicker taskDate;
    private JList<String> tasksList;
    private JLabel tasksOf;
    private JButton allTasks;
    private JButton deleteTask;
    private Database db;
    private ArrayList<Tasks> tasks;

    public MainWindow() {
        newTask.addActionListener(actionEvent -> {
            InsertTask.main(taskDate.getDate(), null);
            displayTasks();
        });

        taskDate.addDateChangeListener(dateChangeEvent -> displayTasks());
        allTasks.addActionListener(actionEvent -> taskDate.setDate(null));
        tasksList.addListSelectionListener(listSelectionEvent -> {
            selectedTasks.clear();
            int[] obj = tasksList.getSelectedIndices();
            for (int anObj : obj) {
                selectedTasks.add(tasks.get(anObj));
            }
            System.out.println("SELECTION BEGIN");
            for (Tasks selectedTask : selectedTasks) {
                selectedTask.getValues();
            }
            System.out.println("SELECTION OVER");
            if (obj.length > 0) {
                deleteTask.setEnabled(true);
            } else {
                deleteTask.setEnabled(false);
            }
        });
        deleteTask.addActionListener(actionEvent -> {
            DeleteTasks.main(selectedTasks);
            displayTasks();
        });
        tasksList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    InsertTask.main(taskDate.getDate(), selectedTasks.get(0));
                    displayTasks();
                }
            }
        });
    }

    public void start() {
        SwingUtilities.invokeLater(new Thread(this::guiStart));
    }


    private void guiStart() {
        try {
            // Sets UI as native, used in OS.
            if (System.getProperty("os.name").toLowerCase().contains("windows"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            else if (System.getProperty("os.name").toLowerCase().contains("linux"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            else UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.err.println(e.getMessage());
        }
        frame = new JFrame("PlanuotojasLite");
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.setResizable(true);
        frame.setVisible(true);
        db = Database.getInstance();
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

    private void displayTasks() {
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
        final DefaultListModel<String> model = new DefaultListModel<>();
        for (Tasks task : tasks) {
            task.getValues();
            model.addElement(task.getStringValues());
        }

        tasksList.setModel(model);
    }

    private void createUIComponents() {

        // TODO: place custom component creation code here
        DatePickerSettings settings = new DatePickerSettings();
        settings.setVisibleDateTextField(false);
        settings.setGapBeforeButtonPixels(0);
        taskDate = new DatePicker(settings);
        JButton datePickerButton = taskDate.getComponentToggleCalendarButton();
        datePickerButton.setText("");
        datePickerButton.setIcon(new ImageIcon(getClass().getResource("/img/notepad.png")));
    }
}
