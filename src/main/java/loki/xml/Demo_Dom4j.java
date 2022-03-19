package loki.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.List;

/**
 * 使用dom4j解析xml文件
 */
public class Demo_Dom4j {
    public static void main(String[] args) throws DocumentException {
        URL xmlPath = Demo_Dom4j.class.getClassLoader().getResource("test.xml");
        // 加载xml文件
        SAXReader saxReader = new SAXReader();
        // 读取xml文件
        Document document = saxReader.read(xmlPath);
        // 获取根节点
        Element rootEle = document.getRootElement();
        System.out.println(rootEle.getName());

        // 获取某个子节点
        System.out.println(rootEle.element("stu").getName());

    }
}
