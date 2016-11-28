package oldCode;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;

public class JListTest {
	ArrayList<String> stringList;
	JList<String> testList;
	Button testButton;
	JFrame testFrame;
	/**
	 * @param args
	 */
	public JListTest(){
		testFrame = new JFrame ("TestJList Updates");
		testFrame.setLayout(new BorderLayout());
		stringList = new ArrayList<String>();
		stringList.add("DDssD");
		testList = new JList<String>();
		testList.setListData(stringList.toArray(new String[stringList.size()]));
		testButton = new Button("FO");
		stringList.add("DDDsdsd");
		testButton.addActionListener (new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		doit();
	    	}
	    });
		testFrame.add(testList,BorderLayout.NORTH);
		System.out.println(testList.getPreferredSize());
		testList.setMinimumSize(testList.getPreferredSize());
		testFrame.add(testButton,BorderLayout.CENTER);
		testFrame.pack();
		testFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		JListTest go = new JListTest();
		System.out.println(""+go.hashCode());
	}
	public void doit(){
		stringList.clear();
	}

}
