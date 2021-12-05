import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * Purdue University -- CS18000
 * Project 5
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class Account {
    private final ForumClient forumClient;

    public Account(ForumClient forumClient) {
        this.forumClient = forumClient;
    }

    public boolean checkAccount(String username, String password) {
        ArrayList<String> accounts = forumClient.getAccounts();
        for (String account : accounts) {
            if (account.substring(0, account.indexOf(";")).matches(username) && account
                    .substring(account.indexOf(";") + 1, account.lastIndexOf(";")).matches(password)) {
                forumClient.setTeacher(account.substring(account.lastIndexOf(";") + 1).matches("true"));
                return true;
            }
        }
        forumClient.setValidAccount(false);
        JOptionPane.showMessageDialog(null, "Incorrect username or password.",
                "Post Forum", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean newUsername(String username) {
        ArrayList<String> accounts = forumClient.getAccounts();

        for (String account : accounts) {
            if (account.substring(0, account.indexOf(";")).matches(username)) {
                forumClient.setValidAccount(false);
                JOptionPane.showMessageDialog(null, "This username is already taken.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean alreadyLoggedIn(String username) {
        return ForumClient.getCurrentUsers().contains(username);
    }

    public boolean logIn() {
        String username = JOptionPane.showInputDialog(null, "Enter your username.",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);

        if (username != null && !username.isEmpty()) {
            if (!alreadyLoggedIn(username)) {
                String password = JOptionPane.showInputDialog(null, "Enter your password.",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);

                if (password != null && !password.isEmpty()) {
                    if (checkAccount(username, password)) {
                        forumClient.setUsername(username);
                        forumClient.setPassword(password);
                        forumClient.sendUsername(username);
                        return true;
                    } else {
                        forumClient.setValidAccount(false);
                    }
                } else if (password != null) {
                    forumClient.setValidAccount(false);
                    JOptionPane.showMessageDialog(null, "Enter your password.",
                            "Post Forum", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                forumClient.setValidAccount(false);
                JOptionPane.showMessageDialog(null, "You are already logged in on another device.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        } else if (username != null) {
            forumClient.setValidAccount(false);
            JOptionPane.showMessageDialog(null, "Enter your username.",
                    "Post Forum", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public boolean signUp() {
        ArrayList<String> accounts = forumClient.getAccounts();
        String username = JOptionPane.showInputDialog(null, "Enter a username (Do not use a semicolon).",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);

        if (username != null && !username.isEmpty()) {
            if (!username.contains(";")) {
                if (newUsername(username)) {
                    String password = JOptionPane.showInputDialog(null, "Enter a password (Do not use a semicolon).",
                            "Post Forum", JOptionPane.QUESTION_MESSAGE);

                    if (password != null && !password.isEmpty()) {
                        if (!password.contains(";")) {
                            int teacher = JOptionPane.showConfirmDialog(null, "Are you a teacher?",
                                    "Post Forum", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if (teacher != JOptionPane.CLOSED_OPTION) {
                                forumClient.setUsername(username);
                                forumClient.setPassword(password);
                                forumClient.sendUsername(username);
                                if (teacher == JOptionPane.YES_OPTION) {
                                    forumClient.setTeacher(true);
                                    accounts.add(String.format("%s;%s;%b", username, password, true));
                                } else {
                                    forumClient.setTeacher(false);
                                    accounts.add(String.format("%s;%s;%b", username, password, false));
                                }
                                forumClient.setAccounts(accounts, true);
                                return true;
                            }
                        } else {
                            forumClient.setValidAccount(false);
                            JOptionPane.showMessageDialog(null, "Your password cannot contain a semicolon.",
                                    "Post Forum", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (password != null) {
                        forumClient.setValidAccount(false);
                        JOptionPane.showMessageDialog(null, "Enter a password.",
                                "Post Forum", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                forumClient.setValidAccount(false);
                JOptionPane.showMessageDialog(null, "Your username cannot contain a semicolon.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        } else if (username != null) {
            forumClient.setValidAccount(false);
            JOptionPane.showMessageDialog(null, "Enter a username",
                    "Post Forum", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void editAccount(ArrayList<String> accounts) {
        String[] options = {"Change Username", "Change Password"};
        String editMenu = (String) JOptionPane.showInputDialog(null, "Log in or sign up.",
                "Post Forum", JOptionPane.PLAIN_MESSAGE, null, options, null);

        String[] accountInfo = new String[3];
        int indexInfo = 0;

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).contains(forumClient.getUsername() + ";" + forumClient.getPassword())) {
                accountInfo = accounts.get(i).split(";");
                indexInfo = i;
                break;
            }
        }

        String username;
        String password;

        if (editMenu != null) {
            if (editMenu.equals("Change Username")) {
                username = JOptionPane.showInputDialog(null, "Enter your new username (Do not use a semicolon).",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);

                if (username != null && !username.isEmpty()) {
                    if (!username.contains(";")) {
                        if (newUsername(username)) {
                            forumClient.setUsername(username);
                            forumClient.sendUsername(username);
                            accountInfo[0] = username;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Your username cannot contain a semicolon.",
                                "Post Forum", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (username != null) {
                    JOptionPane.showMessageDialog(null, "Enter a username",
                            "Post Forum", JOptionPane.ERROR_MESSAGE);
                }

                accounts.set(indexInfo, String.format("%s;%s;%s", accountInfo[0], accountInfo[1], accountInfo[2]));
                forumClient.setAccounts(accounts, true);
            } else if (editMenu.equals("Change Password")) {
                password = JOptionPane.showInputDialog(null, "Enter your new password (Do not use a semicolon).",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);

                if (password != null && !password.isEmpty()) {
                    if (!password.contains(";")) {
                        forumClient.setPassword(password);
                        accountInfo[1] = password;
                    } else {
                        JOptionPane.showMessageDialog(null, "Your password cannot contain a semicolon.",
                                "Post Forum", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (password != null) {
                    JOptionPane.showMessageDialog(null, "Enter a password.",
                            "Post Forum", JOptionPane.ERROR_MESSAGE);
                }

                accounts.set(indexInfo, String.format("%s;%s;%s", accountInfo[0], accountInfo[1], accountInfo[2]));
                forumClient.setAccounts(accounts, true);
            }
        }
    }

    public boolean deleteAccount(ArrayList<String> accounts) {
        int delete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?",
                "Post Forum", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (delete == JOptionPane.YES_OPTION) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).contains(forumClient.getUsername() + ";" + forumClient.getPassword())) {
                    accounts.remove(i);
                    forumClient.setAccounts(accounts, true);
                    break;
                }
            }

            forumClient.setUsername(null);
            forumClient.setPassword(null);
            return true;
        }
        return false;
    }
}
