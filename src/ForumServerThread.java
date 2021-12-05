import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ForumServerThread extends Thread implements Runnable {

    private final Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private String username;

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ForumServerThread(Socket socket) {
        this.socket = socket;
        this.os = null;
        this.is= null;
        this.username = null;
    }

    public synchronized void sendFileNames() {
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add(ForumServer.getPostsFile());
        fileNames.add(ForumServer.getAccountsFile());
        fileNames.add(ForumServer.getVotesFile());

        try {
            os.writeObject(new Message("File Names", fileNames));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendPosts() {
        try {
            os.writeObject(new Message("posts", ForumServer.getPosts()));
            for (int i = 0 ; i < ForumServer.getForumServerThreads().size(); i++) {
                if (os != ForumServer.getForumServerThreads().get(i).os) {
                    ForumServer.getForumServerThreads().get(i).os.writeObject(
                            new Message("posts", ForumServer.getPosts()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendAccounts() {
        try {
            os.writeObject(new Message("accounts", ForumServer.getAccounts()));
            for (int i = 0 ; i < ForumServer.getForumServerThreads().size(); i++) {
                if (os != ForumServer.getForumServerThreads().get(i).os) {
                    ForumServer.getForumServerThreads().get(i).os.writeObject(
                            new Message("accounts", ForumServer.getAccounts()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendVotes() {
        try {
            os.writeObject(new Message("votes", ForumServer.getVotes()));
            for (int i = 0 ; i < ForumServer.getForumServerThreads().size(); i++) {
                if (os != ForumServer.getForumServerThreads().get(i).os) {
                    ForumServer.getForumServerThreads().get(i).os.writeObject(
                            new Message("votes", ForumServer.getVotes()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendCurrentUsers() {
        try {
            ArrayList<String> users = new ArrayList<>();
            for (int i = 0 ; i < ForumServer.getForumServerThreads().size(); i++) {
                if (ForumServer.getForumServerThreads().get(i).getUsername() != null) {
                    users.add(ForumServer.getForumServerThreads().get(i).getUsername());
                }
            }
            for (int i = 0 ; i < ForumServer.getForumServerThreads().size(); i++) {
                ForumServer.getForumServerThreads().get(i).os.writeObject(
                        new Message("Users", users));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendResponse(ArrayList<String> contents, String modifier) {
        try {
            if (modifier != null) {
                os.writeObject(new Message("Response", contents, modifier));
            } else {
                os.writeObject(new Message("Response", contents));
            }
            for (int i = 0 ; i < ForumServer.getForumServerThreads().size(); i++) {
                if (os != ForumServer.getForumServerThreads().get(i).os) {
                    if (modifier != null) {
                        ForumServer.getForumServerThreads().get(i).os.writeObject(
                                new Message("Response", contents, modifier));
                    } else {
                        ForumServer.getForumServerThreads().get(i).os.writeObject(
                                new Message("Response", contents));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendAll(String modifier) {
        try {
            PostReader postReader = new PostReader();
            ArrayList<ArrayList<String>> individualPosts = new ArrayList<>();
            for (int i = 0; i < ForumServer.getPosts().size(); i++) {
                individualPosts.add(postReader.readPost(Integer.parseInt(ForumServer.getPosts().get(i).substring(
                        ForumServer.getPosts().get(i).indexOf(" ") + 1, ForumServer.getPosts().get(i).indexOf(":")))));
            }
            os.writeObject(new Message("All", ForumServer.getPosts(), individualPosts, modifier));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());

            sendFileNames();
            sendPosts();
            sendAccounts();
            sendVotes();
            sendCurrentUsers();

            while (!socket.isClosed() && socket.isConnected()) {
                os.reset();
                Message message = (Message) is.readObject();
                if (message != null) {
                    String listName = message.getListName();
                    ArrayList<String> contents = message.getContents();
                    String modifier = message.getModifier();

                    switch (listName) {
                        case "posts" -> {
                            ForumServer.setPosts(contents);
                            ExtraUtil.writePosts(contents);
                            sendPosts();
                        }
                        case "accounts" -> {
                            ForumServer.setAccounts(contents);
                            ExtraUtil.writeAccounts(contents);
                            sendAccounts();
                        }
                        case "votes" -> {
                            ForumServer.setVotes(contents);
                            ExtraUtil.writeVotes(contents);
                            sendVotes();
                        }
                        case "Username" -> {
                            setUsername(modifier);
                            sendCurrentUsers();
                        }
                        case "post" -> {
                            String fileName = modifier.substring(modifier.indexOf(";") + 1, modifier.lastIndexOf(";"));
                            String newModifier = modifier.substring(0, modifier.indexOf(";")) + ";" + modifier
                                    .substring(modifier.lastIndexOf(";") + 1);
                            ExtraUtil.writePost(contents, fileName);
                            sendResponse(contents, newModifier);
                        }
                        case "Request" -> {
                            int fileNum = message.getNum();
                            PostReader postReader = new PostReader();
                            sendResponse(postReader.readPost(fileNum), modifier);
                        }
                        case "Create" -> {
                            PostManager postManager = new PostManager(this);
                            postManager.createPost(ForumServer.getPosts(), contents, modifier);
                        }
                        case "Edit" -> {
                            PostManager postManager = new PostManager(this);
                            postManager.editPost(ForumServer.getPosts(), contents, modifier.substring(0, modifier
                                    .indexOf(";")), modifier.substring(modifier.indexOf(";") + 1, modifier
                                    .lastIndexOf(";")), modifier.substring(modifier.lastIndexOf(";") + 1));
                        }
                        case "Delete" -> {
                            PostManager postManager = new PostManager(this);
                            postManager.deletePost(ForumServer.getPosts(), contents, modifier);
                        }
                        case "All" -> sendAll(modifier);
                    }
                }
            }
            sendCurrentUsers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
}
