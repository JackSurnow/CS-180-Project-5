import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private final ForumClient forumClient;

    public FileImports(ForumClient forumClient) {
        this.forumClient = forumClient;
    }

    public void importLogic() {
        String[] options;
        if (forumClient.isTeacher()) {
            options = new String[]{"Import Post", "Import Reply"};
        } else {
            options = new String[]{"Import Reply"};
        }
        String importMenu = (String) JOptionPane.showInputDialog(null, "What do you want to import?",
                "Post Forum", JOptionPane.PLAIN_MESSAGE, null, options, null);

        if (importMenu != null) {
            if (importMenu.equals("Import Post")) {
                importPost();
            } else if (importMenu.equals("Import Reply")) {
                importReply();
            }
        }
    }

    public void importPost() {
        String fileName = JOptionPane.showInputDialog(null, "What post file do you want to import?",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);

        if (fileName != null && !fileName.isEmpty()) {
            File f = new File(fileName);
            if (f.exists() && f.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    ArrayList<String> postImport = new ArrayList<>();
                    postImport.add(br.readLine());
                    postImport.add(br.readLine());

                    forumClient.sendCreate(postImport);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Enter a valid file.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        } else if (fileName != null) {
            JOptionPane.showMessageDialog(null, "Enter a file.",
                    "Post Forum", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void importReply() {
        boolean validName = false;
        String fileName = JOptionPane.showInputDialog(null, "What reply file do you want to import?",
                "Post Forum", JOptionPane.QUESTION_MESSAGE);

        if (fileName != null && !fileName.isEmpty()) {
            File f = new File(fileName);
            if (f.exists() && f.isFile()) {
                validName = true;
            } else {
                JOptionPane.showMessageDialog(null, "Enter a valid file.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        } else if (fileName != null) {
            JOptionPane.showMessageDialog(null, "Enter a file.",
                    "Post Forum", JOptionPane.ERROR_MESSAGE);
        }

        if (validName) {
            boolean validParentPost = false;
            try {
                int parentPost = Integer.parseInt(JOptionPane.showInputDialog(null, "What post is this file a reply to?",
                        "Post Forum", JOptionPane.QUESTION_MESSAGE).replaceAll("[^0-9]",""));
                for (int i = 0; i < forumClient.getPosts().size(); i++) {
                    if (forumClient.getPosts().get(i).substring(0, forumClient.getPosts().get(i).indexOf(":"))
                            .matches(String.format("Post %d", parentPost))) {
                        validParentPost = true;
                        forumClient.sendImportReply(fileName, parentPost);
                        break;
                    }
                }

                if (!validParentPost) {
                    JOptionPane.showMessageDialog(null, "Enter a valid post.",
                            "Post Forum", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Enter a valid post.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public synchronized void ImportReplyResponse(String fileName, ArrayList<String> post) {
        String parentReply;
        boolean validParentReply = false;
        boolean first = false;
        try {
            if (post.get(4).matches("There are no replies.")) {
                parentReply = "0";
                first = true;
                validParentReply = true;
            } else {
                parentReply = JOptionPane.showInputDialog(null, "What reply is this a reply to? " +
                        "(To reply to the Post directly, enter 0).", "Post Forum", JOptionPane.QUESTION_MESSAGE);

                if (parentReply != null && !parentReply.isEmpty()) {
                    if (parentReply.toLowerCase().contains("reply ")) {
                        parentReply = parentReply.substring(parentReply.indexOf(" ") + 1);
                    }

                    if (parentReply.equals("0")) {
                        validParentReply = true;
                    } else {
                        for (String s : post) {
                            if (s.contains("Reply " + parentReply.concat(":"))) {
                                validParentReply = true;
                                break;
                            }
                        }
                    }
                } else if (parentReply != null) {
                    JOptionPane.showMessageDialog(null, "Enter a post.",
                            "Post Forum", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (validParentReply) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    String message = br.readLine();
                    if (message != null) {
                        Reply reply = new Reply(forumClient);
                        reply.replyImport(post, parentReply, message, first);
                    }
                } catch (IOException x) {
                    x.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Enter a valid reply.",
                        "Post Forum", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Enter a valid reply.",
                    "Post Forum", JOptionPane.ERROR_MESSAGE);
        }
    }
}
