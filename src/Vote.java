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

public class Vote {
    private ForumClient forumClient;

    public Vote(ForumClient forumClient) {
        this.forumClient = forumClient;
    }

public void voteLogic() {
    String replyNumber = JOptionPane.showInputDialog(null, "Which reply do you want to upvote?",
            "Post Forum", JOptionPane.QUESTION_MESSAGE);

    boolean valid = false;
    if (replyNumber != null && !replyNumber.isEmpty()) {
        if (replyNumber.toLowerCase().contains("reply ")) {
            replyNumber = replyNumber.substring(replyNumber.indexOf(" ") + 1);
        }

        for (String value : forumClient.getPost()) {
            if (value.contains(":") && value.substring(0, value.indexOf(":") + 1)
                    .contains("Reply " + replyNumber.concat(":"))) {
                valid = true;
                break;
            }
        }
    } else if (replyNumber != null) {
        JOptionPane.showMessageDialog(null, "Enter a reply.",
                "Post Forum", JOptionPane.ERROR_MESSAGE);
    }

    if (valid) {
        if (checkVoted(forumClient.getPost(), forumClient.getVotes(), replyNumber)) {
            if (upVote(forumClient.getPost(), forumClient.getVotes(), replyNumber)) {
                forumClient.GUI();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You already upvoted this reply.",
                    "Post Forum", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(null, "Enter a valid reply.",
                "Post Forum", JOptionPane.ERROR_MESSAGE);
    }
}

    public boolean checkVoted(ArrayList<String> post, ArrayList<String> votes, String replyNum) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).contains(";") && votes.get(i).substring(0, votes.get(i).indexOf(";"))
                    .matches(forumClient.getUsername()) && post.get(0).contains(":") && votes.get(i)
                    .substring(votes.get(i).indexOf(";") + 1, votes.get(i).lastIndexOf(";"))
                    .matches(String.format("Post%d.txt", Integer.parseInt(post.get(0)
                            .substring(5, post.get(0).indexOf(":"))))) && votes.get(i)
                    .substring(votes.get(i).lastIndexOf(";") + 1).matches(replyNum)) {
                return false;
            }
        }
        return true;
    }

    public boolean upVote(ArrayList<String> post, ArrayList<String> votes, String replyNum) {
        String postFile = String.format("Post%d.txt", Integer.parseInt(post.get(0)
                .substring(5, post.get(0).indexOf(":"))));
        votes.add(String.format("%s;%s;%s", forumClient.getUsername(), postFile, replyNum));
        forumClient.setVotes(votes, true);

        String numSpaces;
        int numVotes = 1;

        for (int i = 0; i < post.size(); i++) {

            if (post.get(i).contains(":") && post.get(i).substring(0, post.get(i).indexOf(":") + 1)
                    .contains("Reply " + replyNum + ":")) {
                if (post.get(i).substring(0, post.get(i).indexOf("R")).contains("(")) {
                    numSpaces = post.get(i).substring(0, post.get(i).indexOf("("));
                    numVotes = Integer.parseInt(post.get(i).substring(post.get(i).indexOf("(") + 1,
                            post.get(i).indexOf(")"))) + 1;
                } else {
                    numSpaces = post.get(i).substring(0, post.get(i).indexOf("R"));
                }

                post.set(i, String.format("%s(%d) %s", numSpaces, numVotes,
                        post.get(i).substring(post.get(i).indexOf("R"))));
                forumClient.setPost(post, true);
                return true;
            }
        }
        return false;
    }
}
