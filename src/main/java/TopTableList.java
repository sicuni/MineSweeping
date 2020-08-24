import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
/**本类用于实现 Swing 的 TableView 查看效果‘
 *@author  IITII
 *@version  1.0
 *@see  javax.swing.text.TableView
 * */
public class TopTableList extends JFrame {
    private DefaultTableModel defaultTableModel = new DefaultTableModel();
    private JTable jTable = new JTable();
    private JScrollPane jScrollPane = new JScrollPane(jTable);
    //String historyFile = getClass().getResource("./log.txt").toString();
    /**游戏历史记录
     *@param historyFile 历史记录存放位置
     *@param title 排序类型*/
    public void top(String historyFile, String title){
        if (title.equals(""))
            title ="历史记录";
        String easy = "简单";
        String mid = "中等";
        String hard = "困难";
        String titleRow[] = {"难度","是否胜利","游戏时长（秒）","游戏开始时间","游戏结束时间"};
        new TopTableList();
        this.setSize(new Dimension(600,600));
        this.setLocationRelativeTo(null);
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(jScrollPane);
        this.getContentPane().setLayout(new FlowLayout());
        jTable.setModel(defaultTableModel);
        for(String string:titleRow)
            defaultTableModel.addColumn(string);
        this.setVisible(true);
        try {
            FileInputStream inputStream = new FileInputStream(historyFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            ArrayList<String[]> arrayList = new ArrayList<String[]>();
            while ((str = bufferedReader.readLine()) != null) {
                String[] arr = str.split(",");
                arrayList.add(arr);
                //defaultTableModel.addRow(arr);
                //Record record = new Record(format.parse(arr[0]),format.parse(arr[2]),Integer.parseInt(arr[2]),Boolean.parseBoolean(arr[3]));
            }
            if (title.equals("历史记录")){
            while(!arrayList.isEmpty()){
                defaultTableModel.addRow(arrayList.get(0));
                arrayList.remove(0);
            }
            }

            {
                // arr 简单
                // arr1 中等
                // arr2 困难
                ArrayList<Record> arr = new ArrayList();
                ArrayList<Record> arr1 = new ArrayList();
                ArrayList<Record> arr2 = new ArrayList();

                int i = 0;
                while (!arrayList.isEmpty()){
                    if (arrayList.get(0)[0].equals(easy))
                        arr.add( new Record(arrayList.get(0)[0],arrayList.get(0)[1],arrayList.get(0)[2],arrayList.get(0)[3],arrayList.get(0)[4]));
                    else if (arrayList.get(0)[0].equals(mid))
                        arr1.add(new Record(arrayList.get(0)[0],arrayList.get(0)[1],arrayList.get(0)[2],arrayList.get(0)[3],arrayList.get(0)[4]));
                    else if (arrayList.get(0)[0].equals(hard))
                        arr2.add(new Record(arrayList.get(0)[0],arrayList.get(0)[1],arrayList.get(0)[2],arrayList.get(0)[3],arrayList.get(0)[4]));
                    arrayList.remove(0);
                    i++;
                }
                Collections.sort(arr);
                Collections.sort(arr1);
                Collections.sort(arr2);

                String[] string =  new String[5];
                for(int j=0;j<5;j++)
                    string[j] = new String();
                for(Record e : arr){
                    string[0]=e.difficult;
                    string[1]=e.success;
                    string[2]=e.time;
                    string[3]=e.startTime;
                    string[4]=e.endTime;

                    defaultTableModel.addRow(string);
                }
                for(Record e : arr1){
                    string[0]=e.difficult;
                    string[1]=e.success;
                    string[2]=e.time;
                    string[3]=e.startTime;
                    string[4]=e.endTime;

                    defaultTableModel.addRow(string);
                }
                for(Record e : arr2){
                    string[0]=e.difficult;
                    string[1]=e.success;
                    string[2]=e.time;
                    string[3]=e.startTime;
                    string[4]=e.endTime;

                    defaultTableModel.addRow(string);
                }


            }
            bufferedReader.close();
            inputStream.close();
        }catch (IOException e){
            System.err.println("文件打开错误！");
        }
    }
}

/*Unused*/
class Record implements Comparable<Record>{
    public String startTime="";
    public String endTime="";
    public String time="";
    public String success="";
    public String difficult="";
    /*创建默认构造器*/
    public Record(){}
    /**用于记录游戏数据
     * @param startTime 游戏开始时间
     * @param endTime 游戏结束时间
     * @param time 游戏总时间
     * @param success 对局是否胜利
     */
    public Record(String difficult,String success, String time, String startTime, String endTime){
        this.difficult = difficult;
        this.time = time;
        this.success = success;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public int compareTo(Record another){
        if(Integer.parseInt(this.time) < Integer.parseInt(another.time)){
            return -1;
        }
        else if(Integer.parseInt(this.time) > Integer.parseInt(another.time)){
            return 1;
        }
        return 0;
    }
}
