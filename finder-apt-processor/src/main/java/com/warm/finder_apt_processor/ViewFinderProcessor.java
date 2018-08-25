package com.warm.finder_apt_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.warm.finder_apt_annotations.Id;
import com.warm.finder_apt_annotations.OnClick;
import com.warm.finder_apt_annotations.OnLongClick;

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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
//@SupportedAnnotationTypes(value = {"com.warm.finder_apt_annotations.Id","com.warm.finder_apt_annotations.OnClick"})
public class ViewFinderProcessor extends AbstractProcessor {


    private Map<String, FinderCreator> mMap = new LinkedHashMap<>();


    private Messager mMessager;

    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    //这边和上面注解@SupportedAnnotationTypes作用应该是一样的
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Id.class.getCanonicalName());
        set.add(OnClick.class.getCanonicalName());
        return set;
    }


    private FinderCreator checkCreator(String className, TypeElement typeElement) {
        FinderCreator finderCreator = mMap.get(className);

        if (finderCreator == null) {
            finderCreator = new FinderCreator(typeElement);
            mMap.put(className, finderCreator);
        }
        return finderCreator;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        Set<? extends Element> idElements = roundEnvironment.getElementsAnnotatedWith(Id.class);
        for (Element element : idElements) {
            VariableElement variableElement = (VariableElement) element;
            Id id = variableElement.getAnnotation(Id.class);
            int rValue = id.value();

            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            String className = typeElement.getQualifiedName().toString();//如com.warm.viewfinder.AptActivity

            FinderCreator finderCreator = checkCreator(className, typeElement);

            //将id和属性绑定
            finderCreator.putIdMap(rValue, variableElement);
        }
        Set<? extends Element> onClickElements = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        for (Element element : onClickElements) {
            ExecutableElement executableElement = (ExecutableElement) element;
            OnClick onClick = executableElement.getAnnotation(OnClick.class);
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            String className = typeElement.getQualifiedName().toString();//如com.warm.viewfinder.AptActivity
            FinderCreator finderCreator = checkCreator(className, typeElement);

            for (int id : onClick.value()) {
                //将id和方法绑定
                finderCreator.putOnClickMap(id, executableElement);
            }
        }

        Set<? extends Element> onLongClickElements = roundEnvironment.getElementsAnnotatedWith(OnLongClick.class);
        for (Element element : onLongClickElements) {
            ExecutableElement executableElement = (ExecutableElement) element;
            OnLongClick onLongClick = executableElement.getAnnotation(OnLongClick.class);
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            String className = typeElement.getQualifiedName().toString();//如com.warm.viewfinder.AptActivity
            FinderCreator finderCreator = checkCreator(className, typeElement);

            for (int id : onLongClick.value()) {
                //将id和方法绑定
                finderCreator.putOnLongClickMap(id, executableElement);
            }
        }


        for (String name : mMap.keySet()) {
            try {
                FinderCreator creator = mMap.get(name);
                JavaFile javaFile = JavaFile.builder(creator.getPackageName(), creator.getTypeSpec()).build();
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
