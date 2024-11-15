package com.github.lernejo.korekto.grader.maven;

import com.github.lernejo.korekto.grader.maven.parts.*;
import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.Grader;
import com.github.lernejo.korekto.toolkit.GradingConfiguration;
import com.github.lernejo.korekto.toolkit.PartGrader;
import com.github.lernejo.korekto.toolkit.misc.HumanReadableDuration;
import com.github.lernejo.korekto.toolkit.partgrader.GitHubActionsPartGrader;
import com.github.lernejo.korekto.toolkit.partgrader.JacocoCoveragePartGrader;
import com.github.lernejo.korekto.toolkit.partgrader.MavenCompileAndTestPartGrader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MavenGrader implements Grader<LaunchingContext> {

    private final Logger logger = LoggerFactory.getLogger(MavenGrader.class);

    @Override
    public String name() {
        return "Maven training";
    }

    @NotNull
    @Override
    public LaunchingContext gradingContext(GradingConfiguration configuration) {
        return new LaunchingContext(configuration);
    }

    @Override
    public void run(LaunchingContext context) {
        context.getGradeDetails().getParts().addAll(grade(context));
    }

    @NotNull
    @Override
    public Collection<PartGrader<LaunchingContext>> graders() {
        return List.of(
            new Part1Grader("Part 1 - Git", 1.0D),
            new GitHubActionsPartGrader<>("Part 2 - CI", 1.0D),
            new Part3Grader("Part 3 - Coverage", 1.0D),
            new Part4Grader("Part 4 - Badges", 1.0D)
        );
    }

    @Override
    public String slugToRepoUrl(String slug) {
        return "https://github.com/" + slug + "/maven_training";
    }

    @Override
    public boolean needsWorkspaceReset() {
        return true;
    }
}
