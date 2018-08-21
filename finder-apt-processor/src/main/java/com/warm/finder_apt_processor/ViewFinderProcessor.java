package com.warm.finder_apt_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.warm.finder_apt_annotations.Id;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class ViewFinderProcessor extends AbstractProcessor {
    private Map<String, CodeCreator> mMap = new LinkedHashMap<>();

    private Messager mMessager;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Id.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        CodeCreator codeCreator = null;
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Id.class);
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            Id id = variableElement.getAnnotation(Id.class);
            int rValue = id.value();

            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            String className = typeElement.getQualifiedName().toString();//如com.warm.viewfinder.AptActivity

            codeCreator = mMap.get(className);
            if (codeCreator == null) {
                codeCreator = new CodeCreator(typeElement);
                mMap.put(className, codeCreator);
            }
            codeCreator.putMap(rValue, variableElement);
        }

        try {
            if (codeCreator != null) {
                JavaFile javaFile = JavaFile.builder(codeCreator.getPackageName(), codeCreator.getTypeSpec()).build();
                javaFile.writeTo(processingEnv.getFiler());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
