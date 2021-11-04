import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI{
    private JPanel panel1;
    private JTextField variates_num;
    private JComboBox type;
    private JButton confirm;
    private JButton calculate;
    private JButton addConstraint;

    public UI(){

        addToConfirm();

    }

    public static void main() {
        JFrame frame = new JFrame("线性规划助手");
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
                if(num>=2) startNewCalculation(num,tp);
            }
        });
    }

    public void startNewCalculation(int num,String type){
        ;
    }

}
