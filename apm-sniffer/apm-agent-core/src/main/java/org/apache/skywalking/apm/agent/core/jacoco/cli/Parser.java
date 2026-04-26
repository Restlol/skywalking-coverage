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

package org.apache.skywalking.apm.agent.core.jacoco.cli;

public class Parser {

    public TypedOptions parse(String args) {
        TypedOptions options = new TypedOptions();
        String DELEGATE_DOT = ",";
        String[] split = args.split(DELEGATE_DOT);
        for (int i = 0; i < split.length; i++) {
            String DELEGATE_EQUALS = "=";
            String[] res = split[i].split(DELEGATE_EQUALS);
            options.getMap().put(res[0],res[1]);
        }
        return options;
    }
}
