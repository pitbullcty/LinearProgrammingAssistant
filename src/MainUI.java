import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class MainUI {

    private static JFrame frame;
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

        start();
    }

    public void startNewCalculation(String type, InputContoller input) {
        enterTarget(type, input);
        enterConstraint(input);
        calculate(type, input);
    }  //开始新运算

    public void enterTarget(String type, InputContoller input) {
        String target_index;
        if (type == "最大值") target_index = "Max z = ";
        else target_index = "Min z = ";
        for (int i = 0; i < input.getNum(); i++) {
            target_index += "? " + input.getVariates(i);
            if (i != input.getNum() - 1) target_index += " + ";
        }
        target.setText(target_index);
    }  //输入优化目标

    public void enterConstraint(InputContoller input) {
        ActionListener[] action = addConstraint.getActionListeners();
        if(action.length!=0){
            addConstraint.removeActionListener(action[0]);
        }
        addConstraint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cons = "";
                for (int i = 0; i < input.getNum(); i++) {
                    cons += "? " + input.getVariates(i);
                    if (i != input.getNum() - 1) cons += " + ";
                }
                cons += "<= ?\n";
                constraint.append(cons);

            }
        });
    }  //输入约束条件

    public void calculate(String type, InputContoller input) {
        calculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double start_time = System.nanoTime();
                Model model = getModel(type, input);
                Result res = model.calc();
                double end_time = System.nanoTime();
                if (res != null) {
                    res.setTime((end_time-start_time)/100000);
                    showResults(res);
                }
            }
        });
    }  //开始计算

    public void showResults(Result res) {
        resUI(res);
    }  //展示结果


    public Model getModel(String type, InputContoller input) {
        Target target = getTarget(type, input);
        Constraint[] constraints = getConstraint(input);
        return new Model(constraints, target);

    }  //获取模型

    public Target getTarget(String type, InputContoller input) {
        try {
            Target target = input.getTarget(type, this.target.getText());
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "优化目标有误！", "警告", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }  //获取目标

    public Constraint[] getConstraint(InputContoller input) {
        try {
            ArrayList<Constraint> constraints = new ArrayList<>();
            String temp[] = constraint.getText().split("\n");
            for(int i=0;i<temp.length;i++){
                constraints.add(input.getConstraint(temp[i]));
            }
            return constraints.toArray(Constraint[]::new);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "约束条件有误！", "警告", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }  //获取约束条件

    public static void mainUI() {
        if (frame == null) frame = new JFrame("线性规划助手");
        frame.setContentPane(new MainUI().panel1);
        frame.setSize(600, 600); //设置大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }  //显示主界面

    public static void resUI(Result res) {
        if (frame == null) frame = new JFrame("线性规划助手");
        frame.setContentPane(new ResUI(res).getPanel1());
        frame.setSize(600, 600); //设置大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }  //显示结果界面

    public void start() {

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int prenum = num;
                String temp = variates_num.getText().replace(" ", "");
                if (temp != "") num = Integer.parseInt(temp);
                else num = 0;
                String tp = (String) type.getSelectedItem();
                if (num >= 2 && prenum!=num) {
                    constraint.setText("");
                    startNewCalculation(tp, new InputContoller(num));
                }
                if (num < 2) JOptionPane.showMessageDialog(panel1, "变量过少", "警告", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

}
