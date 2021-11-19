import java.util.Arrays;

public class Target{
    private String target;
    private double[] target_data;
    private int type;

    public Target(double[] data,int type){
        target_data = data.clone();
        target = Arrays.toString(target_data);
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public double[] getTarget_data() {
        return target_data;
    }
}