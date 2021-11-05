import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UI{

    private static JFrame frame;
    private JPanel panel1;
    private JTextField variates_num;
    private JComboBox type;
    private JButton confirm;
    private JButton calculate;
    private JButton addConstraint;
    private JScrollPane scrollpane;
    private JPanel constraint;

    private int count=1;

    public UI(){
        addToConfirm();
    }

    public static void mainUI(){
        frame = new JFrame("线性规划助手");
        frame.setContentPane(new UI().panel1);
        frame.setSize(600,600); //设置大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }



    public void addToConfirm(){
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = variates_num.getText().replace(" ","");
                int num;
                if(temp!="") num = Integer.parseInt(temp);
                else num=0;
                String tp = (String)type.getSelectedItem();
                if(num>=2 && num<9) startNewCalculation(tp,new InputContoller(num));
                if(num>=9) JOptionPane.showMessageDialog(panel1, "变量过多", "警告",JOptionPane.WARNING_MESSAGE);
                if(num<2) JOptionPane.showMessageDialog(panel1, "变量过少", "警告",JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void startNewCalculation(String type,InputContoller input){
        String target_index;
        if(type=="最大值")  target_index = "Max z = ";
        else target_index = "Min z = ";
        setTargetUI(target_index,input);
        setConstraintUI(input);
        addToConstraint(input);
    }

    public void setTargetUI(String target_index,InputContoller input){
        frame.setContentPane(panel1);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 2;
        for(int i=0;i<input.getNum();i++){
            target_index+="? "+input.getVariates(i);
            if(i!=input.getNum()-1) target_index+=" + ";
        }
        JTextField target = new JTextField(target_index);
        panel1.add(target,gbc);
        frame.setContentPane(panel1);
    }

    public void setConstraintUI(InputContoller input){
        frame.setContentPane(panel1);
        String cons="";
        constraint.setLayout(new GridLayout(10,1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        for(int i=0;i<input.getNum();i++){
            cons+="? "+input.getVariates(i);
            if(i!=input.getNum()-1) cons+=" + ";
        }
        cons+="<= ?";
        constraint.add(new JTextField(cons),gbc);
        frame.setContentPane(panel1);
    }

    public void addToConstraint(InputContoller input){
        addConstraint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(panel1);
                String cons="";
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = count;
                for(int i=0;i<input.getNum();i++){
                    cons+="? "+input.getVariates(i);
                    if(i!=input.getNum()-1) cons+=" + ";
                }
                cons+="<= ?";
                count++;
                constraint.add(new JTextField(cons),gbc);
                frame.setContentPane(panel1);
            }
        });
    }


}
