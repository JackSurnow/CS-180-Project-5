import javax.swing.*;
import java.util.ArrayList;

public class Account {

    private Main main;

    public Account(Main main) {
        this.main = main;
    }

    public boolean checkAccount(String username, String password) {
        ArrayList<String> accounts = Main.getAccounts();

        for (String account : accounts) {
            if (account.substring(0, account.indexOf(";")).matches(username) && account
                    .substring(account.indexOf(";") + 1, account.lastIndexOf(";")).matches(password)) {
                main.setTeacher(account.substring(account.lastIndexOf(";") + 1).matches("true"));
                return true;
            }
        }
        return false;
    }

    public boolean newUsername(String username) {
        ArrayList<String> accounts = Main.getAccounts();

        for (String account : accounts) {
            if (account.substring(0, account.indexOf(";")).matches(username)) {
                JOptionPane.showMessageDialog(null,
                        "Error: Username already taken", "Username error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean logIn() {
        String username = JOptionPane.showInputDialog(null, "Enter your username.",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Please enter a username",
                    "Username log in error", JOptionPane.ERROR_MESSAGE);
            main.setValidAccount(false);
            return false;
        }

        String password = JOptionPane.showInputDialog(null, "Enter your password.",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Please enter a password",
                    "Password log in error", JOptionPane.ERROR_MESSAGE);
            main.setValidAccount(false);
            return false;
        }
        if (checkAccount(username, password)) {
            main.setUsername(username);
            main.setPassword(password);
            return true;
        }
        return false;
    }

    public boolean signUp() {
        ArrayList<String> accounts = Main.getAccounts();
        String username = JOptionPane.showInputDialog(null, "Enter your username (Do not use a semicolon).",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Please enter a username",
                    "Username sign up error", JOptionPane.ERROR_MESSAGE);
            main.setValidAccount(false);
            return false;
        } else if (username.contains(";")) {
            JOptionPane.showMessageDialog(null, "Error: Username contains semicolon",
                    "Username sign up error", JOptionPane.ERROR_MESSAGE);
            main.setValidAccount(false);
            return false;
        } else if (!newUsername(username)) {
            main.setValidAccount(false);
            return false;
        }

        String password = JOptionPane.showInputDialog(null, "Enter your password (Do not use a semicolon).",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Please enter a password",
                    "Password sign up error", JOptionPane.ERROR_MESSAGE);
            main.setValidAccount(false);
            return false;
        } else if (password.contains(";")) {
            JOptionPane.showMessageDialog(null, "Error: Password contains semicolon",
                    "Username sign up error", JOptionPane.ERROR_MESSAGE);
            main.setValidAccount(false);
            return false;
        }

        int teacher = JOptionPane.showConfirmDialog(null, "Are you a teacher?",
                "Post Forum", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (teacher != JOptionPane.CLOSED_OPTION) {
            if (newUsername(username) && !username.contains(";") && !password.contains(";") &&
                    (teacher == JOptionPane.YES_OPTION || teacher == JOptionPane.NO_OPTION)) {
                main.setUsername(username);
                main.setPassword(password);
                if (teacher == JOptionPane.YES_OPTION) {
                    main.setTeacher(true);
                    accounts.add(String.format("%s;%s;%b", username, password, true));
                } else {
                    main.setTeacher(false);
                    accounts.add(String.format("%s;%s;%b", username, password, false));
                }
                ExtraUtil.writeAccounts(accounts);
                Main.setAccounts(accounts);
                return true;
            } else {
                main.setValidAccount(false);
            }
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
            if (accounts.get(i).contains(main.getUsername() + ";" + main.getPassword())) {
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
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter a new username",
                            "Username change error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (!newUsername(username) && !username.contains(";")) {
                    main.setUsername(username);
                    accountInfo[0] = username;
                } else if (username.contains(";")) {
                    JOptionPane.showMessageDialog(null, "Error: Username contains semicolon",
                            "Username change error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                accounts.set(indexInfo, String.format("%s;%s;%s", accountInfo[0], accountInfo[1], accountInfo[2]));
                Main.setAccounts(accounts);
                ExtraUtil.writeAccounts(accounts);
            } else if (editMenu.equals("Change Password")) {
                password = JOptionPane.showInputDialog(null, "Enter your new password (Do not use a semicolon).",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);
                if (!password.contains(";") && !password.isEmpty()) {
                    main.setPassword(password);
                    accountInfo[1] = password;
                } else if (password.contains(";")) {
                    JOptionPane.showMessageDialog(null,
                            "Error: Password contains semicolon", "Password change error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter a new password",
                            "Password change error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                accounts.set(indexInfo, String.format("%s;%s;%s", accountInfo[0], accountInfo[1], accountInfo[2]));
                Main.setAccounts(accounts);
                ExtraUtil.writeAccounts(accounts);
            }
        }
    }

    public boolean deleteAccount(ArrayList<String> accounts) {
        int delete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?",
                "Post Forum", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (delete == JOptionPane.YES_OPTION) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).contains(main.getUsername() + ";" + main.getPassword())) {
                    accounts.remove(i);
                    ExtraUtil.writeAccounts(accounts);
                    Main.setAccounts(accounts);
                    break;
                }
            }

            main.setUsername(null);
            main.setPassword(null);
            return true;
        }
        return false;
    }

}
