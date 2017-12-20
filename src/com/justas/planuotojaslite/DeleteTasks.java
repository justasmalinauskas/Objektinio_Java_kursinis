package com.justas.planuotojaslite;

import com.justas.planuotojasbase.Database;
import com.justas.planuotojasbase.Tasks;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class DeleteTasks extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<String> deleteList;
    private final ArrayList<Tasks> deleteTasks;

    private DeleteTasks(ArrayList<Tasks> args) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDeleteList(args);
        deleteTasks = args;
        setLocationRelativeTo(MainWindow.frame);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setDeleteList(ArrayList<Tasks> args) {
        final DefaultListModel<String> model = new DefaultListModel<>();
        for (Tasks arg : args) {
            arg.getValues();
            model.addElement(arg.getStringValues());
        }

        deleteList.setModel(model);
    }

    private void onOK() {
        Database db = Database.getInstance();
        for (Tasks deleteTask : deleteTasks) {
            System.out.println(deleteTask.getID());
            db.removeTask(deleteTask.getID());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(ArrayList<Tasks> args) {
        DeleteTasks dialog = new DeleteTasks(args);
        dialog.pack();
        dialog.setVisible(true);
    }
}
