package com.github.wgierke.dda.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.github.wgierke.dda.Student;
import com.github.wgierke.dda.messages.GeneCheckForStudentMessage;
import com.github.wgierke.dda.messages.GenesMatchedMessage;

public class GeneChecker extends AbstractLoggingActor {
    private void checkGenes(GeneCheckForStudentMessage geneMessage) {
        Student student = geneMessage.getStudent();
        String[] genes = geneMessage.getGenes();

        String longestSequence = "";
        Integer genePartner = -1;

        for (int i = 0; i < genes.length; i++) {
            if (i == student.getId()) {
                continue;
            }
            String substring = longestCommonSubstring(genes[student.getId()], genes[i]);
            if (substring.length() > longestSequence.length()) {
                longestSequence = substring;
                genePartner = i;
            }
        }
        student.setGenePartner(genePartner);
        student.setLongestGeneMatch(longestSequence);
        this.log().debug("Received gene challenge for student " + student.getName() + " (ID: " + student.getId() + ")");
        this.getSender().tell(new GenesMatchedMessage(student), this.getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GeneCheckForStudentMessage.class, this::checkGenes)
                .matchAny(object -> this.log().info(this.getClass().getName() + " received unknown message: " + object.toString()))
                .build();
    }

    public static Props props() {
        return Props.create(GeneChecker.class);
    }

    /*
    From https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Longest_common_substring#Java
     */
    private static String longestCommonSubstring(String S1, String S2) {
        int Start = 0;
        int Max = 0;
        for (int i = 0; i < S1.length(); i++) {
            for (int j = 0; j < S2.length(); j++) {
                int x = 0;
                while (S1.charAt(i + x) == S2.charAt(j + x)) {
                    x++;
                    if (((i + x) >= S1.length()) || ((j + x) >= S2.length())) break;
                }
                if (x > Max) {
                    Max = x;
                    Start = i;
                }
            }
        }
        return S1.substring(Start, (Start + Max));
    }
}
