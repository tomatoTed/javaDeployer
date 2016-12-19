package com.gx.javaDeployer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


/**
 * 窗口组件
 */
public class UIComponent {
	private JFrame frame;//窗口
	private Container contentPane;//主要容器
	private JPanel conainer;//内容区域主要容器
	private JTextArea outputText;//文本区域
	private JLabel textLabel;//底部状态栏
	private boolean uploading=false;
	private int deployType=1;//部署类型,1整包部署,2增量部署
	private Listener listener;
	Object[][] data;
	private JButton button;
	private JTable table;
	private static final Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
	private Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
	public UIComponent() {
		frame = new JFrame("远程部署工具 v2.0");
		frame.setIconImage(new ImageIcon(this.getClass().getResource("icon.jpg")).getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		contentPane=frame.getContentPane();
		addMenuButton();
		addMainConainer();
		addToolBar();
		showArea();
	}

	public void setListener(Listener listener) {
		this.listener = listener;
		try {
			listener.onLaunch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean getUploading(){
		return this.uploading;
	}
	private void addMainConainer(){
		conainer=new JPanel(new BorderLayout());
		contentPane.add(conainer,BorderLayout.CENTER);
	}

	public int getDeployType() {
		return deployType;
	}

	/**
	 * 添加菜单
	 * @author gaoxiang @date 2016年11月4日
	 */
	private void addMenuButton() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JRadioButton radio1 = new JRadioButton("整包部署");// 创建单选按钮
		radio1.setSelected(true);
		radio1.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(uploading){
					return;
				}
				deployType=e.getStateChange();
				if(listener!=null){
					try {
						listener.onRadioChange(deployType);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		JRadioButton radio2 = new JRadioButton("增量部署");// 创建单选按钮
		ButtonGroup group = new ButtonGroup();// 创建单选按钮组
		group.add(radio1);// 将radioButton1增加到单选按钮组中
		group.add(radio2);// 将radioButton2增加到单选按钮组中
		toolBar.add(radio1);
		toolBar.add(radio2);
		contentPane.add(toolBar, BorderLayout.NORTH);
		button=new JButton("开始部署");
		button.setBackground(new Color(97, 255, 105));
		toolBar.add(button);


		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(uploading){return;}
				uploading=true;
				button.setBackground(new Color(153, 153, 153));
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									setText("开始部署");
								}
							});
							if(listener!=null){
								if(deployType==1){
									listener.onStart();
								}else if(deployType==2){
									int row = 0;
									data = new Object[table.getRowCount()][3];
									while (row < table.getRowCount()) {
										data[row][0] = table.getModel().getValueAt(row, 0);
										data[row][1] = table.getModel().getValueAt(row, 1).toString();
										row++;
									}
									listener.onSelectedStart();
								}
							}
							uploading=false;
							button.setBackground(new Color(97, 255, 105));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}).start();
			}
		});
	}
	/**
	 * 添加底部状态栏
	 * @author gaoxiang @date 2016年11月4日
	 */
	private void addToolBar(){
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		textLabel = new JLabel("初始化中...");
		toolBar.add(textLabel);
		toolBar.setBackground(new Color(222, 140, 102));
		contentPane.add(toolBar,BorderLayout.SOUTH);
	}
	/**
	 * 显示文本域
	 * @author gaoxiang @date 2016年11月2日
	 */
	public void showArea(){
		clean();
		deployType=1;
		outputText = new JTextArea(20, 100);
		outputText.setLineWrap(true);//激活自动换行功能
		outputText.setAutoscrolls(true);//自动滚动
		outputText.setBackground(new Color(239, 240, 220));
		conainer.add(new JScrollPane(outputText),BorderLayout.CENTER);
		frame.pack();
		resetPosition();
		frame.setVisible(true);
	}
	/**
	 * 显示列表
	 * @author gaoxiang @date 2016年11月2日
	 */
	public void showTable(){
		clean();
		String[] columnNames = {"部署","源码文件"};
		DefaultTableModel model = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int col)	{
				return getValueAt(0, col).getClass();
			}
		};

		table = new JTable(model); // 用数据模型创建JTable，JTable会自动监听到数据模型中的数据改变并显示出来
		model.setDataVector(data, columnNames);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.getColumnModel().getColumn(1).setMaxWidth(1500);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		conainer.add(scrollPane);
		frame.pack();
		resetPosition();
		frame.setVisible(true);
	}
	/**
	 * 追加content
	 * @author gaoxiang @date 2016年8月23日
	 */
	public void appendText(final String text){
		if(outputText==null){return;}
		outputText.append(text+"\n");
		outputText.setCaretPosition(outputText.getText().length());
	}
	/**
	 * 设置title
	 * @author gaoxiang @date 2016年8月23日
	 */
	public void setText(final String text){
		if(textLabel==null){return;}
		textLabel.setText(text);
	}
	/**
	 * 设置title
	 * @author gaoxiang @date 2016年8月23日
	 */
	public void setTextAsync(final String text){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setText(text);
			}
		});
	}
	public void resetPosition(){
		int windowWidth = frame.getWidth(); // 获得窗口宽
		int windowHeight = frame.getHeight(); // 获得窗口高
		int screenWidth = screenSize.width; // 获取屏幕的宽
		int screenHeight = screenSize.height; // 获取屏幕的高
		//System.out.println("windowWidth="+windowWidth+",windowHeight="+windowHeight+",screenWidth="+screenWidth+"screenHeight="+screenHeight);
		frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);// 设置窗口居中显示
	}
	public void clean(){
		conainer.removeAll();
	}
	public static void main(String[] args) throws InterruptedException {
		UIComponent ui=new UIComponent();
		@SuppressWarnings("unused")
		final Object[][] data = {
				{false,"src/java/main/weixin/service/FunctionService.java"},
				{false,"resources/main/index.js"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"},

				{false,"resources/img/photowall/bg.png"},
				{false,"resources/img/photowall/bg.png"}
		};
		//ui.showArea();
		ui.showArea();
		//Thread.sleep(1000l);
		//ui.showTable(data, null);
		//ui.clean();
	}
}
