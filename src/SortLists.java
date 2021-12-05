import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Purdue University -- CS18000
 * Project 5
 *
 * @author Jason Bodzy, Jack Surnow, Jack Weston, Irfan Hussain, and Jungeun Hwang
 * @version 1.0
 */

public class SortLists {
    private ForumClient forumClient;

    public SortLists(ForumClient forumClient) {
        this.forumClient = forumClient;
    }

    public ArrayList<String> sortReplies(Message message, String studentName) {
        forumClient.setPost(new ArrayList<>(), false);
        ArrayList<String> sort = new ArrayList<>();
        for (int i = 0; i < message.getSortContents().size(); i++) {
            for (int j = 0; j < message.getSortContents().get(i).size(); j++) {
                String line = message.getSortContents().get(i).get(j);
                if (line.contains(":") && line.contains("Reply ") && line.substring(0, line.indexOf("R"))
                        .contains("(") && line.contains(" said \"") && line.substring(line.indexOf(":") + 2,
                        line.indexOf(" said \"")).matches(studentName)) {
                    sort.add(String.format("%d %s", sort.size() + 1, line.substring(line.indexOf("("))));
                } else if (line.contains(":") && line.contains("Reply ") && line.contains(" said \"") && line
                        .substring(line.indexOf(":") + 2, line.indexOf(" said \"")).matches(studentName)) {
                    sort.add(String.format("%d %s", sort.size() + 1, line.substring(line.indexOf("R"))));
                }
            }
        }
        forumClient.setSort(sort);
        if (sort.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no replies by that student.",
                    "Post Forum", JOptionPane.ERROR_MESSAGE);
        }
        return sort;
    }

    public ArrayList<String> sortGrades(Message message, String studentName) {
        forumClient.setPost(new ArrayList<>(), false);
        ArrayList<String> sort = new ArrayList<>();
        for (int i = 0; i < message.getSortContents().size(); i++) {
            for (int j = 0; j < message.getSortContents().get(i).size(); j++) {
                String line = message.getSortContents().get(i).get(j);
                if (line.contains(":") && line.contains(" said \"") && line.contains("Reply ") && line
                        .substring(0, line.indexOf("R")).contains("(") && line.contains(",") && line.substring(line
                        .lastIndexOf(",")).contains("Grade: ") && line.substring(line.indexOf(":") + 2, line
                        .indexOf(" said \"")).matches(studentName)) {
                    sort.add(line.substring(line.indexOf("(")));
                } else if (line.contains(":") && line.contains(" said \"") && line.contains("Reply ") && line
                        .contains(",") && line.substring(line.lastIndexOf(",")).contains("Grade: ") && line
                        .substring(line.indexOf(":") + 2, line.indexOf(" said \"")).matches(studentName)) {
                    sort.add(line.substring(line.indexOf("R")));
                }
            }
        }
        forumClient.setSort(sort);
        return sort;
    }

    public ArrayList<String> sortVotesMost(Message message) {
        forumClient.setPost(new ArrayList<>(), false);
        ArrayList<String> sort = new ArrayList<>();
        for (int i = 0; i < message.getContents().size(); i++) {
            sort.add(message.getContents().get(i));

            ArrayList<String> transfer = new ArrayList<>();

            for (int j = 0; j < message.getSortContents().get(i).size(); j++) {
                boolean entered = false;
                String value = message.getSortContents().get(i).get(j);
                if (value.contains(":") && value.contains(" said \"") &&
                        value.contains("Reply ") && value.substring(0, value
                        .indexOf("R")).contains("(")) {
                    if (transfer.size() > 0) {
                        for (int k = 0; k < transfer.size(); k++) {
                            int numVotes = Integer.parseInt(value.substring(value
                                    .indexOf("(") + 1, value.indexOf(")")));
                            if (!entered && numVotes > Integer.parseInt(transfer.get(k)
                                    .substring(transfer.get(k).indexOf("(") + 1,
                                            transfer.get(k).indexOf(")")))) {
                                transfer.add(k, value.substring(value.indexOf("(")));
                                entered = true;
                            }
                        }

                        if (!entered) {
                            transfer.add(value.substring(value.indexOf("(")));
                        }

                    } else {
                        transfer.add(value.substring(value.indexOf("(")));
                    }
                }
            }
            for (String s : transfer) {
                sort.add(s);
            }
        }
        forumClient.setSort(sort);
        return sort;
    }

    public ArrayList<String> sortVotesLeast(Message message) {
        forumClient.setPost(new ArrayList<>(), false);
        ArrayList<String> sort = new ArrayList<>();
        for (int i = 0; i < message.getContents().size(); i++) {
            sort.add(message.getContents().get(i));

            ArrayList<String> transfer = new ArrayList<>();

            for (int j = 0; j < message.getSortContents().get(i).size(); j++) {
                boolean entered = false;
                String value = message.getSortContents().get(i).get(j);
                if (value.contains(":") && value.contains(" said \"") &&
                        value.contains("Reply ") && value.substring(0, value
                        .indexOf("R")).contains("(")) {
                    if (transfer.size() > 0) {
                        for (int k = 0; k < transfer.size(); k++) {
                            int numVotes = Integer.parseInt(value.substring(value
                                    .indexOf("(") + 1, value.indexOf(")")));
                            if (!entered && numVotes > Integer.parseInt(transfer.get(k)
                                    .substring(transfer.get(k).indexOf("(") + 1,
                                            transfer.get(k).indexOf(")")))) {
                                transfer.add(k, value.substring(value.indexOf("(")));
                                entered = true;
                            }
                        }

                        if (!entered) {
                            transfer.add(value.substring(value.indexOf("(")));
                        }

                    } else {
                        transfer.add(value.substring(value.indexOf("(")));
                    }
                }
            }
            for (int j = transfer.size() - 1; j >= 0; j--) {
                sort.add(transfer.get(j));
            }
        }
        forumClient.setSort(sort);
        return sort;
    }
}
