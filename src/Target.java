import java.util.Arrays;

public class Target{
    private String target;
    private double[] target_data;

    public Target(double[] data){
        target_data = data.clone();
        target = Arrays.toString(target_data);
    }

    public double[] getTarget_data() {
        return target_data;
    }
}