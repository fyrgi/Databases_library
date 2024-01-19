import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

public class Library extends JFrame implements ActionListener {
    JButton login, create, cancel, save, logout, reserve, edit, currentLoan, loanHistory, search;
    JPanel initialPanel, loginPanel, createAccount, searchPanel, profilePanel, loggedPanel, editPanel;
    JLabel label, headline, nameLabel, emailLabel, adressLabel, passwordLabel, repeatPassLabel;
    JScrollPane scroll;


    public static JTable jt;
    JTextField searchField, nameField, emailField;
    JPasswordField passwordField, oldPasswordField, newPasswordField, repeatPassword;
    int currentUserId = -1;
    Library(){
        setTitle("Library");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        getContentPane().add(initialPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("create")){
            User nu = new User();
            boolean executeInsert = true;
            try {
                if(!nameField.getText().isEmpty()){
                    nu.setName(nameField.getText().trim());
                } else {
                    System.out.println("The name cannot be empty");
                    executeInsert = false;
                }
                if(!emailField.getText().isEmpty()){
                    nu.setEmail(emailField.getText().trim().toLowerCase());
                } else {
                    System.out.println("The email cannot be empty");
                    executeInsert = false;
                }
                String pass1 = new String(passwordField.getPassword());
                String pass2 = new String(repeatPassword.getPassword());
                if(pass1.length() < 4 || pass1.contains(" ")){
                    System.out.println("The password should contain 4 or more symbols.\nThe symbols cannot be whitespace");
                    executeInsert = false;
                } else {
                    if(pass1.equals(pass2)){
                        nu.setPassword(pass1);
                    } else {
                        executeInsert = false;
                        System.out.println("The passwords don't match");
                    }
                }
                if(executeInsert){
                    nu.save();
                    init();
                    getContentPane().remove(createAccount);
                    getContentPane().add(initialPanel);
                    getContentPane().invalidate();
                    getContentPane().revalidate();
                } else {
                    System.out.println("Fix the errors first.");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if(e.getActionCommand().equals("cancel")){
            init();
            getContentPane().remove(loginPanel);
            getContentPane().add(initialPanel);
            getContentPane().invalidate();
            getContentPane().revalidate();
            currentUserId = -1;
        } else if(e.getActionCommand().equals("cancelnew")){
            init();
            getContentPane().remove(createAccount);
            getContentPane().add(initialPanel);
            getContentPane().invalidate();
            getContentPane().revalidate();
            currentUserId = -1;
        } else if(e.getActionCommand().equals("login")){
            String pass = new String(passwordField.getPassword());
            try {
                User nu = new User(emailField.getText(), pass);
                if(nu.getId()<0){
                    System.out.println("The email or password are wrong");
                } else {
                    currentUserId = nu.getId();
                    loggedIn();
                    getContentPane().remove(loginPanel);
                    getContentPane().add(loggedPanel);
                    getContentPane().invalidate();
                    getContentPane().revalidate();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if(e.getActionCommand().equals("exists")){
            login();
            getContentPane().remove(initialPanel);
            getContentPane().add(loginPanel);
            getContentPane().invalidate();
            getContentPane().revalidate();
        } else if(e.getActionCommand().equals("new")){
            newAccount();
            getContentPane().remove(initialPanel);
            getContentPane().add(createAccount);
            getContentPane().invalidate();
            getContentPane().revalidate();
        } else if (e.getActionCommand().equals("edit")){
            User currentUser = null;
            try {
                currentUser = new User(currentUserId);
                editProfile(currentUser);
                getContentPane().remove(loggedPanel);
                getContentPane().add(editPanel);
                getContentPane().invalidate();
                getContentPane().revalidate();
            } catch (SQLException ex) {
                System.out.println("Cannot find user.");
                throw new RuntimeException(ex);
            }
        }
    }

    private void init(){
        initialPanel = new JPanel();
        initialPanel.setLayout(new GridLayout(5,2,10,10));
        JLabel libTitle = new JLabel("Library of Fulköping");
        libTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
        libTitle.setBorder(BorderFactory.createEmptyBorder(0,0,50,50));
        initialPanel.add(libTitle, BorderLayout.NORTH);
        login = new JButton();
        login.setText("Sign In");
        login.setActionCommand("exists");
        login.addActionListener(this);
        initialPanel.add(login);
        create = new JButton();
        create.setText("New account");
        create.setActionCommand("new");
        create.addActionListener(this);
        initialPanel.add(create);
        displayMedia();
        initialPanel.add(scroll);
    }

    private void loggedIn(){
        JPanel tablePannel = new JPanel();
        tablePannel.setLayout(new GridLayout(1,1,0,0));
        loggedPanel = new JPanel();
        loggedPanel.setLayout(new GridLayout(6,2,10,10));
        loggedPanel.setSize(300,250);
        loggedPanel.setBorder(BorderFactory.createEmptyBorder(50,50,20,50));
        try {
            User user = new User(currentUserId);
            JLabel subTitle = new JLabel("Welcome " + user.getName());
            subTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
            subTitle.setBorder(BorderFactory.createEmptyBorder(0,0,50,50));
            loggedPanel.add(subTitle, BorderLayout.NORTH);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loanHistory = new JButton();
        loanHistory.setText("Loan History");
        loanHistory.setActionCommand("showHistory");
        loanHistory.addActionListener(this);
        loggedPanel.add(loanHistory);
        currentLoan = new JButton();
        currentLoan.setText("Current loan");
        currentLoan.setActionCommand("showCurrent");
        currentLoan.addActionListener(this);
        loggedPanel.add(currentLoan);
        edit = new JButton();
        edit.setText("Edit profile");
        edit.setActionCommand("edit");
        edit.addActionListener(this);
        loggedPanel.add(edit);
        logout = new JButton();
        logout.setText("Log Out");
        logout.setActionCommand("logout");
        logout.addActionListener(this);
        loggedPanel.add(logout);
        JSeparator js = new JSeparator();
        loggedPanel.add(js);
        searchField = new JTextField();
        searchField.setHorizontalAlignment(SwingConstants.LEFT);
        loggedPanel.add(searchField);
        search = new JButton();
        search.setText("Search");
        search.setActionCommand("search");
        search.addActionListener(this);
        loggedPanel.add(search);
        displayMedia();
        loggedPanel.add(scroll, BorderLayout.SOUTH);
    }
    private void newAccount(){
        createAccount = new JPanel();
        createAccount.setLayout(new GridLayout(5,2,50,50));
        createAccount.setSize(300,250);
        createAccount.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        nameLabel = label("Full name");
        createAccount.add(nameLabel);
        nameField = new JTextField();
        nameField.setHorizontalAlignment(SwingConstants.LEFT);
        createAccount.add(nameField);
        emailLabel = label("Email");
        createAccount.add(emailLabel);
        emailField = new JTextField();
        emailField.setHorizontalAlignment(SwingConstants.LEFT);
        createAccount.add(emailField);
        passwordLabel = label("Password");
        createAccount.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setHorizontalAlignment(SwingConstants.LEFT);
        createAccount.add(passwordField);
        repeatPassLabel = label("Repeat password");
        createAccount.add(repeatPassLabel);
        repeatPassword = new JPasswordField();
        repeatPassword.setHorizontalAlignment(SwingConstants.LEFT);
        createAccount.add(repeatPassword);
        create = new JButton();
        create.setText("Create");
        create.setActionCommand("create");
        create.addActionListener(this);
        createAccount.add(create);
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setActionCommand("cancelnew");
        cancel.addActionListener(this);
        createAccount.add(cancel);
    }

    private void login(){
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5,2,10,10));
        loginPanel.setSize(300,250);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        emailLabel = label("Email");
        loginPanel.add(emailLabel);
        emailField = new JTextField();
        emailField.setHorizontalAlignment(SwingConstants.LEFT);
        loginPanel.add(emailField);
        passwordLabel = label("Password");
        loginPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setHorizontalAlignment(SwingConstants.LEFT);
        loginPanel.add(passwordField);
        login = new JButton();
        login.setText("Log In");
        login.setActionCommand("login");
        login.addActionListener(this);
        loginPanel.add(login);
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);
        loginPanel.add(cancel);
    }

    private void editProfile(User currentUser) throws SQLException {
        editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(5,2,10,10));
        nameLabel = label("Full name");
        editPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setText(currentUser.getName());
        nameField.setHorizontalAlignment(SwingConstants.LEFT);
        editPanel.add(nameField);
        emailLabel = label("Email");
        editPanel.add(emailLabel);
        emailField = new JTextField();
        emailField.setText(currentUser.getEmail());
        emailField.setHorizontalAlignment(SwingConstants.LEFT);
        editPanel.add(emailField);
        save = new JButton();
        save.setText("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        editPanel.add(save);
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setActionCommand("canceledit");
        cancel.addActionListener(this);
        editPanel.add(cancel);
    }

    private JLabel label(String labelText){
        label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        label.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        label.setForeground(Color.BLUE);
        return label;
    }

    private void displayMedia() {
        try {
            Media media = new Media();
            media.print();
            JTableHeader header = jt.getTableHeader();
            header.setBackground(Color.lightGray);
            header.setFont(new Font("Tahoma", Font.BOLD, 12));
            scroll = new JScrollPane(jt);
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
