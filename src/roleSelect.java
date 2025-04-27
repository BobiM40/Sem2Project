import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class roleSelect extends JFrame {
    private JComboBox roles;
    private JPanel rolePanel;
    private JButton proceedButton;

    public roleSelect(){
        setSize(500, 500);
        setContentPane(rolePanel);
        setVisible(true);

        proceedButton.setEnabled(false);

        roles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proceedButton.setEnabled(true);
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(roles.getSelectedIndex()){
                    case 0:
                        new studentLogin();
                        dispose();
                        break;

                    case 1:
                        new teacherLogin();
                        dispose();
                        break;

                    case 2:
                        new adminLogin();
                        dispose();
                        break;

                    default:
                        proceedButton.setEnabled(false);
                        break;
                }
            }
        });
    }
}
