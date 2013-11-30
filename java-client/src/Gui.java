

import java.awt.*;
import javax.swing.*;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;
	static final boolean ENABLE_GRAPHICS = false;

	JPanel panelNorth, panelWest, panelEast, panelSouth, panelCenter; //Primary Panels
	JPanel panelNorthLeft, panelNorthMiddle, panelNorthRight, panelWestLeft, panelWestRight, panelEastLeft, panelEastRight, panelCenterLeft, panelCenterMiddle, panelCenterRight; //Secondary Panels

	TextField strafeXField, strafeYField, rotField, fireField, FLMPField, FRMPField, BLMPField,
			BRMPField, intakeDirectionField, intakeField, killField, fieldOrientField, gyroField;

	JLabel xBoxLabel, motorLabel, statusLabel, UILabel, UILabel2, pictureLabel;
	
	ControlGraphicPanel controlGraphicPanel;
	MotorGraphicPanel motorGraphicPanel;
	ImageGraphicPanel imageGraphicPanel, image2GraphicPanel;
	
	ImageIcon icon = new ImageIcon("src\\images\\logo2.gif");
	
	Packet packet;

	public Gui(int width, int height, Packet packet) {
		
		//Sets frame properties
		this.packet = packet;
		setTitle("U.W. DawgBytes 2012");
		setSize(width, height);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		
		//Four panels for different regions
		panelNorth = new JPanel(new GridLayout(0, 3));
		panelWest = new JPanel(new GridLayout(0, 2));
		panelEast = new JPanel(new GridLayout(0, 2));
		panelCenter = new JPanel(new GridLayout(0, 3));
		
		//Seperate main regions to seperate areas
		panelNorthLeft = new JPanel(new GridLayout(0,1));
		panelNorthMiddle = new JPanel(new GridLayout(0,1));
		panelNorthRight = new JPanel(new GridLayout(0,1));
		panelNorth.add(panelNorthLeft);
		panelNorth.add(panelNorthMiddle);
		panelNorth.add(panelNorthRight);
		
		panelWestLeft = new JPanel(new GridLayout(0, 1));
		panelWestRight = new JPanel(new GridLayout(0, 1));
		panelWest.add(panelWestLeft);
		panelWest.add(panelWestRight);
		
		if (ENABLE_GRAPHICS) {
			controlGraphicPanel = new ControlGraphicPanel();	
			panelWestLeft.add(controlGraphicPanel);
		}
		
		panelEastLeft = new JPanel(new GridLayout(0, 1));
		panelEastRight = new JPanel(new GridLayout(0, 1));
		panelEast.add(panelEastLeft);
		panelEast.add(panelEastRight);
		
		if (ENABLE_GRAPHICS) {
			motorGraphicPanel = new MotorGraphicPanel();	
			panelEastRight.add(motorGraphicPanel);
		}
		
		panelCenterLeft = new JPanel(new GridLayout(0, 1));
		panelCenterMiddle = new JPanel(new GridLayout(0, 1));
		panelCenterRight = new JPanel(new GridLayout(0, 1));
		panelCenter.add(panelCenterLeft);
		panelCenter.add(panelCenterMiddle);
		panelCenter.add(panelCenterRight);

		// UW Labels - Labels at top for program info
		
		imageGraphicPanel = new ImageGraphicPanel();
		imageGraphicPanel.setPreferredSize(new Dimension(150, 150));
		pictureLabel = new JLabel();
		pictureLabel.setIcon(icon);
		imageGraphicPanel.add(pictureLabel);
		panelNorthLeft.add(imageGraphicPanel);
		
		addSpaces(panelNorthMiddle, 1);
		
		UILabel = new JLabel("U.W. DawgBytes 2012");
		UILabel.setHorizontalAlignment(JLabel.CENTER);
		panelNorthMiddle.add(UILabel);

		UILabel2 = new JLabel("GROUND VEHICLE UI");
		UILabel2.setHorizontalAlignment(JLabel.CENTER);
		UILabel2.setAlignmentX(CENTER_ALIGNMENT);
		panelNorthMiddle.add(UILabel2);
		
		addSpaces(panelNorthMiddle, 1);
		
		image2GraphicPanel = new ImageGraphicPanel();
		image2GraphicPanel.setPreferredSize(new Dimension(150, 150));
		pictureLabel = new JLabel();
		pictureLabel.setIcon(icon);
		image2GraphicPanel.add(pictureLabel);
		panelNorthRight.add(image2GraphicPanel);
		
		// Gampad Labels and Fields - Labels and Text Fields at left for controller info

		xBoxLabel = new JLabel("XBox Controller:");
		xBoxLabel.setHorizontalAlignment(JLabel.CENTER);
		panelWestRight.add(xBoxLabel);

		strafeXField = new TextField(20);
		strafeXField.setText("Strafe X: ");
		strafeXField.setEditable(false);
		panelWestRight.add(strafeXField);

		strafeYField = new TextField(20);
		strafeYField.setText("Strafe Y: ");
		strafeYField.setEditable(false);
		panelWestRight.add(strafeYField);

		rotField = new TextField(20);
		rotField.setText("Rotation: ");
		rotField.setEditable(false);
		panelWestRight.add(rotField);

		fireField = new TextField(20);
		fireField.setText("Fire: ");
		fireField.setEditable(false);
		panelWestRight.add(fireField);

		intakeField = new TextField(20);
		intakeField.setText("Intake: ");
		intakeField.setEditable(false);
		panelWestRight.add(intakeField);

		intakeDirectionField = new TextField(20);
		intakeDirectionField.setText("Intake Reverse: ");
		intakeDirectionField.setEditable(false);
		panelWestRight.add(intakeDirectionField);
		
		addSpaces(panelWestRight, 1);

		// Motor Labels and Fields - Labels and Fields  at right for motor info
		
		motorLabel = new JLabel("Drive Motors:");
		motorLabel.setHorizontalAlignment(JLabel.CENTER);
		panelEastLeft.add(motorLabel);

		FLMPField = new TextField(20);
		FLMPField.setText("FL: ");
		FLMPField.setEditable(false);
		panelEastLeft.add(FLMPField);	

		FRMPField = new TextField(20);
		FRMPField.setText("FR: ");
		FRMPField.setEditable(false);
		panelEastLeft.add(FRMPField);

		BRMPField = new TextField(20);
		BRMPField.setText("BR: ");
		BRMPField.setEditable(false);
		panelEastLeft.add(BRMPField);
		
		BLMPField = new TextField(20);
		BLMPField.setText("BL: ");
		BLMPField.setEditable(false);
		panelEastLeft.add(BLMPField);
		
		addSpaces(panelEastLeft, 3);

		// Status Label and Fields  - Labels and Fields  at center for status info
		
		statusLabel = new JLabel("Status: ");
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		panelCenterMiddle.add(statusLabel);
		
		killField = new TextField(20);
		killField.setText("Kill: ");
		killField.setEditable(false);
		panelCenterMiddle.add(killField);
		
		fieldOrientField = new TextField(20);
		fieldOrientField.setText("FieldOrient: ");
		fieldOrientField.setEditable(false);
		panelCenterMiddle.add(fieldOrientField);
		
		gyroField = new TextField(20);
		gyroField.setText("Gyro: ");
		gyroField.setEditable(false);
		panelCenterMiddle.add(gyroField);
		
		addSpaces(panelCenterMiddle, 4);

		add(panelNorth, BorderLayout.NORTH);
		add(panelWest, BorderLayout.WEST);
		add(panelEast, BorderLayout.EAST);
		add(panelCenter, BorderLayout.CENTER);
		
		//finalize everything and show
		pack();
		setVisible(true);
	}
	
	//updates all text field information
	void update() {
		strafeXField.setText("Strafe X: " + packet.leftXBoxX);
		strafeYField.setText("Strafe Y: " + packet.leftXBoxY);
		rotField.setText("Rotation: " + packet.rightXBoxX);
		fireField.setText("Fire: " + packet.feed);
		FLMPField.setText("FL: " + packet.MP[0]);
		FRMPField.setText("FR: " + packet.MP[1]);
		BRMPField.setText("BR: " + packet.MP[2]);
		BLMPField.setText("BL: " + packet.MP[3]);
		intakeField.setText("Intake: " + packet.intake);
		intakeDirectionField.setText("Intake Reverse: " + packet.intakeDirection);
		killField.setText("Kill: " + packet.kill);
		fieldOrientField.setText("Field Orient: " + packet.fieldOrient);
		gyroField.setText("Gyro: "); //TODO: Read Gyro value
		
		if (ENABLE_GRAPHICS) {
			controlGraphicPanel.updateData(packet.leftXBoxX, packet.leftXBoxY, packet.rightXBoxX);
			controlGraphicPanel.repaint(10);
			motorGraphicPanel.updateData(packet.MP[0], packet.MP[1], packet.MP[2], packet.MP[3], Math.abs(packet.MP[0])/packet.MP[0], Math.abs(packet.MP[1])/packet.MP[1], Math.abs(packet.MP[2])/packet.MP[2], Math.abs(packet.MP[3])/packet.MP[3]);
			motorGraphicPanel.repaint(10);
		}
	}
	
	//adds blank labels for layout spacing
	void addSpaces(JPanel panel, int spaces) {
		for (int i=0; i<spaces; i++) {
			JLabel l = new JLabel();
			panel.add(l);
		}
	}

}
