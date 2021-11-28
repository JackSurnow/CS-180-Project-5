import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * Purdue University -- CS18000
 * Project 4
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class FileImports {
    private Main main;

    public FileImports(Main main) {
        this.main = main;
    }

    public boolean importPost(ArrayList<String> posts) {
        String fileName = JOptionPane.showInputDialog(null, "What post file do you want to import?",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);

        if (fileName != null) {
            File f = new File(fileName);
            PostManager postManager = new PostManager(main);
            if (f.exists() && f.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    ArrayList<String> postImport = new ArrayList<>();
                    postImport.add(br.readLine());
                    postImport.add(br.readLine());
                    return postManager.createPost(posts, postImport, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean importReply(ArrayList<String> post) {
        boolean validName = false;
        boolean validParentPost = false;
        boolean validParentReply = false;
        boolean first = false;
        String parentReply;

        String fileName = JOptionPane.showInputDialog(null, "What reply file do you want to import?",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);

        if (fileName != null) {
            File f = new File(fileName);
            if (f.exists() && f.isFile()) {
                validName = true;
            }
        }

        try {
            int parentPost = Integer.parseInt(JOptionPane.showInputDialog(null, "What post is this file a reply to?",
                    "Post Forum", JOptionPane.QUESTION_MESSAGE));
            for (int i = 0; i < Main.getPosts().size(); i++) {
                if (Main.getPosts().get(i).substring(0, Main.getPosts().get(i).indexOf(":"))
                        .matches(String.format("Post %d", parentPost))) {
                    validParentPost = true;
                    break;
                }
            }

            if (post.get(4).matches("There are no replies.")) {
                parentReply = "0";
                first = true;
                validParentReply = true;
            } else {
                parentReply = JOptionPane.showInputDialog(null, "What reply is this a reply to? " +
                        "(To reply to the Post directly, enter 0).", "Post Forum", JOptionPane.QUESTION_MESSAGE);

                if (parentReply != null) {
                    if (parentReply.toLowerCase().contains("reply ")) {
                        parentReply = parentReply.substring(parentReply.indexOf(" ") + 1);
                    }

                    for (int i = 0; i < post.size(); i++) {
                        if (post.get(i).contains("Reply " + parentReply.concat(":"))) {
                            validParentReply = true;
                            break;
                        }
                    }
                }
            }

            if (validName && validParentPost && validParentReply) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    String message = br.readLine();
                    if (message != null) {
                        Reply reply = new Reply(main);
                        return reply.replyImport(post, parentReply, message, String.format("Post%d.txt",
                                parentPost), first);
                    }
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
