
public class Model {
    Constraint[] constraints;
    Target target;

    public Model(Constraint[] cons,Target tar){
        this.constraints = cons.clone();
        target = tar;
    }

}
