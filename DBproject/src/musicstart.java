import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class musicstart extends JFrame {
    private JTextField musicIdField;

    public musicstart() {
        setTitle("음악 시작 화면");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);

        JLabel musicIdLabel = new JLabel("음악 ID:");
        musicIdField = new JTextField();

        JButton playMusicButton = new JButton("음악 재생");
        playMusicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playMusic();
            }
        });
        PlaylistViewer Viewer = new PlaylistViewer();
        Viewer.setVisible(true);


        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(musicIdLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(musicIdField)
                        .addComponent(playMusicButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(musicIdLabel)
                        .addComponent(musicIdField))
                .addComponent(playMusicButton)
        );
    }

    private void playMusic() {
        String musicID = musicIdField.getText();
        try {
            openInternetURL(musicID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openInternetURL(String musicID) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "1234");

            // 음악 ID로부터 인터넷 주소를 가져오는 쿼리
            String query = "SELECT musictitle FROM Music WHERE MusicID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, musicID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String internetURL = resultSet.getString("musictitle");
                        if (internetURL != null && !internetURL.isEmpty()) {
                            // 인터넷 주소로 이동
                            Desktop.getDesktop().browse(new java.net.URI(internetURL));
                        } else {
                            JOptionPane.showMessageDialog(null, "해당 음악에 대한 인터넷 주소가 없습니다.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "해당 음악이 존재하지 않습니다.");
                    }
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
