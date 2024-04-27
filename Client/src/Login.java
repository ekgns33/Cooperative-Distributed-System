import javax.swing.*;

public class Login extends JFrame {
    private JTextField idField;
    private JTextField ipField;
    private JButton loginButton;

    public Login() {
        // 로그인 창 초기화 코드 추가
        setTitle("로그인");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        idField = new JTextField(20);
        ipField = new JTextField(20);
        loginButton = new JButton("로그인");

        // 로그인 버튼에 액션 리스너 추가
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            if (authenticate()) {
                Board board = new Board();
                board.setVisible(true);
                dispose(); // 로그인 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "error");
            }
        });

        add(new JLabel("ID:"));
        add(idField);
        add(new JLabel("IP:"));
        add(ipField);
        add(loginButton);

        setLocationRelativeTo(null);
    }

    private boolean authenticate() {
        return true;
    }
}
