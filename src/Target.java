public class Target{
    private double[] target_data;

    public Target(double[] data){
        target_data = data.clone();

    }

    public double[] getTarget_data() {
        return target_data;
    }
}