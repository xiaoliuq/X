package sss;
import java.awt.Container;

import java.awt.event.ActionEvent;     //�����¼�
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
	
	JFrame frame = new JFrame("ѧ��ѡ�ι���ϵͳ");
	// �ڴ����еõ�һ������
	Container container = frame.getContentPane();
	
	// ���캯��,��ʼ������ֵ
	public LoginFrame() { 
		
	   
		// �������ľ��Բ���
		container.setLayout(null);          
		
		labelImage = new JLabel();
		labelImage.setIcon(new ImageIcon("res/wang.jpg"));
		labelImage.setBounds(10,20,100,100);
		
		//�����û���ǩ��ʾ��Ϣ
		userLabel = new JLabel("ѧ��:");       
		userLabel.setBounds(110, 40, 40, 40);
  
		
		// �û����ı���
		userNameField = new JTextField(); 
		userNameField.setBounds(170, 50, 120, 20);  
		
		// �����ǩ
		passwordLabel = new JLabel("����:");  
		passwordLabel.setBounds(110, 70, 40, 40);  

		
		// �����ı���
		passwordField = new JPasswordField();  
		passwordField.setBounds(170, 80, 120, 20);  
		
		// ��¼��ť
		loginButton = new JButton("��¼");  
		loginButton.setBounds(60, 140, 80, 30);  
		loginButton.addActionListener(new buttonListenner());     //��addActionListener������buttonListennerע���loginButton�ļ�����
		
		// �˳���ť
		exitButton = new JButton("�˳�");  
		exitButton.setBounds(170, 140, 80, 30);  
		exitButton.addActionListener(new buttonListenner());  
		

		container.add(labelImage);
		// ���˳���ť��ӵ�����
		container.add(exitButton);  
		// ����¼��ť��ӵ�����
		container.add(loginButton);  
		// �������ı�����ӵ�����
		container.add(passwordField);  
		// �������ǩ��ӵ�����
		container.add(passwordLabel);  
		// ���û����ı�����ӵ�����
		container.add(userNameField);  
		// ���û���ǩ�ŵ�������
		container.add(userLabel);                              
		
		/* ���ô���ɼ��;��� */  
		frame.setVisible(true);  
		frame.setLocationRelativeTo(null);
		frame.setLocation(800, 400);
		frame.setSize(320, 230);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        //�رմ�����ʲô��
	}  

	public static void main(String[] args) {  
		new LoginFrame();                  //��LoginFrame��
	}  
	
	
	class buttonListenner implements ActionListener {  
		public void actionPerformed(ActionEvent e) {  
			if (exitButton == e.getSource()) {  
				System.exit(0);  
			}  
			if (loginButton == e.getSource()) {  
				/* �����ݿ����ȡ��֤����; */  
				try {  
					Class.forName("com.mysql.jdbc.Driver"); // ע������
					String url = "jdbc:mysql://localhost/student?useUnicode=true&characterEncoding=utf8&useSSL=true";//����li
					Connection conection = DriverManager.getConnection(url,"root","123456");  
					Statement statement = conection.createStatement();  // ����������ɾ�Ĳ�
					String sql = "SELECT * FROM user";  // ѡ���û���  
					/* ��ȡ����� */  
					ResultSet resultSet = statement.executeQuery(sql); // ִ�з��ض���������resultset���ͷ���ex������sql��Ϣ
					while(resultSet.next()){  // ÿ��һ�� �ɹ�true
						String userName = null;
						String password = null;
						userName = resultSet.getString(1); // �õ�id
						password = resultSet.getString(2);// �õ�����
						if (userName.equals(userNameField.getText())) {  
							if (password.equals(new String(passwordField.getPassword()))) {  
								
								frame.dispose();
								new MainFrame(userName,statement);
							
							}
						}
						else {
					  }
					}
					
					JOptionPane.showMessageDialog(null, "�˺Ż��������!"); 
					resultSet.close();  
				}catch (SQLException e1) {  
					  
				}catch(ClassNotFoundException e2){
					JOptionPane.showMessageDialog(null, "�����");
				}
			}  
		}  
	}  
}  