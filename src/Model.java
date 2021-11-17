import java.util.ArrayList;

public class Model {

    private static double M = 999999; //大M法选取的M取值

    private int m;  //方程个数
    private int n;  //变量个数
    private int on; //初始变量个数
    private int an; //添加人工变量个数


    private double[][] A; //系数矩阵
    private double[] C;//价值矩阵
    private double[] b; //资源常数


    private double[] sigma;  //检验数
    private double[] theta;  //行检验数

    private int[][] UnitMatrix; //记录单位子块位置
    private double z;



    public Model(Constraint[] cons, Target tar) {
        convertToModel(cons, tar);
    }

    public void convertToModel(Constraint[] cons, Target tar) {
        m = cons.length;
        on = tar.getTarget_data().length;

        int addcons = 0;
        for (int i = 0; i < cons.length; i++) {
            if (cons[i].gettype() != 2) {
                addcons++;
            }
        } //化为标准形式需要添加的个数

        n = tar.getTarget_data().length + addcons;

        A = new double[m][n];
        C = new double[n];
        b = new double[m];
        theta = new double[m];
        sigma = new double[n];
        UnitMatrix = new int[m][2];

        for (int i = 0; i < m; i++) {
            double c = 1;
            if (i < cons.length) {
                double[] temp = cons[i].getConstraintdata();
                b[i] = temp[temp.length - 1];
                if (b[i] < 0) {
                    b[i] *= -1;
                    c = -1;
                } //将b全部化为正值
                for (int j = 0; j < n; j++) {
                    if (j < temp.length - 1) {
                        C[j] = tar.getTarget_data()[j];
                        sigma[j] = C[j];
                        A[i][j] = temp[j] * c;
                    } else {
                        if (j == i + temp.length - 1) {
                            A[i][j] = cons[i].gettype();
                        }
                    }
                }
            }
        }//初步处理
    }


    //判断是否需要使用大M法，即是否含有单位子矩阵
    public boolean isBigM() {
        for (int i = 0; i < UnitMatrix.length; i++) {
            for (int j = 0; j < UnitMatrix[0].length; j++) {
                UnitMatrix[i][j] = -1;
            }
        }
        boolean isUnit = true;
        for (int j = 0; j < A[0].length; j++) {
            for (int i = 0; i < A.length; i++) {
                if (A[i][j] == 1) {
                    isUnit = true;
                    for (int k = 0; k < A.length; k++) {
                        if (A[k][j] != A[i][j] && A[k][j] != 0)
                            isUnit = false;
                    } //判断一列元素是否只含有一个1
                    if (isUnit) {
                        UnitMatrix[i][0] = i;
                        UnitMatrix[i][1] = j;
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            if (UnitMatrix[i][0] == -1) return true;
        }
        return false;
    }

    //转换为大M法
    public void convertToBigM() {
        int addlength = 0;
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
            for (int j = 0; j < C.length; j++) {
                temp_sigma[j] -= C[UnitMatrix[i][1]] * A[UnitMatrix[i][0]][j];
            }
          //  z -= C[UnitMatrix[i][1]] * b[UnitMatrix[i][0]];
        }

        sigma = temp_sigma;

    }

    private boolean hasNegative() {
        for (int i = 0; i < sigma.length; i++) {
            if (sigma[i] < 0)
                return true;
        }
        return false;
    }

    private int[] selectBaseVar() {

        int indexOfC[] = new int[sigma.length];

        for (int i = 0; i < indexOfC.length; i++) {
            indexOfC[i] = -1;
        }

        ArrayList<Integer> indexList = new ArrayList<Integer>();

        for (int i = 0; i < sigma.length; i++) {
            if (sigma[i] < 0)
                indexList.add(i);
        }

        for (int i = 0; i < indexList.size(); i++) {
            indexOfC[i] = indexList.get(i);
        }

        return indexOfC;
    }

    private int[] selectOffBaseVar(int[] indexOfBaseVar) {

        int[] indexOfMin = new int[2];
        for (int i = 0; i < indexOfMin.length; i++) {
            indexOfMin[i] = -1;
        }
        // min初始化时设置一个足够大的数
        double min = 1e10;
        for (int k = 0; k < b.length; k++) {
            if (b[k] == 0) {
                for (int i = 0; i < indexOfBaseVar.length; i++) {
                    if (i >= 0) {
                        for (int j = 0; j < A.length; j++) {
                            if (A[j][i] > 0 && b[j] / A[j][i] < min) {
                                min = b[j] / A[j][i];
                                indexOfMin[0] = j;
                                indexOfMin[1] = i;
                            }
                        }
                    }
                }
            }
        } // 当系数为0时，可能存在退化,选取底行左边第一个负元素k避免循环

        if (indexOfMin[0] > -1) {
            return indexOfMin;
        }

        for (int i = 0; i < indexOfBaseVar.length; i++) {
            if (i >= 0) {
                for (int j = 0; j < A.length; j++) {
                    if (A[j][i] > 0 && b[j] / A[j][i] < min) {
                        // min要重新赋最小值
                        min = b[j] / A[j][i];
                        indexOfMin[0] = j;
                        indexOfMin[1] = i;
                    }
                }
            }
        }

        return indexOfMin;
    }

    private double rotation(double[][] a, double[] b, double[] c, double z, int[] indexOfOffBaseVar) {
        // 若直接将a[indexOfOffBaseVar[0]][indexOfOffBaseVar[1]]放进表达式中，此值在循环过程中会变化，故先将此值复制出来
        double temp1 = a[indexOfOffBaseVar[0]][indexOfOffBaseVar[1]];
        for (int j = 0; j < a[indexOfOffBaseVar[0]].length; j++) {
            a[indexOfOffBaseVar[0]][j] /= temp1;
        }
        b[indexOfOffBaseVar[0]] /= temp1;
        for (int i = 0; i < a.length; i++) {
            if (i != indexOfOffBaseVar[0]) {
                double[] temp2 = Arrays.copyOf(a[i], a[i].length);
                for (int j = 0; j < a[i].length; j++) {
                    a[i][j] -= temp2[indexOfOffBaseVar[1]] / a[indexOfOffBaseVar[0]][indexOfOffBaseVar[1]]
                            * a[indexOfOffBaseVar[0]][j];
                }
                b[i] -= temp2[indexOfOffBaseVar[1]] / a[indexOfOffBaseVar[0]][indexOfOffBaseVar[1]] * b[indexOfOffBaseVar[0]];
            }
        }
        double temp3 = c[indexOfOffBaseVar[1]];
        for (int j = 0; j < c.length; j++) {
            c[j] -= temp3 / a[indexOfOffBaseVar[0]][indexOfOffBaseVar[1]] * a[indexOfOffBaseVar[0]][j];
        }
        z -= temp3 / a[indexOfOffBaseVar[0]][indexOfOffBaseVar[1]] * b[indexOfOffBaseVar[0]];
        return z;
    }


    public Result calc() {

        return null;
    }

}
