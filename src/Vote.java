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
    private Main main;

    public Vote(Main main) {
        this.main = main;
    }

    public boolean checkVoted(ArrayList<String> post, ArrayList<String> votes, String replyNum) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).contains(";") && votes.get(i).substring(0, votes.get(i).indexOf(";"))
                    .matches(main.getUsername()) && post.get(0).contains(":") && votes.get(i)
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
        votes.add(String.format("%s;%s;%s", main.getUsername(), postFile, replyNum));
        Main.setVotes(votes);
        PostWriter.writePost(votes, Main.getVotesFile());

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
                PostWriter.writePost(post, postFile);
                return true;
            }
        }
        return false;
    }
}
