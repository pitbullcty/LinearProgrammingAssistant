

public class InputContoller {
    private int num;
    private String[] variates;

    public InputContoller(int num) {
        this.num = num;
        variates = new String[num];
        for (int i = 0; i < num; i++) {
            variates[i] = "x" + i;
        }
    } //生成变量名

    public String getVariates(int index) {
        return variates[index];
    }

    public int getNum() {
        return num;
    }

    public Target getTarget(String target) {
        int[] num_index = new int[num];
        double[] data = new double[num];
        int[] op_index = new int[num];
        int j=0;
        target = target.replace(" ", "");
        for (int i = 0; i < num; i++) {
            num_index[i] = target.indexOf(variates[i]);
        }
        for(int i = 0;i<target.length();i++){
            if(target.charAt(i)=='+' || target.charAt(i)=='='){
                op_index[j++] = i;
            }
        }
        for(int i=0;i<num;i++){
            data[i] = Double.parseDouble(target.substring(op_index[i]+1,num_index[i]));
        }
        return new Target(data);
    }

    public Constraint getConstraint(String constraint) {
        return null;
    }

}
