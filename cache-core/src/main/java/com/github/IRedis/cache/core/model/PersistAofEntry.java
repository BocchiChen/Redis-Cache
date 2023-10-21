package com.github.IRedis.cache.core.model;

public class PersistAofEntry {
    Object[] params;

    String methodName;

    public static PersistAofEntry newInstance(){
        return new PersistAofEntry();
    }

    public PersistAofEntry setParams(Object[] params){
        this.params = params;
        return this;
    }

    public Object[] getParams(){
        return this.params;
    }

    public PersistAofEntry setMethodName(String methodName){
        this.methodName = methodName;
        return this;
    }

    public String getMethodName(){
        return this.methodName;
    }
}
