package oldCode;
import ij.IJ;

import java.awt.Button;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class LayoutTests {
	/**
	 * @param args
	 */
	JList<String> SecondList;
	String[] array1 = {"loo papier","bob roll"};
	String[] array2 = {"tip ek", "pritt stick"};
	JList<Integer> theComponentc;
	public static void main(String[] args) {
		LayoutTests hugger = new LayoutTests();
	}
	
	public void updateList(){
		
		
	}
	public LayoutTests(){
		// TODO Auto-generated method stub
		
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//For each component to be added to this container:
		//...Create the component...
		TextArea theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS A");
		//...Set instance variables in the GridBagConstraints instance...
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(theComponent, c);
		theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS B");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(theComponent, c);
		theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS C");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		pane.add(theComponent, c);
		theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS D");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(theComponent, c);
		theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS E");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		pane.add(theComponent, c);
		theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS F");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		pane.add(theComponent, c);
		theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS G");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(theComponent, c);
		theComponent = new TextArea("WOOTY\n\n WOOT\n\n CLICKS H");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		pane.add(theComponent, c);
		LinkedList<String,Integer> HowardsList = new LinkedList<String,Integer>();
		HowardsList.put("PopulateArray1",3);
		HowardsList.put("PopulateArray2",4);
		theComponentc = new JList<Integer>(HowardsList);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(theComponentc, c);

		SecondList = new JList<String>();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		theComponentc.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				System.out.println(e.toString());
				if (e.getValueIsAdjusting() == true){
					if (theComponentc.getSelectedIndex()==0){
						SecondList.setListData(array1);
					}
					if (theComponentc.getSelectedIndex()==1){
						SecondList.setListData(array2);
					}
				}
				System.out.println("D"+theComponentc.getSelectedValue());
			}
		});
		pane.add(SecondList, c);
		Button gooey = new Button("SelectNone");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
	    gooey.addActionListener (new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		theComponentc.clearSelection();
	    	}
	    });
	    pane.add(gooey,c);
	    
	    JPanel paneff = new JPanel();
	    JLabel testLabel = new JLabel("E");
	    testLabel.setOpaque(true);
	    testLabel.setBackground(Color.BLUE);
	    paneff.setOpaque(true);
	    paneff.setBackground(Color.GREEN);
	    paneff.setPreferredSize(new Dimension(1000,1000));
		JScrollPane gooey2 = new JScrollPane();
	    paneff.add(testLabel);
		gooey2.setOpaque(true);
		gooey2.setBackground(Color.BLACK);
		gooey2.setPreferredSize(new Dimension(400,250));
		gooey2.add(paneff);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 3;
	    pane.add(gooey2,c);

		//theComponentc.setListData();
		JFrame testProg = new JFrame("Test Prog");
		JScrollPane scrollpane = new JScrollPane(pane);
		testProg.add(scrollpane);
		//testProg.add(theComponent);
		testProg.setMinimumSize(new Dimension(400,400));
		testProg.setBackground(Color.RED);
		testProg.show();
	}
}
