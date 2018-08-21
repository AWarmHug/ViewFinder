package com.warm.finder_apt_processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * 作者：warm
 * 时间：2018-08-21 19:10
 * 描述：
 */
public class CodeCreator {
    private TypeElement host;//activity
    private String hostName;
    private String packageName;

    private Map<Integer, VariableElement> mVariableElementMap = new LinkedHashMap<>();


    public CodeCreator(TypeElement host) {
        this.host = host;
        hostName=host.getSimpleName().toString();
        PackageElement packageElement= (PackageElement) host.getEnclosingElement();
        this.packageName=packageElement.getQualifiedName().toString();
    }

    public void putMap(int rValue, VariableElement var) {
        mVariableElementMap.put(rValue, var);
    }


    public TypeSpec getTypeSpec() {

        TypeSpec typeSpec = TypeSpec.classBuilder(hostName + "_ViewFinder")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(createMethod())
                .build();
        return typeSpec;
    }

    private MethodSpec createMethod() {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("find")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.get(host.asType()), "host");
        for (int id : mVariableElementMap.keySet()) {
            builder.addCode(
                    "host." + mVariableElementMap.get(id).getSimpleName().toString() + "=host.findViewById(" + id + ");"
            );
        }
        MethodSpec methodSpec = builder.build();
        return methodSpec;
    }

    public String getPackageName() {
        return packageName;
    }
}
