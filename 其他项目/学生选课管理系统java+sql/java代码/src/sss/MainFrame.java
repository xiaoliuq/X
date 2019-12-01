package sss ;

import java.awt.BorderLayout;   //��λ����
import java.awt.Component;      //���󴰿ڹ��߰�-�����
import java.awt.FlowLayout;     //��������
import java.awt.event.MouseEvent;   //����¼�
import java.awt.event.MouseListener;   //�ӿڣ�������
import java.sql.ResultSet;      //���ݿ����������ݱ�ͨ��ͨ��ִ�в�ѯ���ݿ���������
import java.sql.ResultSetMetaData;  //�й��������ݿ����Ϣ
import java.sql.SQLException;       //���ؾ�������ʱ�������쳣
import java.sql.Statement;          //�����˵��ض����ݿ������֮�󣬾Ϳ��ø����ӷ��� SQL ���

import javax.swing.DefaultCellEditor;    //��Ԫ��������
import javax.swing.JCheckBox;            //��ȡ��Щ��ѡ��
import javax.swing.JFrame;               //��ܽṹ
import javax.swing.JLabel;               //��ǩ
import javax.swing.JOptionPane;          //��Ϣ�Ի�������
import javax.swing.JPanel;               //���
import javax.swing.JScrollPane;          //�����Ի���
import javax.swing.JTable;               //��
import javax.swing.table.DefaultTableModel;   //���ģ��  
import javax.swing.table.TableCellRenderer;  //�ӿ�
import javax.swing.table.TableColumnModel;  //��һ���ӿ�,���涨������������"��(��)"�йصķ���
  
@SuppressWarnings("serial")
public class MainFrame extends JFrame {  
    private JPanel content;  
    private JPanel userPanel;  
    private JTable informationTable;  
    private JLabel course;  
    private Object[][] columsStrings;  
    String user;  
    Statement state;  
    int courses = 0;  
      
    public MainFrame(String userName, Statement statement) {   // ���췽��
        super();  
        
        user = userName;  
        state = statement;  
        getInformation(userName, statement);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // �رմ���
        /* 1.ѡ����Ϣ */  
        userPanel = new JPanel();   
        setSize(500, 330);  
        content = new JPanel();  
        add(content);  
        content.setLayout(new BorderLayout(5, 5));  
        /* ������ָ����������1.ѡ����Ϣ,2���γ̲��� */  
        userPanel.setLayout(new FlowLayout());  
        add(userPanel, BorderLayout.NORTH);  
        userPanel.add(new JLabel("��ѡ�γ�:"));  
        course = new JLabel();  
        userPanel.add(course);  
        /* 2���γ̲��� */  
        JPanel coursePanel = new JPanel();  
        add(coursePanel, BorderLayout.CENTER);  
        informationTable = setTable(userName, statement);  
        informationTable.addMouseListener(new MouseAdapter());  
        JScrollPane courseScroll = new JScrollPane(informationTable);  
        coursePanel.add(courseScroll);  
  
        setVisible(true);  
        setLocationRelativeTo(null);    // ���ô��������ָ�������λ��
        
    }  
  
    
    
  
	class TestTableCellRenderer extends JCheckBox implements TableCellRenderer {  // ��д���෽��ʹ�������
  
        public Component getTableCellRendererComponent(JTable table,  
                Object value, boolean isSelected, boolean hasFocus, int row,  
                int column) {  
            Boolean b = (Boolean) value;  
            this.setSelected(b.booleanValue());
            
            return this;  
  
        }  
    }  
  
    class MouseAdapter implements MouseListener { // ��ʵ�� �ӿ� 
        public void mouseClicked(MouseEvent e) {   // ������������ϵ�����������������¼�
            setData(user, state);  
  
        }  
  
        public void mouseEntered(MouseEvent e) {   // ���������������������������¼�
        }  
  
        public void mouseExited(MouseEvent e) {    // ����������뿪�������������¼�
        }  
  
        public void mousePressed(MouseEvent e) {   // ������������ϰ�����������������¼�
        }  
  
        public void mouseReleased(MouseEvent e) {  // ��������������ͷ���������������¼�
        }  
    }  
  
