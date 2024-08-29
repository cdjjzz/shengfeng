package com.src.shengfeng;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.util.DocTrees;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.spi.ToolProvider;

@Slf4j
public class MyDoclet implements Doclet {
    /**
     * 执行文档读取
     *
     * @param args args
     */
    public static void main(String[] args) {
        log.info("main");
          log.info("lsf");
        //组装命令
        String[] javadocCommand =
                new String[]{
                        "-encoding", "utf8",
                        "-private",
                        "-doclet", "com.src.shengfeng.MyDoclet",//自定义MyDoclet的全限定名
                        "-docletpath", "E:\\java-source\\spd-project\\java\\trunk\\eyedsion-spd\\eyedsion-spd-internal\\target\\classes",//编译后文件根路径
                        "-sourcepath", "E:\\java-source\\spd-project\\java\\trunk\\eyedsion-spd\\eyedsion-spd-internal\\src\\main\\java",//要读取注释的包路径
                        "-subpackages", "com.eyedsion.spd.internal.web.basicinfo.domain.request"//具体那个包
                };
        Optional<ToolProvider> docTool = ToolProvider.findFirst("javadoc");
        //执行命令后会执行下面的run方法
        docTool.get().run(System.out,System.err, javadocCommand);
    }

    @Override
    public void init(Locale locale, Reporter reporter) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        return null;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return null;
    }

    /**
     * 文档被初始化后可以读取到数据类
     *
     * @param environment 文档结构对象
     * @return
     */
    @Override
    public boolean run(DocletEnvironment environment) {
        log.info("run() ing...");
        //获取文档树
        final DocTrees docTrees = environment.getDocTrees();
        //遍历节点书
        for (Element element : environment.getIncludedElements()) {
            //节点名称
            String elementName = element.getSimpleName().toString();
            //节点类型
            ElementKind kind = element.getKind();
            String elementType = kind.toString();
            //只看类文件
            if (!kind.equals(ElementKind.CLASS)) {
                continue;
            }
            //转变下节点类型
            TypeElement type = TypeElement.class.cast(element);
            String qualifiedName = type.getQualifiedName().toString();
            log.info("---------------------Class---------------------");
            log.info("节点名称:{}; 节点类型:{}; 全限定名称{}", elementName, elementType, qualifiedName);
            //获取节点上的注解列表
            List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
            if (!annotationMirrors.isEmpty()) {
                log.info("  注解:{}", annotationMirrors);
            }

            //获取当前节点的注释
            DocCommentTree commentTree = docTrees.getDocCommentTree(element);
            String classComment = commentTree.getFullBody().toString();
            log.info("  注释:{}", unicode2String(classComment));
            List<? extends DocTree> classCommentTags = commentTree.getBlockTags();
            log.info("  注释标签:{}", unicode2String(classCommentTags.toString()));
            //获取类中元素
            List<? extends Element> enclosedElements = type.getEnclosedElements();
            for (Element innerElement : enclosedElements) {
                //内部节点名称
                String innerElementName = innerElement.getSimpleName().toString();
                //内部节点类型
                ElementKind innerKind = innerElement.getKind();
                String innerType = innerKind.toString();
                //修饰符
                Set<Modifier> modifiers = innerElement.getModifiers();
                DocCommentTree innerCommentTree = docTrees.getDocCommentTree(innerElement);
                //只看方法和字段
                if (innerKind.equals(ElementKind.METHOD)) {
                    //方法信息
                    log.info("      方法:{}---{} {}", innerType, modifiers, innerElementName);
                    if(innerCommentTree!=null&&innerCommentTree.getFullBody()!=null){
                        log.info("              方法注释:{}", unicode2String(innerCommentTree.getFullBody().toString()));
                    }
                } else if (innerKind.equals(ElementKind.FIELD)) {
                    //字段信息
                    String filedType = innerElement.asType().toString();
                    log.info("      字段:{}",innerElementName);
                    if(innerCommentTree!=null&&innerCommentTree.getFullBody()!=null){
                        log.info("              字段注释:{}", unicode2String(innerCommentTree.getFullBody().toString()));
                    }
                } else {
                    continue;
                }
            }
        }
        return true;
    }
    public static String unicode2String(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        Matcher matcher = pattern.matcher(str);

        char ch;

        while (matcher.find()) {

            ch = (char) Integer.parseInt(matcher.group(2), 16);

            str = str.replace(matcher.group(1), ch + "");

        }

        return str;

    }
//...省略其他的
}

