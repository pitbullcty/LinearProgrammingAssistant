import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class UI {

    private static JFrame frame;
    private JPanel panel1;
    private JTextField variates_num;
    private JComboBox type;
    private JButton confirm;
    private JButton calculate;
    private JButton addConstraint;
    private JScrollPane scrollpane;
    private JPanel constraint;
    private JTextField target;
    private HashMap<String, Component> constraintHashMap;
   // private JTextField target;


    private int count = 1;

    public UI() {
        constraintHashMap = new HashMap<>();
        start();
    }

    public static void mainUI() {
        frame = new JFrame("线性规划助手");
        frame.setContentPane(new UI().panel1);
        frame.setSize(600, 600); //设置大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public void start() {
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = variates_num.getText().replace(" ", "");
                int num;
                if (temp != "") num = Integer.parseInt(temp);
                else num = 0;
                String tp = (String) type.getSelectedItem();
                if (num >= 2 && num < 9) startNewCalculation(tp, new InputContoller(num));
                if (num >= 9) JOptionPane.showMessageDialog(panel1, "变量过多", "警告", JOptionPane.WARNING_MESSAGE);
                if (num < 2) JOptionPane.showMessageDialog(panel1, "变量过少", "警告", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void startNewCalculation(String type, InputContoller input) {
        String target_index;
        if (type == "最大值") target_index = "Max z = ";
        else target_index = "Min z = ";
        enterTarget(target_index, input);
        setConstraintUI(input);
        enterConstraint(input);
        calculate(input);
    }

    public Model getModel(InputContoller input) {
        Target target = getTarget(input);
        return null;
    }

    public Target getTarget(InputContoller input) {
        Target target = input.getTarget(this.target.getText());
        return target;
    }

    public Constraint[] getConstraint(InputContoller input) {
        return null;
    }

    public void createComponentMap(JPanel panel) {
        Component[] components = panel.getComponents();
        for (int i = 0; i < components.length; i++) {
            constraintHashMap.put(components[i].getName(), components[i]);
        }
    }

    public Component getComponentByName(String name) {
        if (constraintHashMap.containsKey(name)) {
            return constraintHashMap.get(name);
        }
        else return null;
    }

    public void calculate(InputContoller input) {

        calculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createComponentMap(constraint);
                Model model = getModel(input);
            }
        });
    }

    public void enterTarget(String target_index, InputContoller input) {

        for (int i = 0; i < input.getNum(); i++) {
            target_index += "? " + input.getVariates(i);
            if (i != input.getNum() - 1) target_index += " + ";
        }
       target.setText(target_index);

    }

    public void setConstraintUI(InputContoller input) {
        frame.setContentPane(panel1);
        String cons = "";
        constraint.setLayout(new GridLayout(10, 1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        for (int i = 0; i < input.getNum(); i++) {
            cons += "? " + input.getVariates(i);
            if (i != input.getNum() - 1) cons += " + ";
        }
        cons += "<= ?";
        JTextField constraint_text = new JTextField(cons);
        constraint_text.setName("cons0");
        constraint.add(constraint_text, gbc);
        frame.setContentPane(panel1);
    }

    public void enterConstraint(InputContoller input) {
        addConstraint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(panel1);
                String cons = "";
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = count;
                for (int i = 0; i < input.getNum(); i++) {
                    cons += "? " + input.getVariates(i);
                    if (i != input.getNum() - 1) cons += " + ";
                }
                cons += "<= ?";
                count++;
                JTextField constraint_text = new JTextField(cons);
                constraint_text.setName("cons"+count);
                constraint.add(constraint_text, gbc);
                frame.setContentPane(panel1);
            }
        });
    }


}
