package com.src.shengfeng;

import com.github.javaparser.JavaToken;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.utils.SourceRoot;
import com.src.shengfeng.enumlsf.SystemParamConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class JavaParseAddDataAnnotation1 {

    public static void main(String[] args) {
        Path sourcePath = Path.of("E:\\java-source\\yining\\src\\main\\java\\com\\eyedsion\\his\\web\\service"); // 指向源代码目录
        deal(sourcePath);
    }
    public static void deal(Path sourcePath){
        SourceRoot sourceRoot = new SourceRoot(sourcePath);
        try {
            sourceRoot.tryToParse().stream().forEach(t->processCompilationUnits(t));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processCompilationUnits(ParseResult<CompilationUnit> result) {
        result.getResult().stream().forEach(cu -> {
         /*   new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, Void arg) {
                    super.visit(n, arg);
*//*
                    if(!cu.getImports().toString().contains("lombok.Data")){
                        cu.addImport("lombok.Data");
                    }
                    // 添加 @Data 注解
                    if (!n.getAnnotations().toString().contains("@Data")) {
                        n.addAnnotation("Data");
                    }
                    System.out.println(n.getMethods());
                    // 创建一个要删除的方法列表
                    List<MethodDeclaration> methodsToRemove = n.getMethods().stream()
                            .filter(JavaParseAddDataAnnotation1::isGetterOrSetter)
                            .collect(Collectors.toList());

                    // 从原始列表中删除这些方法
                    methodsToRemove.forEach(n::remove);*//*
                }
            }.visit(cu, null);*/
            //paramValueGetter.getUserParamValue(getCurrentUserCode(), param.getSelectDepartCode(), "107122");
           new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodCallExpr n, Void arg) {
                    super.visit(n, arg);
                    if(n.getNameAsString().startsWith("getUserParamValue")){
                        if(n.getArguments().size()==3){
                            boolean isUser=false;
                            boolean isSystem=true;
                            Expression expression= n.getArguments().get(0);
                            String v=n.getArguments().get(2).toString();
                            if(!(expression instanceof NullLiteralExpr)){
                                isUser=true;
                            }
                            if(isSystem) {
                                try {
                                    if (isUser) {
                                        n.replace(
                                                new NameExpr("sysParamService.getStringByUser(SysParamEnum._" + v + ")"));
                                    } else {
                                        n.replace(
                                                new NameExpr("sysParamService.getString(SysParamEnum._" + v + ")"));
                                    }
                                } catch (Exception e) {
                                    if (isUser) {
                                       n.replace(
                                                new NameExpr("sysParamService.getStringByUser(SysParamEnum._" + v + ")"));
                                    } else {
                                       n.replace(
                                                new NameExpr("sysParamService.getString(SysParamEnum._" + v + ")"));
                                    }
                                }
                            }
                        }
                    }
                }
            }.visit(cu, null);
           // 将更改保存回文件
             try {
                File file = new File(cu.getStorage().get().getPath().toString());
                FileWriter writer = new FileWriter(file);
                writer.write(new PrettyPrinter().print(cu));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static boolean isGetterOrSetter(MethodDeclaration method) {
        String methodName = method.getNameAsString();
        return methodName.matches("(get|set||is)[A-Z].*");
    }
}