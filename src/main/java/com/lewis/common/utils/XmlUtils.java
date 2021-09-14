package com.lewis.common.utils;

import org.dom4j.io.SAXReader;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * xml工具类
 *
 * @author taozi
 */
public class XmlUtils {

    /**
     * xml自定义封装头
     */
    private static String xmlHeaderCustom = "";

    /**
     * map转xml
     *
     * @param map
     * @return String
     */
    public static String map2xml(Map map) {
        if (map == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Collection<String> keyset = map.keySet();
        List<String> list = new ArrayList<String>(keyset);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            String k = list.get(i);
            String v = (String) map.get(list.get(i));
            sb.append("<" + k + ">" + v + "</" + k + ">");
        }
        return setXmlHeader(sb.substring(0));
    }

    /**
     * 给map转xml设置xml头
     *
     * @param needToJoin 需要拼接的字符串
     * @return String
     */
    public static String setXmlHeader(String needToJoin) {
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\"?>");
        if (!"".equals(xmlHeaderCustom)) {
            sb.append("<").append(xmlHeaderCustom).append(">");
        }
        sb.append(needToJoin);
        if (!"".equals(xmlHeaderCustom)) {
            sb.append("</").append(xmlHeaderCustom).append(">");
        }
        return sb.substring(0);
    }

    /**
     * xml转map
     *
     * @param xml
     * @return Map<String, String>
     */
    public static Map<String, String> xml2map(String xml) {
        // 先将返回的字符集转换为utf-8
        xml = xml.replaceFirst("encoding=\".*\"" , "encoding=\"UTF-8\"");
        if (null == xml || "".equals(xml)) {
            return null;
        }
        Map<String, String> map = new HashMap<>(10);
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List list = root.getChildren();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                // 将节点名赋值给k给后面的map当key
                String k = e.getName();
                String v = "";
                // 获取e的子节点信息
                List children = e.getChildren();
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = XmlUtils.getChildrenText(children);
                }
                map.put(k, v);
            }
            // キャンセル流
            in.close();
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return map;

    }

    /**
     * list转xml
     *
     * @param list
     * @return String
     */
    public static String getChildrenText(List list) {
        // 用递归方法将自己集合内的东西全部转换为xml格式
        StringBuffer sb = new StringBuffer();
        if (!list.isEmpty()) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list2 = e.getChildren();
                sb.append("<" + name + ">");
                if (!list2.isEmpty()) {
                    sb.append(XmlUtils.getChildrenText(list2));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    /**
     * 获取某标签中指定属性值
     *
     * @param xml         -xml字符串
     * @param rootElement -标签名
     * @param key         -属性名
     * @return                -属性值
     */
    public static String getThisLabelTheValue(String xml, String rootElement, String key) throws Exception {
        // 创建xml解析器
        SAXReader reader = new SAXReader();
        org.dom4j.Document doc = reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        // 读取属性 注意： 获取属性，必须先得到属性所在的标签
        org.dom4j.Element conElem = doc.getRootElement().element(rootElement);
        // 在标签上获取属性(根据属性名获得对应的属性值)
        return conElem.attributeValue(key);
    }

    /**
     * 获取某标签下的某标签的属性值
     *
     * @param elements -某标签下的标签列表
     * @param index    -列表的第index个元素
     * @param key      -属性名
     * @return
     */
    public static String getElementThisLabelTheValue(List elements, int index, String key) {
        org.dom4j.Element element = (org.dom4j.Element) elements.get(index);
        return element.attributeValue(key);
    }

    /**
     * 获取某标签下的标签
     *
     * @param xml         -xml字符串
     * @param rootElement -需要检出的标签
     * @return                 -该标签下的标签list
     */
    public static List getElementTheAllElement(String xml, String rootElement) throws Exception {
        // 创建xml解析器
        SAXReader reader = new SAXReader();
        org.dom4j.Document doc = reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        // 读取属性 注意： 获取属性，必须先得到属性所在的标签
        return doc.getRootElement().element(rootElement).elements();
    }

    /**
     * 获取某标签的文本内容
     *
     * @param xml         -xml字符串
     * @param rootElement -需要检出的标签
     * @throws Exception
     * @return                -文本内容
     */
    public static String getText(String xml, String rootElement) throws Exception {
        // 创建xml解析器
        SAXReader reader = new SAXReader();
        org.dom4j.Document doc = reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        // 读取属性 注意： 获取属性，必须先得到属性所在的标签
        org.dom4j.Element conElem = doc.getRootElement().element(rootElement);
        return conElem.getText();
    }

    /**
     * 获取某标签下的某标签的文本内容
     *
     * @param elements    -需要获取的xml转换成的列表对象
     * @param index       -列表的第index个元素
     * @param rootElement -标签下想要获取的标签名
     * @return                -所需的标签文本内容
     */
    public static String getElementText(List elements, int index, String rootElement) {
        org.dom4j.Element element = (org.dom4j.Element) elements.get(index);
        return element.element(rootElement).getText();
    }

    public static String getXmlHeaderCustom() {
        return xmlHeaderCustom;
    }

    public static void setXmlHeaderCustom(String xmlHeaderCustom) {
        XmlUtils.xmlHeaderCustom = xmlHeaderCustom;
    }
}
