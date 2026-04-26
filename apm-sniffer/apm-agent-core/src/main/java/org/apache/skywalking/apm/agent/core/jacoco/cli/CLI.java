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
import org.apache.skywalking.apm.agent.core.jacoco.cli.annotations.Option;
import org.apache.skywalking.apm.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CLI {

    public  void inject(Object obj,TypedOptions options) throws InvocationTargetException, IllegalAccessException {
        List<Method> setterMethods = ReflectionUtils.getSetterMethods(obj.getClass());
        for (Method method : setterMethods){
            if(method.isAnnotationPresent(Option.class)){
                Option annotation = method.getAnnotation(Option.class);
                String value = options.getMap().get(annotation.key());
                if(StringUtil.isNotEmpty(value)){
                    ReflectionUtils.invokeSetter(obj,method,value);
                }
            }
        }
    }

    public TypedOptions parse(String s) {
        Parser parser = new Parser();
        return parser.parse(s);
    }
}
