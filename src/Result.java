public class Result {
    private double[] result_data;
    private String message;
    private double time;


    public Result(double[] data){
        result_data  = data.clone();
         message = "有最优解";
    }

    public Result(String message){
        this.message = message;
        result_data =null;
        this.time =0;
    }

    public String toString(){
        return save();
    }

    public void setTime(double time) {
        this.time = time;
    }



    public double getTime() {
        return time;
    }

    public String save(){
        if(result_data!=null){
            String res="最优解为\n";
            for(int i=0;i<result_data.length-1;i++){
                res+="x"+i+" = " +String.format("%f",result_data[i])+"\n";
            }
            res+="z = "+String.format("%f",result_data[result_data.length-1]);
            return res;
        }
        else{
            return message;
        }
    }

}
