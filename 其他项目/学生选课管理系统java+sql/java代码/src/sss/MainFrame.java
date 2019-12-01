package sss ;

import java.awt.BorderLayout;   //方位布局
import java.awt.Component;      //抽象窗口工具包-组件类
import java.awt.FlowLayout;     //流动布局
import java.awt.event.MouseEvent;   //鼠标事件
import java.awt.event.MouseListener;   //接口，监视器
import java.sql.ResultSet;      //数据库结果集的数据表，通常通过执行查询数据库的语句生成
import java.sql.ResultSetMetaData;  //有关整个数据库的信息
import java.sql.SQLException;       //返回警告或错误时引发的异常
import java.sql.Statement;          //建立了到特定数据库的连接之后，就可用该连接发送 SQL 语句

import javax.swing.DefaultCellEditor;    //单元格放置组件
import javax.swing.JCheckBox;            //获取哪些被选中
import javax.swing.JFrame;               //框架结构
import javax.swing.JLabel;               //标签
import javax.swing.JOptionPane;          //消息对话框的组件
import javax.swing.JPanel;               //面板
import javax.swing.JScrollPane;          //下拉对话框
import javax.swing.JTable;               //表
import javax.swing.table.DefaultTableModel;   //表格模型  
import javax.swing.table.TableCellRenderer;  //接口
import javax.swing.table.TableColumnModel;  //是一个接口,里面定义了许多与表格的"列(行)"有关的方法
  
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
      
    public MainFrame(String userName, Statement statement) {   // 构造方法
        super();  
        
        user = userName;  
        state = statement;  
        getInformation(userName, statement);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 关闭窗口
        /* 1.选课信息 */  
        userPanel = new JPanel();   
        setSize(500, 330);  
        content = new JPanel();  
        add(content);  
        content.setLayout(new BorderLayout(5, 5));  
        /* 将窗体分割成两个部分1.选课信息,2。课程部分 */  
        userPanel.setLayout(new FlowLayout());  
        add(userPanel, BorderLayout.NORTH);  
        userPanel.add(new JLabel("已选课程:"));  
        course = new JLabel();  
        userPanel.add(course);  
        /* 2。课程部分 */  
        JPanel coursePanel = new JPanel();  
        add(coursePanel, BorderLayout.CENTER);  
        informationTable = setTable(userName, statement);  
        informationTable.addMouseListener(new MouseAdapter());  
        JScrollPane courseScroll = new JScrollPane(informationTable);  
        coursePanel.add(courseScroll);  
  
        setVisible(true);  
        setLocationRelativeTo(null);    // 设置窗口相对于指定组件的位置
        
    }  
  
    
    
  
	class TestTableCellRenderer extends JCheckBox implements TableCellRenderer {  // 重写父类方法使表格字体
  
        public Component getTableCellRendererComponent(JTable table,  
                Object value, boolean isSelected, boolean hasFocus, int row,  
                int column) {  
            Boolean b = (Boolean) value;  
            this.setSelected(b.booleanValue());
            
            return this;  
  
        }  
    }  
  
    class MouseAdapter implements MouseListener { // 类实现 接口 
        public void mouseClicked(MouseEvent e) {   // 负责处理在组件上单击鼠标键触发的鼠标事件
            setData(user, state);  
  
        }  
  
        public void mouseEntered(MouseEvent e) {   // 负责处理处理鼠标进入组件触发的鼠标事件
        }  
  
        public void mouseExited(MouseEvent e) {    // 负责处理鼠标离开组件触发的鼠标事件
        }  
  
        public void mousePressed(MouseEvent e) {   // 负责处理在组件上按下鼠标键触发的鼠标事件
        }  
  
        public void mouseReleased(MouseEvent e) {  // 负责处理在组件上释放鼠标键触发的鼠标事件
        }  
    }  
  
    private void setData(String userName, Statement statement) {  
        int nRow = informationTable.getSelectedRow();  
        if ( (Boolean) informationTable.getValueAt(nRow, 4)) {  
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,  
                    "是否确认选择这一课程!")) {  
                try {  
                    String sql = "INSERT  INTO studentcourse(username,cnumber) VALUE('"  
                            + userName + "','" + columsStrings[nRow][0] + "')";  
                   statement.executeUpdate(sql);        // 返回值，更新记数量
                   JOptionPane.showMessageDialog(null, "选课成功!");  
                   getStudentCourse(userName, statement);  
                   informationTable.setValueAt(true, nRow, 4);// 单元格中的值设置为 aValue true为选中
                   informationTable.setValueAt("已选课", nRow, 5);
                } 
                catch (SQLException e) {                // 处理异常块
                    JOptionPane.showMessageDialog(null, "选课错误!");  
                }  
            } 
            else {  
                informationTable.setValueAt(false, nRow, 4);  
             
            }  
        } 
        else {  
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,  
                    "是否删除这一课程!")) {  
                try {  
                    String sql = "DELETE FROM studentcourse WHERE username="  
                            + userName + " AND cnumber="  
                            + columsStrings[nRow][0];  
                    statement.executeUpdate(sql);  
                    getStudentCourse(userName, statement);  
                    JOptionPane.showMessageDialog(null, "退选成功!");  
                    informationTable.setValueAt(false, nRow, 4); 
                    informationTable.setValueAt("未选课", nRow, 5);
                } 
                catch (SQLException e) {  
                    JOptionPane.showMessageDialog(null, "退选错误!");  
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
            setTitle("欢迎登录:" + nameString + "同学,您的学号为:" + numString); 
            
            resultSet.close();  
        } 
        catch (SQLException e) {  
            JOptionPane.showMessageDialog(null, "获取学生信息错误!");  
        }  
    }  
  
    private JTable setTable(String userName, Statement statement) {  
  
        String[] columnames = { "课号", "名称", "学分", "学时", "选择","课程" };  
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
            // 默认选课状态为false  
            for (int i = 0; i < courses; i++) {  
                columsStrings[i][4] = new Boolean(false);
                columsStrings[i][5] = "未选课";
            }  
            getStudentCourse(userName, statement);  
            resultSet.close();  
  
        } 
        catch (Exception e) {  
            JOptionPane.showMessageDialog(null, "获取课程的数据错误2!");  
            e.printStackTrace();  
        }  
  
        // 设置表格的样式  
        DefaultTableModel tableModel;  
        tableModel = new DefaultTableModel(columsStrings, columnames) {  
            @Override  
            public boolean isCellEditable(int rowIndex, int columnIndex) {  
                if (columnIndex != 4)  
                    return false;    // 这个是可以编辑的列  
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
        // 从数据库中获取该学生已经选了课程的消息  
        sql = "SELECT cname FROM course WHERE cnumber IN"  
                + "(SELECT cnumber FROM studentcourse WHERE username ="  
                + userName + ")"; 
        
        resultSet = statement.executeQuery(sql);  
        // 设置单选框的选择状态  
        boolean flag = false;  
        while (resultSet.next()) {  
            for (int j = 0; j < courses; j++) {  
                if (resultSet.getString(1).equals(columsStrings[j][1])) {  
                    columsStrings[j][4] = true;  
                    flag = true;  
                    columsStrings[j][5] = "已选课";
                }  
            }  
        }  
        // 显示已选课程状态  
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