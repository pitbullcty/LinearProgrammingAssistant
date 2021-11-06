public class Constraint{
    double[] constraint_data;
    Constraint(double[] data){
        constraint_data = data.clone();
    }
    public double[] getConstraintdata() {
        return constraint_data;
    }
}