package com.spring.commons.entity.hutool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.XmlUtil;
import org.junit.Test;
import org.w3c.dom.*;

import java.io.File;
import java.io.FileFilter;
import java.util.List;


/**
 * @Description: XmlUtilDemo
 * @Author lenovo
 * @Date: 2021/5/19 15:51
 * @Version 1.0
 */
public class XmlUtilDemo {

    public static void main(String[] args) {
//        Document document = XmlUtil.parseXml("<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
//                "<returnsms> " +
//                "  <returnstatus>Success（成功）</returnstatus>  " +
//                "  <message>ok</message>  " +
//                "  <remainpoint>1490</remainpoint>  " +
//                "  <taskID>885</taskID>  " +
//                "  <successCounts>1</successCounts> " +
//                "</returnsms>");

        Document document = XmlUtil.readXML("demo.xml");
        NodeList childNodes = document.getChildNodes();
        each(childNodes);
//        for (int i = 0; i < childNodes.getLength(); i++) {
//            Node item = childNodes.item(i);
//            String nodeName = item.getNodeName();
//            System.out.println(nodeName);
//        }


    }
    public static void each(NodeList childNodes){
        int length = childNodes.getLength();
        if (length != 0){
            for (int i = 0; i < length; i++) {
                Node item = childNodes.item(i);
                String nodeName = item.getNodeName();
                if (!"#text".equals(nodeName)){
                    System.out.println("nodeName:"+nodeName);
                    System.out.println("节点内容："+item.getTextContent()+"----value:"+item.getNodeValue());
                    NamedNodeMap attributes = item.getAttributes();
                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node item1 = attributes.item(j);
                        System.out.println(nodeName+"标签"+item1.getNodeName()+"属性："+item1.getTextContent()+"----value:"+item.getNodeValue());
                    }
                }
                if (item.hasChildNodes())
                    each(item.getChildNodes());
            }
        }else {
            Node item = childNodes.item(0);
            String nodeName = item.getNodeName();
            System.out.println("======"+nodeName);
        }
    }



    @Test
    public void test1(){
        Document xml = XmlUtil.createXml("mapper");
        Node mapperNode = xml.getFirstChild();
        Element mapperElement = (Element)mapperNode;
        mapperElement.setAttribute("namespace","com.email.cloud.mapper.PermissionMapper");
        //Node node = new Node();

        String toStr = XmlUtil.toStr(xml);
        System.out.println(toStr);

    }
    @Test
    public void test2(){
//        File[] ls = FileUtil.ls("E:\\图片");
//        for (int i = 0; i < ls.length; i++) {
//            System.out.println(ls[i].getName());
//        }
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getAbsolutePath().contains("$")){
                    System.out.println(pathname.getAbsolutePath());
                    return false;
                }

                return true;
            }
        };
        List<File> files = FileUtil.loopFiles("E:\\IDEAProjects");
//        FileWriter writer = new FileWriter("E:\\test.txt");
//
//        files.forEach(o-> {
//            writer.append(o.getAbsolutePath());
//            //System.out.println(o.getAbsolutePath());
//        });
//        System.out.println("-----------end---------");


        FileAppender appender = new FileAppender(new File("E:\\test2.txt"), 16, true);
        files.forEach(o-> {
            appender.append(o.getAbsolutePath());
            //System.out.println(o.getAbsolutePath());
        });
        appender.flush();
        appender.toString();
        System.out.println("-----------end---------");
        FileNameUtil.isType("111",new String[]{});
    }
}
