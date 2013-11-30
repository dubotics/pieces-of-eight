

import java.awt.*;
import java.awt.image.*;
import javax.swing.JPanel;

public class ImageGraphicPanel extends JPanel {	
	
	BufferedImage bufferedImage = null;

	private static final long serialVersionUID = 1L;
	
	public ImageGraphicPanel() {
		setPreferredSize(new Dimension(50, 50));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
