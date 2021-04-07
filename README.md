# korekto-maven-grader
Korekto grader for Maven101 exercise

[![Build](https://github.com/lernejo/korekto-maven-grader/actions/workflows/build.yml/badge.svg)](https://github.com/lernejo/korekto-maven-grader/actions)

## Launch locally

To launch the tool locally, run `com.github.lernejo.korekto.toolkit.launcher.GradingJobLauncher` with the
argument `-s=mySlug`

### With Maven

```bash
mvn compile exec:java -Dexec.mainClass="com.github.lernejo.korekto.toolkit.launcher.GradingJobLauncher" -Dexec.args="-s=mySlug"
```

### With intelliJ

![Demo Run Configuration](https://raw.githubusercontent.com/lernejo/korekto-toolkit/main/docs/demo_run_configuration.png)
