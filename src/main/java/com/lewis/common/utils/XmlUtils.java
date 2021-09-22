package com.lewis.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * xml工具类
 *
 * @author Lewis
 */
@Slf4j
public class XmlUtils {

    /**
     * xml自定义封装头
     */
    private static String xmlHeaderCustom = "";

    /**
	 * 取得Root节点
     * @param document document对象
     * @return 内容对象
     */
    public static Element getRootElement(Document document) {
        // root元素是xml文档的根节点。一切XML分析都是从Root元素开始的。
        return document.getRootElement();
    }

    /**
     * 根据xml文件路径获取到xml字符串
     * @param filePath 文件路径
     * @return xml字符串
     */
    public static String getXmlStrByFilePath(String filePath) {
        SAXReader reader = new SAXReader();
        // 获取到xml文件
        Document document = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return "文件不存在，请检查路径：" + filePath + "是否存在！";
            }
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document.asXML();
    }

    /**
     * 获取某标签中指定属性值
     *
     * @param xmlStr         -xml字符串
     * @param rootElement -标签名
     * @param key         -属性名
     * @return            -属性值
     */
    public static String getThisLabelTheValue(String xmlStr, String rootElement, String key) throws Exception {
        // 创建xml解析器
        Document doc = getDocumentByXmlStr(xmlStr);
        // 读取属性 注意： 获取属性，必须先得到属性所在的标签
        Element conElem = doc.getRootElement().element(rootElement);
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
        Element element = (Element) elements.get(index);
        return element.attributeValue(key);
    }

    /**
     * 获取某标签下的标签list
     *
     * @param xmlStr       -xml字符串
     * @param elementName -需要检出的标签
     * @return            -该标签下的标签list
     */
    public static List<Element> getElements(String xmlStr, String elementName) {
        // 创建xml解析器
        Document doc = getDocumentByXmlStr(xmlStr);
        // 读取属性 注意： 获取属性，必须先得到属性所在的标签
        // 获取根节点
        Element root = doc.getRootElement();
        // 根节点下根据标签名字获取该节点
        Element element = root.element(elementName);
        // 若不存在，则向下循环查找
        if (null == element) {
            element = getElementByElements(root.elements(), elementName);
        }
        // 若最终为空，直接返回null
        if (null == element) {
            return null;
        }
        // 若不为空，返回该节点下的节点列表
        return element.elements();
    }

    /**
     * 从节点列表中根据节点名称来找到对应的节点
     * @param elements 节点列表
     * @param elementName 节点名称
     * @return 节点元素
     */
    public static Element getElementByElements(List<Element> elements, String elementName) {
        for (Element e : elements) {
            String name = getElementName(e);
            log.info("查询中 - {}", name);
            // 若同名，则证明找到了
            if (elementName.equals(name)) {
                log.info("查到了 - {}", name);
                return e;
            }
            List list = e.elements();
            if (list.size() != 0) {
                Element element = getElementByElements(list, elementName);
                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
	 * 获取节点中的节点list
     * @param element 节点
     * @return 节点list
     */
    public static List<Element> getElementsByElement(Element element) {
        return element.elements();
    }

    /**
     * 获取该节点的标签名
     * @param element 节点对象
     * @return 标签名
     */
    public static String getElementName(Element element) {
        return element.getName();
    }

    /**
     * 获取该节点的标签内容
     * @param element 节点对象
     * @return 标签内容
     */
    public static String getElementText(Element element) {
        return element.getText();
    }

    /**
     * 根据xml字符串创建xml解析器
     * @param xmlStr xml字符串
     * @return xml解析器
     */
    public static Document getDocumentByXmlStr(String xmlStr) {
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(new ByteArrayInputStream(xmlStr.getBytes(StandardCharsets.UTF_8)));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return doc;
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
        Document doc = reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        // 读取属性 注意： 获取属性，必须先得到属性所在的标签
        Element conElem = doc.getRootElement().element(rootElement);
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
        Element element = (Element) elements.get(index);
        return element.element(rootElement).getText();
    }

    public static String getXmlHeaderCustom() {
        return xmlHeaderCustom;
    }

    public static void setXmlHeaderCustom(String xmlHeaderCustom) {
        XmlUtils.xmlHeaderCustom = xmlHeaderCustom;
    }
}
