package com.xyzlf.apt.processor;

import com.xyzlf.apt.annotations.BindView;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//@AutoService(Processor.class)
public class ViewProcessor extends AbstractProcessor {

    private static final String PROXY = "ViewInject";

    private Elements mElements;
    private Messager mMessager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(BindView.class.getName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        if (elements.size() > 0) {
            Map<String, ClassInfo> classInfoMap = new HashMap<>();

            for (Element element : elements) {
                if (element instanceof VariableElement) {

                    if (element.getKind() != ElementKind.FIELD) {
                        mMessager.printMessage(Diagnostic.Kind.ERROR, BindView.class.getSimpleName() + " must be declared on field.");
                        break;
                    }

                    if (element.getModifiers().contains(Modifier.PRIVATE)) {
                        mMessager.printMessage(Diagnostic.Kind.ERROR, element.getSimpleName() + " can not be private.");
                        break;
                    }

                    VariableElement variableElement = (VariableElement) element;

                    TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
                    //全类名 如：com.xyzlf.apt.MainActivity
                    String fullClassName = classElement.getQualifiedName().toString();

                    ClassInfo classInfo = classInfoMap.get(fullClassName);
                    if (null == classInfo) {
                        classInfo = new ClassInfo();

                        //获取包名信息
                        PackageElement packageElement = mElements.getPackageOf(classElement);
                        String packageName = packageElement.getQualifiedName().toString();

                        //获取类名 如：MainActivity
                        String simpleClassName = fullClassName.substring(packageName.length() + 1).replace('.', '$');
                        //拼接代理类名 如：MainActivity$$ViewInject
                        String proxySimpleClassName = simpleClassName + "$$" + PROXY;
                        String proxyFullClassName = packageName + "." + proxySimpleClassName;

                        //设置包名，类名
                        classInfo.setPackageName(packageName);
                        classInfo.setSimpleClassName(proxySimpleClassName);
                        classInfo.setFullClassName(proxyFullClassName);
                        classInfo.setTypeElement(classElement);

                        classInfoMap.put(fullClassName, classInfo);
                    }

                    BindView bindViewAnnotation = variableElement.getAnnotation(BindView.class);
                    int id = bindViewAnnotation.value();
                    classInfo.getVariableElementMap().put(id, variableElement);
                }
            }

            for (Map.Entry<String, ClassInfo> entry : classInfoMap.entrySet()) {
                ClassInfo info = entry.getValue();

                try {
                    JavaFileObject jfo = mFiler.createSourceFile(info.getFullClassName(), info.getTypeElement());
                    Writer writer = jfo.openWriter();

                    writer.write(generateJavaCode(info));
                    writer.flush();
                    writer.close();

                } catch (IOException e) {
                    mMessager.printMessage(Diagnostic.Kind.ERROR, "---------" + e.getMessage());
                }

            }
        }

        return false;
    }

    private String generateJavaCode(ClassInfo info) {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(info.getPackageName()).append(";\n\n");
        builder.append("import com.xyzlf.apt.api.*;\n\n");

        builder.append("public class ").append(info.getSimpleClassName())
                .append(" implements ").append(PROXY).append("<").append(info.getTypeElement().getQualifiedName()).append(">");
        builder.append(" {\n");

        //生成method方法
        builder.append("@Override\n ");
        builder.append("public void inject(").append(info.getTypeElement().getQualifiedName()).append(" host, Object source ) {\n");

        Map<Integer, VariableElement> variableElementMap = info.getVariableElementMap();
        for (Map.Entry<Integer, VariableElement> elementEntry : variableElementMap.entrySet()) {
            int id = elementEntry.getKey();
            VariableElement element = elementEntry.getValue();

            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.append("if(source instanceof android.app.Activity){\n");
            builder.append("host.").append(name).append(" = ");
            builder.append("(").append(type).append(")(((android.app.Activity)source).findViewById( ").append(id).append("));\n");
            builder.append("}else{\n");
            builder.append("host.").append(name).append(" = ");
            builder.append("(").append(type).append(")(((android.view.View)source).findViewById( ").append(id).append("));\n");
            builder.append("};\n");
        }
        builder.append("}\n\n");
        //生成method方法

        builder.append("}\n");

        return builder.toString();
    }


}
