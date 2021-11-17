import java.util.Arrays;

public class Constraint{
    private String constraint;
    private double[] constraint_data;
    private int type;

    Constraint(double[] data,int type){
        constraint_data = data.clone();
        constraint = Arrays.toString(constraint_data);
        this.type = type;
    }

    public double[] getConstraintdata() {
        return constraint_data;
    }
    public int gettype(){
        return type;
    }
}