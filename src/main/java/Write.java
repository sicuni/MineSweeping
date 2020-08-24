import java.io.*;
import java.util.ArrayList;
/**
 * 文件写入类
 * @author IITII
 * @see java.util.ArrayList
 * */
public class Write {
    /** 向文件追加内容
     * @param write 要写入的文件
     * @param fileName 要写入的内容
     */
    public void writeAppend(String write, String fileName) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(write);
            writer.close();
        } catch (IOException e) {
            System.err.println("文件写入错误!!!");
            //outDescription.setText("文件写入错误!!!");
        }
    }
    /**从文件读取内容, 并返回一个 ArrayList<String> 的二维数组
     * @param fileName 要读取的文件名
     * @return arrayList 一个 ArrayList<String> 的二维数组
     * */
    public ArrayList<String> readLine(String fileName){
        ArrayList <String> arrayList = new ArrayList<String>();
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;

            while ((str = bufferedReader.readLine()) != null) {
                //String[] arr = str.split(",");
                arrayList.add(str);
                //Record record = new Record(format.parse(arr[0]),format.parse(arr[2]),Integer.parseInt(arr[2]),Boolean.parseBoolean(arr[3]));
            }
            bufferedReader.close();
            inputStream.close();
        }catch (IOException e){
            System.err.println("文件打开错误！");
        }
        return arrayList;
    }
    /**从文件读取内容, 并返回一个 ArrayList<String[]> 的二维数组
     * @param fileName 要读取的文件名
     * @param split 分隔符
     * @return arrayList 一个 ArrayList<String[]> 的二维数组
     * */
    public ArrayList<String[]> readLine(String fileName, String split){
        ArrayList <String[]> arrayList = new ArrayList<String[]>();
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                String[] arr = str.split(split);
                arrayList.add(arr);
                //Record record = new Record(format.parse(arr[0]),format.parse(arr[2]),Integer.parseInt(arr[2]),Boolean.parseBoolean(arr[3]));
            }
            bufferedReader.close();
            inputStream.close();
        }catch (IOException e){
            System.err.println("文件打开错误！");
        }
        return arrayList;
    }
}
