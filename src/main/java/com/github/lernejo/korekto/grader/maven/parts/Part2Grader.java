package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.Exercise;
import com.github.lernejo.korekto.toolkit.GradePart;
import com.github.lernejo.korekto.toolkit.PartGrader;
import com.github.lernejo.korekto.toolkit.thirdparty.git.GitContext;
import com.github.lernejo.korekto.toolkit.thirdparty.github.GitHubNature;
import com.github.lernejo.korekto.toolkit.thirdparty.github.WorkflowRun;
import com.github.lernejo.korekto.toolkit.thirdparty.github.WorkflowRunConclusion;
import com.github.lernejo.korekto.toolkit.thirdparty.github.WorkflowRunStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

// TODO replace by built-in version in KTK 1.3.4
public class Part2Grader implements PartGrader<LaunchingContext> {

    private static final Set<String> mainBranchNames = Set.of("main", "master");

    @Override
    public String name() {
        return "Part 2 - CI";
    }

    @Override
    public Double maxGrade() {
        return 1.0;
    }

    @Override
    public GradePart grade(LaunchingContext context) {
        Optional<GitHubNature> gitHubNature = context.getExercise().lookupNature(GitHubNature.class);
        if (gitHubNature.isEmpty()) {
            return result(List.of("Not a GitHub project"), 0D);
        }
        List<WorkflowRun> actionRuns = gitHubNature.get().listActionRuns();

        Optional<WorkflowRun> latestWorkflowRun = actionRuns.stream()
            .filter(wr -> wr.getStatus() == WorkflowRunStatus.completed)
            .filter(wr -> mainBranchNames.contains(wr.getHead_branch()))
            .filter(wr -> wr.getConclusion() == WorkflowRunConclusion.success)
            .findFirst();
        if (latestWorkflowRun.isEmpty()) {
            return result(List.of("Latest CI run is not in success state"), 0D);
        } else {
            return result(List.of(), 1D);
        }
    }
}
