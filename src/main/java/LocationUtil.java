
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public class LocationUtil implements MouseListener,MouseMotionListener{
	JFrame frame;
	//鼠标的开始坐标
	private int xx,yy;
	private boolean isDraging=false;
	//构造方法初始化监听
	public LocationUtil(JFrame frame) {
		this.frame=frame;
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
	}
	//按压并拖动时触发
	@Override
	public void mouseDragged(MouseEvent e) {
		if(isDraging){
			//获取窗体移动的那一瞬间的位置
			int left=frame.getLocation().x;
			int right=frame.getLocation().y;
			frame.setLocation(left+e.getX()-xx, right+e.getY()-yy);
		}
	}

	//@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	//@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	//按的动作
	//@Override
	public void mousePressed(MouseEvent e) {
		isDraging=true;
		xx=e.getX();//鼠标正要移动时的位置
		yy=e.getY();
	}
	//松的动作
	//@Override
	public void mouseReleased(MouseEvent e) {
		isDraging=false;
	}

	//@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	//@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
