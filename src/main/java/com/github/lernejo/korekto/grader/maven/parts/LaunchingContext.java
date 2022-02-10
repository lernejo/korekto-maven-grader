package com.github.lernejo.korekto.grader.maven.parts;

import com.github.lernejo.korekto.toolkit.GradingConfiguration;
import com.github.lernejo.korekto.toolkit.GradingContext;
import com.github.lernejo.korekto.toolkit.misc.Equalator;

public class LaunchingContext extends GradingContext {
    final Equalator equalator = new Equalator(1);

    public LaunchingContext(GradingConfiguration configuration) {
        super(configuration);
    }
}
