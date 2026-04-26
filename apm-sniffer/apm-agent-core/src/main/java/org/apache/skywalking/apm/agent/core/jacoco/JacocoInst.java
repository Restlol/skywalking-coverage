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

import org.apache.skywalking.apm.util.StringUtil;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.InjectedClassRuntime;
import org.jacoco.core.runtime.ModifiedSystemClassRuntime;
import org.jacoco.core.runtime.RuntimeData;
import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class JacocoInst {

    private final RuntimeData data = new RuntimeData();
    private IRuntime runtime;

    private void createRuntime(final Instrumentation inst) throws Exception {
        if (redefineJavaBaseModule(inst)) {
            this.runtime = new InjectedClassRuntime(Object.class, "$JaCoCo");
        }
        this.runtime = ModifiedSystemClassRuntime.createFor(inst, "java/lang/UnknownError");
    }

    private  boolean redefineJavaBaseModule(final Instrumentation instrumentation) throws Exception {
        try {
            Class.forName("java.lang.Module");
        } catch (final ClassNotFoundException e) {
            return false;
        }

        Instrumentation.class.getMethod("redefineModule",
                Class.forName("java.lang.Module"),
                Set.class,
                Map.class,
                Map.class,
                Set.class,
                Map.class
        ).invoke(instrumentation,
                getModule(Object.class),
                Collections.emptySet(),
                Collections.emptyMap(),
                Collections.singletonMap("java.lang",
                        Collections.singleton(
                                getModule(InjectedClassRuntime.class))),
                Collections.emptySet(),
                Collections.emptyMap()
        );
        return true;
    }

    private Object getModule(final Class<?> cls) throws Exception {
        return Class.class
                .getMethod("getModule")
                .invoke(cls);
    }

    public RuntimeData getData() {
        return data;
    }

    public IRuntime getRuntime() {
        return runtime;
    }

    public void useJacoco(final Instrumentation inst) throws Exception {
        if(Boolean.parseBoolean(JacocoArgumentsHolder.arguments.isOpenJacoco()) && StringUtil.isNotEmpty(JacocoArgumentsHolder.arguments.getScanPackage())){
            createRuntime(inst);
            this.runtime.startup(data);
            inst.addTransformer(new JacocoCoverageTransformer(this.runtime));
        }
    }

}
