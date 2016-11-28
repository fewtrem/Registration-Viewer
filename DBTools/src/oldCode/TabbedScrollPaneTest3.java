package oldCode;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class TabbedScrollPaneTest3 {
	Dimension theDimension;
	/**
	 * @param args
	 */
	JFrame theFrame;
	public TabbedScrollPaneTest3(){
		theDimension =new Dimension(200,200);
		// Uncomment to get views for well positioned components...
		// A for inside panel, B for outside frame.
		theFrame = new JFrame();
		
		//B theFrame.setLayout(null);
		theFrame.setMinimumSize(new Dimension(100,100));
		theFrame.getContentPane().setBackground(Color.BLUE);
		JPanel theSubPanel = new JPanel();
		//A theSubPanel.setLayout(null);
		//A theSubPanel.setPreferredSize(new Dimension(1000,200));
		
		// On the subpanel
		Button theButton = new Button("dd");
		theButton.setLocation(0,0);
		theButton.setSize(new Dimension(1000,50));
		theSubPanel.add(theButton);
		
		JPanel thePanel2 = new JPanel();
		//A thePanel2.setBounds(0,50,1000,50);
		thePanel2.setPreferredSize(new Dimension(1000,50));
		thePanel2.setBackground(Color.GREEN);
		theSubPanel.add(thePanel2);
		System.out.println(theSubPanel.getPreferredSize());
		
		JScrollPane sp = new JScrollPane(theSubPanel);
		
		sp.setPreferredSize(theDimension);
		theDimension.setSize(400,50);
		//JPanel jp = new JPanel();
		//jp.add(sp);
		//System.out.println(jp.getPreferredSize());
		JTabbedPane yy = new JTabbedPane();
		ImageIcon icon = new ImageIcon();
		yy.addTab("D",icon,sp,"Dss");
		System.out.println(yy.getPreferredSize());
		//This JPanl will fit around the preferred size...
		//JPanel jp2 = new JPanel();
		//jp2.add(yy);
		//jp2.setPreferredSize(new Dimension(200,200));
		//B jp2.setSize(jp2.getPreferredSize());
		//B jp2.setLocation(0,0);
		JPanel fullFramePanel = new JPanel();
		fullFramePanel.setLayout(new BorderLayout());
		fullFramePanel.add(yy,BorderLayout.EAST);
		fullFramePanel.add(new Button("DD"),BorderLayout.WEST);
		fullFramePanel.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				theDimension.setSize(theFrame.getWidth()-200,theFrame.getHeight());
			}
		});
		theFrame.add(fullFramePanel);
		theFrame.setVisible(true);
	}
	public static void main(String[] args) {
		TabbedScrollPaneTest3 g = new TabbedScrollPaneTest3();
	}

}
