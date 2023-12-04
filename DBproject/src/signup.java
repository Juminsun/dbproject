import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class signup extends JFrame {

    private static final String driver = "oracle.jdbc.OracleDriver";
    private static final String url = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String id = "system";
    private static final String pw = "1234";

    private JTextField userIdField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public signup() {
        setTitle("회원가입");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);

        JLabel userIdLabel = new JLabel("아이디:");
        userIdField = new JTextField();
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("비밀번호 확인:");
        confirmPasswordField = new JPasswordField();

        JButton registerButton = new JButton("회원가입");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRegisterButtonClicked();
            }
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(userIdLabel)
                        .addComponent(passwordLabel)
                        .addComponent(confirmPasswordLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(userIdField)
                        .addComponent(passwordField)
                        .addComponent(confirmPasswordField)
                        .addComponent(registerButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(userIdLabel)
                        .addComponent(userIdField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(confirmPasswordLabel)
                        .addComponent(confirmPasswordField))
                .addComponent(registerButton)
        );

    }

    private void onRegisterButtonClicked() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return;
        }

        try {
            if (registerUser(userId, password)) {
                JOptionPane.showMessageDialog(this, "회원가입 성공! 사용자 ID: " + userId);

                // 회원가입 성공 시 로그인 화면으로 전환
                login loginFrame = new login();
                loginFrame.setVisible(true);
                this.dispose();  // 현재 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "회원가입 실패. 이미 존재하는 아이디입니다.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean registerUser(String userId, String password) throws Exception {
        Class.forName(driver);
        Connection db = DriverManager.getConnection(url, id, pw);

        // 이미 존재하는 아이디인지 확인
        String checkQuery = "SELECT user_id FROM userinfo WHERE user_id = ?";

        try (PreparedStatement checkStatement = db.prepareStatement(checkQuery)) {
            checkStatement.setString(1, userId);
            try (var resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    return false;  // 이미 존재하는 아이디
                }
            }
        }

        // 회원가입 쿼리 실행
        String insertQuery = "INSERT INTO userinfo (user_id, password) VALUES (?, ?)";
        try (PreparedStatement insertStatement = db.prepareStatement(insertQuery)) {
            insertStatement.setString(1, userId);
            insertStatement.setString(2, password);
            insertStatement.executeUpdate();
            return true;
        } finally {
            db.close();
        }
    }

}
