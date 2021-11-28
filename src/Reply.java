import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * Purdue University -- CS18000
 * Project 4
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class Reply {
    private Main main;
    private int previous;
    private String replyNumber;
    private String[] replyParts;
    private int count;
    private static boolean firstReply = false;

    public Reply(Main main) {
        this.main = main;
        this.previous = 0;
        this.replyNumber = null;
    }

    public boolean replyManual(ArrayList<String> post) {
        String fileName = String.format("Post%d.txt", Integer.parseInt(post.get(0)
                .substring(post.get(0).indexOf(" ") + 1, post.get(0).indexOf(":"))));

        // checks if this is the first reply
        if (post.get(post.size() - 1).matches("There are no replies.")) {
            replyNumber = "0";
            firstReply = true;
        } else {
            replyNumber = JOptionPane.showInputDialog(null, "What comment would you like to reply to? (To reply to" +
                            " the Post directly, enter 0).", "Post Forum", JOptionPane.QUESTION_MESSAGE);
            firstReply = false;

            if (replyNumber != null && replyNumber.toLowerCase().contains("reply ")) {
                replyNumber = replyNumber.substring(replyNumber.indexOf(" ") + 1);
            }
        }

        boolean valid = false;
        if (replyNumber != null) {
            replyParts = replyNumber.split("\\.");
            count = 1;

            // ensures the user entered a valid reply number
            for (String value : post) {
                if (value.contains(":") && value.substring(0, value.indexOf(":") + 1)
                        .contains("Reply " + replyNumber.concat(":"))) {
                    valid = true;
                }
            }
        }

        if (replyNumber != null && (valid || replyNumber.equals("0"))) {

            // asks what the user wants to say
            String message = JOptionPane.showInputDialog(null, "What do you want to say?",
                    "Post Forum", JOptionPane.QUESTION_MESSAGE);
            if (message != null) {
                reply(post, message, fileName);
                return true;
            }
        }
        return false;
    }

    public boolean replyImport(ArrayList<String> post, String parentReply, String message,
                                   String fileName, boolean first) {
        replyNumber = parentReply;
        firstReply = first;

        replyParts = replyNumber.split("\\.");
        count = 1;

        reply(post, message, fileName);
        return true;
    }

    public void reply(ArrayList<String> post, String message, String fileName) {

        // checks if the user wants to reply to the post itself or a reply on the post
        if (replyNumber.equals("0")) {

            // deletes "There are no replies."
            if (firstReply) {
                replyNumber = "1";
                post.remove(post.size() - 1);
            } else {
                if (post.get(post.size() - 1).substring(0, post.get(post.size() - 1).indexOf(":")).contains(".")) {

                    // isolates the reply number if the last String in the ArrayList has a multipart id
                    try {
                        replyNumber = String.format("%d", Integer.parseInt(post.get(post.size() - 1).substring(
                                post.get(post.size() - 1).indexOf("y") + 2, post.get(post.size() - 1).indexOf("."))) + 1);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {

                    // isolates the reply number if the last String in the ArrayList has a one part id
                    try {
                        replyNumber = String.format("%d", Integer.parseInt(post.get(post.size() - 1).substring(
                                post.get(post.size() - 1).indexOf("y") + 2, post.get(post.size() - 1).indexOf(":"))) + 1);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            previous = post.size() - 1;
        } else {

            // iterates through the list to find where to add the new reply
            for (int i = 0; i < post.size(); i++) {
                if (post.get(i).contains("Reply " + replyNumber.concat(".")
                        .concat(String.valueOf(count)).concat(":"))) {
                    previous = i;

                    // ensures that a reply does not precede the sub replies of the reply before it
                    for (int j = previous + 1; j < post.size(); j++) {
                        if (post.get(j).contains("Reply " + replyNumber.concat(".")
                                .concat(String.valueOf(count)).concat("."))) {
                            previous++;
                        }
                    }
                    count += 1;
                } else if (post.get(i).contains("Reply " + replyNumber.concat(":"))) {
                    previous = i;
                }
            }

            // creates the new id of the reply
            replyNumber = replyNumber.concat(".").concat(String.valueOf(count));
        }

        // creates the output String which will be added to the ArrayList of the post
        StringBuilder output = new StringBuilder(String.format("Reply %s: %s said \"%s\" at %s",
                replyNumber, main.getUsername(), message, ExtraUtil.time()));
        System.out.println(output);

        // adds the correct amount of indentations
        if (replyNumber.contains(".")) {
            for (int i = 0; i < replyParts.length; i++) {
                output.insert(0, "    ");
            }
        }

        // adds the output to the correct location in the ArrayList
        post.add(previous + 1, output.toString());
        PostWriter.writePost(post, fileName);
        main.setPost(post);
    }
}