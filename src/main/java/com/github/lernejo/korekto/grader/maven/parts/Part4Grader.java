package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.PartGrader;
import com.github.lernejo.korekto.toolkit.thirdparty.markdown.Badge;
import com.github.lernejo.korekto.toolkit.thirdparty.markdown.MarkdownFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public record Part4Grader(String name, Double maxGrade) implements PartGrader<LaunchingContext> {

    @NotNull
    @Override
    public GradePart grade(LaunchingContext context) {
        MarkdownFile readme = new MarkdownFile(context.getExercise().getRoot().resolve("README.md"));
        List<Badge> badges = readme.getBadges();
        List<String> explanations = new ArrayList<>();
        String expectedGitHubActionsBadge = "https://github.com/" + context.getExercise().getName().toLowerCase(Locale.ROOT) + "/actions/workflows/build.yml/badge.svg";
        String expectedGitHubWorkflowBadge = "https://github.com/" + context.getExercise().getName().toLowerCase(Locale.ROOT) + "/workflows/build/badge.svg";
        double grade = maxGrade();
        boolean gitHubActionBadgePresent = badges.stream()
            .anyMatch(b ->
                b.getImageUrl().toLowerCase(Locale.ROOT).startsWith(expectedGitHubActionsBadge)
                    || b.getImageUrl().toLowerCase(Locale.ROOT).startsWith(expectedGitHubWorkflowBadge)
            );
        if (!gitHubActionBadgePresent) {
            explanations.add("Missing GitHub action Badge");
            grade -= maxGrade() / 2;
        }
        Optional<Badge> codecovBadge = badges.stream().filter(b -> b.getImageUrl().startsWith("https://codecov.io/")
            && b.getImageUrl().toLowerCase().contains(context.getExercise().getName().toLowerCase())
            && b.getImageUrl().contains(".svg")).findFirst();

        String expectedCodecovLink = "https://codecov.io/gh/" + context.getExercise().getName();
        if (codecovBadge.isEmpty()) {
            explanations.add("Missing Codecov Badge");
            grade -= maxGrade() / 2;
        } else if (!context.equalator.equals(codecovBadge.get().getTargetUrl(), expectedCodecovLink)) {
            explanations.add("Codecov Badge wrong link, expected: " + expectedCodecovLink + ", found: " + codecovBadge.get().getTargetUrl());
            grade -= maxGrade() / 2;
        }
        return result(explanations, grade);
    }
}
