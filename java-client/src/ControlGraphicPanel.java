

import java.awt.*;
import javax.swing.JPanel;

public class ControlGraphicPanel extends JPanel {
	
	float x;
	float y;
	float rot;
	

	private static final long serialVersionUID = 1L;
	
	public ControlGraphicPanel() {
		setPreferredSize(new Dimension(50, 50));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2);
		g.setColor(Color.RED);
		g.drawLine(getWidth()/2, getHeight()/2, (int) (getWidth()/2+x*getWidth()/4), getHeight()/2);
		g.setColor(Color.BLUE);
		g.drawLine(getWidth()/2, getHeight()/2, getWidth()/2, (int) (getHeight()/2-y*getHeight()/4));
		g.setColor(Color.GREEN);
		g.drawArc(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 90, (int) (rot*-90));
	}
	
	public void updateData(float leftXBoxX, float leftXBoxY, float rightXBoxX) {
		x = leftXBoxX;
		y = leftXBoxY;
		rot = rightXBoxX;
	}

}
