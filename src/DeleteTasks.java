import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DeleteTasks extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList deleteList;
    private ArrayList<Tasks> deleteTasks;

    public DeleteTasks(ArrayList<Tasks> args) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDeleteList(args);
        deleteTasks = args;
        setLocationRelativeTo(MainWindow.frame);

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

    private void setDeleteList(ArrayList<Tasks> args) {
        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < args.size(); i++) {
            args.get(i).getValues();
            model.addElement(args.get(i).getStringValues());
        }

        deleteList.setModel(model);
    }

    private void onOK() {
        Database db = new Database();
        db.createDB();
        for (int i = 0; i < deleteTasks.size(); i++) {
            System.out.println(deleteTasks.get(i).getID());
            db.removeTask(deleteTasks.get(i).getID());
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
