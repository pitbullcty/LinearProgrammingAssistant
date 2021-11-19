import java.util.ArrayList;
import java.util.Collections;

public class Model {

    private final static double M = 999999; //大M法选取的M取值

    private int m;  //方程个数
    private int n;  //变量个数
    private int on; //初始变量个数

    private double[][] A; //系数矩阵
    private double[] C;//价值矩阵
    private double[] b; //资源常数

    private double[] sigma;  //检验数
    private double[] theta;  //行检验数

    private int[][] UnitMatrix; //记录单位子块位置
    private int[] baseVarites; //基变量

    private int indexIN = -1; //换入变量
    private int indexOut = -1; //换出变量
    private int addlength = 0; //人工变量个数

    private boolean isDegeneration = false; //问题是否退化
    private int type; //问题类型


    public Model(Constraint[] cons, Target tar) {
        convertToModel(cons, tar);
    }

    public void convertToModel(Constraint[] cons, Target tar) {

        m = cons.length;
        on = tar.getTarget_data().length;

        int addcons = 0;

        for(var i:cons){
            if(i.gettype()!=0) addcons++;
        }//化为标准形式需要添加的个数

        n = tar.getTarget_data().length + addcons;
        type = tar.getType();
        baseVarites = new int[m];

        A = new double[m][n];
        C = new double[n];
        b = new double[m];

        theta = new double[m];
        sigma = new double[n];
        UnitMatrix = new int[m][2];
        int offset = 0;

        for (int i = 0; i < m; i++) {
            double c = 1;
            double[] temp = cons[i].getConstraintdata();
            b[i] = temp[temp.length - 1];
            if (b[i] < 0) {
                b[i] *= -1;
                c = -1;
            }
            for (int j = 0; j < n; j++) {
                if (j < temp.length - 1) {
                    C[j] = tar.getTarget_data()[j];
                    sigma[j] = C[j];
                    A[i][j] = temp[j] * c;
                } else {
                    if (cons[i].gettype() != 0 && j == offset + temp.length - 1) {
                        A[i][j] = cons[i].gettype();
                        offset++;
                        break;
                    }
                }
            }
        }
    }

