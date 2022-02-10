package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.PartGrader;
import com.github.lernejo.korekto.toolkit.thirdparty.maven.MavenExecutor;
import com.github.lernejo.korekto.toolkit.thirdparty.maven.MavenJacocoReport;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Part3Grader implements PartGrader<LaunchingContext> {

    @Override
    public String name() {
        return "Part 3 - Coverage";
    }

    @Nullable
    @Override
    public Double maxGrade() {
        return 1.0;
    }

    @Override
    public GradePart grade(LaunchingContext context) {
        MavenExecutor.executeGoal(context.getExercise(), context.getConfiguration().getWorkspace(), "test");
        List<MavenJacocoReport> reports = MavenJacocoReport.from(context.getExercise());
        MavenJacocoReport mergedReport = MavenJacocoReport.merge(reports);

        if (reports.isEmpty()) {
            return result(List.of("No JaCoCo report produced after `mvn test`, check tests and plugins"), 0D);
        } else if (mergedReport.getRatio() < 0.9D) {
            return result(List.of("Code coverage: " + mergedReport.getRatio() * 100 + "%, expected: 100%"), 0D);
        } else {
            return result(List.of(), maxGrade());
        }
    }
}
