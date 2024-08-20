package com.src.shengfeng;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.utils.SourceRoot;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@SpringBootTest
class ShengfengApplicationTests {



    @Resource
    private Map<String, String> systemMessage;

    @Test
    void contextLoads() {
        //E:\java-source\yining\src\main\java\com\eyedsion\his\web\controller
        //E:\java-source\yining\src\main\java\com\eyedsion\his\web\service
        Path sourcePath = Path.of("E:\\java-source\\yining\\src\\main\\java\\com\\eyedsion\\his\\web\\service"); // 指向源代码目录
        deal(sourcePath);
    }
    public  void deal(Path sourcePath){
        try {
            if (Files.isDirectory(sourcePath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath)) {
                    for (Path path : stream) {
                        if (Files.isDirectory(path)) {
                            System.out.println(path);
                            SourceRoot sourceRoot = new SourceRoot(path);
                            sourceRoot.tryToParse().stream().forEach(t->processCompilationUnits(t));
                        }else{
                            SourceRoot sourceRoot = new SourceRoot(path.getParent());
                            sourceRoot.tryToParse().stream().forEach(t->processCompilationUnits(t));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void processCompilationUnits(ParseResult<CompilationUnit> result) {
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
           new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(StringLiteralExpr n, Void arg) {
                    super.visit(n, arg);
                    // 检查字符串字面量是否为 "system"
                    if (n.getValue().startsWith("material-3")
                    ) {
                        n.getParentNode().get().replace(new NameExpr("\""+systemMessage.get(n.getValue())+"\""));
                    }
                }
            }.visit(cu, null);
           /* new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodCallExpr n, Void arg) {
                    super.visit(n, arg);
                    // 检查是否为 System.out.println 调用
                    if ("printRequestParam".equals(n.getNameAsString())) {
                        System.out.println(n.getNameAsString());
                        n.remove();
                    }
                }
            }.visit(cu, null);*/

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
