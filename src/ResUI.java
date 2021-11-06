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

    public ResUI(Result res){
        addTosave();
        addToContinuity();
        results.append(res.toString());
    }

    public void addToContinuity(){
        continuity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    MainUI.mainUI();
            }
        });
    }

    public void addTosave(){
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public void save(){
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
            out.write(formatter2.format(date));
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
