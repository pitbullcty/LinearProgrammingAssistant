import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class MainUI {

    private JFrame frame;
    private JPanel panel1;
    private JTextField variates_num;
    private JComboBox type;
    private JButton confirm;
    private JButton calculate;
    private JButton addConstraint;
    private JScrollPane scrollpane;
    private JTextField target;
    private JTextArea constraint;

    private int count = 0;
    private int num;

    public MainUI() {
        frame = new JFrame("线性规划助手");
        frame.setContentPane(panel1);
        frame.setSize(600, 600); //设置大小
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        start();
    }

    public void startNewCalculation(String type, InputContoller input) {
        enterTarget(type, input);
        enterConstraint(input);
        calculate(type, input);
    }  //开始新运算

    public void enterTarget(String type, InputContoller input) {
        StringBuilder target_index = new StringBuilder();
        if (type.equals("最大值"))  target_index.append("Max z = ");
        else  target_index.append("Min z = ");
        for (int i = 0; i < input.getNum(); i++) {
            target_index.append("? ").append(input.getVariates(i));
            if (i != input.getNum() - 1) target_index.append(" + ");
        }
        target.setText(target_index.toString());
    }  //输入优化目标

    public void enterConstraint(InputContoller input) {
        ActionListener[] action = addConstraint.getActionListeners();
        if (action.length != 0) {
            addConstraint.removeActionListener(action[0]);
        }
        addConstraint.addActionListener(e -> {
            StringBuilder cons = new StringBuilder();
            for (int i = 0; i < input.getNum(); i++) {
                cons.append("? ").append(input.getVariates(i));
                if (i != input.getNum() - 1) cons.append(" + ");
            }
            cons.append("<= ?\n");
            constraint.append(cons.toString());
        });
    }  //输入约束条件

    public void calculate(String type, InputContoller input) {
        calculate.addActionListener(e -> {
            double start_time = System.nanoTime();
            Model model = getModel(type, input);
            Result res = model.calc();
            double end_time = System.nanoTime();
            if (model.getisDegeneration()) {
                JOptionPane.showMessageDialog(panel1, "计算过程存在退化，因此可能存在错误！", "警告", JOptionPane.WARNING_MESSAGE);
            }
            if (res != null) {
                res.setTime((end_time - start_time) / 1000000);
                showResults(res);
            }

        });
    }  //开始计算

    public void showResults(Result res) {
        frame.dispose();
        frame.dispose();
        ResUI ui = new ResUI(res);
    }  //展示结果


    public Model getModel(String type, InputContoller input) {
        Target target = getTarget(type, input);
        Constraint[] constraints = getConstraint(input);
        return new Model(constraints, target);

    }  //获取模型

    public Target getTarget(String type, InputContoller input) {
        try {
            return input.getTarget(type, this.target.getText());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "优化目标有误！", "警告", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }  //获取目标

    public Constraint[] getConstraint(InputContoller input) {
        try {
            ArrayList<Constraint> constraints = new ArrayList<>();
            String[] temp = constraint.getText().split("\n");
            for (var i : temp) {
                constraints.add(input.getConstraint(i));
            }
            return constraints.toArray(Constraint[]::new);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "约束条件有误！", "警告", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }  //获取约束条件




    public void start() {

        confirm.addActionListener(e -> {
            boolean isSelected = true;
            int prenum = num;
            String temp = variates_num.getText().replace(" ", "");
            if (!temp.equals("")) num = Integer.parseInt(temp);
            else num = 0;
            String tp = (String) type.getSelectedItem();
            if(tp==null || tp.equals("")){
                JOptionPane.showMessageDialog(panel1, "请选择问题类型！", "警告", JOptionPane.WARNING_MESSAGE);
                isSelected = false;
            }
            if (num >= 2 && prenum != num && isSelected) {
                constraint.setText("");
                startNewCalculation(tp, new InputContoller(num));
            }
            if (num < 2) JOptionPane.showMessageDialog(panel1, "变量过少", "警告", JOptionPane.WARNING_MESSAGE);
        });
    }

}
