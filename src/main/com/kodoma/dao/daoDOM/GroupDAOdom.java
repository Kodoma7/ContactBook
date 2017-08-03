package main.com.kodoma.dao.daoDOM;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.exceptions.WrongGroupName;
import main.com.kodoma.exceptions.WrongIDFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created by Кодома on 29.07.2017.
 */
public class GroupDAOdom implements DAO<Group> {
    public static GroupDAOdom instance;

    private GroupDAOdom() {
    }

    public static GroupDAOdom getInstance() {
        if (instance == null) {
            instance = new GroupDAOdom();
        }
        return instance;
    }

    private void checkFileExists() throws IOException {
        File file = new File("contactbook.xml");
        if (!file.exists())
            file.createNewFile();
    }

    private List<String> getGroupsNames(Document document) throws XPathExpressionException {
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user/group");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<String> names = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            names.add(n.getTextContent());
        }
        return names;
    }

    private Document getDocument(String name) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(name);
        return document;
    }

    private void writeDocument(Document document) throws TransformerFactoryConfigurationError, TransformerException, FileNotFoundException {
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(document);
        FileOutputStream fos = new FileOutputStream("contactbook.xml");
        StreamResult result = new StreamResult(fos);
        tr.transform(source, result);
    }

    @Override
    public void add(Group obj) throws Exception {

    }

    @Override
    public void edit(Group group, String newName) throws Exception {
        checkFileExists();

        Document document = getDocument("contactbook.xml");
        List<String> names = getGroupsNames(document);

        if (!names.contains(group.getNameGroup())) throw new WrongGroupName();

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user/group");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (group.getNameGroup().equals(n.getTextContent())) {
                n.getFirstChild().setNodeValue(newName);
            }
        }
        writeDocument(document);
    }

    @Override
    public void remove(Group group) throws Exception {
        checkFileExists();

        Document document = getDocument("contactbook.xml");
        List<String> names = getGroupsNames(document);

        if (!names.contains(group.getNameGroup())) throw new WrongGroupName();

        NodeList languages = document.getElementsByTagName("user");
        Element lang;

        for(int i=0; i<languages.getLength();i++) {
            lang = (Element) languages.item(i);
            Node nameGroup = lang.getElementsByTagName("group").item(0).getFirstChild();

            if (nameGroup.getTextContent().equals(group.getNameGroup())) {
                Node g = lang.getElementsByTagName("group").item(0).getFirstChild();
                g.setNodeValue(" ");
            }
        }
        writeDocument(document);
    }

    @Override
    public void show(Group group) throws Exception {
        Document document = getDocument("contactbook.xml");

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList userList = userNode.getChildNodes();

            Element element = (Element)userList;

            Node g = element.getElementsByTagName("group").item(0).getFirstChild();

            if (g.getTextContent().equals(group.getNameGroup()))
                System.out.println("контакт: " + userNode.getTextContent());
        }
    }

    @Override
    public void showAll() throws Exception {
        Document document = getDocument("contactbook.xml");

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList userList = userNode.getChildNodes();

            Element element = (Element)userList;

            Node g = element.getElementsByTagName("group").item(0).getFirstChild();

            System.out.println("группа: " + g.getTextContent());
        }
    }

    @Override
    public void label(long id, String nameOfGroup) throws WrongIDFormat {

    }

    @Override
    public void deleteLabel(long id) throws WrongIDFormat {

    }

    @Override
    public void setObserver(Observer o) {

    }

    @Override
    public void setData(Data data) {

    }
}
