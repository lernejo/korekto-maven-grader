package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.Exercise;
import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.misc.Equalator;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitContext;
import com.github.lernejo.korekto.toolkit.thirdparty.markdown.Badge;
import com.github.lernejo.korekto.toolkit.thirdparty.markdown.MarkdownFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Part4Grader implements PartGrader {
    private final Equalator equalator;

    public Part4Grader(Equalator equalator) {
        this.equalator = equalator;
    }

    @Override
    public String name() {
        return "Part 4 - Badges";
    }

    @Override
    public GradePart grade(GitContext c, Exercise exercise) {
        MarkdownFile readme = new MarkdownFile(exercise.getRoot().resolve("README.md"));
        List<Badge> badges = readme.getBadges();
        List<String> explanations = new ArrayList<>();
        String expectedGitHubActionsBadge = "https://github.com/" + exercise.getName().toLowerCase(Locale.ROOT) + "/actions/workflows/build.yml/badge.svg";
        String expectedGitHubWorkflowBadge = "https://github.com/" + exercise.getName().toLowerCase(Locale.ROOT) + "/workflows/build/badge.svg";
        double grade = 1;
        boolean gitHubActionBadgePresent = badges.stream()
            .anyMatch(b ->
                b.getImageUrl().toLowerCase(Locale.ROOT).startsWith(expectedGitHubActionsBadge)
                    || b.getImageUrl().toLowerCase(Locale.ROOT).startsWith(expectedGitHubWorkflowBadge)
            );
        if (!gitHubActionBadgePresent) {
            explanations.add("Missing GitHub action Badge");
            grade -= 0.5D;
        }
        Optional<Badge> codecovBadge = badges.stream().filter(b -> b.getImageUrl().startsWith("https://codecov.io/") && b.getImageUrl().toLowerCase().contains(exercise.getName().toLowerCase()) && b.getImageUrl().contains(".svg")).findFirst();
        String expectedCodecovLink = "https://codecov.io/gh/" + exercise.getName();
        if (codecovBadge.isEmpty()) {
            explanations.add("Missing Codecov Badge");
            grade -= 0.5D;
        } else if (!equalator.equals(codecovBadge.get().getTargetUrl(), expectedCodecovLink)) {
            explanations.add("Codecov Badge wrong link, expected: " + expectedCodecovLink + ", found: " + codecovBadge.get().getTargetUrl());
            grade -= 0.5D;
        }
        return result(explanations, grade);
    }
}
