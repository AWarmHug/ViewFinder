package com.warm.finder_apt_processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * 作者：warm
 * 时间：2018-08-21 19:10
 * 描述：
 */
public class FinderCreator {
    private TypeElement host;//activity
    private String hostName;
    private String packageName;

    private Map<Integer, VariableElement> mIdElementMap = new LinkedHashMap<>();
    private Map<Integer, ExecutableElement> mOnClickElementMap = new LinkedHashMap<>();
    private Map<Integer, ExecutableElement> mOnLongClickElementMap = new LinkedHashMap<>();


    public FinderCreator(TypeElement host) {
        this.host = host;
        hostName = host.getSimpleName().toString();
        PackageElement packageElement = (PackageElement) host.getEnclosingElement();
        this.packageName = packageElement.getQualifiedName().toString();
    }

    public void putIdMap(int rValue, VariableElement var) {
        mIdElementMap.put(rValue, var);
    }

    public void putOnClickMap(int rValue, ExecutableElement var) {
        mOnClickElementMap.put(rValue, var);
    }

    public void putOnLongClickMap(int rValue, ExecutableElement var) {
        mOnLongClickElementMap.put(rValue, var);
    }


    public TypeSpec getTypeSpec() {

        TypeSpec typeSpec = TypeSpec.classBuilder(hostName + "_ViewFinder")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(createMethod())
                .build();
        return typeSpec;
    }

    private MethodSpec createMethod() {

        ClassName viewClassName = ClassName.get("android.view", "View");


        MethodSpec.Builder builder = MethodSpec.methodBuilder("find")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeName.get(host.asType()), "host", Modifier.FINAL);
        for (int id : mIdElementMap.keySet()) {
            builder.addCode(
                    "host." + mIdElementMap.get(id).getSimpleName().toString() + "=host.findViewById(" + id + ");\n"
            );
        }
        if (!mOnClickElementMap.isEmpty() || !mOnLongClickElementMap.isEmpty()) {
            builder.addStatement("$T view", viewClassName);
        }

        for (int id : mOnClickElementMap.keySet()) {

            checkIdBeFound(builder, id);

            TypeSpec click = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ClassName.get("android.view", "View.OnClickListener"))
                    .addMethod(MethodSpec
                            .methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(void.class)
                            .addParameter(viewClassName, "v")
                            .addCode(
                                    "host." + mOnClickElementMap.get(id).getSimpleName().toString() + "(" + (mOnClickElementMap.get(id).getParameters().size() == 1 ? "$N" : "") + ");\n", "v"
                            )
                            .build()
                    )
                    .build();
            builder.addStatement("$N.setOnClickListener($L)", "view", click);
        }

        for (int id : mOnLongClickElementMap.keySet()) {
            checkIdBeFound(builder, id);
//            bt2.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    return false;
//                }
//            });
            TypeSpec onLongClick = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ClassName.get("android.view", "View.OnLongClickListener"))
                    .addMethod(MethodSpec
                            .methodBuilder("onLongClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(boolean.class)
                            .addParameter(viewClassName, "v")
                            .addCode(
                                    "host." + mOnLongClickElementMap.get(id).getSimpleName().toString() + "(" + (mOnLongClickElementMap.get(id).getParameters().size() == 1 ? "$N" : "") + ");\n", "v"
                            )
                            .addCode("return false;")
                            .build()
                    )
                    .build();
            builder.addStatement("$N.setOnLongClickListener($L)", "view", onLongClick);

        }


        MethodSpec methodSpec = builder.build();
        return methodSpec;
    }

    //检查当前的id是否已经被findViewById
    private void checkIdBeFound(MethodSpec.Builder builder, int id) {
        if (mIdElementMap.keySet().contains(id)) {
            builder.addCode("view = host." + mIdElementMap.get(id).getSimpleName().toString() + ";\n");
        } else {
            builder.addCode("view = host.findViewById(" + id + ")" + ";\n");
        }
    }


    public String getPackageName() {
        return packageName;
    }
}
