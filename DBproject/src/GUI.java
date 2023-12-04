import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GUI extends JFrame {
    public GUI() {
        setTitle("음악 스트리밍");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 배경 이미지를 위한 JPanel 생성
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        setContentPane(backgroundPanel);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JButton logInButton = new JButton("로그인");
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logIn();
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(logInButton, constraints);

        JButton signUpButton = new JButton("회원가입");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(signUpButton, constraints);

        add(panel);
    }

    private void logIn() {
        login log = new login();
        log.setVisible(true);
    }

    private void signUp() {
        signup sign = new signup();
        sign.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        try {
            // 이미지를 읽어와 배경 이미지로 설정
            File imageFile = new File("D:/DB/멜론사진.jpg");
            if (imageFile.exists()) {
                backgroundImage = ImageIO.read(imageFile);
            } else {
                System.err.println("이미지 파일이 존재하지 않습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 이미지를 그려서 배경으로 사용
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
