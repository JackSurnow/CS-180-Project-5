import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ForumClient extends Thread implements Runnable {
    private static final String host = "localhost";
    private static final int port = 4242;

    private static ArrayList<String> currentUsers = new ArrayList<>();

    private static ForumClient forumClient = null;
    private static boolean stop = false;
    private static Socket socket;

    static {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObjectOutputStream os;
    private ObjectInputStream is;

    private static String postsFile;
    private static String accountsFile;
    private static String votesFile;

    private ArrayList<String> posts;
    private ArrayList<String> accounts;
    private ArrayList<String> votes;
    private ArrayList<JButton> postsButton;
    private ArrayList<JLabel> postsLabel;

    private String username;
    private String password;
    private boolean teacher;
    private ArrayList<String> post;
    private ArrayList<String> sort;
    private boolean validAccount;

    private final JFrame frame = new JFrame("Post Forum");
    private final Container content = frame.getContentPane();

    private final JFrame frameRight = new JFrame("Post Forum");
    private final Container contentRight = frameRight.getContentPane();

    private final JFrame frameRightVote = new JFrame("Post Forum");
    private final Container contentRightVote = frameRightVote.getContentPane();

    private final JFrame frameRightSort = new JFrame("Post Forum");
    private final Container contentRightSort = frameRightSort.getContentPane();

    private final JFrame frameRightGrade = new JFrame("Post Forum");
    private final Container contentRightGrade = frameRightGrade.getContentPane();

    private final JFrame frameScroll = new JFrame("Post Forum");
    private final Container contentScroll = frameScroll.getContentPane();

    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollableTextArea = new JScrollPane(textArea);

    private final GridBagConstraints c = new GridBagConstraints();

    private final JScrollPane jScrollPane = new JScrollPane(contentScroll);

    JButton viewRepliesByVotes;
    JButton importFiles;
    JButton gradeReplies;
    JButton viewGrades;
    JButton editAccount;
    JButton deleteAccount;
    JButton logOut;

    JButton createPost;
    JButton editPost;
    JButton deletePost;

    JButton replyVote;
    JButton reply;
    JButton upvote;

    JButton most;
    JButton least;
    JButton grade;

    public synchronized static ArrayList<String> getCurrentUsers() {
        return currentUsers;
    }

    public synchronized static String getPostsFile() {
        return postsFile;
    }

    public synchronized static String getAccountsFile() {
        return accountsFile;
    }

    public synchronized static String getVotesFile() {
        return votesFile;
    }

    public synchronized ArrayList<String> getPosts() {
        return posts;
    }

    public synchronized ArrayList<String> getAccounts() {
        return accounts;
    }

    public synchronized ArrayList<String> getVotes() {
        return votes;
    }

    public synchronized String getUsername() {
        return username;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized boolean isTeacher() {
        return teacher;
    }

    public synchronized ArrayList<String> getPost() {
        return post;
    }

    public synchronized ArrayList<String> getSort() {
        return sort;
    }

    public synchronized static void setCurrentUsers(ArrayList<String> currentUsers) {
        ForumClient.currentUsers = currentUsers;
    }

    public synchronized void setOs(ObjectOutputStream os) {
        this.os = os;
    }

    public synchronized void setIs(ObjectInputStream is) {
        this.is = is;
    }

    public synchronized static void setPostsFile(String postsFile) {
        ForumClient.postsFile = postsFile;
    }

    public synchronized static void setAccountsFile(String accountsFile) {
        ForumClient.accountsFile = accountsFile;
    }

    public synchronized static void setVotesFile(String votesFile) {
        ForumClient.votesFile = votesFile;
    }

    public synchronized void setPosts(ArrayList<String> posts, boolean send) {
        this.posts = posts;
        if (send) {
            forumClient.sendPosts();
        }
    }

    public synchronized void setAccounts(ArrayList<String> accounts, boolean send) {
        this.accounts = accounts;
        if (send) {
            forumClient.sendAccounts();
        }
    }

    public synchronized void setVotes(ArrayList<String> votes, boolean send) {
        this.votes = votes;
        if (send) {
            forumClient.sendVotes();
        }
    }

    public synchronized void setPostsButton(ArrayList<JButton> postsButton) {
        this.postsButton = postsButton;
    }

    public synchronized void setPostsLabel(ArrayList<JLabel> postsLabel) {
        this.postsLabel = postsLabel;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }

    public synchronized void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public synchronized void setPost(ArrayList<String> post, boolean send) {
        this.post = post;
        if (send) {
            try {
                if (!post.isEmpty()) {
                    String fileName = String.format("Post%d.txt", Integer.parseInt(post.get(0)
                            .substring(post.get(0).indexOf(" ") + 1, post.get(0).indexOf(":"))));
                    forumClient.sendPost(fileName, post);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setSort(ArrayList<String> sort) {
        this.sort = sort;
    }

    public synchronized void setValidAccount(boolean validAccount) {
        this.validAccount = validAccount;
    }

    public ForumClient() {
        forumClient = this;

        this.os = null;
        this.is = null;

        this.posts = null;
        this.accounts = null;
        this.votes = null;
        this.postsButton = new ArrayList<>();
        this.postsLabel = new ArrayList<>();

        this.username = null;
        this.password = null;
        this.teacher = true;
        this.post = new ArrayList<>();
        this.sort = new ArrayList<>();
        this.validAccount = true;
    }

    public static void main(String[] args) {
        try {
            ForumClient forumClient = new ForumClient();

            forumClient.setOs(new ObjectOutputStream(socket.getOutputStream()));
            forumClient.setIs(new ObjectInputStream(socket.getInputStream()));

            while (ForumClient.getPostsFile() == null || ForumClient.getAccountsFile() == null || ForumClient
                    .getVotesFile() == null || forumClient.getPosts() == null || forumClient.getAccounts() == null
                    || forumClient.getVotes() == null) {
                forumClient.Receive();
            }

            SwingUtilities.invokeLater(forumClient);

            while (!stop && !socket.isClosed()) {
                forumClient.Receive();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Account account = new Account(forumClient);
            boolean valid = false;

            do {
                validAccount = true;
                if (accounts != null && !accounts.isEmpty()) {
                    String[] options = {"Log In", "Sign Up"};
                    String logInMenu = (String) JOptionPane.showInputDialog(null, "Log in or sign up.",
                            "Post Forum", JOptionPane.PLAIN_MESSAGE, null, options, null);

                    if (logInMenu != null) {
                        if (logInMenu.equals("Log In")) {
                            if (account.logIn()) {
                                valid = true;
                            }
                        } else if (logInMenu.equals("Sign Up")) {
                            if (account.signUp()) {
                                valid = true;
                            }
                        }
                    }
                } else if (accounts != null) {
                    if (account.signUp()) {
                        valid = true;
                    }
                }
            } while (!validAccount);

            if (valid) {
                content.setLayout(new BorderLayout());

                contentScroll.setLayout(new GridBagLayout());
                c.gridheight = 50;
                c.gridwidth = 500;
                c.anchor = GridBagConstraints.LINE_START;

                contentRight.setLayout(new GridLayout(1, 1));
                contentRightVote.setLayout(new GridLayout(2, 1));
                contentRightSort.setLayout(new GridLayout(2, 1));
                contentRightGrade.setLayout(new GridLayout(1, 1));

                scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                content.add(scrollableTextArea, BorderLayout.CENTER);

                if (posts.size() > 0) {
                    for (int i = 0; i < posts.size(); i++) {
                        c.gridy = 50 * i;
                        contentScroll.add(displayPosts(posts, postsButton, postsLabel,
                                i, actionListener), c);
                    }
                }

                jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                jScrollPane.setPreferredSize(new Dimension(350, 800));

                content.add(jScrollPane, BorderLayout.WEST);

                viewRepliesByVotes = new JButton("View Votes");
                viewRepliesByVotes.addActionListener(actionListener);
                importFiles = new JButton("Import Files");
                importFiles.addActionListener(actionListener);
                gradeReplies = new JButton("Grade Replies");
                gradeReplies.addActionListener(actionListener);
                viewGrades = new JButton("View Grades");
                viewGrades.addActionListener(actionListener);
                editAccount = new JButton("Edit Account");
                editAccount.addActionListener(actionListener);
                deleteAccount = new JButton("Delete Account");
                deleteAccount.addActionListener(actionListener);
                logOut = new JButton("Log Out");
                logOut.addActionListener(actionListener);

                createPost = new JButton("Create");
                createPost.addActionListener(actionListener);
                editPost = new JButton("Edit");
                editPost.addActionListener(actionListener);
                deletePost = new JButton("Delete");
                deletePost.addActionListener(actionListener);

                reply = new JButton("Reply");
                reply.addActionListener(actionListener);

                replyVote = new JButton("Reply");
                replyVote.addActionListener(actionListener);
                upvote = new JButton("Vote");
                upvote.addActionListener(actionListener);

                most = new JButton("Most");
                most.addActionListener(actionListener);
                least = new JButton("Least");
                least.addActionListener(actionListener);
                grade = new JButton("Grade");
                grade.addActionListener(actionListener);


                JPanel panelTop = new JPanel();
                panelTop.add(importFiles);
                if (teacher) {
                    panelTop.add(viewRepliesByVotes);
                }
                if (teacher) {
                    panelTop.add(gradeReplies);
                } else {
                    panelTop.add(viewGrades);
                }
                panelTop.add(editAccount);
                panelTop.add(deleteAccount);
                panelTop.add(logOut);
                content.add(panelTop, BorderLayout.NORTH);

                JPanel panelBot = new JPanel();
                panelBot.add(createPost);
                panelBot.add(editPost);
                panelBot.add(deletePost);

                if (teacher) {
                    content.add(panelBot, BorderLayout.SOUTH);
                }

                contentRightVote.add(replyVote);
                contentRightVote.add(upvote);
                contentRight.add(reply);

                contentRightSort.add(most);
                contentRightSort.add(least);
                contentRightGrade.add(grade);

                frame.setSize(1000, 800);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.addWindowListener(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendPosts() {
        try {
            os.writeObject(new Message("posts", getPosts()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendAccounts() {
        try {
            os.writeObject(new Message("accounts", getAccounts()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendVotes() {
        try {
            os.writeObject(new Message("votes", getVotes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendPost(String fileName, ArrayList<String> contents) {
        try {
            os.writeObject(new Message("post", contents, "GUI;" + fileName + ";" + getUsername()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendRequest(int postNum, String modifier) {
        try {
            if (modifier != null) {
                os.writeObject(new Message("Request", postNum, modifier));
            } else {
                os.writeObject(new Message("Request", postNum));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendCreate(ArrayList<String> postImport) {
        try {
            os.writeObject(new Message("Create", postImport, getUsername()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendEdit(ArrayList<String> postImport, String editMenu, String change) {
        try {
            os.writeObject(new Message("Edit", postImport, editMenu + ";" + change + ";" + getUsername()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendDelete(ArrayList<String> postImport, int deleteConfirmation) {
        try {
            os.writeObject(new Message("Delete", postImport, deleteConfirmation + ";" + getUsername()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendAll(String modifier) {
        try {
            os.writeObject(new Message("All", modifier));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendImportReply(String fileName, int parentPost) {
        try {
            os.writeObject(new Message("Request", parentPost, "NO;" + fileName + ";" + getUsername()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendUsername(String username) {
        try {
            os.writeObject(new Message("Username", username));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Receive() {
        try {
            os.reset();
            Message message = (Message) is.readObject();
            if (message != null) {
                switch (message.getListName()) {
                    case "File Names" -> {
                        setPostsFile(message.getContents().get(0));
                        setAccountsFile(message.getContents().get(1));
                        setVotesFile(message.getContents().get(2));
                    }
                    case "Users" -> setCurrentUsers(message.getContents());
                    case "posts" -> setPosts(message.getContents(), false);
                    case "accounts" -> setAccounts(message.getContents(), false);
                    case "votes" -> setVotes(message.getContents(), false);
                    case "Response" -> {
                        String modifier = "";
                        String fileName = "";
                        String studentName = "";
                        if (message.getModifier() != null) {
                            modifier = message.getModifier().substring(0, message.getModifier().indexOf(";"));
                            if (modifier.equals("GUI")) {
                                studentName = message.getModifier().substring(message.getModifier().indexOf(";") + 1);
                            } else if (modifier.equals("NO")) {
                                fileName = message.getModifier().substring(message.getModifier().indexOf(";") + 1,
                                        message.getModifier().lastIndexOf(";"));
                                studentName = message.getModifier().substring(message.getModifier()
                                        .lastIndexOf(";") + 1);
                            }
                        }
                        if (studentName.equals(username) || message.getContents().isEmpty() || (post != null &&
                                !post.isEmpty() && message.getContents() != null && !message.getContents().isEmpty()
                                && post.get(0).substring(post.get(0).indexOf(" ") + 1, post.get(0).indexOf(":"))
                                .matches(message.getContents().get(0).substring(message.getContents().get(0)
                                                .indexOf(" ") + 1, message.getContents().get(0).indexOf(":"))))) {
                            if (modifier.equals("GUI")) {
                                setPost(message.getContents(), false);
                                GUI();
                            } else if (modifier.equals("NO")) {
                                if (studentName.equals(username)) {
                                    setPost(message.getContents(), false);
                                    FileImports fileImports = new FileImports(forumClient);
                                    fileImports.ImportReplyResponse(fileName, message.getContents());
                                }
                            }
                        } else if (modifier.equals("GUI")) {
                            GUIScroll();
                        }
                    }
                    case "All" -> {
                        String modifier = message.getModifier().substring(0, message.getModifier().indexOf(";"));
                        String studentName = message.getModifier().substring(message.getModifier().indexOf(";") + 1);
                        if (message.getModifier() != null) {
                            switch (modifier) {
                                case "Grade" -> {
                                    Grader grader = new Grader(forumClient);
                                    grader.gradeLogic(message);
                                }
                                case "Sort Replies" -> {
                                    SortLists sortLists = new SortLists(forumClient);
                                    if (GUISort(sortLists.sortReplies(message, studentName))) {
                                        content.add(contentRightGrade, BorderLayout.EAST);
                                        content.validate();
                                        content.repaint();
                                    }
                                }
                                case "Sort Grades" -> {
                                    SortLists sortLists = new SortLists(forumClient);
                                    GUISort(sortLists.sortGrades(message, studentName));
                                }
                                case "Sort Most" -> {
                                    SortLists sortLists = new SortLists(forumClient);
                                    if (GUISort(sortLists.sortVotesMost(message))) {
                                        content.add(contentRightSort, BorderLayout.EAST);
                                        content.validate();
                                        content.repaint();
                                    }
                                }
                                case "Sort Least" -> {
                                    SortLists sortLists = new SortLists(forumClient);
                                    if (GUISort(sortLists.sortVotesLeast(message))) {
                                        content.add(contentRightSort, BorderLayout.EAST);
                                        content.validate();
                                        content.repaint();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }

    public void GUI() {
        textArea.setText("");
        for (int i = 0; i < post.size(); i++) {
            textArea.append(post.get(i));
            if (i < post.size() - 1) {
                textArea.append("\n");
            }
        }

        GUIScroll();

        content.remove(contentRight);
        content.remove(contentRightVote);
        content.remove(contentRightSort);
        content.remove(contentRightGrade);
        if (!post.isEmpty()) {
            if (post.get(4).matches("There are no replies.")) {
                content.add(contentRight, BorderLayout.EAST);
            } else {
                content.add(contentRightVote, BorderLayout.EAST);
            }
        }
        content.validate();
        content.repaint();
    }

    public synchronized void GUIScroll() {
        contentScroll.removeAll();
        contentScroll.validate();
        forumClient.setPostsButton(new ArrayList<>());
        forumClient.setPostsLabel(new ArrayList<>());
        postsButton = new ArrayList<>();
        postsLabel = new ArrayList<>();

        if (posts.size() > 0) {
            for (int i = 0; i < posts.size(); i++) {
                c.gridy = 50 * i;
                contentScroll.add(displayPosts(posts, postsButton, postsLabel,
                        i, actionListener), c);
            }
        }

        contentScroll.validate();
        contentScroll.repaint();
        content.validate();
        content.repaint();
    }

    public boolean GUISort(ArrayList<String> sort) {
        if (sort != null && !sort.isEmpty()) {
            textArea.setText("");
            for (int i = 0; i < sort.size(); i++) {
                textArea.append(sort.get(i));
                if (i < sort.size() - 1) {
                    textArea.append("\n");
                }
            }

            content.remove(contentRight);
            content.remove(contentRightVote);
            content.remove(contentRightSort);
            content.remove(contentRightGrade);
            content.validate();
            content.repaint();

            return true;
        }
        return false;
    }

    public static JPanel displayPosts(ArrayList<String> posts, ArrayList<JButton> postsButton,
                                      ArrayList<JLabel> postsLabel, int i, ActionListener actionListener) {
        postsButton.add(new JButton(String.format("Post %d", Integer.parseInt(posts.get(i).substring(
                posts.get(i).indexOf(" ") + 1, posts.get(i).indexOf(":"))))));
        postsButton.get(i).addActionListener(actionListener);
        postsLabel.add(new JLabel(posts.get(i)));
        JPanel panel = new JPanel();
        panel.add(postsButton.get(i));
        panel.add(postsLabel.get(i));
        return panel;
    }

    WindowListener listener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException x) {
                x.printStackTrace();
            }
            stop = true;
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewRepliesByVotes || e.getSource() == most) {
                sendAll("Sort Most;" + getUsername());
            } else if (e.getSource() == least) {
                sendAll("Sort Least;" + getUsername());
            } else if (e.getSource() == importFiles) {
                FileImports fileImports = new FileImports(forumClient);
                fileImports.importLogic();
            } else if (e.getSource() == gradeReplies) {
                String studentName = JOptionPane.showInputDialog(null, "Who's replies do you want to grade?",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);
                if (studentName != null && !studentName.isEmpty()) {
                    sendAll("Sort Replies;" + studentName);
                } else if (studentName != null) {
                    JOptionPane.showMessageDialog(null, "Enter a username.",
                            "Post Forum", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == viewGrades) {
                sendAll("Sort Grades;" + getUsername());
            } else if (e.getSource() == editAccount) {
                Account account = new Account(forumClient);
                account.editAccount(accounts);
            } else if (e.getSource() == deleteAccount) {
                Account account = new Account(forumClient);
                if (account.deleteAccount(accounts)) {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            } else if (e.getSource() == logOut) {
                setUsername(null);
                setPassword(null);
                setTeacher(false);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else if (e.getSource() == createPost) {
                String title = JOptionPane.showInputDialog(null, "What is the new title?",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);
                String description = JOptionPane.showInputDialog(null, "What is the new description?",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);
                ArrayList<String> create = new ArrayList<>();
                create.add(title);
                create.add(description);
                sendCreate(create);
            } else if (e.getSource() == editPost) {
                if (!post.isEmpty()) {
                    String[] options = {"Change Title", "Change Description"};
                    String editMenu = (String) JOptionPane.showInputDialog(null, "What do you want to edit?",
                            "Post Forum", JOptionPane.PLAIN_MESSAGE, null, options, null);
                    if (editMenu.equals("Change Title")) {
                        String title = JOptionPane.showInputDialog(null, "What is the new title?",
                                "Post Forum", JOptionPane.QUESTION_MESSAGE);
                        sendEdit(post, editMenu, title);
                    } else if (editMenu.equals("Change Description")) {
                        String description = JOptionPane.showInputDialog(null, "What is the new description?",
                                "Post Forum", JOptionPane.QUESTION_MESSAGE);
                        sendEdit(post, editMenu, description);
                    }
                }
            } else if (e.getSource() == deletePost) {
                if (!post.isEmpty()) {
                    int deleteConfirmation = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete this Post?", "Post Forum",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    sendDelete(post, deleteConfirmation);
                }
            } else if (e.getSource() == upvote) {
                Vote vote = new Vote(forumClient);
                vote.voteLogic();
            } else if (e.getSource() == replyVote || e.getSource() == reply) {
                Reply reply = new Reply(forumClient);
                reply.replyManual(post);
            } else if (e.getSource() == grade) {
                sendAll("Grade;" + getUsername());
            } else if (e.getSource() instanceof JButton) {
                try {
                    sendRequest(Integer.parseInt(((JButton) e.getSource()).getText().substring(((JButton)
                            e.getSource()).getText().indexOf(" ") + 1)), "GUI;" + username);
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        }
    };
}
