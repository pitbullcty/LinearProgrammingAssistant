public class Result {
    private double[] result_data;
    String message;
    public Result(double[] data){
        result_data  = data.clone();
         message = "有最优解";
    }

    public Result(String message){
        this.message = message;
        result_data =null;
    }

}
