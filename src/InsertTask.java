import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;

public class InsertTask extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField taskname;
    private DatePicker taskdate;

    public InsertTask(LocalDate args) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        taskdate.setDate(args);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (taskdate.getDate() != null && taskname.getText() != null) {
            Database insert = new Database();
            insert.createDB();
            insert.insertTask(taskname.getText(), taskdate.toString());
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "You must insert task name and/or date");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(LocalDate args) {
        InsertTask dialog = new InsertTask(args);
        dialog.pack();
        dialog.setVisible(true);
    }
}
