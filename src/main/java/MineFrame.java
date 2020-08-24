import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.*;


public class MineFrame extends JFrame implements ActionListener, MouseListener{
	/*
	 * 处理图片
	 */
	//存放共有的图片
	private static ImageIcon[] imgShare={new ImageIcon("image/bomb.gif"),new ImageIcon("image/1.gif"),
			new ImageIcon("image/2.gif"),new ImageIcon("image/3.gif"),new ImageIcon("image/4.gif"),
			new ImageIcon("image/5.gif"),new ImageIcon("image/6.gif"),new ImageIcon("image/7.gif"),
			new ImageIcon("image/8.gif")};
	//定义私有的三种不同风格的图片
	private static ImageIcon[] img1={new ImageIcon("image/back.jpg"),new ImageIcon("image/but_bac.png"),
			new ImageIcon("image/but_open.png"),new ImageIcon("image/hq.png"),new ImageIcon("image/style1.png"),
			new ImageIcon("image/style2.png"),new ImageIcon("image/style3.png")};
	private static ImageIcon[] img2={new ImageIcon("image/style2/back.png"),new ImageIcon("image/style2/but_bac.png"),
			new ImageIcon("image/style2/but_open.png"),new ImageIcon("image/style2/hq.png"),new ImageIcon("image/style2/style1.png"),
			new ImageIcon("image/style2/style2.png"),new ImageIcon("image/style2/style3.png")};
	private static ImageIcon[] img3={new ImageIcon("image/style3/back.png"),new ImageIcon("image/style3/but_bac.png"),
			new ImageIcon("image/style3/but_open.png"),new ImageIcon("image/style3/hq.png"),new ImageIcon("image/style3/style1.png"),
			new ImageIcon("image/style3/style2.png"),new ImageIcon("image/style3/style3.png")};

	private String logFile = "src/main/resources/log.txt";
	private String logFileAbsPath = new File(logFile).getAbsolutePath();
	private String topFile = "src/main/resources/top.txt";
	//private String topFile = getClass().getResource("log.txt").toString();
	private String logText = "";
	private String topText = "";
	private Write write = new Write();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date startT = new Date();
	private String diffcult = "简单";
	private String style = "宁静";
	static Music musicc = new Music();

	//main方法
	public static void main(String[] args) {
		musicc.start();
		new MineFrame(14, 14, 20, 237, 121);
	}
	/*
	 * 面板中地雷的行数row和列数col，整个面板中地雷的个数numbers，地雷面板的上边距upSpace和左边距leftSpace
	 */
	private int row;
	private int col;
	private int numbers;
	private int upSpace;
	private int leftSpace;
	//声明一个标签用来显示背景图片
	JLabel mainL;
	static ImageIcon[][] img={img1,img2,img3};//定义一个数组来管理3个样式的图片数组
	int id=0;//设置当前样式数组的下标
	//设置开始、关闭、小化、切换三种风格的图片
	JButton close,start,min,getTips, playPause,style3;

	/*
	 * 要在时间处和雷数处放两个标签让时间走，并统计此时雷数，开启线程做时间的计时
	 */
	JLabel timeL,numberL;//两个标签的名字
	//每次开始时间的统计
	int timecont=0;
	int numberS=0;//剩余的雷数
	//设置是否开始游戏开关
	boolean isStart=false;
	Thread timeThread;//定义一个时间线程

	//定义简单，中等，难的三个菜单栏
	JMenuItem easy,medium,hard,top,viewLog,list,changeBackground;
	//定义按钮数组
	Mine[][] buts;
	//定义存放雷区的面板
	JPanel mineJpanel;
	//标识当前位置周围雷数的数组
	int[][] counts;
	//定义游戏是否第一次点击
	boolean isOnClick=true;
	//添加鼠标左键是否按下和右键是否按下
	boolean flagL=false;
	boolean flagR=false;
	//构造方法初始化这5个变量
	public MineFrame(int row, int col, int numbers, int leftSpace, int upSpace) throws HeadlessException {
		super();
		this.row = row;
		this.col = col;
		this.numbers = numbers;
		this.leftSpace = leftSpace;
		this.upSpace = upSpace;
		//调用自定义方法
		initFrame();
	}
	/*
	 * 自定义方法初始化窗体
	 */
	public void initFrame(){
		//为主标签赋值
		mainL=new JLabel();
		mainL.setMaximumSize(new Dimension(900, 600));
		mainL.setMinimumSize(new Dimension(900, 600));
		//为标签设置图片
		mainL.setIcon(img[id][0]);
		initBut(mainL);//设置按钮方法
		initLabel(mainL);//设置时间和剩余雷数的统计的方法
		
		initBar(mainL);//设置菜单的方法
		
		initMine(mainL);//设置雷区面板

		this.add(mainL);//将标签添加到窗体中

		this.setSize(900, 600);
		this.setIconImage(new ImageIcon("image/icon.jpg").getImage());
		this.setLocationRelativeTo(null);//设置窗体居中
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭按钮
		this.setUndecorated(true);//去掉窗体边框
		//AWTUtilities.setWindowOpaque(this, false);//设置窗体透明
		this.setVisible(true);

		//添加窗体移动的类
		new LocationUtil(this);
	} 

