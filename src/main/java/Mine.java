

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Mine extends JButton{
	//标识当前按钮处于第几行第几列
	int rowCut;
	int colCut;
	//判断该按钮是否为雷
	boolean isMine=false;
	//判断该按钮是否被点击过
	boolean isClick=false;
	public Mine(int rowCut, int colCut,ImageIcon img) {
		//调用父类的构造方法
		super(img);
		this.rowCut = rowCut;
		this.colCut = colCut;
	}
	
}
