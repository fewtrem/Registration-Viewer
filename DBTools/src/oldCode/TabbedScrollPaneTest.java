package oldCode;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class TabbedScrollPaneTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame theFrame = new JFrame();
		theFrame.setLayout(null);
		theFrame.setMinimumSize(new Dimension(100,100));
		theFrame.getContentPane().setBackground(Color.BLUE);
		JPanel theSubPanel = new JPanel();
		//theSubPanel.setLocation(0,0);
		//theSubPanel.setSize(100,100);
		theSubPanel.setLayout(null);
		theSubPanel.setPreferredSize(new Dimension(1000,200));
		//JPanel thePanel = new JPanel();
		Button theButton = new Button();
		theButton.setLocation(0,0);
		theButton.setSize(new Dimension(1000,50));
		theSubPanel.add(theButton);
		//thePanel.setSize(new Dimension(1000,50));
		//thePanel.setLocation(0,0);
		//thePanel.setBackground(Color.RED);
		//theSubPanel.add(thePanel);
		JPanel thePanel2 = new JPanel();
		//thePanel2.setSize(new Dimension(100,50));
		//thePanel2.setLocation(0,50);
		thePanel2.setBounds(0,50,1000,50);
		thePanel2.setBackground(Color.GREEN);
		theSubPanel.add(thePanel2);
		
		
		JScrollPane sp = new JScrollPane(theSubPanel);
		//sp.setPreferredSize(new Dimension(200,200));
		sp.setBounds(0,0,200,200);
		JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.add(sp);
		jp.setBounds(0,0,200,200);
		JTabbedPane yy = new JTabbedPane();
		ImageIcon icon = new ImageIcon();
		yy.addTab("D",icon,jp,"Dss");
		JPanel jp2 = new JPanel();
		jp2.setLayout(null);
		jp2.add(yy);
		jp2.setBounds(0,0,200,200);
		yy.setBounds(0,0,200,200);
		theFrame.add(jp2);
		theFrame.setVisible(true);
	}

}