	/*
	 * 自定义方法设置各个按钮
	 */
	public void initBut(JLabel label){
		getTips=new JButton("提示");//初始化按钮
		getTips.setBounds(728, 322, 78, 39);//设置按钮位置
		getTips.setBorderPainted(false);//设置边框没有
		getTips.addActionListener(this);//加动作监听
		label.add(getTips);

		playPause =new JButton("暂停/播放");//初始化按钮
		playPause.setBounds(728, 391, 78, 39);//设置按钮位置
		playPause.setBorderPainted(false);//设置边框没有
		playPause.addActionListener(this);//加动作监听
		label.add(playPause);
//
//		style3=new JButton(img[id][6]);//初始化按钮
//		style3.setBounds(728, 460, 78, 39);//设置按钮位置
//		style3.setBorderPainted(false);//设置边框没有
//		style3.addActionListener(this);//加动作监听
//		label.add(style3);

		//最小化按钮
		min=new JButton(new ImageIcon("image/min.png"));
		min.setBounds(800, 16, 31, 31);
		min.setBorderPainted(false);
		min.setContentAreaFilled(false);//设置按钮透明
		min.addActionListener(this);
		label.add(min);
		//关闭按钮
		close=new JButton(new ImageIcon("image/close.png"));
		close.setBounds(841, 16, 31, 31);
		close.setBorderPainted(false);
		close.setContentAreaFilled(false);//设置按钮透明
		close.addActionListener(this);
		label.add(close);
		//开始初始化按钮
		start=new JButton(new ImageIcon("image/flush.png"));
		start.setBounds(435, 16, 31, 31);
		start.setBorderPainted(false);
		start.setContentAreaFilled(false);//设置按钮透明
		start.addActionListener(this);
		label.add(start);
	}
	//设置菜单的方法
	private void initBar(JLabel mainL) {
		//创建菜单栏
		JMenuBar bar=new JMenuBar();
		bar.setBounds(10, 1, 120, 64);//菜单位置和大小
		bar.setBorderPainted(false);//设置没有边框

		//创建菜单
		JMenu menu=new JMenu("            菜    单            ");
		//menu.setIcon(new ImageIcon("image/bar.png"));
		//menu.setIcon(new ImageIcon("image/home.png"));
		menu.setContentAreaFilled(false);//设置透明
		menu.setBorderPainted(false);//设置无边框
		bar.add(menu);//将菜单添加到菜单栏

		//初始化菜单项
		easy=new JMenuItem("简单");
		easy.addActionListener(this);//难度不同雷数也不同，所以加动作监听
		easy.setBackground(Color.blue);//背景
		easy.setForeground(Color.pink);//字体颜色
		menu.add(easy);
		medium=new JMenuItem("中等");
		medium.addActionListener(this);
		medium.setForeground(Color.GRAY);
		menu.add(medium);
		hard=new JMenuItem("困难");
		hard.addActionListener(this);
		hard.setForeground(Color.red);
		menu.add(hard);
		top=new JMenuItem("查看历史记录");
		top.addActionListener(this);
		menu.add(top);
		list=new JMenuItem("排行榜");
		list.addActionListener(this);
		menu.add(list);
		viewLog=new JMenuItem("查看日志");
		viewLog.addActionListener(this);
		menu.add(viewLog);

		changeBackground = new JMenuItem("选择背景图片");
		changeBackground.addActionListener(this);
		menu.add(changeBackground);

		mainL.add(bar);

	}
	/** 
	 * 设置时间标签和雷数统计标签
	 */
	private void initLabel(JLabel mainL) {
		timeL=new JLabel();//创建一个时间得的标签
		timeL.setText(timecont+"");//时间显示
		timeL.setBounds(736, 144, 80, 40);//设置时间显示的位置
		timeL.setFont(new Font("微软雅黑", Font.BOLD, 20));
		//设置字体颜色
		timeL.setForeground(Color.blue);
		timeL.setBackground(Color.white);
		timeL.setOpaque(true);
		mainL.add(timeL);

		numberL=new JLabel();
		numberL.setText(numberS+"");
		numberL.setBounds(736, 213, 80, 40);
		numberL.setFont(new Font("微软雅黑", Font.BOLD, 20));
		numberL.setBackground(Color.white);
		numberL.setOpaque(true);
		mainL.add(numberL);
	}
	/*
	 * 设置雷区的方法
	 */
	private void initMine(JLabel mainL) {
		//初始化雷区面板，GridLayout 类是一个布局处理器，它以矩形网格形式对容器的组件进行布置。
		//容器被分成大小相等的矩形，一个矩形中放置一个组件
		mineJpanel=new JPanel(new GridLayout(row, col));
		//设置雷区面板的位置和大小
		mineJpanel.setBounds(leftSpace, upSpace, 22*col, 22*row);
		//初始化按钮
		buts=new Mine[row][col];
		for (int i = 0; i < buts.length; i++) {
			for (int j = 0; j < buts[i].length; j++) {
				//创建一个雷按钮的对象
				Mine but=new Mine(i, j, img[id][1]);
				//将创建的雷按钮对象赋值给buts[][],把每个对象都放在数组里
				buts[i][j]=but;
				//给每个按钮添加鼠标事件
				buts[i][j].addMouseListener(this);
				//将创建好的按钮添加到面板中
				mineJpanel.add(but);
			}
		}
		//将雷区面板加到主标签上
		mainL.add(mineJpanel);
		//做布雷
		layMine();
		//统计每个按钮周围八个按钮中的雷数
		counts=new int[row][col];
		for (int i = 0; i < buts.length; i++) {
			for (int j = 0; j < buts[i].length; j++) {
				counts[i][j]=counts(i,j);
			}
		}
	}
	//统计当前按钮周围的八个按钮中的雷数
	private int counts(int i, int j) {
		int cut=0;//标识当前按钮周围的雷数
		for (int a = Math.max(0, i-1); a <= Math.min(row-1, i+1); a++) {
			for (int b = Math.max(0, j-1); b <= Math.min(col-1, j+1); b++) {
				if(buts[a][b].isMine){
					cut++;
				}
			}
		}
		//返回雷数
		return cut;
	}
	//布雷的方法
	private void layMine() {
		//开始的时候剩余的雷数和总的雷数是相等的
		numberS=numbers;
		numberL.setText(numberS+"");//在显示剩余雷数标签处回显
		for (int i = 0; i < numbers; i++) {
			int a=(int) (Math.random()*row);
			int b=(int) (Math.random()*col);
			//判断当前位置是否为雷
			if(!buts[a][b].isMine){
				//如果这个按钮不是雷，则设置这个按钮成雷
				buts[a][b].isMine=true;
			}else{//如果这个按钮是雷,则i--
				i--;
			}
		}
	}

