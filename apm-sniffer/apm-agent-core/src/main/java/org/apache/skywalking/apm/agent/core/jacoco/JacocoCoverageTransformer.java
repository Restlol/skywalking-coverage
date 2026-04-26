/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.agent.core.jacoco;

import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class JacocoCoverageTransformer implements ClassFileTransformer {

    private static final ILog logger = LogManager.getLogger(JacocoCoverageTransformer.class);

    private final Instrumenter instrumenter;

    public JacocoCoverageTransformer(IRuntime runtime) {
        this.instrumenter = new Instrumenter(runtime);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        try {
            if (loader == null) {
                return null;
            }

            if (classBeingRedefined != null) {
                return null;
            }

            String urlPath = JacocoArgumentsHolder.arguments.getScanPackage().replace(".", "/");
            if (className == null) {
                return null;
            }
            if (!className.startsWith(urlPath)) {
                return null;
            }
            if (className.contains("$")) {
                return null;
            }
            if (className.equals(JacocoCoverageTransformer.class.getName())) {
                return null;
            }
            return instrumenter.instrument(classfileBuffer, className);
        } catch (Exception e) {
            logger.error("jacoco transform error", e);
        }
        return null;
    }


}