        //判断是否需要使用大M法，即是否含有单位子矩阵
        public boolean isBigM () {
            for (int i = 0; i < UnitMatrix.length; i++) {
                baseVarites[i] = -1;  //基变量值全部先取-1
                for (int j = 0; j < UnitMatrix[0].length; j++) {
                    UnitMatrix[i][j] = -1;
                }
            }
            boolean isUnit;
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < A[0].length; j++) {
                    if (A[i][j] == 1) {
                        isUnit = true;
                        for (int k = 0; k < A.length; k++) {
                            if (A[k][j] != 0 && k != i) {
                                isUnit = false;
                                break;
                            }
                        } //判断一列元素是否只含有一个1
                        if (isUnit) {
                            UnitMatrix[i][0] = i;
                            UnitMatrix[i][1] = j;
                        }
                    }
                }
            }

            for (int i = 0; i < m; i++) {
                baseVarites[i] = UnitMatrix[i][1];  //基变量为单位矩阵的位置
                if (UnitMatrix[i][0] == -1) return true;
            }
            return false;
        }

        //转换为大M法
        public void convertToBigM () {

            for (int i = 0; i < m; i++) {
                if (UnitMatrix[i][0] == -1) addlength++;
            }//计算要增加的变量个数,使得矩阵中存在单位阵

            double[] temp_sigma = new double[n + addlength];
            if (addlength > 0) {
                double[][] tempA = new double[m][n + addlength];
                double[] tempC = new double[n + addlength];
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        tempA[i][j] = A[i][j];
                        tempC[j] = C[j];
                        temp_sigma[j] = sigma[j];
                    }
                }
                int offset = 0;
                for (int i = 0; i < UnitMatrix.length; i++) {
                    if (UnitMatrix[i][0] == -1) {
                        tempC[A[0].length + offset] = -M;
                        temp_sigma[A[0].length + offset] = -M;
                        UnitMatrix[i][0] = i;
                        UnitMatrix[i][1] = A[0].length + offset;
                        for (int j = 0; j < A.length; j++) {
                            if (i == j)
                                tempA[j][A[0].length + offset] = 1;
                            else
                                tempA[j][A[0].length + offset] = 0;
                        }
                        offset++;
                    }
                }
                A = tempA;
                C = tempC;
            }

            for (int i = 0; i < UnitMatrix.length; i++) {
                baseVarites[i] = UnitMatrix[i][1];
            }
            sigma = temp_sigma;

        }

        public boolean isBest () {
            if (indexIN != -1 && indexOut != -1) {
                baseVarites[indexOut] = indexIN;
            }//更新检验数
            for (int i = 0; i < sigma.length; i++) {
                sigma[i] = C[i];
                for (int j = 0; j < m; j++) {
                    sigma[i] -= A[j][i] * C[baseVarites[j]];
                }
            }
            boolean isbest = true;
            for(var i:sigma){
                if(i>0){
                    isbest=false;
                    break;
                }
            }
            return isbest;
        } //判断是否具有最优解

        public int findIndexIN () {
            int index = 0;
            for (int i = 0; i < sigma.length; i++) {
                if (sigma[i] > sigma[index]) {
                    index = i;
                } //找到sigma中最大的换入
            }
            return index;
        }

        public int findIndexOut () {

            ArrayList<Double> temp = new ArrayList<>();
            ArrayList<Integer> same_index = new ArrayList<>();
            int count = 0;
            int same_count = 0;
            double min;
            int index;

            for (int i = 0; i < m; i++) {
                theta[i] = b[i] / A[i][indexIN];
            }

            for(var i:theta){
                if(i>=0) temp.add(i);
            }

            min = Collections.min(temp);

            for (int i = 0; i < theta.length; i++) {
                if (theta[i] == min) {
                    same_count++;
                    same_index.add(i);
                }
            }

            if (same_count == 1) {
                index = same_index.get(0);
            } else {
                isDegeneration = true;
                for (var i : same_index) {
                    if (baseVarites[i] >= n) return i;
                }
                index = same_index.get(0);
            }  //处理退化，优先换出人工变量

            return index;
        }

        public void updateMatrix () {
            double temp = A[indexOut][indexIN];
            for (int i = 0; i < n; i++) {
                A[indexOut][i] /= temp;
            }
            b[indexOut] /= temp; //主元行所在行除以主元
            for (int i = 0; i < m; i++) {
                double temp1 = A[i][indexIN] / A[indexOut][indexIN];
                if (i != indexOut) {
                    for (int j = 0; j < n; j++) {
                        A[i][j] -= A[indexOut][j] * temp1;
                    }
                    b[i] -= b[indexOut] * temp1;
                }  //非主元行主元行所在列初等行变换为0
            }
        } //高斯消元变换矩阵

        public Result getBest () {
            double z = 0;
            double[] temp = new double[sigma.length + 1];
            double[] res = new double[on + 1];
            boolean isINF = false;
            for(var i:baseVarites){
                if(i>=n)  return new Result("大M无法消去，无解！");
            }
            for (int i = 0; i < sigma.length; i++) {
                if (sigma[i] == 0) {
                    for(var j:baseVarites){
                        if(j==i) {
                            isINF=true;
                            break;
                        }
                    }
                    if (!isINF) {
                        return new Result("有无穷多个解！");
                    }
                }
            } //是否有非基变量为0
            for (int i = 0; i < baseVarites.length; i++) {
                z += C[baseVarites[i]] * b[i];
                temp[baseVarites[i]] = b[i];
            }

            for (int i = 0; i < on; i++) {
                res[i] = temp[i];
            }

            z*= type;
            res[on] = z;
            return new Result(res);
        }

        public Result calc () {
            if (isBigM()) convertToBigM();
            while (!isBest()) {  //判断最优解
                indexIN = findIndexIN(); //找换入变量
                indexOut = findIndexOut(); //找换出变量
                if (indexOut == -1)
                    return new Result("原问题有无界解"); //若没有换出变量
                updateMatrix(); //更新矩阵
            }
            return getBest();
        }

        public boolean getisDegeneration () {
            return isDegeneration;
        }

    }


