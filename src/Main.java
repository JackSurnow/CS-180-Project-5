import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main extends JComponent implements Runnable{
    private static String postsFile = "Posts.txt";
    private static String accountsFile = "Accounts.txt";
    private static String votesFile = "Votes.txt";

    private static Main[] currentUsers = new Main[100];
    private int userIndex;

    private static ArrayList<String> posts = new ArrayList<>();
    private static ArrayList<String> accounts = new ArrayList<>();
    private static ArrayList<String> votes = new ArrayList<>();
    private static ArrayList<JButton> postsButton = new ArrayList<>();
    private static ArrayList<JLabel> postsLabel = new ArrayList<>();

    private String username;
    private String password;
    private boolean teacher;
    private ArrayList<String> post;
    private ArrayList<String> sort;
    private boolean again;
    private boolean validAccount;

    private JFrame frame = new JFrame("Post Forum");
    private Container content = frame.getContentPane();

    private JFrame frameRight = new JFrame("Post Forum");
    private Container contentRight = frameRight.getContentPane();

    private JFrame frameRightVote = new JFrame("Post Forum");
    private Container contentRightVote = frameRightVote.getContentPane();

    private JFrame frameRightSort = new JFrame("Post Forum");
    private Container contentRightSort = frameRightSort.getContentPane();

    private JFrame frameRightGrade = new JFrame("Post Forum");
    private Container contentRightGrade = frameRightGrade.getContentPane();

    private static JFrame frameScroll = new JFrame("Post Forum");
    private static Container contentScroll = frameScroll.getContentPane();

    private static JTextArea textArea = new JTextArea();
    private static JScrollPane scrollableTextArea = new JScrollPane(textArea);

    private GridBagConstraints c = new GridBagConstraints();

    private JScrollPane jScrollPane = new JScrollPane(contentScroll);

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

    public synchronized static String getPostsFile() {
        return postsFile;
    }

    public synchronized static String getAccountsFile() {
        return accountsFile;
    }

    public synchronized static String getVotesFile() {
        return votesFile;
    }

    public synchronized static ArrayList<String> getPosts() {
        return posts;
    }

    public synchronized static ArrayList<String> getAccounts() {
        return accounts;
    }

    public synchronized static ArrayList<String> getVotes() {
        return votes;
    }

    public synchronized static ArrayList<JButton> getPostsButton() {
        return postsButton;
    }

    public synchronized static ArrayList<JLabel> getPostsLabel() {
        return postsLabel;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public ArrayList<String> getPost() {
        return post;
    }

    public ArrayList<String> getSort() {
        return sort;
    }

    public synchronized static void setPosts(ArrayList<String> posts) {
        Main.posts = posts;
    }

    public synchronized static void setAccounts(ArrayList<String> accounts) {
        Main.accounts = accounts;
    }

    public synchronized static void setVotes(ArrayList<String> votes) {
        Main.votes = votes;
    }

    public synchronized static void setPostsButton(ArrayList<JButton> postsButton) {
        Main.postsButton = postsButton;
    }

    public synchronized static void setPostsLabel(ArrayList<JLabel> postsLabel) {
        Main.postsLabel = postsLabel;
    }

    public void setUserIndex(int userIndex) {
        this.userIndex = userIndex;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public void setPost(ArrayList<String> post) {
        this.post = post;
    }

    public void setSort(ArrayList<String> sort) {
        this.sort = sort;
    }

    public void setValidAccount(boolean validAccount) {
        this.validAccount = validAccount;
    }

    public void GUI() {
        textArea.setText("");
        for (int i = 0; i < post.size(); i++) {
            textArea.append(post.get(i));
            if (i < post.size() - 1) {
                textArea.append("\n");
            }
        }

        contentScroll.removeAll();
        contentScroll.validate();
        Main.setPostsButton(new ArrayList<>());
        Main.setPostsLabel(new ArrayList<>());

        if (posts.size() > 0) {
            for (int i = 0; i < posts.size(); i++) {
                c.gridy = 50 * i;
                contentScroll.add(GUIDisplay.displayPosts(posts, postsButton, postsLabel,
                        i, actionListener), c);
            }
        }

        contentScroll.validate();
        contentScroll.repaint();

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

    WindowListener listener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            if (again) {
                String[] args = {};
                main(args);
            }
        }
    };

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewRepliesByVotes || e.getSource() == most) {
                SortLists sortLists = new SortLists(currentUsers[userIndex]);
                if (GUISort(sortLists.sortVotesMost(posts))) {
                    content.add(contentRightSort, BorderLayout.EAST);
                    content.validate();
                    content.repaint();
                }
            } else if (e.getSource() == least) {
                SortLists sortLists = new SortLists(currentUsers[userIndex]);
                if (GUISort(sortLists.sortVotesLeast(posts))) {
                    content.add(contentRightSort, BorderLayout.EAST);
                    content.validate();
                    content.repaint();
                }
            } else if (e.getSource() == importFiles) {
                String[] options;
                if (teacher) {
                    options = new String[]{"Import Post", "Import Reply"};
                } else {
                    options = new String[]{"Import Reply"};
                }
                String importMenu = (String) JOptionPane.showInputDialog(null, "What do you want to import?",
                        "Post Forum", JOptionPane.PLAIN_MESSAGE, null, options, null);

                FileImports fileImports = new FileImports(currentUsers[userIndex]);
                boolean valid = false;

                if (importMenu.equals("Import Post")) {
                    valid = fileImports.importPost(posts);
                } else if (importMenu.equals("Import Reply")) {
                    valid = fileImports.importReply(post);
                }

               if (valid) {
                   GUI();
               }
            } else if (e.getSource() == gradeReplies) {
                SortLists sortLists = new SortLists(currentUsers[userIndex]);
                String studentName = JOptionPane.showInputDialog(null, "Who's replies do you want to grade?",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);
                if (studentName != null) {
                    if (GUISort(sortLists.sortReplies(posts, studentName))) {
                        content.add(contentRightGrade, BorderLayout.EAST);
                        content.validate();
                        content.repaint();
                    }
                }
            } else if (e.getSource() == viewGrades) {
                SortLists sortLists = new SortLists(currentUsers[userIndex]);
                GUISort(sortLists.sortGrades(posts));
            } else if (e.getSource() == editAccount) {
                Account account = new Account(currentUsers[userIndex]);
                account.editAccount(accounts);
            } else if (e.getSource() == deleteAccount) {
                Account account = new Account(currentUsers[userIndex]);
                if (account.deleteAccount(accounts)) {
                    again = true;
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            } else if (e.getSource() == logOut) {
                username = null;
                password = null;
                teacher = false;
                again = true;
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else if (e.getSource() == createPost) {
                PostManager postManager = new PostManager(currentUsers[userIndex]);
                if (postManager.createPost(Main.getPosts(), new ArrayList<>(), false)) {
                    GUI();
                }
            } else if (e.getSource() == editPost) {
                PostManager postManager = new PostManager(currentUsers[userIndex]);
                if (postManager.editPost(Main.getPosts(), getPost())) {
                    GUI();
                }
            } else if (e.getSource() == deletePost) {
                PostManager postManager = new PostManager(currentUsers[userIndex]);
                if (postManager.deletePost(Main.getPosts(), getPost())) {
                    GUI();
                }
            } else if (e.getSource() == upvote) {
                Vote vote = new Vote(currentUsers[userIndex]);
                String replyNumber = JOptionPane.showInputDialog(null, "Which reply do you want to upvote?",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE);

                boolean valid = false;
                if (replyNumber != null) {
                    if (replyNumber.toLowerCase().contains("reply ")) {
                        replyNumber = replyNumber.substring(replyNumber.indexOf(" ") + 1);
                    }

                    for (String value : post) {
                        if (value.contains(":") && value.substring(0, value.indexOf(":") + 1)
                                .contains("Reply " + replyNumber.concat(":"))) {
                            valid = true;
                            break;
                        }
                    }
                }

                if (valid && vote.checkVoted(post, votes, replyNumber)) {
                    if (vote.upVote(post, votes, replyNumber)) {
                        GUI();
                    }
                }
            } else if (e.getSource() == replyVote || e.getSource() == reply) {
                Reply reply = new Reply(currentUsers[userIndex]);
                if (reply.replyManual(post)) {
                    GUI();
                }
            } else if (e.getSource() == grade) {
                Grader grader = new Grader(currentUsers[userIndex]);
                try {
                    int index = Integer.parseInt(JOptionPane.showInputDialog(null, "Select a reply index",
                            "Post Forum", JOptionPane.QUESTION_MESSAGE));
                    String replyMessage = sort.get(index - 1).substring(sort.get(index - 1).indexOf("R"));
                    String replyNum = replyMessage.substring(replyMessage.indexOf("y") + 2, replyMessage.indexOf(":"));

                    boolean done = false;
                    if (index != -1) {
                        for (int i = 0; i < posts.size(); i++) {
                            int postFile = Integer.parseInt(posts.get(i).substring(posts.get(i).indexOf(" ") + 1,
                                    posts.get(i).indexOf(":")));
                            try (BufferedReader br = new BufferedReader(new FileReader(String.format("Post%d.txt",
                                    postFile)))) {
                                String line = br.readLine();

                                PostReader postReader = new PostReader();
                                while (!done && line != null) {
                                    if (line.contains(replyMessage)) {
                                        post = postReader.readPost(postFile);
                                        String grade = JOptionPane.showInputDialog(null, "Enter " +
                                                "a grade.", "Post Forum", JOptionPane.QUESTION_MESSAGE);
                                        if (grade != null) {
                                            grader.gradePost(post, replyNum, grade, postFile);
                                            GUI();
                                        }
                                        done = true;
                                    }
                                    line = br.readLine();
                                }
                            } catch (Exception c) {
                                c.printStackTrace();
                            }

                            if (done) {
                                break;
                            }
                        }
                    }
                } catch (NumberFormatException x) {
                    x.printStackTrace();
                }
            } else if (e.getSource() instanceof JButton) {
                PostReader postReader = new PostReader();
                post = postReader.readPost(Integer.parseInt(((JButton) e.getSource())
                        .getText().substring(((JButton) e.getSource()).getText().indexOf(" ") + 1)));
                GUI();
            }
        }
    };

    public Main() {
        this.username = null;
        this.password = null;
        this.teacher = true;
        this.post = new ArrayList<>();
        this.sort = new ArrayList<>();
        this.again = false;
        this.validAccount = true;
        this.userIndex = -1;
    }

    public static void main(String[] args) {
        int index = -1;
        for (int i = 0; i < currentUsers.length; i++) {
            if (currentUsers[i] == null) {
                currentUsers[i] = new Main();
                currentUsers[i].setUserIndex(i);
                index = i;
                break;
            }
        }
        if (index != -1) {
            SwingUtilities.invokeLater(currentUsers[index]);
        }
    }

    public void run() {
        frame = new JFrame("Post Forum");
        content = frame.getContentPane();

        frameRight = new JFrame("Post Forum");
        contentRight = frameRight.getContentPane();

        frameRightVote = new JFrame("Post Forum");
        contentRightVote = frameRightVote.getContentPane();

        frameRightSort = new JFrame("Post Forum");
        contentRightSort = frameRightSort.getContentPane();

        frameRightGrade = new JFrame("Post Forum");
        contentRightGrade = frameRightGrade.getContentPane();

        frameScroll = new JFrame("Post Forum");
        contentScroll = frameScroll.getContentPane();

        textArea = new JTextArea();
        scrollableTextArea = new JScrollPane(textArea);

        c = new GridBagConstraints();

        jScrollPane = new JScrollPane(contentScroll);

        accounts = ExtraUtil.readAccounts();
        Account account = new Account(currentUsers[userIndex]);
        boolean valid = false;

        do {
            validAccount = true;
            if (accounts != null && !accounts.isEmpty()) {
                String[] options = {"Log In", "Sign Up"};
                String logInMenu = (String) JOptionPane.showInputDialog(null, "Log in or sign up.",
                        "Post Forum", JOptionPane.PLAIN_MESSAGE, null, options, null);

                if (logInMenu.equals("Log In")) {
                    if (account.logIn()) {
                        valid = true;
                    }
                } else if (logInMenu.equals("Sign Up")) {
                    if (account.signUp()) {
                        valid = true;
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

            PostReader postReader = new PostReader();
            posts = postReader.readPosts();
            post = new ArrayList<>();
            sort = new ArrayList<>();
            votes = ExtraUtil.readVotes();

            scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            content.add(scrollableTextArea, BorderLayout.CENTER);

            if (posts.size() > 0) {
                for (int i = 0; i < posts.size(); i++) {
                    c.gridy = 50 * i;
                    contentScroll.add(GUIDisplay.displayPosts(posts, postsButton, postsLabel,
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
    }
}