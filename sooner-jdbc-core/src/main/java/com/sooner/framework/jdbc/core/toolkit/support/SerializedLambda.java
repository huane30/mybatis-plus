/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sooner.framework.jdbc.core.toolkit.support;

import com.sooner.framework.jdbc.core.toolkit.ExceptionUtils;
import com.sooner.framework.jdbc.core.toolkit.SerializationUtils;
import com.sooner.framework.jdbc.core.toolkit.StringPool;

import java.io.*;

/**
 * 这个类是从 {@link java.lang.invoke.SerializedLambda} 里面 copy 过来的
 * 字段信息完全一样
 * <p>
 * 负责将一个支持序列的 Function 序列化为 SerializedLambda
 *
 * @author HCL
 * @since 2018/05/10
 */
public class SerializedLambda implements Serializable {

    private static final long serialVersionUID = 8025925345765570181L;
    private Class<?> capturingClass;
    private String functionalInterfaceClass;
    private String functionalInterfaceMethodName;
    private String functionalInterfaceMethodSignature;
    private String implClass;
    private String implMethodName;
    private String implMethodSignature;
    private int implMethodKind;
    private String instantiatedMethodType;
    private Object[] capturedArgs;

    /**
     * 通过反序列化转换 class
     *
     * @param lambda lambda对象
     * @return 返回解析后的 SerializedLambda
     */
    public static SerializedLambda convert(Property lambda) {
        byte[] bytes = SerializationUtils.serialize(lambda);
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(bytes)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                Class<?> clazz = super.resolveClass(objectStreamClass);
                return clazz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clazz;
            }
        }) {
            return (SerializedLambda) objIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw ExceptionUtils.mpe("This is impossible to happen", e);
        }
    }

    @Override
    public String toString() {
        return super.toString() +
            implClass.replace(StringPool.SLASH, StringPool.DOT) +
            StringPool.HASH + implMethodName;
    }

    public Class<?> getCapturingClass() {
        return capturingClass;
    }

    public void setCapturingClass(Class<?> capturingClass) {
        this.capturingClass = capturingClass;
    }

    public String getFunctionalInterfaceClass() {
        return functionalInterfaceClass;
    }

    public void setFunctionalInterfaceClass(String functionalInterfaceClass) {
        this.functionalInterfaceClass = functionalInterfaceClass;
    }

    public String getFunctionalInterfaceMethodName() {
        return functionalInterfaceMethodName;
    }

    public void setFunctionalInterfaceMethodName(String functionalInterfaceMethodName) {
        this.functionalInterfaceMethodName = functionalInterfaceMethodName;
    }

    public String getFunctionalInterfaceMethodSignature() {
        return functionalInterfaceMethodSignature;
    }

    public void setFunctionalInterfaceMethodSignature(String functionalInterfaceMethodSignature) {
        this.functionalInterfaceMethodSignature = functionalInterfaceMethodSignature;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public String getImplMethodName() {
        return implMethodName;
    }

    public void setImplMethodName(String implMethodName) {
        this.implMethodName = implMethodName;
    }

    public String getImplMethodSignature() {
        return implMethodSignature;
    }

    public void setImplMethodSignature(String implMethodSignature) {
        this.implMethodSignature = implMethodSignature;
    }

    public int getImplMethodKind() {
        return implMethodKind;
    }

    public void setImplMethodKind(int implMethodKind) {
        this.implMethodKind = implMethodKind;
    }

    public String getInstantiatedMethodType() {
        return instantiatedMethodType;
    }

    public void setInstantiatedMethodType(String instantiatedMethodType) {
        this.instantiatedMethodType = instantiatedMethodType;
    }

    public Object[] getCapturedArgs() {
        return capturedArgs;
    }

    public void setCapturedArgs(Object[] capturedArgs) {
        this.capturedArgs = capturedArgs;
    }
}
