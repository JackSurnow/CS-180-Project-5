import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;

public class ForumServer extends Thread implements Runnable {
    private static final int port = 4242;

    private static final String postsFile = "Posts.txt";
    private static final String accountsFile = "Accounts.txt";
    private static final String votesFile = "Votes.txt";

    private static final ArrayList<ForumServerThread> forumServerThreads = new ArrayList<>();

    private static ArrayList<String> posts = ExtraUtil.readPosts();
    private static ArrayList<String> accounts = ExtraUtil.readAccounts();
    private static ArrayList<String> votes = ExtraUtil.readVotes();

    public synchronized static String getPostsFile() {
        return postsFile;
    }

    public synchronized static String getAccountsFile() {
        return accountsFile;
    }

    public synchronized static String getVotesFile() {
        return votesFile;
    }

    public synchronized static ArrayList<ForumServerThread> getForumServerThreads() {
        return forumServerThreads;
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

    public synchronized static void addForumServerThread(ForumServerThread forumServerThread) {
        forumServerThreads.add(forumServerThread);
        forumServerThreads.get(forumServerThreads.indexOf(forumServerThread)).start();
    }

    public synchronized static void setPosts(ArrayList<String> posts) {
        ForumServer.posts = posts;
    }

    public synchronized static void setAccounts(ArrayList<String> accounts) {
        ForumServer.accounts = accounts;
    }

    public synchronized static void setVotes(ArrayList<String> votes) {
        ForumServer.votes = votes;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            ForumServer forumServer = new ForumServer();
            forumServer.start();

            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    addForumServerThread(new ForumServerThread(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if (!forumServerThreads.isEmpty()) {
            for (int i = 0; i < forumServerThreads.size(); i++) {
                if (!forumServerThreads.get(i).getSocket().isConnected()) {
                    forumServerThreads.remove(forumServerThreads.get(i));
                }
            }
        }
    }
}