	/*
	 * 鼠标的点击事件
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		//返回鼠标点击时的按钮，返回的是鼠标按钮的状态
		int V=e.getButton();
		//获取鼠标点击的组件的状态
		Mine but=(Mine)e.getSource();
		//获取当前按钮的行，列
		int i=but.rowCut;
		int j=but.colCut;
		//判断是否是第一次点击
		if(isOnClick){//如果是第一次点击，开启线程开始计时
			isStart=true;//线程里的计时开关
			startTime();//开始线程的方法
		}
		//设置第一次点击为false
		isOnClick=false;

		//判断当前的鼠标点击是左键并且点击的按钮不是插红旗的按钮
		if(V==MouseEvent.BUTTON1 && but.getIcon()!=img[id][3]){
			if(but.isMine){//如果当前按钮是雷则游戏结束
				failed();//游戏结束的方法
			}else{//不是雷
				if(counts[i][j]==0){//按钮点开周围没有雷
					buts[i][j].setIcon(img[id][2]);//设置这个按钮的图片为点开的图片
					buts[i][j].isClick=true;//设置按钮被点击过
					//遍历这个按钮周围的按钮并把不是雷的换成打开的图片
					moveMine(i,j);
				}else{//此按钮周围有雷
					int count=counts[i][j];//此按钮周围的雷数
					buts[i][j].isClick=true;//设置点击过
					buts[i][j].setIcon(imgShare[count]);//设置此按钮为周围雷数的数字图片

					//做是否赢了的判断
					int nClick=0;//标识没有被点击的按钮数
					//通过循环遍历统计没有被点击过的按钮的数量
					for (int m = 0; m < buts.length; m++) {
						for (int n = 0; n < buts[i].length; n++) {
							//当前按钮没有被点击过或者当前按钮为红旗按钮
							if(!buts[m][n].isClick || buts[m][n].getIcon()==img[id][3]){
								nClick++;
							}
						}
					}
					if(nClick==numbers){//没有被点击过的按钮或者红旗的按钮等于总共的雷数
						//游戏成功
						success();
					}
				}
			}
		}else if (V==MouseEvent.BUTTON3) {//点击的是右键
			//当前按钮没有被点击过并且也不是没有雷的空按钮
			if(!buts[i][j].isClick && buts[i][j].getIcon()!=img[id][2]){
				buts[i][j].isClick=true;
				buts[i][j].setIcon(img[id][3]);//设置此按钮为红旗按钮
				//雷统计标签减一
				numberS--;//剩余雷数
				numberL.setText(numberS+"");//更新剩余雷的数量;
			}else if (buts[i][j].getIcon()==img[id][3]) {//如果此按钮是红旗按钮
				buts[i][j].isClick=false;//设置此按钮没有被点击过
				buts[i][j].setIcon(img[id][1]);//设置此按钮为没有打开的按钮图标
				//雷数加一
				numberS++;
				numberL.setText(numberS+"");
			}
		}
	}
	//成功的话弹出窗体
	private void success() {
		//会话框	parentComponent窗体对象	message输出的信息	title会话框的标题	optionType会话框的类型
		topText = diffcult+",是,"+ timecont +","+format.format(startT)+","+format.format(new Date())+"\n";
		logText = diffcult+", 是, "+ timecont +", "+format.format(startT)+", "+format.format(new Date())+", " + style+"\n";
		write.writeAppend(logText,logFile);
		write.writeAppend(topText,topFile);
		//System.out.println(topText);
		//System.out.println(logText);
		int x=JOptionPane.showConfirmDialog(this, "赢了，重玩点是，退出点否", "提示",JOptionPane.YES_NO_OPTION);
		if(x==0){
			stopTime();
			isStart=true;
			startTime();
		}else{
			System.exit(0);
		}
	}
	/*
	 * 有雷按钮换成打开图片的方法
	 */
	private void moveMine(int i, int j) {
		//对当前按钮（i，j）周围的八个按钮进行循环遍历
		for (int a = Math.max(0, i-1); a <= Math.min(row-1, i+1); a++) {
			for (int b = Math.max(0, j-1); b <= Math.min(col-1, j+1); b++) {
				if(!buts[a][b].isClick){//此按钮没有被点击过
					if(counts[a][b]==0){//此按钮周围没有雷
						if(buts[a][b].getIcon() != img[id][3]){//此按钮也不是红旗按钮
							buts[a][b].setIcon(img[id][2]);//换点击过的空图片
						}
						//设置被点击过
						buts[a][b].isClick=true;
						//通过递归继续判断
						moveMine(a, b);
					}else{//此按钮周围有雷
						int count=counts[a][b];//统计此按钮周围的雷数
						buts[a][b].isClick=true;//设置此按钮被点击过
						buts[a][b].setIcon(imgShare[count]);//设置这个按钮的图片是对应的雷数
					}
				}
			}
		}
	}
	/*
	 * 游戏结束的处理
	 */
	private void failed() {
		//首先让时间停止
		isStart=false;
		//循环遍历每个按钮周围的有没有雷
		for (int i = 0; i < buts.length; i++) {
			for (int j = 0; j < buts[i].length; j++) {
				//如果当前的按钮为雷，那么现实雷的图片
				if(buts[i][j].isMine){
					buts[i][j].setIcon(imgShare[0]);
				}
			}
		}
		//点击到雷了，要结束游戏
		topText = diffcult+",是,"+ timecont +","+format.format(startT)+","+format.format(new Date())+"\n";
		logText = diffcult+", 是, "+ timecont +", "+format.format(startT)+", "+format.format(new Date())+", " +style+"\n";
		write.writeAppend(logText,logFile);
		write.writeAppend(topText,topFile);
		//System.out.println(topText);
		//System.out.println(logText);
		int x=JOptionPane.showConfirmDialog(this, "踩到雷了，是否继续，点击否退出", "提示", JOptionPane.YES_NO_OPTION);
		if(x==0 || x==-1){//点击是或者点击关闭按钮
			stopTime();
			isStart=true;
			startTime();
		}else{
			System.exit(0);
		}
	}
	//鼠标按下触发
	@Override
	public void mousePressed(MouseEvent e) {
		//获取鼠标点击的按钮，早雷区中
		Mine but=(Mine) e.getSource();
		if(e.getButton()==MouseEvent.BUTTON1 && flagL==false){
			flagL=true;
		}
		if(e.getButton()==MouseEvent.BUTTON3 && flagR==false){
			flagR=true;
		}
		if(flagL&& flagR){
			//左右键同时按下
			doubleClick(but.rowCut,but.colCut);
		}
	}
	//左右键同时双击
	private void doubleClick(int i, int j) {
		//表示当前按钮周围的雷数
		int mintCount=0;
		//表示当前按钮周围的红旗数
		int hqCount=0;
		for (int a = Math.max(0, i-1); a <=Math.min(i+1, row-1); a++) {
			for (int b = Math.max(0, j-1); b <= Math.min(j+1, col-1); b++) {
				//判断当前按钮是不是雷
				if(buts[a][b].isMine){
					mintCount++;
					if(buts[a][b].getIcon()==img[id][3]){//判断图片是不是红旗按钮
						hqCount++;
					}
				}else{
					//当前按钮不是雷，当时普片标注了红旗，就做失败的处理
					if(buts[a][b].getIcon()==img[id][3]){
						failed();
					}
				}
			}
		}
		//如果雷数等于标注的红旗数
		if(mintCount==hqCount){
			//这个按钮不是雷并且也不是红旗图片才能点击
			if(!buts[i][j].isMine && buts[i][j].getIcon()!=img[id][3]){
				moveMine(i, j);
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1){
			flagL=false;
		}
		if(e.getButton()==MouseEvent.BUTTON3){
			flagR=false;
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {

	}
	@Override
	public void mouseExited(MouseEvent e) {

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==close){//如果点击的是关闭按钮
			System.exit(0);//窗体关闭
		}else if(e.getSource()==min){//如果点击的是小化按钮
			MineFrame.this.setState(JFrame.ICONIFIED);//窗体小化
		}else if (e.getSource()==start) {
			stopTime();//线程结束
			isStart=true;
			startTime();//线程开始
		}else if(e.getSource()==getTips){
			// TODO： 提示
			Random random=new Random();
			int times=1000;//计算1000次随机点是否是雷
			boolean find=false;
			int x=0,y=0;//不是雷的点的下标
			while (times != 0){
				x=random.nextInt(buts.length);
				y=random.nextInt(buts[x].length);
				if (buts[x][y].isMine==false&&buts[x][y].isClick==false&&buts[x][y].getIcon()!=img[id][3]){
					find=true;
					break;
				}
				if (find==true)
					break;
				times--;
			}

			//如果找到了可以的点，就打开，否则，一定有红旗点是雷
			if (find==true){
				//判断是否是第一次点击
				if(isOnClick){//如果是第一次点击，开启线程开始计时
					isStart=true;//线程里的计时开关
					startTime();//开始线程的方法
				}
				//设置第一次点击为false
				isOnClick=false;

				if(counts[x][y] == 0){//周围是否有雷
					buts[x][y].isClick = true;//设置按钮被点击过
					buts[x][y].setIcon(img[id][2]);//设置这个按钮的图片为点开的图片
					moveMine(x,y);//继续打开他周围的雷区
				}else{//此按钮周围有雷
					int count=counts[x][y];//此按钮周围的雷数
					buts[x][y].setIcon(imgShare[count]);//设置此按钮为周围雷数的数字图片
					buts[x][y].isClick=true;//设置点击过

					//做是否赢了的判断
					int num=0;//标识没有被点击的按钮数
					//通过循环遍历统计没有被点击过的按钮的数量
					for (int m = 0; m < buts.length; m++) {
						for (int n = 0; n < buts[x].length; n++) {
							//当前按钮没有被点击过或者当前按钮为红旗按钮
							if(buts[m][n].isClick == false || buts[m][n].getIcon()==img[id][3]){
								num ++;
							}
						}
					}
					if(num==numbers){//没有被点击过的按钮或者红旗的按钮等于总共的雷数
						success();
					}
				}
			}
		}else if(e.getSource()==playPause){
			if(musicc.isAlive()) musicc.stop();
			else{
				musicc=new Music();
				musicc.start();
			}
		}
//		else if(e.getSource()==playPause){
//			style = "清爽";
//			exchange(1);
//		}else if(e.getSource()==style3){
//			style = "粉嫩";
//			exchange(2);
//		}else if (e.getSource()==easy) {
//			new MineFrame(14, 14, 20, 237, 121);
//			this.dispose();//释放当前窗体
//		}
		else if (e.getSource()==medium) {
			MineFrame mineFrame =  new MineFrame(17, 17, 50, 192, 100);
			mineFrame.diffcult = "中等";
			this.dispose();
		}else if (e.getSource()==hard) {
			MineFrame mineFrame =  new MineFrame(19, 25, 100, 87, 112);
			mineFrame.diffcult = "困难";
			this.dispose();
		}else if (e.getSource()==top){
			 new TopTableList().top(topFile,"");
			 this.dispose();
			//this.setVisible(false);
		}else if (e.getSource()==list){
			new TopTableList().top(topFile,"排行榜");
			this.dispose();
			//this.setVisible(false);
		}else if(e.getSource() == viewLog) {
			try {
				Runtime.getRuntime().exec("cmd /c start \" \" \"" + logFileAbsPath + "\"");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if (e.getSource() == changeBackground) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showDialog(this, "设置为背景");
			mainL.setIcon(new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()));
		}
	}

	//线程开始计时
	private void startTime() {
		timeThread=new Thread(){
			@Override
			public void run() {
				startT = new Date();
				while(isStart){
					timecont++;
					timeL.setText(timecont+"");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		timeThread.start();
		//只要不是第一次点击按刷新的时候回重新布雷
		if(!isOnClick){
			for (int i = 0; i < buts.length; i++) {
				for (int j = 0; j < buts[i].length; j++) {
					buts[i][j].setIcon(img[id][1]);
					buts[i][j].isClick=false;
					buts[i][j].isMine=false;
				}
			}
			layMine();//重新布雷
			//统计每个按钮周围8个位置的雷数
			counts = new int[row][col];
			for(int i=0; i<buts.length; i++){
				for(int j=0; j<buts[i].length; j++){
					//counts[i][j]存放当前按钮（i， j）周围的8个按钮的雷数
					counts[i][j] = counts(i, j);
				}
			}
		}
	}
	//停止线程
	private void stopTime() {
		//判断当前时间是否为空
		if(timeThread!=null){//如果有时间
			//关闭计时开关
			isStart=false;
			//停止线程
			timeThread.stop();
		}
		//设置时间为0
		timecont=0;
	}

	//改变颜色的方法
	public void exchange(int i){
		int oldId=id;
		this.id=i;
		mainL.setIcon(img[id][0]);
		getTips.setIcon(img[id][4]);
		playPause.setIcon(img[id][5]);
		style3.setIcon(img[id][6]);
		for (int a = 0; a < buts.length; a++) {
			for (int b = 0; b < buts[i].length; b++) {//buts[i].length表示放图片数组的长度
				if(!buts[a][b].isClick){
					buts[a][b].setIcon(img[id][1]);
				}
				if(buts[a][b].getIcon()==img[oldId][3]){//如果按钮是原来的一个红旗按钮
					buts[a][b].setIcon(img[id][3]);
				}
				if(buts[a][b].getIcon()==img[oldId][2]){//如果为原来的一个打开的按钮
					buts[a][b].setIcon(img[id][2]);
				}
			}
		}
	}



}
