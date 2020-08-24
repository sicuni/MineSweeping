import java.util.Date;

public class timeCalc {
    public void calcSecond(Date date, int second, String format){
        try {
            int hour, minute;
            hour = second / 3600;
            if (hour > 24) {
               throw  new LargeTimeException();
            }
            minute = (second - hour * 3600) / 60;
            second = second - hour * 300 - minute * 60;
            System.out.println(hour + ":" + minute + ":" + second);
        }catch (LargeTimeException e){
            System.err.println("数据过大！");
        }
    }

}
class LargeTimeException extends Exception{
    public LargeTimeException(){}
    public LargeTimeException(String message){
        super(message);
    }
}
