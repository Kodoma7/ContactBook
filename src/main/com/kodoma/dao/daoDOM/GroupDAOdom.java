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
import java.util.*;

/**
 * Created by Кодома on 29.07.2017.
 */
public class GroupDAOdom extends Observable implements DAO<Group> {
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
        setChanged();
        notifyObservers("Группа " + newName + " отредактирована.");
    }

    @Override
    public void remove(Group group) throws Exception {
        checkFileExists();

        Document document = getDocument("contactbook.xml");
        List<String> names = getGroupsNames(document);

        if (!names.contains(group.getNameGroup())) throw new WrongGroupName();

        NodeList languages = document.getElementsByTagName("user");
        Element lang;

        for(int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameGroup = lang.getElementsByTagName("group").item(0).getFirstChild();

            if (nameGroup.getTextContent().equals(group.getNameGroup())) {
                Node g = lang.getElementsByTagName("group").item(0).getFirstChild();
                g.setNodeValue(" ");
            }
        }
        writeDocument(document);
        setChanged();
        notifyObservers("Группа " + group.getNameGroup() + "удалена.");
    }

    @Override
    public void show(Group obj) throws Exception {
        Document document = getDocument("contactbook.xml");

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        Element element;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList userList = userNode.getChildNodes();

            element = (Element)userList;

            Node groupNode = element.getElementsByTagName("group").item(0).getFirstChild();

            if (groupNode.getTextContent().equals(obj.getNameGroup())) {
                String ID = element.getElementsByTagName("id").item(0).getTextContent();
                String firstName = element.getElementsByTagName("first_name").item(0).getTextContent();
                String lastName = element.getElementsByTagName("last_name").item(0).getTextContent();
                String address = element.getElementsByTagName("address").item(0).getTextContent();
                String phoneNumber = element.getElementsByTagName("phone_number").item(0).getTextContent();
                String group = element.getElementsByTagName("group").item(0).getTextContent();

                setChanged();
                String result = String.format("Group: %s [id: %s, name: %s %s, address: %s, phone number: %s]",
                        group, ID, firstName, lastName, address, phoneNumber);
                notifyObservers(result);
            }
        }
    }

    @Override
    public void showAll() throws Exception {
        Document document = getDocument("contactbook.xml");
        HashSet<String> groupsNames = new HashSet<>();

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList userList = userNode.getChildNodes();

            Element element = (Element)userList;
            Node group = element.getElementsByTagName("group").item(0).getFirstChild();
            groupsNames.add(group.getTextContent());
        }

        for (String x : groupsNames) {
            setChanged();
            notifyObservers(x);
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
        addObserver(o);
    }

    @Override
    public void setData(Data data) {

    }
}
