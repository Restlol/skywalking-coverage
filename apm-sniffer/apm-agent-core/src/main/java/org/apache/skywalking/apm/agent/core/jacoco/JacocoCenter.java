package org.apache.skywalking.apm.agent.core.jacoco;

import org.apache.skywalking.apm.util.StringUtil;

public enum JacocoCenter {
    INSTANCE;

    private final JacocoInst coverage = new JacocoInst();

    public JacocoInst getCoverage() {
        return coverage;
    }

    public boolean outputCoverageFile(){
        return Boolean.parseBoolean(JacocoArgumentsHolder.arguments.isOpenJacoco()) && StringUtil.isNotEmpty(JacocoArgumentsHolder.arguments.getScanPackage());
    }
}
