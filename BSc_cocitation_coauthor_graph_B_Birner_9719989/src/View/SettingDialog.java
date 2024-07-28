package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import Model.PropertiesCache;

public class SettingDialog extends JDialog {

	
	private JRadioButton windowsR;
    private JRadioButton linuxMacR;
    private JRadioButton jaConR;
    private JRadioButton neinConR;
    private JRadioButton jaSplitR;
    private JRadioButton neinSplitR;
    private JRadioButton jaMultiR;
    private JRadioButton neinMultiR;
    private JButton exitB;
    private JButton okB;
	
	
    /**
	 * initializes all components and adds the components to the <code>JDialog<code>.
	 */
	public SettingDialog() {
		
		
		setModal(true);
		
		//creates the basic Panel
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		
		
		
		
		JPanel sysP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		sysP.setBorder(BorderFactory.createEmptyBorder(12,20,0,20));
		
		JPanel sysRP = new JPanel();
		sysRP.setLayout(new BoxLayout(sysRP, BoxLayout.Y_AXIS));
		
		JLabel sys = new JLabel("Betriebssystem:");
		windowsR = new JRadioButton("Windows");
		linuxMacR = new JRadioButton("Linux/Mac");
		ButtonGroup group1 = new ButtonGroup();
		group1.add(windowsR);
		group1.add(linuxMacR);
		
		sysP.add(sys, BorderLayout.WEST);
		sysRP.add(windowsR);
		sysRP.add(linuxMacR);
		sysP.add(Box.createRigidArea(new Dimension(165,0)));	
		sysP.add(sysRP, BorderLayout.EAST);
		
		
		
		
		JPanel conP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		conP.setBorder(BorderFactory.createEmptyBorder(12,20,0,20));
		
		JPanel conRP = new JPanel();
		conRP.setLayout(new BoxLayout(conRP, BoxLayout.Y_AXIS));
		
		JLabel con = new JLabel("Konsolidierung:");
		jaConR = new JRadioButton("ja");
		neinConR = new JRadioButton("nein");
		ButtonGroup group2 = new ButtonGroup();
		group2.add(jaConR);
		group2.add(neinConR);
		
		conP.add(con, BorderLayout.WEST);
		conRP.add(jaConR);
		conRP.add(neinConR);
		conP.add(Box.createRigidArea(new Dimension(171,0)));	
		conP.add(conRP, BorderLayout.EAST);
		
		
		
		
		JPanel splitP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		splitP.setBorder(BorderFactory.createEmptyBorder(12,20,0,20));
		
		JPanel splitRP = new JPanel();
		splitRP.setLayout(new BoxLayout(splitRP, BoxLayout.Y_AXIS));
		
		JLabel split = new JLabel("Split");
		jaSplitR = new JRadioButton("ja");
		neinSplitR = new JRadioButton("nein");
		ButtonGroup group3 = new ButtonGroup();
		group3.add(jaSplitR);
		group3.add(neinSplitR);
		
		//gets the information which system is used
		PropertiesCache prop = PropertiesCache.getInstance();
		String syst = prop.getProperty("system");
		int s = 225;
		if (syst.equals("linux/mac")) {s = 237;}
		
		splitP.add(split, BorderLayout.WEST);
		splitRP.add(jaSplitR);
		splitRP.add(neinSplitR);
		splitP.add(Box.createRigidArea(new Dimension(s,0)));	
		splitP.add(splitRP, BorderLayout.EAST);
		
		
		
		
		
		
		JPanel multiP = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		multiP.setBorder(BorderFactory.createEmptyBorder(12,20,0,20));
		
		JPanel multiRP = new JPanel();
		multiRP.setLayout(new BoxLayout(multiRP, BoxLayout.Y_AXIS));
		
		JLabel multi = new JLabel("Multithreading");
		jaMultiR = new JRadioButton("ja");
		neinMultiR = new JRadioButton("nein");
		ButtonGroup group4 = new ButtonGroup();
		group4.add(jaMultiR);
		group4.add(neinMultiR);
		
		s = 177;
		if (syst.equals("linux/mac")) {s = 175;}
		
		multiP.add(multi, BorderLayout.WEST);
		multiRP.add(jaMultiR);
		multiRP.add(neinMultiR);
		multiP.add(Box.createRigidArea(new Dimension(s,0)));	
		multiP.add(multiRP, BorderLayout.EAST);
		
		
		
		
		
		
		setRadioButtons();
		
		
		
		JPanel buttonP = new JPanel();
		buttonP.setLayout(new BoxLayout(buttonP, BoxLayout.X_AXIS));
		buttonP.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		okB = new JButton("OK");
		exitB = new JButton("Abbruch");
		
		buttonP.add(okB);
		buttonP.add(Box.createRigidArea(new Dimension(100,0)));	
		buttonP.add(exitB);
		
		basic.add(sysP);
		basic.add(conP);
		basic.add(splitP);
		basic.add(multiP);
		basic.add(buttonP);
		
		//adds the basic-Panel to the JDialog
		add(basic);
		
		
		/*
		 *sets the Dialog settings 
		 */
		setTitle ("Neue Analyse");
		setSize(400,375);
		setResizable(false);
		setLocationRelativeTo(null);                    
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	
	/**
	 * gets the selected system
	 * 
	 * @return "w" if Windows is selected and "lm" if Linux/Mac is selected. If nothing is selected then it returns null.
	 */
	public String getSystem() {
		if(windowsR.isSelected()) {
			return "w";
		}
		if(linuxMacR.isSelected()) {
			return "lm";
		}
		return null;
	}
	
	
	
	/**
	 * gets the consolidation
	 * 
	 * @return "j" if consolidation is selected and "n" if no consolidation is selected. If nothing is selected then it returns null.
	 */
	public String getConsolidation() {
		if(jaConR.isSelected()) {
			return "j";
		}
		if(neinConR.isSelected()) {
			return "n";
		}
		return null;
	}
	
	
	
	/**
	 * gets the split
	 * 
	 * @return "j" if split is selected and "n" if no split is selected. If nothing is selected then it returns null.
	 */
	public String getSplit() {
		if(jaSplitR.isSelected()) {
			return "j";
		}
		if(neinSplitR.isSelected()) {
			return "n";
		}
		return null;
	}
	
	
	
	/**
	 * gets the multithreading
	 * 
	 * @return "j" if multithreading is selected and "n" if no multithreading is selected. If nothing is selected then it returns null.
	 */
	public String getMulti() {
		if(jaMultiR.isSelected()) {
			return "j";
		}
		if(neinMultiR.isSelected()) {
			return "n";
		}
		return null;
	}
	
	
	
	/**
	 * This method sets the radio-buttons
	 */
	private void setRadioButtons() {
		
		PropertiesCache prop = PropertiesCache.getInstance();
		String sys = prop.getProperty("system");
		String con = prop.getProperty("consolidation");
		String split = prop.getProperty("split");
		String multi = prop.getProperty("multi");
		
		if( sys != null) {
			if( sys.equals("windows")) {
				windowsR.setSelected(true);
			}else {
				linuxMacR.setSelected(true);
			}
		}
		
		if( con != null) {
			if( con.equals("on")) {
				jaConR.setSelected(true);
			}else {
				neinConR.setSelected(true);
			}
		}
		
		if( split != null) {
			if( split.equals("on")) {
				jaSplitR.setSelected(true);
			}else {
				neinSplitR.setSelected(true);
			}
		}
		
		if( multi != null) {
			if( multi.equals("on")) {
				jaMultiR.setSelected(true);
			}else {
				neinMultiR.setSelected(true);
			}
		}
	}
	
	

	/**
	 * adds a listener to the ok-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setOkButtonListener(ActionListener listener) {  
		okB.addActionListener(listener);
	}
	
	
	
	/**
	 * adds a listener to the exit-Button.
	 * 
	 * @param listener the listener that should be added.
	 */
	public void setExitButtonListener(ActionListener listener) {  
		exitB.addActionListener(listener);
	}
	
	
	
}
