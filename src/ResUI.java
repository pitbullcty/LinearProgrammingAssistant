import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResUI {

    private JPanel panel1;
    private JTextArea results;
    private JButton save;
    private JButton continuity;
    private JLabel time;

    public ResUI(Result res){
        setTime(res);
        addTosave(res);
        addToContinuity();
        results.append(res.toString());
    }

    public void setTime(Result res) {
        time.setText(String.format("本次运算时间为%.2f ms",res.getTime()));
    }

    public void addToContinuity(){
        continuity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    MainUI.mainUI();
            }
        });
    }

    public void addTosave(Result res){
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save(res);
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public void save(Result res){
        Date date = new Date();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        File dir = new File("./Logs");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String filename =  "./Logs/" + formatter1.format(date) + ".txt";
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename));
            out.write(results.getText()+"\n");
            out.write("");
            out.write(formatter2.format(date)+"\n");
            out.write(String.format("本次运算时间为%.2f ms",res.getTime()));
            out.close();
            JOptionPane.showMessageDialog(panel1, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
