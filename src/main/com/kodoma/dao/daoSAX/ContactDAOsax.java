package main.com.kodoma.dao.daoSAX;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.BoundException;
import main.com.kodoma.exceptions.WrongIDFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created by Кодома on 29.07.2017.
 */
public class ContactDAOsax implements DAO<User> {
    public static ContactDAOsax instance;

    private ContactDAOsax() {
    }

    public static ContactDAOsax getInstance() {
        if (instance == null) {
            instance = new ContactDAOsax();
        }
        return instance;
    }

    private void checkFileExists() throws IOException {
        File file = new File("contactbook.xml");
        if (!file.exists())
            file.createNewFile();
    }

    private long getAvalableId(long id, List<Long> list) throws BoundException {
        if (id < Long.MAX_VALUE && !list.contains(id))
            return id++;
        else if (id < Long.MAX_VALUE)
            return getFreeId(list);
        else throw new BoundException();
    }

    private long getFreeId(List<Long> list) {
        long nextId;
        long result = 0;
        for (long id : list) {
            nextId = id;
            if (!list.contains(++nextId)) {
                result = nextId;
                break;
            }
        }
        return result;
    }

    private List<Long> getListID(Document document) throws XPathExpressionException, BoundException {
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user/id");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<Long> idList = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            long id = Long.parseLong(n.getTextContent());
            idList.add(id);
        }
        return idList;
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
    public void add(User user) throws Exception {
        checkFileExists();
        Document document = getDocument("contactbook.xml");
        List<Long> idList = getListID(document);

        long avalableID = getAvalableId(0, idList);

        Node root = document.getDocumentElement();
        Element book = document.createElement("user");

        Element title = document.createElement("id");
        title.setTextContent(avalableID + "");
        Element author = document.createElement("first_name");
        author.setTextContent(user.getFname());

        Element date = document.createElement("last_name");
        date.setTextContent(user.getLname());

        Element isbn = document.createElement("address");
        isbn.setTextContent(user.getAddress());

        Element publisher = document.createElement("phone_number");
        publisher.setTextContent(user.getPhoneNumber() + "");

        Element cost = document.createElement("group");
        cost.setTextContent(user.getGroup());

        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(date);
        book.appendChild(isbn);
        book.appendChild(publisher);
        book.appendChild(cost);

        root.appendChild(book);

        writeDocument(document);
    }

    @Override
    public void edit(User user, String str) throws Exception {
        Document document = getDocument("contactbook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("user");
        Element lang;

        for (int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("id").item(0).getFirstChild();
            long id = Long.parseLong(nameID.getTextContent());

            if (id == user.getId()) {
                Node firstName = lang.getElementsByTagName("first_name").item(0).getFirstChild();
                firstName.setNodeValue(user.getFname());
            }
        }
        writeDocument(document);
    }

    @Override
    public void remove(User user) throws Exception {
        Document document = getDocument("contactbook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList list = nodes.item(i).getChildNodes();
            Node n = list.item(1);

            Element element = (Element) n;
            Node ID = element.getElementsByTagName("id").item(0);

            System.out.println(ID.getNodeName());
            System.out.println(ID.getTextContent());

            long id = Long.parseLong(ID.getTextContent());

            if (id == user.getId())
                userNode.removeChild(n);
        }
        writeDocument(document);
    }

    @Override
    public void show(User user) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        Document document = getDocument("contactbook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        String fileName = "contactbook.xml";
        StringBuilder sb = new StringBuilder();

        DefaultHandler handler = new DefaultHandler() {
            boolean idFound = false;
            boolean record = false;
            int i = 0;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("id")) {
                    idFound = true;
                }
            }

            @Override
            public void characters(char ch[], int start, int length) throws SAXException {
                if (record && i < 9) {
                    sb.append(new String(ch, start, length));
                    i++;
                }

                if (idFound) {
                    long id = Long.parseLong(new String(ch, start, length));
                    if (id == user.getId()) {
                        record = true;
                    }
                    idFound = false;
                }
            }
        };

        saxParser.parse(fileName, handler);
        System.out.println(sb.toString());
    }

    @Override
    public void showAll() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        String fileName = "contactbook.xml";

        DefaultHandler handler = new DefaultHandler() {
            boolean isID = false;
            boolean isFirstName = false;
            boolean isLastName = false;
            boolean isAddress = false;
            boolean isPhoneNumber = false;
            boolean isGroup = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("id")) isID = true;
                else if (qName.equalsIgnoreCase("first_name")) isFirstName = true;
                else if (qName.equalsIgnoreCase("last_name")) isLastName = true;
                else if (qName.equalsIgnoreCase("address")) isAddress = true;
                else if (qName.equalsIgnoreCase("phone_number")) isPhoneNumber = true;
                else if (qName.equalsIgnoreCase("group")) isGroup = true;
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String result = new String(ch, start, length);
                if (isID) {
                    System.out.println("id: " + result);
                    isID = false;
                }
                else if (isFirstName) {
                    System.out.println("first_name: " + result);
                    isFirstName = false;
                }
                else if (isLastName) {
                    System.out.println("last_name: " + result);
                    isLastName = false;
                }
                else if (isAddress) {
                    System.out.println("address: " + result);
                    isAddress = false;
                }
                else if (isPhoneNumber) {
                    System.out.println("phone_number: " + result);
                    isPhoneNumber = false;
                }
                else if (isGroup) {
                    System.out.println("group: " + result + "\n");
                    isGroup = false;
                }
            }
        };
        saxParser.parse(fileName, handler);
    }

    @Override
    public void searchUser(String name) throws Exception {

    }

    @Override
    public void label(long id, String nameOfGroup) throws Exception {
        Document document = getDocument("contactbook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(id)) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("user");
        Element lang;

        for (int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("id").item(0).getFirstChild();
            long idL = Long.parseLong(nameID.getTextContent());

            if (idL == id) {
                Node group = lang.getElementsByTagName("group").item(0).getFirstChild();
                group.setNodeValue(nameOfGroup);
            }
        }
        writeDocument(document);
    }

    @Override
    public void deleteLabel(long id) throws Exception {
        Document document = getDocument("contactbook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(id)) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("user");
        Element lang;

        for (int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("id").item(0).getFirstChild();
            long idL = Long.parseLong(nameID.getTextContent());

            if (idL == id) {
                Node group = lang.getElementsByTagName("group").item(0).getFirstChild();
                group.setNodeValue(" ");
            }
        }
        writeDocument(document);
    }

    @Override
    public void setObserver(Observer o) {

    }

    @Override
    public void setData(Data data) {

    }
}