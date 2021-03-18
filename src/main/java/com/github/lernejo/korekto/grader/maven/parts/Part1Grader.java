package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.Exercise;
import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.misc.Equalator;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitContext;

import java.util.List;
import java.util.stream.Collectors;

public class Part1Grader implements PartGrader {

    private static final List<String> expectedCommitMessages = List.of(
        "Initial commit",
        "Setup project layout",
        "Setup Maven",
        "Setup Maven Wrapper",
        "Setup GitHub CI",
        "Add test to match 100% coverage",
        "Add live badges"
    );

    private final Equalator equalator;

    public Part1Grader(Equalator equalator) {
        this.equalator = equalator;
    }

    @Override
    public String name() {
        return "Part 1 - Git";
    }

    public GradePart grade(GitContext git, Exercise exercise) {
        List<String> mainCommits = git.listOrderedCommits().stream().map(rc -> rc.getShortMessage()).collect(Collectors.toList());
        if (!equalator.equals(mainCommits, expectedCommitMessages)) {
            String formattedExpectedCommits = expectedCommitMessages.stream().collect(Collectors.joining("\n        * ", "\n        * ", "\n"));
            String formattedActualCommits = mainCommits.stream().collect(Collectors.joining("\n        * ", "\n        * ", "\n"));
            String explanations = "Expecting commits to be" + formattedExpectedCommits + "but was" + formattedActualCommits;
            return result(List.of(explanations), 0);
        } else {
            return result(List.of(), 1);
        }
    }
}
