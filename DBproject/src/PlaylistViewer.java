import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistViewer extends JFrame {
    private JTable musicTable;
    private DefaultTableModel tableModel;

    public PlaylistViewer() {
        setTitle("플레이리스트 보기");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 테이블 모델 생성
        tableModel = new DefaultTableModel();
        tableModel.addColumn("MusicID");
        tableModel.addColumn("MusicTitle");
        tableModel.addColumn("Artist");

        // 테이블 생성 및 모델 설정
        musicTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(musicTable);
        add(scrollPane, BorderLayout.CENTER);





        // 버튼 패널 생성
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // 데이터 로드
        loadMusicData();
    }

    private void loadMusicData() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "1234");

            // 음악 데이터 불러오기
            String query = "SELECT * FROM Music";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String musicID = resultSet.getString("MusicID");
                    String musicTitle = resultSet.getString("MusicName");
                    String artist = resultSet.getString("Artist");

                    // 테이블 모델에 데이터 추가
                    tableModel.addRow(new Object[]{musicID, musicTitle, artist});
                }
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMusic() {
        // 사용자에게 음악 정보를 입력받아 데이터베이스에 추가하는 코드 작성
        // ...

        // 추가 후에 테이블 모델 업데이트
        tableModel.addRow(new Object[]{"NewMusicID", "NewMusicTitle", "NewArtist", "NewAlbum"});
    }

    private void deleteMusic() {
        int selectedRow = musicTable.getSelectedRow();
        if (selectedRow != -1) {
            // 선택된 행의 MusicID를 가져와서 데이터베이스에서 삭제하는 코드 작성
            String musicID = (String) tableModel.getValueAt(selectedRow, 0);
            // ...

            // 삭제 후에 테이블 모델 업데이트
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 음악을 선택하세요.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlaylistViewer playlistViewer = new PlaylistViewer();
            playlistViewer.setVisible(true);
        });
    }
}
