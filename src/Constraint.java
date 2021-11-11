import java.util.Arrays;

public class Constraint{
    private String constraint;
    private double[] constraint_data;
    Constraint(double[] data){
        constraint_data = data.clone();
        constraint = Arrays.toString(constraint_data);
    }
    public double[] getConstraintdata() {
        return constraint_data;
    }
}