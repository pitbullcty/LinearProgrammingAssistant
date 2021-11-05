
public class Model {
    private int m;  //方程个数
    private int n;  //变量个数
    private double[][] A; //系数矩阵
    private double[] C;//价值矩阵
    private double[] b; //资源常数
    private int[] baseVarites;  //基本变量
    private double[] theta; //b的检验数
    private double[] sigma; //最终检验数

    private int indexOUt=-1;
    private int indexIN =-1;


    public Model(Constraint[] cons,Target tar){
        convertToModel(cons,tar);
    }

    public void convertToModel(Constraint[] cons,Target tar){
        m = cons.length;
        n = tar.getTarget_data().length+m;
        A = new double[m][n];
        C = new double[n];
        b = new double[m];
        baseVarites = new int[m];
        theta = new double[n];
        sigma = new double[m];

        for(int i=0;i<m;i++){
            if(i<cons.length) {
                double[] temp = cons[i].getConstraintdata();
                baseVarites[m-i-1] = n-i-1;
                b[i] = temp[temp.length-1];
                for(int j=0;j<n;j++){
                    if(j<temp.length-1){
                        C[j] = tar.getTarget_data()[j];
                        A[i][j] = temp[j];
                    }
                    else{
                        if(j==i+temp.length-1){
                            A[i][j]=1;
                        }
                    }
                }
            }
        }

    }


    public void createCalculator(){

    }

}
