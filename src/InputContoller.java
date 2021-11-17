public class InputContoller {
    private int num;
    private String[] variates;

    public InputContoller(int num) {
        this.num = num;
        variates = new String[num];
        for (int i = 0; i < num; i++) {
            variates[i] = "x" + i;
        }
    }

    public String getVariates(int index) {
        return variates[index];
    } //生成变量名

    public int getNum() {
        return num;
    }

    public Target getTarget(String type,String target) throws NumberFormatException,IndexOutOfBoundsException{
        int[] num_index = new int[num];
        double[] data = new double[num];
        int[] op_index = new int[num];
        int j=0;
        double c=1;
        target = target.replace(" ", "");
        for (int i = 0; i < num; i++) {
            num_index[i] = target.indexOf(variates[i]);
        }
        for(int i = 0;i<target.length();i++){
            if(target.charAt(i)=='+' || target.charAt(i)=='='){
                op_index[j++] = i;
            }
        }
        if(type.equals("最小值")){
            c=-1;
        }
        for(int i=0;i<num;i++){
            data[i] = c * Double.parseDouble(target.substring(op_index[i]+1,num_index[i]));
        }
        return new Target(data);
    } //获取优化目标

    public Constraint getConstraint(String constraint)  throws NumberFormatException,IndexOutOfBoundsException{
        int[] num_index = new int[num];
        double[] data = new double[num+1];
        int[] op_index = new int[num];
        int j=0;
        constraint = constraint.replace(" ", "");
        for (int i = 0; i < num; i++) {
            num_index[i] = constraint.indexOf(variates[i]);
        }
        for(int i = 0;i<constraint.length();i++){
            if(constraint.charAt(i)=='+' || constraint.charAt(i)=='='){
                op_index[j++] = i;
            }
        }
        for(int i=0;i<num+1;i++){
            if(i!=0 && i!=num) data[i] = Double.parseDouble(constraint.substring(op_index[i-1]+1,num_index[i]));
            else if(i==0){
                data[i] = Double.parseDouble(constraint.substring(0,num_index[i]));
            }
            else{
                data[i] = Double.parseDouble(constraint.substring(op_index[i-1]+1));
            }
        }
        return new Constraint(data);
    } //获取限制条件

    public void toStandardForm() throws Exception{
        ;
    }

}
