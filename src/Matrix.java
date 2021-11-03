public class Matrix {
    private double[][] data;
    private int row;
    private int col;

    Matrix(Matrix M) {
        this.col = M.col;
        this.row = M.row;
        data = new double[this.row][this.col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                data[i][j] = M.data[i][j];
            }
        }
    }

    Matrix(int row, int col) {
        this.col = col;
        this.row = row;
        data = new double[row][col];
    }

    Matrix(double[][] data) {
        this.data = new double[data.length][];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i].clone();
        }
        this.row = data.length;
        this.col = data[0].length;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public double getValue(int rindex, int cindex) {
        return this.data[rindex][cindex];
    }

    Matrix Add(Matrix M) {
        if (this.row != M.row || this.col != M.col) {
            try {
                throw new Exception("相加的矩阵维数不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = new Matrix(M.row, M.col);
        for (int i = 0; i < M.getRow(); i++) {
            for (int j = 0; j < M.getCol(); j++) {
                res.data[i][j] = M.data[i][j] + this.data[i][j];
            }
        }
        return res;
    }


    public static Matrix Add(Matrix M1, Matrix M2) {
        if (M1.row != M2.row || M1.col != M2.col) {
            try {
                throw new Exception("相加的矩阵维数不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = new Matrix(M1.row, M1.col);
        for (int i = 0; i < M1.getRow(); i++) {
            for (int j = 0; j < M1.getCol(); j++) {
                res.data[i][j] = M1.data[i][j] + M2.data[i][j];
            }
        }
        return res;
    }

    public Matrix Sub(Matrix M) {
        if (this.row != M.row || this.col != M.col) {
            try {
                throw new Exception("相减的矩阵维数不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = new Matrix(M.row, M.col);
        for (int i = 0; i < M.getRow(); i++) {
            for (int j = 0; j < M.getCol(); j++) {
                res.data[i][j] = this.data[i][j] - M.data[i][j];
            }
        }
        return res;
    }

    public static Matrix Sub(Matrix M1, Matrix M2) {
        if (M1.row != M2.row || M1.col != M2.col) {
            try {
                throw new Exception("相减的矩阵维数不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = new Matrix(M1.row, M1.col);
        for (int i = 0; i < M1.getRow(); i++) {
            for (int j = 0; j < M1.getCol(); j++) {
                res.data[i][j] = M1.data[i][j] - M2.data[i][j];
            }
        }
        return res;
    }

    public Matrix Multiply(Matrix M) {
        if (this.col != M.row) {
            try {
                throw new Exception("相乘的矩阵维度不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = new Matrix(M.row, M.col);
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < M.col; j++) {
                for (int k = 0; k < this.col; k++)
                    res.data[i][j] = res.data[i][j] + this.data[i][k] * M.data[k][j];
            }
        }
        return res;
    }

    public Matrix Multiply(double n) {
        Matrix res = new Matrix(this);
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                res.data[i][j] *= n;
            }
        }
        return res;
    }

    public static Matrix Multiply(Matrix M1, Matrix M2) {
        if (M1.col != M2.row) {
            try {
                throw new Exception("相乘的矩阵维数不匹配！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = new Matrix(M1.row, M1.col);
        for (int i = 0; i < M1.row; i++) {
            for (int j = 0; j < M2.col; j++) {
                for (int k = 0; k < M1.col; k++)
                    res.data[i][j] = res.data[i][j] + M1.data[i][k] * M2.data[k][j];
            }
        }
        return res;
    }

    public static Matrix Multiply(Matrix M1, int n) {
        Matrix res = new Matrix(M1.row, M1.col);
        for (int i = 0; i < M1.row; i++) {
            for (int j = 0; j < M1.col; j++) {
                res.data[i][j] *= n;
            }
        }
        return res;
    }

    public Matrix Pow(int n) {
        Matrix res = new Matrix(this);
        for (int i = 0; i < n; i++) {
            res = Matrix.Multiply(res, this);
        }
        return res;
    }

    public static Matrix Pow(Matrix M, int n) {
        Matrix res = new Matrix(M);
        for (int i = 0; i < n; i++) {
            res = Matrix.Multiply(res, M);
        }
        return res;
    }

    public Matrix Divide(double n) {
        if (n == 0) {
            try {
                throw new Exception("除数为0!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = new Matrix(this);
        for (int i = 0; i < this.getRow(); i++) {
            for (int j = 0; j < this.getCol(); j++) {
                res.data[i][j] /= n;
            }
        }
        return res;
    }


    public Matrix Transfer() {
        Matrix res = new Matrix(this.col, this.row);
        for (int i = 0; i < this.col; i++) {
            for (int j = 0; j < this.row; j++) {
                res.data[i][j] = this.data[j][i];
            }
        }
        return res;
    }

    public double getDet() {
        if (this.getRow() != this.getCol()) {
            try {
                throw new Exception("只有方阵才有行列式");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.getRow() == 1) return this.data[0][0];
        if (this.getRow() == 2) {
            return this.data[0][0] * this.data[1][1] - this.data[1][0] * this.data[0][1];
        }
        double DetValue = 0;
        for (int i = 0; i < this.getCol(); i++) {
            Matrix m = this.getComplementaryMinor(0, i);
            DetValue += Math.pow(-1, i) * m.getDet() * this.data[0][i];
        }
        return DetValue;
    }

    public Matrix getComplementaryMinor(int rindex, int cindex) {
        Matrix res = new Matrix(this.getRow() - 1, this.getCol() - 1);
        int row = 0, col = 0;
        for (int i = 0; i < this.getRow(); i++) {
            if (i == rindex) continue;
            for (int j = 0; j < this.getCol(); j++) {
                if (j == cindex) continue;
                res.data[row][col] = this.data[i][j];
                col++;
                if (col >= res.getCol()) {
                    col = 0;
                    row++;
                }
            }
        }
        return res;
    }

    public Matrix Inverse() {
        if (this.col != this.row) {
            try {
                throw new Exception("只有方阵才可求逆");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix WithMatrix = new Matrix(this.getRow(), this.getCol());
        for (int i = 0; i < WithMatrix.getRow(); i++) {
            for (int j = 0; j < WithMatrix.getCol(); j++) {
                double temp = Math.pow(-1, i + j) * this.getComplementaryMinor(j, i).getDet();
                if (Math.abs(temp) <= 10e-6) temp = 0;
                WithMatrix.data[i][j] = temp;
            }
        }
        double DetValue = this.getDet();
        if (Math.abs(DetValue) <= 10e-6) {
            try {
                throw new Exception("矩阵行列式为0无逆");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Matrix res = WithMatrix.Divide(DetValue);
        return res;
    }

    public String toString() {
        String res = "";
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                res = res + data[i][j] + " ";
            }
            if (i != this.row) res += "\n";
        }
        return res;
    }

    public static void main(String[] args) {
        double[][] s1 = {{1, 2, 3}, {1, 3, 4}, {2, 4, 5}};
        double[][] s2 = {{1, 2}, {3, 4}, {5, 6}};

        Matrix S1 = new Matrix(s1);
        Matrix S2 = new Matrix(s2);

        System.out.println(S2.Transfer());
        System.out.println();
        System.out.println(S1.Inverse());
    }
}
