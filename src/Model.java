
public class Model {
    private int m;  //方程个数
    private int n;  //变量个数
    private double[][] A; //系数矩阵
    private double[] C;//价值矩阵
    private double[] b; //资源常数
    private int[] baseVarites;  //基本变量

    private double[] sigma;  //检验数
    private double[] theta;  //行检验数

    private int indexIN = -1;
    private int indexOut = -1;


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
        theta = new double[m];
        sigma = new double[n];
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

    public boolean isBest() {
        if(indexIN != -1 && indexOut != -1){
            baseVarites[indexOut] = indexIN;
        }//更新检验数
        for (int i = 0; i < n; i++) {
            double temp = sigma[i];
            for (int j = 0; j < m; j++) {
                temp -= A[j][i] * C[baseVarites[j]];
            }
            sigma[i] = temp;
        }
        boolean isbest = true;
        for (int i = 0; i <  sigma.length; i++) {
            if(sigma[i] > 0)
                isbest = false;
        }
        return !isbest;
    } //判断是否具有最优解


    public int findIndexIN() {
        int index = 0;
        for (int i = 0; i < sigma.length; i++) {
            if(sigma[i] > sigma[index]){
                index = i;
            } //找到sigma最大的换入
        }
        return index;
    }

    public int findIndexOut(){
        int index = 0;
        for (int i = 0; i < m; i++) {
            if( Double.compare(A[i][indexIN], 0) != 0)
                theta[i] = b[i] / A[i][indexIN];
            else {
                theta[i] = 0;
            }
        }
        for (int i = 0; i < theta.length; i++) {
            if(theta[i] <= 0){
                return -1;
            }else {
                if(theta[i] < theta[index])
                    index = i;
            }
        }
        return index;
    }

    public void updateMatrix(){
        double temp = A[indexOut][indexIN];
        for (int i = 0; i < n; i++) {
            A[indexOut][i] /= temp;
        }
        b[indexOut] /= temp; //主元行所在行除以主元
        for (int i = 0; i < m; i++) {
            double temp1 = A[i][indexIN]/A[indexOut][indexIN];
            if(i != indexOut){
                for (int j = 0; j < n; j++) {
                    A[i][j] -= A[indexOut][j]*temp1;
                }
                b[i] -= b[indexOut] * temp1;
            }  //非主元行主元行所在列初等行变换为0
        }
    } //高斯消元变换矩阵

    public double[] getBest(){
        double z=0;
        double[] res= new double[m+1];
        for (int i = 0; i < baseVarites.length; i++) {
            z += C[baseVarites[i]] * b[i];
            res[i] = b[i];
        }
        res[res.length]=z;
        return res;
    }



    public Result solve(){
        while (!isBest()) {  //判断最优解
            indexIN = findIndexIN(); //找换入变量
            indexOut = findIndexOut(); //找换出变量
            if(indexOut == -1)
                return new Result("无可行解！"); //若没有换出变量
            updateMatrix(); //更新矩阵
        }
        return new Result(getBest());
    }

}
