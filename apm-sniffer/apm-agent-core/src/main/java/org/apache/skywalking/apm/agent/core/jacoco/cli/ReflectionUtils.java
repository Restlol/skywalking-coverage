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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class ReflectionUtils {

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot instantiate " + clazz.getName(), e);
        }
    }

    public static boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterTypes().length == 1;
    }

    public static List<Method> getSetterMethods(Class<?> clazz) {
        List<Method> setters = new LinkedList<Method>();
        for (Method method : clazz.getMethods()) {
            if (ReflectionUtils.isSetter(method)) {
                setters.add(method);
            }
        }
        return setters;
    }

    public static void invokeSetter(Object obj, Method method, String value) throws InvocationTargetException, IllegalAccessException {
        method.invoke(obj,value);
    }
}
