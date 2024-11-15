package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.PartGrader;
import com.github.lernejo.korekto.toolkit.misc.OS;
import com.github.lernejo.korekto.toolkit.misc.Processes;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitContext;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitNature;
import org.eclipse.jgit.revwalk.RevCommit;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public record Part1Grader(String name, Double maxGrade) implements PartGrader<LaunchingContext> {

    private static final List<String> expectedCommitMessages = List.of(
        "Initial commit",
        "Setup project layout",
        "Setup Maven",
        "Setup Maven Wrapper",
        "Setup GitHub CI",
        "Add test to match 100% coverage",
        "Add live badges"
    );

    @NotNull
    public GradePart grade(LaunchingContext context) {
        Path target = context.getExercise().getRoot().resolve("target");
        if (Files.exists(target)) {
            Processes.launch(OS.Companion.getCURRENT_OS().deleteDirectoryCommand(target));
        }
        GitContext gitContext = context.getExercise().lookupNature(GitNature.class).get().getContext();
        List<String> mainCommits = gitContext.listOrderedCommits().stream().map(RevCommit::getShortMessage).collect(Collectors.toList());
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
