public class InputContoller {
    int num;
    String[] variates;

    public InputContoller(int num){
        this.num=num;
        variates = new String[num];
        for(int i=0;i<num;i++){
            variates[i]="x"+i;
        }
    } //生成变量名

    public String getVariates(int index) {
        return variates[index];
    }



}
