

import java.awt.*;

import javax.swing.JPanel;

public class MotorGraphicPanel extends JPanel {
	
	float flp;
	float frp;
	float brp;
	float blp;
	float fld;
	float frd;
	float brd;
	float bld;
	Color color;
	

	private static final long serialVersionUID = 1L;
	
	public MotorGraphicPanel() {
		setPreferredSize(new Dimension(50, 50));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
		
		if (fld == 1f) {
			color = new Color(0, Math.min((int)(flp*1.5),255), 0);
		} else {
			color = new Color(Math.min((int)(flp*1.5),255), 0, 0);
		}
		
		g2d.setColor(color);
		g2d.drawLine(getWidth()/4+getWidth()/16, getHeight()/4+3*getHeight()/16, getWidth()/4+3*getWidth()/16, getHeight()/4+getHeight()/16);
		
		if (frd == 1f) {
			color = new Color(0, Math.min((int)(frp*1.5),255), 0);
		} else {
			color = new Color(Math.min((int)(frp*1.5),255), 0, 0);
		}
		
		g2d.setColor(color);
		g2d.drawLine(getWidth()/4+5*getWidth()/16, getHeight()/4+getHeight()/16, getWidth()/4+7*getWidth()/16, getHeight()/4+3*getHeight()/16);
		
		if (brd == 1f) {
			color = new Color(0, Math.min((int)(brp*1.5),255), 0);
		} else {
			color = new Color(Math.min((int)(brp*1.5),255), 0, 0);
		}
		
		g2d.setColor(color);
		g2d.drawLine(getWidth()/4+5*getWidth()/16, getHeight()/4+7*getHeight()/16, getWidth()/4+7*getWidth()/16, getHeight()/4+5*getHeight()/16);
		
		if (bld == 1f) {
			color = new Color(0, Math.min((int)(blp*1.5),255), 0);
		} else {
			color = new Color(Math.min((int)(blp*1.5),255), 0, 0);
		}
		
		g2d.setColor(color);
		g2d.drawLine(getWidth()/4+getWidth()/16, getHeight()/4+5*getHeight()/16, getWidth()/4+3*getWidth()/16, getHeight()/4+7*getHeight()/16);
		
	
	}
	
	public void updateData(float FLP, float FRP, float BRP, float BLP, float FLD, float FRD, float BRD, float BLD) {
		flp = FLP;
		frp = FRP;
		brp = BRP;
		blp = BLP;
		fld = FLD;
		frd = FRD;
		brd = BRD;
		bld = BLD;
	}
}
