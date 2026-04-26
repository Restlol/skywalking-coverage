package org.apache.skywalking.apm.agent.core.jacoco;

import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.data.SessionInfoStore;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CoverageExportHook extends Thread{

    public CoverageExportHook(String name) {
        super(name);
    }

    @Override
    public void run() {
        try (FileOutputStream fileStream = new FileOutputStream("./coverage.exec", true);
             final OutputStream bufferedStream = new BufferedOutputStream(fileStream)) {
            final ExecutionDataStore executionData = new ExecutionDataStore();
            final SessionInfoStore sessionInfos = new SessionInfoStore();
            JacocoCenter.INSTANCE.getCoverage().getData().collect(executionData, sessionInfos, false);
            ExecutionDataWriter writer = new ExecutionDataWriter(bufferedStream);
            sessionInfos.accept(writer);
            executionData.accept(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
