package com.taozi.web.core.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * swagger配置：
 * 示例：在入参前加入@SwaggerApiHide({"userName"}) == 隐藏userName这个参数
 * 在入参前加入@SwaggerApiHide({"*"}) == 隐藏所有
 */
@Component
@Order
public class SwaggerApiHideImpl implements ParameterBuilderPlugin {

    @Autowired
    private TypeResolver typeResolver;

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Class originClass = parameterContext.resolvedMethodParameter().getParameterType().getErasedType();


        Optional<SwaggerApiHide> optional = methodParameter.findAnnotation(SwaggerApiHide.class);
        if (optional.isPresent()) {
            Random random = new Random();
            String name = originClass.getSimpleName() + "Model" + random.nextInt(100);  //model 名称
            List<String> properties = Arrays.asList(optional.get().value());

            String propertiesCompare = "*";
            if (properties.size() == 1 && propertiesCompare.equals(properties.get(0))) {
                properties = new ArrayList<>();
                Field[] fields = originClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    properties.add(fields[i].getName());
                }
            }
            try {
                parameterContext.getDocumentationContext()
                        .getAdditionalModels()
                        .add(typeResolver.resolve(createRefModelIgp(properties.toArray(new String[properties.size()]), originClass.getPackage() + "." + name, originClass)));  //像documentContext的Models中添加我们新生成的Class
            } catch (Exception e) {
                e.printStackTrace();
            }

            parameterContext.parameterBuilder()  //修改Map参数的ModelRef为我们动态生成的class
                    .parameterType("body")
                    .modelRef(new ModelRef(name))
                    .description("此行不用管")
                    .name(name);
        }

    }

    private Class createRefModelIgp(String[] propertys, String name, Class origin) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(name);
        try {
            Field[] fields = origin.getDeclaredFields();
            List<Field> fieldList = Arrays.asList(fields);
            List<String> ignorePropertys = Arrays.asList(propertys);
            List<Field> dealFileds = fieldList.stream().filter(s -> !ignorePropertys.contains(s.getName())).collect(Collectors.toList());
            for (Field field : dealFileds) {
                CtField ctField = new CtField(ClassPool.getDefault().get(field.getType().getName()), field.getName(), ctClass);
                ctField.setModifiers(Modifier.PUBLIC);
                ApiModelProperty ampAnno = origin.getDeclaredField(field.getName()).getAnnotation(ApiModelProperty.class);
                String attributes = java.util.Optional.ofNullable(ampAnno).map(s -> s.value()).orElse("");
                if (StringUtils.isNotBlank(attributes)) { //添加model属性说明
                    ConstPool constPool = ctClass.getClassFile().getConstPool();
                    AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                    Annotation ann = new Annotation(ApiModelProperty.class.getName(), constPool);
                    ann.addMemberValue("value" , new StringMemberValue(attributes, constPool));
                    attr.addAnnotation(ann);
                    ctField.getFieldInfo().addAttribute(attr);
                }
                ctClass.addField(ctField);
            }
            return ctClass.toClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

}
