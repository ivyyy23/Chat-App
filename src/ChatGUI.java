import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChatGUI implements MessageListener {
    private JFrame frame;
    private Client client;

    private boolean loggedIn = false;  // true if user's logged in

    private String username;
    private String password;

    // JPanel variables
    private JPanel root;
    private JPanel cards;
    private JPanel dashboardPanel;
    private JPanel homePanel;
    private JPanel registerPanel;
    private JPanel loginPanel;
    private JPanel chatPanel;
    private JPanel registerUsernamePanel;
    private JPanel registerPasswordPanel;
    private JPanel loginUsernamePanel;
    private JPanel loginPasswordPanel;
    private JPanel accountPanel;
    private JPanel inputPanel;

    // String variables
    private final String DASHBOARD_PANEL = "Dashboard";
    private final String HOME_PANEL = "Home Page";
    private final String REGISTER_PANEL = "Register";
    private final String LOGIN_PANEL = "Login";
    private final String CHAT_PANEL = "Chat";

    // JLabel variables
    private JLabel welcomeText;
    private JLabel dashboardText;
    private JLabel accountText;
    private JLabel registerText;
    private JLabel descriptionText;
    private JLabel loginText;
    private JLabel chatText;
    private JLabel registerUsernameText;
    private JLabel registerPasswordText;
    private JLabel loginUsernameText;
    private JLabel loginPasswordText;

    // JButton variables
    private JButton homeButton;
    private JButton registrationPageButton;  // opens registration page
    private JButton loginPageButton;  // opens login page
    private JButton registerButton;
    private JButton loginButton;
    private JButton chatButton;
    private JButton sendButton;

    // JTextField variables
    private JTextField registerUsernameField;
    private JTextField registerPasswordField;
    private JTextField loginUsernameField;
    private JTextField loginPasswordField;
    private JTextField inputField;

    private JScrollPane logScrollPane;

    // JTextArea variables
    private JTextArea chatLog;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatGUI());
    }

    public ChatGUI() {
        try {
            createGUI();
        } catch (IOException e) {
            showError("Server disconnected.");
            frame.dispose();
        }
    }

    @Override
    public void onMessageReceived(String msg) {
        SwingUtilities.invokeLater(() -> {
            chatLog.append(msg + "\n");
        });
    }

    // Send message to server
    private void sendMessage() {
        String message = inputField.getText().trim();

        if (!message.isEmpty()) {
            client.sendMessage(message);  // Send to server via client
            inputField.setText("");  // Clear input field
        }
    }

    private void createGUI() throws IOException {
        frame = new JFrame("Chatty");

        // initializing the client
        client = new Client();
        client.setMessageListener(this);
        client.startListening();

        // setting up labels
        welcomeText = new JLabel("Welcome to Chatty!");
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 30));
        accountText = new JLabel("Register if you're new here, or log in to an existing account");
        accountText.setFont(new Font("Arial", Font.PLAIN, 18));
        dashboardText = new JLabel("Chatty");
        dashboardText.setFont(new Font("Arial", Font.PLAIN, 30));
        descriptionText = new JLabel("A place to connect with friends and family.");
        descriptionText.setFont(new Font("Arial", Font.PLAIN, 18));
        registerText = new JLabel("Register");
        registerText.setFont(new Font("Arial", Font.PLAIN, 30));
        loginText = new JLabel("Login");
        loginText.setFont(new Font("Arial", Font.PLAIN, 30));
        chatText = new JLabel("Chat");
        chatText.setFont(new Font("Arial", Font.PLAIN, 30));
        registerUsernameText = new JLabel("Username:");
        registerUsernameText.setFont(new Font("Arial", Font.PLAIN, 18));
        loginUsernameText = new JLabel("Username:");
        loginUsernameText.setFont(new Font("Arial", Font.PLAIN, 18));
        registerPasswordText = new JLabel("Password:");
        registerPasswordText.setFont(new Font("Arial", Font.PLAIN, 18));
        loginPasswordText = new JLabel("Password:");
        loginPasswordText.setFont(new Font("Arial", Font.PLAIN, 18));

        // setting up buttons
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 15));

        homeButton.addActionListener(e -> {
            if (loggedIn) {
                showPanel(DASHBOARD_PANEL);
            } else {
                showPanel(HOME_PANEL);
            }
        });

        registrationPageButton = new JButton("Create a new account");
        registrationPageButton.setFont(new Font("Arial", Font.PLAIN, 15));
        registrationPageButton.addActionListener(e -> showPanel(REGISTER_PANEL));

        loginPageButton = new JButton("Login using an existing account");
        loginPageButton.setFont(new Font("Arial", Font.PLAIN, 15));
        loginPageButton.addActionListener(e -> showPanel(LOGIN_PANEL));
        
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 15));
        registerButton.addActionListener(e -> register());

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 15));
        loginButton.addActionListener(e -> login());

        chatButton = new JButton("Chat");
        chatButton.setFont(new Font("Arial", Font.PLAIN, 15));
        chatButton.addActionListener(e -> showPanel(CHAT_PANEL));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.PLAIN, 15));
        sendButton.addActionListener(e -> sendMessage());

        // setting up text fields
        registerUsernameField = new JTextField(50);
        registerUsernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        registerUsernameField.setMaximumSize(new Dimension(registerUsernameField.getPreferredSize().width, registerUsernameField.getPreferredSize().height));
        registerPasswordField = new JTextField(50);
        registerPasswordField.setFont(new Font("Arial", Font.PLAIN, 15));
        registerPasswordField.setMaximumSize(new Dimension(registerPasswordField.getPreferredSize().width, registerPasswordField.getPreferredSize().height));
        loginUsernameField = new JTextField(50);
        loginUsernameField.setFont(new Font("Arial", Font.PLAIN, 15));
        loginUsernameField.setMaximumSize(new Dimension(loginUsernameField.getPreferredSize().width, loginUsernameField.getPreferredSize().height));
        loginPasswordField = new JTextField(50);
        loginPasswordField.setFont(new Font("Arial", Font.PLAIN, 15));
        loginPasswordField.setMaximumSize(new Dimension(loginPasswordField.getPreferredSize().width, loginPasswordField.getPreferredSize().height));
        inputField = new JTextField(100);
        inputField.setFont(new Font("Arial", Font.PLAIN, 15));
        inputField.addActionListener(e -> sendMessage());
        inputField.setMaximumSize(new Dimension(inputField.getPreferredSize().width, inputField.getPreferredSize().height));

        // setting up text area variables
        chatLog = new JTextArea(50, 50);
        chatLog.setFont(new Font("Arial", Font.PLAIN, 15));
        chatLog.setMaximumSize(new Dimension(chatLog.getPreferredSize().width, chatLog.getPreferredSize().height));

        //setting up scroll pane
        logScrollPane = new JScrollPane(chatLog);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // setting up panels
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BoxLayout(dashboardPanel, BoxLayout.Y_AXIS));
        homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        cards = new JPanel(new CardLayout());
        root = new JPanel(new BorderLayout());
        registerUsernamePanel = new JPanel();
        registerUsernamePanel.setLayout(new BoxLayout(registerUsernamePanel, BoxLayout.X_AXIS));
        loginUsernamePanel = new JPanel();
        loginUsernamePanel.setLayout(new BoxLayout(loginUsernamePanel, BoxLayout.X_AXIS));
        registerPasswordPanel = new JPanel();
        registerPasswordPanel.setLayout(new BoxLayout(registerPasswordPanel, BoxLayout.X_AXIS));
        loginPasswordPanel = new JPanel();
        loginPasswordPanel.setLayout(new BoxLayout(loginPasswordPanel, BoxLayout.X_AXIS));
        accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.X_AXIS));
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        // dashboard
        dashboardPanel.add(dashboardText);
        dashboardPanel.add(descriptionText);
        dashboardPanel.add(chatButton);
        // arranging component alignment
        dashboardText.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionText.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // account
        accountPanel.add(registrationPageButton, Component.LEFT_ALIGNMENT);
        dashboardPanel.add(Box.createRigidArea(new Dimension(5,0)));
        accountPanel.add(loginPageButton, Component.RIGHT_ALIGNMENT);

        // home
        homePanel.add(welcomeText);
        homePanel.add(accountText);
        homePanel.add(accountPanel);
        // arranging component alignment
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountText.setAlignmentX(Component.CENTER_ALIGNMENT);
        accountPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // registerUsernamePanel 
        registerUsernamePanel.add(registerUsernameText);
        registerUsernamePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        registerUsernamePanel.add(registerUsernameField);
        registerUsernameText.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerUsernameField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // registerPasswordPanel 
        registerPasswordPanel.add(registerPasswordText);
        registerPasswordPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        registerPasswordPanel.add(registerPasswordField);
        registerPasswordText.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerPasswordField.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // loginUsernamePanel 
        loginUsernamePanel.add(loginUsernameText);
        loginUsernamePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        loginUsernamePanel.add(loginUsernameField);
        loginUsernameText.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginUsernameField.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // loginPasswordPanel 
        loginPasswordPanel.add(loginPasswordText);
        loginPasswordPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        loginPasswordPanel.add(loginPasswordField);
        loginPasswordText.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPasswordField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        // register
        registerPanel.add(registerText);
        registerPanel.add(registerUsernamePanel);
        registerPanel.add(registerPasswordPanel);
        registerPanel.add(registerButton);
        // arranging component alignment
        registerText.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerUsernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerPasswordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // login
        loginPanel.add(loginText);
        loginPanel.add(loginUsernamePanel);
        loginPanel.add(loginPasswordPanel);
        loginPanel.add(loginButton);
        // arranging component alignment
        loginText.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginUsernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPasswordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // chat
        chatPanel.add(chatText);
        chatPanel.add(logScrollPane);
        chatPanel.add(inputPanel);
        // arranging component alignment
        chatText.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatLog.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // chat input panel
        inputPanel.add(inputField, Component.LEFT_ALIGNMENT);
        inputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        inputPanel.add(sendButton, Component.RIGHT_ALIGNMENT);

        // adding home button to all pages
        root.add(homeButton, BorderLayout.NORTH);
        root.add(cards, BorderLayout.CENTER);

        // adding all panels to the main panel called cards
        cards.add(dashboardPanel, DASHBOARD_PANEL);  // showed if logged in
        cards.add(registerPanel, REGISTER_PANEL);
        cards.add(loginPanel, LOGIN_PANEL);
        cards.add(chatPanel, CHAT_PANEL);
        cards.add(homePanel, HOME_PANEL);  // showed if not logged in

        showPanel(HOME_PANEL);

        frame.add(root);
        // frame settings
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    private void showPanel(String panel) {
        CardLayout cl = (CardLayout) cards.getLayout();

        if (panel.equals(HOME_PANEL)) {
            homeButton.setVisible(false);
        } else {
            homeButton.setVisible(true);
        }

        cl.show(cards, panel);
    }

    private void register() {
        username = registerUsernameField.getText().trim();
        password = registerPasswordField.getText();

        if (password.length() < 8) {
            showError("Password must be greater than or equal to 8 characters.");
            return;
        }

        if (username.isBlank() || password.isBlank()) {
            showError("Username and password cannot be empty.");
            return;
        }

        registerButton.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return client.register(username, password);
            }

            @Override
            protected void done() {
                registerButton.setEnabled(true);
                try {
                    boolean success = get();

                    if (success) {
                        JOptionPane.showMessageDialog(frame,
                                "Registration successful! You are now logged in.");
                        showPanel(DASHBOARD_PANEL);

                        registerUsernameField.setText("");
                        registerPasswordField.setText("");
                        loggedIn = true;
                    } else {
                        showError("Username already exists! Please try another username.");
                    }
                } catch (Exception e) {
                    showError("Connection error: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void login() {
        username = loginUsernameField.getText().trim();
        password = loginPasswordField.getText();

        if (password.length() < 8) {
            showError("Incorrect username or password.");
            return;
        }

        if (username.isBlank() || password.isBlank()) {
            showError("Username and password cannot be empty.");
            return;
        }

        loginButton.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return client.login(username, password);
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                try {
                    boolean success = get();

                    if (success) {
                        JOptionPane.showMessageDialog(frame,
                                "Login successful! You are now logged in.");
                        showPanel(DASHBOARD_PANEL);

                        loginUsernameField.setText("");
                        loginPasswordField.setText("");
                        loggedIn = true;
                    } else {
                        showError("Incorrect username or password.");
                    }
                } catch (Exception e) {
                    showError("Connection error: " + e.getMessage());
                }
            }
        }.execute();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, "Error: "+ message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
