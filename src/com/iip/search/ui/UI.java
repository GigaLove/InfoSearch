package com.iip.search.ui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.iip.search.GoalSearch;
import com.iip.search.entity.Problem;
import com.iip.search.entity.SearchResult;

public class UI {
	public static void main(String[] args) {
		JFrame uFrame = new UIFrame();
		uFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		uFrame.setVisible(true);
	}
}

class UIFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int frameWidth = 500;
	private static final int frameHeight = 530;
	private JPanel panel;			
	

	public UIFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int x = screenSize.width / 3;
		int y = screenSize.height / 5;
		setTitle("Search");
		setLocation(x, y);
		setSize(frameWidth, frameHeight);
		
		// 将界面添加到frame中
		panel = new UIPanel();
		panel.setBounds(0, 0, 500, 530);
		add(panel);
	}

}

class UIPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String RUMANNIA_MAP = "Rumania-map.txt";
	private static final String RUMANNIA_DISTANCE = "Rumania-distance.txt";
	private static final String RUMANNIA_PHOTO = "Rumania-photo.png";
	private static final String HIT_MAP = "HIT-map.txt";
	private static final String HIT_DISTANCE = "HIT-distance.txt";
	private static final String HIT_PHOTO = "HIT-photo.png";
	
	
	private int pType;
	private int sType;

	private JLabel labels[] = { new JLabel("问      题："),
			new JLabel("搜索策略："), new JLabel("DLS深度："), 
			new JLabel("出发地："), new JLabel("目的地："), 
			new JLabel("搜索过程: ") };
	private JRadioButton[] radioButtons = { new JRadioButton("罗马尼亚问题"), 
			new JRadioButton("哈工大校园问题"), new JRadioButton("BFS"), 
			new JRadioButton("IDS"), new JRadioButton("DLS"), new JRadioButton("A* Search")};
	private ButtonGroup problemGroup = new ButtonGroup();
	private ButtonGroup sTypeGroup = new ButtonGroup(); 
	private JButton confirmButton = new JButton(" 确定 ");
	private JTextField limitTextField = new JTextField(20);
	private JTextField startTextField = new JTextField(20);
	private JTextField endTextField = new JTextField(20);
	private JTextArea outputTextArea = new JTextArea();
	private JScrollPane jp = new JScrollPane(outputTextArea);
	/**
	 * 初始化界面
	 * @param imageShare
	 */
	public UIPanel() {
		setBounds(new Rectangle(0, 0, 500, 530));
		setLayout(null);
		
		for (JLabel label : labels) {
			this.add(label);
		}
		labels[0].setBounds(40, 20, 100, 20);
		labels[1].setBounds(40, 60, 100, 20);
		labels[2].setBounds(40, 100, 100, 20);
		labels[3].setBounds(40, 140, 100, 20);
		labels[4].setBounds(40, 180, 100, 20);
		labels[5].setBounds(40, 220, 100, 20);
		
		for (JRadioButton rb : radioButtons) {
			this.add(rb);
			rb.addActionListener(this);
		}
		
		radioButtons[0].setBounds(150, 20, 120, 20);
		radioButtons[1].setBounds(300, 20, 120, 20);
		radioButtons[2].setBounds(120, 60, 80, 20);
		radioButtons[3].setBounds(200, 60, 80, 20);
		radioButtons[4].setBounds(280, 60, 80, 20);
		radioButtons[5].setBounds(360, 60, 100, 20);
		
		problemGroup.add(radioButtons[0]);
		problemGroup.add(radioButtons[1]);
		sTypeGroup.add(radioButtons[2]);
		sTypeGroup.add(radioButtons[3]);
		sTypeGroup.add(radioButtons[4]);
		sTypeGroup.add(radioButtons[5]);
		
		this.add(confirmButton);
		this.add(limitTextField);
		this.add(jp);
		this.add(startTextField);
		this.add(endTextField);
		
		limitTextField.setBounds(130, 100, 250, 20);
		startTextField.setBounds(130, 140, 250, 20);
		endTextField.setBounds(130, 180, 250, 20);
		jp.setBounds(40, 240, 400, 200);
		
		confirmButton.addActionListener(this);
		confirmButton.setBounds(200, 450, 100, 20);
		
	}

	// 按钮监听事件
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == radioButtons[0]) {
			pType = 0;
			openPhoto(RUMANNIA_PHOTO);
		}
		else if (e.getSource() == radioButtons[1]) {
			pType = 1;
			openPhoto(HIT_PHOTO);
		}
		else if (e.getSource() == radioButtons[2]) {
			sType = 0;
		}
		else if (e.getSource() == radioButtons[3]) {
			sType = 1;
		}
		else if (e.getSource() == radioButtons[4]) {
			sType = 2;
		}
		else if (e.getSource() == radioButtons[5]) {
			sType = 3;
		}
		else {
			outputTextArea.setText("");
			String startStr = startTextField.getText().trim();
			String endStr = endTextField.getText().trim();
			SearchResult searchResult = null;
			
			if (startStr == null || startStr.equals("")) {
				JOptionPane.showMessageDialog(UIPanel.this, "请输入出发地");
				return;
			}			
			if (endStr == null || endStr.equals("")) {
				JOptionPane.showMessageDialog(UIPanel.this, "请输入目的地");
				return;
			}
			
			if (pType == 0) {
				searchResult = search(RUMANNIA_MAP, startStr, endStr, RUMANNIA_DISTANCE, 
						RUMANNIA_PHOTO);
			}
			else {
				searchResult = search(HIT_MAP, startStr, endStr, HIT_DISTANCE, 
						HIT_PHOTO);
			}
			
			if (searchResult != null) {
				outputTextArea.append(searchResult.toString());
			}
		}
	}
	
	private SearchResult search(String pStr, String startStr, String endStr, String assistStr, 
			String photoStr) {
		Problem p = new Problem(pStr, startStr, endStr);
		p.readProblem();
		
		if (!p.getNodeMap().containsKey(startStr)) {
			JOptionPane.showMessageDialog(UIPanel.this, "请输入有效出发地");
			return null;
		}			
		if (!p.getNodeMap().containsKey(endStr)) {
			JOptionPane.showMessageDialog(UIPanel.this, "请输入有效目的地");
			return null;
		}
		
		SearchResult searchResult = null;
		int DLSLimit = 10;
		
		switch(sType) {
		case 0:
			searchResult = GoalSearch.BFS(p);
			break;
		case 1:
			searchResult = GoalSearch.IDS(p);
			break;
		case 2:
			
			try {
				DLSLimit = Integer.parseInt(limitTextField.getText().trim()); 
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
				JOptionPane.showMessageDialog(UIPanel.this, "请输入有效数字");
				return null;
			}
			
			searchResult = GoalSearch.DLS(p, DLSLimit);
			break;
		case 3:
			searchResult = GoalSearch.AStarSearch(p, assistStr);
			break;
		default:
			break;
		}
		return searchResult;
	}
	
	private void openPhoto(String fileName) {
		Desktop desk = Desktop.getDesktop();
		File file = new File(fileName);
		try {
			desk.open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
}

