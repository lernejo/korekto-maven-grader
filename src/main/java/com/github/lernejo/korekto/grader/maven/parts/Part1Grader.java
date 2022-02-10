package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.PartGrader;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitContext;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitNature;

import java.util.List;
import java.util.stream.Collectors;

public class Part1Grader implements PartGrader<LaunchingContext> {

    private static final List<String> expectedCommitMessages = List.of(
        "Initial commit",
        "Setup project layout",
        "Setup Maven",
        "Setup Maven Wrapper",
        "Setup GitHub CI",
        "Add test to match 100% coverage",
        "Add live badges"
    );

    @Override
    public String name() {
        return "Part 1 - Git";
    }

    @Override
    public Double maxGrade() {
        return 1.0;
    }

    public GradePart grade(LaunchingContext context) {
        GitContext gitContext = context.getExercise().lookupNature(GitNature.class).get().getContext();
        List<String> mainCommits = gitContext.listOrderedCommits().stream().map(rc -> rc.getShortMessage()).collect(Collectors.toList());
        if (!context.equalator.equals(mainCommits, expectedCommitMessages)) {
            String formattedExpectedCommits = expectedCommitMessages.stream().collect(Collectors.joining("\n        * ", "\n        * ", "\n"));
            String formattedActualCommits = mainCommits.stream().collect(Collectors.joining("\n        * ", "\n        * ", "\n"));
            String explanations = "Expecting commits to be" + formattedExpectedCommits + "but was" + formattedActualCommits;
            return result(List.of(explanations), 0);
        } else {
            return result(List.of(), maxGrade());
        }
    }
}