    private void setData(String userName, Statement statement) {  
        int nRow = informationTable.getSelectedRow();  
        if ( (Boolean) informationTable.getValueAt(nRow, 4)) {  
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,  
                    "�Ƿ�ȷ��ѡ����һ�γ�!")) {  
                try {  
                    String sql = "INSERT  INTO studentcourse(username,cnumber) VALUE('"  
                            + userName + "','" + columsStrings[nRow][0] + "')";  
                   statement.executeUpdate(sql);        // ����ֵ�����¼�����
                   JOptionPane.showMessageDialog(null, "ѡ�γɹ�!");  
                   getStudentCourse(userName, statement);  
                   informationTable.setValueAt(true, nRow, 4);// ��Ԫ���е�ֵ����Ϊ aValue trueΪѡ��
                   informationTable.setValueAt("��ѡ��", nRow, 5);
                } 
                catch (SQLException e) {                // �����쳣��
                    JOptionPane.showMessageDialog(null, "ѡ�δ���!");  
                }  
            } 
            else {  
                informationTable.setValueAt(false, nRow, 4);  
             
            }  
        } 
        else {  
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,  
                    "�Ƿ�ɾ����һ�γ�!")) {  
                try {  
                    String sql = "DELETE FROM studentcourse WHERE username="  
                            + userName + " AND cnumber="  
                            + columsStrings[nRow][0];  
                    statement.executeUpdate(sql);  
                    getStudentCourse(userName, statement);  
                    JOptionPane.showMessageDialog(null, "��ѡ�ɹ�!");  
                    informationTable.setValueAt(false, nRow, 4); 
                    informationTable.setValueAt("δѡ��", nRow, 5);
                } 
                catch (SQLException e) {  
                    JOptionPane.showMessageDialog(null, "��ѡ����!");  
                }  
            } 
            else {  
                informationTable.setValueAt(true, nRow, 4);  
            }  
        }  
    }  
  
    private void getInformation(String userName, Statement statement) {  
        try {  
            String sql = "SELECT * FROM student where username=" + userName;  
            ResultSet resultSet = statement.executeQuery(sql);  
            resultSet.next();  
            String numString = resultSet.getString(1);  
            String nameString = resultSet.getString(2);  
            setTitle("��ӭ��¼:" + nameString + "ͬѧ,����ѧ��Ϊ:" + numString); 
            
            resultSet.close();  
        } 
        catch (SQLException e) {  
            JOptionPane.showMessageDialog(null, "��ȡѧ����Ϣ����!");  
        }  
    }  
  
    private JTable setTable(String userName, Statement statement) {  
  
        String[] columnames = { "�κ�", "����", "ѧ��", "ѧʱ", "ѡ��","�γ�" };  
        try {  
            String sql = "SELECT * FROM course order by credit desc";  
            ResultSet resultSet = statement.executeQuery(sql);  
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();  
            while (resultSet.next()) {  
                courses++;  
            }  
            int nColus = resultSetMetaData.getColumnCount();  
            resultSet.first();  
            resultSet.relative(-1);
            columsStrings = new Object[courses][nColus+2];  
            for (int i = 0; resultSet.next(); i++) {  
                for (int j = 1; j <= nColus; j++) {  
  
                    columsStrings[i][j - 1] = new String(resultSet.getString(j));
                   
                }  
            }  
            resultSet.close();  
            // Ĭ��ѡ��״̬Ϊfalse  
            for (int i = 0; i < courses; i++) {  
                columsStrings[i][4] = new Boolean(false);
                columsStrings[i][5] = "δѡ��";
            }  
            getStudentCourse(userName, statement);  
            resultSet.close();  
  
        } 
        catch (Exception e) {  
            JOptionPane.showMessageDialog(null, "��ȡ�γ̵����ݴ���2!");  
            e.printStackTrace();  
        }  
  
        // ���ñ�����ʽ  
        DefaultTableModel tableModel;  
        tableModel = new DefaultTableModel(columsStrings, columnames) {  
            @Override  
            public boolean isCellEditable(int rowIndex, int columnIndex) {  
                if (columnIndex != 4)  
                    return false;    // ����ǿ��Ա༭����  
                return true;  
            }  
        };  
  
        JTable table = new JTable(tableModel);  
        TableColumnModel tcm = table.getColumnModel();  
        tcm.getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));  
        tcm.getColumn(4).setCellRenderer(new TestTableCellRenderer());  
        table.setModel(tableModel);  
  
        return table;  
    }  
  
    private void getStudentCourse(String userName, Statement statement)  
            throws SQLException {  
        String sql;  
        ResultSet resultSet;  
        // �����ݿ��л�ȡ��ѧ���Ѿ�ѡ�˿γ̵���Ϣ  
        sql = "SELECT cname FROM course WHERE cnumber IN"  
                + "(SELECT cnumber FROM studentcourse WHERE username ="  
                + userName + ")"; 
        
        resultSet = statement.executeQuery(sql);  
        // ���õ�ѡ���ѡ��״̬  
        boolean flag = false;  
        while (resultSet.next()) {  
            for (int j = 0; j < courses; j++) {  
                if (resultSet.getString(1).equals(columsStrings[j][1])) {  
                    columsStrings[j][4] = true;  
                    flag = true;  
                    columsStrings[j][5] = "��ѡ��";
                }  
            }  
        }  
        // ��ʾ��ѡ�γ�״̬  
        if (flag) {  
            StringBuffer buffer = new StringBuffer();  
            resultSet.first();  
            buffer.append(resultSet.getString(1));  
            while (resultSet.next()) {  
                buffer.append(",");  
                buffer.append(resultSet.getString(1));  
            }  
            course.setText(buffer.toString());  
        } 
        else {  
            course.setText("");  
        }  
  
        resultSet.close();  
    }  
}  