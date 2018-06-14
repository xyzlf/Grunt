package com.xyzlf.apt.processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class ClassInfo {

    private String packageName;
    private String simpleClassName;
    private String fullClassName;

    private TypeElement typeElement;

    private Map<Integer, VariableElement> variableElementMap = new HashMap<>();

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public Map<Integer, VariableElement> getVariableElementMap() {
        return variableElementMap;
    }

    @Override
    public String toString() {
        return packageName + " - " + simpleClassName + " - " + typeElement.getSimpleName();
    }
}
