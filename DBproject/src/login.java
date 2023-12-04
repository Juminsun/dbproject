import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class login extends JFrame {
    private static final String driver = "oracle.jdbc.OracleDriver";
    private static final String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String id = "system";
    private static final String pw = "1234";

    private JTextField userIdField;
    private JPasswordField passwordField;

    public login() {
        setTitle("로그인");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);

        JLabel userIdLabel = new JLabel("아이디:");
        userIdField = new JTextField();
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                String username = userIdField.getText();
                String password = new String(passwordField.getPassword());
                try {
                    if (checkCredentials(username, password)) {
                        JOptionPane.showMessageDialog(null, username + "님 로그인 성공!");
                        //////////////////////////////////////로그인 성공하면 어디로 들어갈지!/////////////
                        musicstart mst = new musicstart();
                        mst.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "로그인 실패. 이름 또는 비밀번호를 확인하세요.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });



        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(userIdLabel)
                        .addComponent(passwordLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(userIdField)
                        .addComponent(passwordField)
                        .addComponent(loginButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(userIdLabel)
                        .addComponent(userIdField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField))
                .addComponent(loginButton)
        );
    }


    public static boolean checkCredentials(String usernameToFind, String passwordToFind) throws Exception {
        Class.forName(driver);
        Connection db = DriverManager.getConnection(url, id, pw);

        String sql = "SELECT * FROM userinfo WHERE user_id = ? AND password = ?";
        PreparedStatement pstmt = db.prepareStatement(sql);
        pstmt.setString(1, usernameToFind);
        pstmt.setString(2, passwordToFind);

        ResultSet rs = pstmt.executeQuery();

        boolean credentialsMatch = rs.next();

        db.close();

        return credentialsMatch;
    }

}