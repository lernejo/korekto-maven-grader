package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.Exercise;
import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.GradingConfiguration;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitContext;
import com.github.lernejo.korekto.toolkit.thirdparty.maven.MavenExecutor;
import com.github.lernejo.korekto.toolkit.thirdparty.maven.MavenJacocoReport;

import java.util.List;
import java.util.Optional;

public class Part3Grader implements PartGrader {

    private final GradingConfiguration configuration;

    public Part3Grader(GradingConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String name() {
        return "Part 3 - Coverage";
    }

    @Override
    public GradePart grade(GitContext c, Exercise exercise) {
        MavenExecutor.executeGoal(exercise, configuration.getWorkspace(), "test");
        Optional<MavenJacocoReport> jacocoReport = MavenJacocoReport.from(exercise);

        if (jacocoReport.isEmpty()) {
            return result(List.of("No JaCoCo report produced after `mvn test`, check tests and plugins"), 0D);
        } else if (jacocoReport.get().getRatio() < 0.9D) {
            return result(List.of("Code coverage: " + jacocoReport.get().getRatio() * 100 + "%, expected: 100%"), 0D);
        } else {
            return result(List.of(), 1D);
        }
    }
}
