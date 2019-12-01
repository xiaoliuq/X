package sss;
import java.awt.Container;

import java.awt.event.ActionEvent;     //动作事件
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;  

@SuppressWarnings({ "serial", "unused" })
class LoginFrame extends JFrame { 

	private JLabel userLabel;  
	private JLabel passwordLabel;  
	private JPasswordField passwordField;  
	private JTextField userNameField;  
	private JButton loginButton;  
	private JButton exitButton; 
	private JLabel labelImage;
	
	JFrame frame = new JFrame("学生选课管理系统");
	// 在窗体中得到一个容器
	Container container = frame.getContentPane();
	
	// 构造函数,初始化对象值
	public LoginFrame() { 
		
	   
		// 设置面板的绝对布局
		container.setLayout(null);          
		
		labelImage = new JLabel();
		labelImage.setIcon(new ImageIcon("res/wang.jpg"));
		labelImage.setBounds(10,20,100,100);
		
		//设置用户标签提示信息
		userLabel = new JLabel("学号:");       
		userLabel.setBounds(110, 40, 40, 40);
  
		
		// 用户名文本框
		userNameField = new JTextField(); 
		userNameField.setBounds(170, 50, 120, 20);  
		
		// 密码标签
		passwordLabel = new JLabel("密码:");  
		passwordLabel.setBounds(110, 70, 40, 40);  

		
		// 密码文本框
		passwordField = new JPasswordField();  
		passwordField.setBounds(170, 80, 120, 20);  
		
		// 登录按钮
		loginButton = new JButton("登录");  
		loginButton.setBounds(60, 140, 80, 30);  
		loginButton.addActionListener(new buttonListenner());     //用addActionListener方法把buttonListenner注册成loginButton的监视器
		
		// 退出按钮
		exitButton = new JButton("退出");  
		exitButton.setBounds(170, 140, 80, 30);  
		exitButton.addActionListener(new buttonListenner());  
		

		container.add(labelImage);
		// 将退出按钮添加到容器
		container.add(exitButton);  
		// 将登录按钮添加到容器
		container.add(loginButton);  
		// 将密码文本框添加到容器
		container.add(passwordField);  
		// 将密码标签添加到容器
		container.add(passwordLabel);  
		// 将用户名文本框添加到容器
		container.add(userNameField);  
		// 把用户标签放到面板里边
		container.add(userLabel);                              
		
		/* 设置窗体可见和居中 */  
		frame.setVisible(true);  
		frame.setLocationRelativeTo(null);
		frame.setLocation(800, 400);
		frame.setSize(320, 230);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        //关闭窗口有什么用
	}  

	public static void main(String[] args) {  
		new LoginFrame();                  //打开LoginFrame类
	}  
	
	
	class buttonListenner implements ActionListener {  
		public void actionPerformed(ActionEvent e) {  
			if (exitButton == e.getSource()) {  
				System.exit(0);  
			}  
			if (loginButton == e.getSource()) {  
				/* 对数据库的提取验证操作; */  
				try {  
					Class.forName("com.mysql.jdbc.Driver"); // 注册驱动
					String url = "jdbc:mysql://localhost/student?useUnicode=true&characterEncoding=utf8&useSSL=true";//建立li
					Connection conection = DriverManager.getConnection(url,"root","123456");  
					Statement statement = conection.createStatement();  // 发起请求增删改查
					String sql = "SELECT * FROM user";  // 选择用户表  
					/* 获取结果集 */  
					ResultSet resultSet = statement.executeQuery(sql); // 执行返回多个结果集以resultset类型返回ex做传递sql信息
					while(resultSet.next()){  // 每次一行 成功true
						String userName = null;
						String password = null;
						userName = resultSet.getString(1); // 得到id
						password = resultSet.getString(2);// 得到密码
						if (userName.equals(userNameField.getText())) {  
							if (password.equals(new String(passwordField.getPassword()))) {  
								
								frame.dispose();
								new MainFrame(userName,statement);
							
							}
						}
						else {
					  }
					}
					
					JOptionPane.showMessageDialog(null, "账号或密码错误!"); 
					resultSet.close();  
				}catch (SQLException e1) {  
					  
				}catch(ClassNotFoundException e2){
					JOptionPane.showMessageDialog(null, "类错误");
				}
			}  
		}  
	}  
}  