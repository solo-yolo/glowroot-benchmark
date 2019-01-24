package org.glowroot.benchmark;

public interface ApmArgs {

    String GLOWROOT_AGENT = "-javaagent:glowroot/glowroot.jar";
    String NEWRELIC_AGENT = "-javaagent:newrelic/newrelic.jar";

}
