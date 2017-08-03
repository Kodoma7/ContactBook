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
        File file = new File("ContactBook.xml");
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
        XPathExpression expr = xpath.compile("ContactBook/User/ID");

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
        // Создается дерево DOM документа из файла
        Document document = documentBuilder.parse(name);
        return document;
    }

    private static void writeDocument(Document document) throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("ContactBook.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
        } catch (TransformerException | IOException e) {
            System.out.println("exception");
        }
    }

    @Override
    public void add(User user) throws Exception {
        System.out.println("работает ContactDAOsax - add");
        /*checkFileExists();
        Document document = getDocument("Employees.xml");
        List<Long> listID = getListID(document);
        long avalableID = getAvalableId(0, listID);

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter("Employees.xml", true));

        writer.writeStartElement("Employee");

        // Заполняем все тэги для книги
        // ID
        writer.writeStartElement("ID");
        writer.writeCharacters(avalableID + "");
        writer.writeEndElement();
        // FirstName
        writer.writeStartElement("FirstName");
        writer.writeCharacters(user.getFname());
        writer.writeEndElement();
        // LastName
        writer.writeStartElement("LastName");
        writer.writeCharacters(user.getLname());
        writer.writeEndElement();
        // Address
        writer.writeStartElement("Address");
        writer.writeCharacters(user.getAddress());
        writer.writeEndElement();
        // PhoneNumber
        writer.writeStartElement("PhoneNumber");
        writer.writeCharacters(user.getPhoneNumber() + "");
        writer.writeEndElement();
        // Group
        writer.writeStartElement("Group");
        writer.writeCharacters(user.getGroup());
        writer.writeEndElement();

        // Закрываем корневой элемент
        writer.writeEndElement();

        // Закрываем XML-документ
        writer.writeEndDocument();
        writer.flush();*/
        checkFileExists();
        Document document = getDocument("ContactBook.xml");

        System.out.println("Поиск id...");

        List<Long> idList = getListID(document);

        long avalableID = getAvalableId(0, idList);

        Node root = document.getDocumentElement();
        Element book = document.createElement("User");

        Element title = document.createElement("ID");
        title.setTextContent(avalableID + "");
        Element author = document.createElement("FirstName");
        author.setTextContent(user.getFname());

        Element date = document.createElement("LastName");
        date.setTextContent(user.getLname());

        Element isbn = document.createElement("Address");
        isbn.setTextContent(user.getAddress());

        Element publisher = document.createElement("PhoneNumber");
        publisher.setTextContent(user.getPhoneNumber() + "");

        Element cost = document.createElement("Group");
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
        System.out.println("работает ContactDAOsax - edit");
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("User");
        Element lang;
        // проходим по каждому элементу Employee
        for (int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("ID").item(0).getFirstChild();
            long id = Long.parseLong(nameID.getTextContent());

            if (id == user.getId()) {
                Node firstName = lang.getElementsByTagName("FirstName").item(0).getFirstChild();
                firstName.setNodeValue(user.getFname());
            }
        }
        writeDocument(document);
    }

    @Override
    public void remove(User user) throws Exception {
        System.out.println("работает ContactDAOsax - remove");
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("ContactBook");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList list = nodes.item(i).getChildNodes(); //тэги
            Node n = list.item(1);

            Element element = (Element) n;
            Node ID = element.getElementsByTagName("ID").item(0);

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
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        String fileName = "ContactBook.xml";
        StringBuilder sb = new StringBuilder();

        DefaultHandler handler = new DefaultHandler() {
            boolean idFound = false;
            boolean record = false;
            int i = 0;

            // Метод вызывается когда SAXParser "натыкается" на начало тэга
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                // Если тэг имеет имя ID, то мы этот момент отмечаем - начался тэг ID
                if (qName.equalsIgnoreCase("ID")) {
                    idFound = true;
                }
            }

            // Метод вызывается когда SAXParser считывает текст между тэгами
            @Override
            public void characters(char ch[], int start, int length) throws SAXException {
                if (record && i < 9) {
                    sb.append(new String(ch, start, length));
                    i++;
                }
                // Если перед этим мы отметили, что имя тэга ID - значит нам надо текст использовать.
                if (idFound) {
                    long id = Long.parseLong(new String(ch, start, length));
                    if (id == user.getId()) {
                        record = true;
                    }
                    idFound = false;
                }
            }
        };

        // Стартуем разбор методом parse, которому передаем наследника от DefaultHandler, который будет вызываться в нужные моменты
        saxParser.parse(fileName, handler);
        System.out.println(sb.toString());
    }

    @Override
    public void showAll() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        String fileName = "ContactBook.xml";

        DefaultHandler handler = new DefaultHandler() {
            boolean isID = false;
            boolean isFirstName = false;
            boolean isLastName = false;
            boolean isAddress = false;
            boolean isPhoneNumber = false;
            boolean isGroup = false;

            // Метод вызывается когда SAXParser "натыкается" на начало тэга
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("ID")) isID = true;
                else if (qName.equalsIgnoreCase("FirstName")) isFirstName = true;
                else if (qName.equalsIgnoreCase("LastName")) isLastName = true;
                else if (qName.equalsIgnoreCase("Address")) isAddress = true;
                else if (qName.equalsIgnoreCase("PhoneNumber")) isPhoneNumber = true;
                else if (qName.equalsIgnoreCase("Group")) isGroup = true;
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String result = new String(ch, start, length);
                if (isID) {
                    System.out.println("ID: " + result);
                    isID = false;
                }
                else if (isFirstName) {
                    System.out.println("FirstName: " + result);
                    isFirstName = false;
                }
                else if (isLastName) {
                    System.out.println("LastName: " + result);
                    isLastName = false;
                }
                else if (isAddress) {
                    System.out.println("Address: " + result);
                    isAddress = false;
                }
                else if (isPhoneNumber) {
                    System.out.println("PhoneNumber: " + result);
                    isPhoneNumber = false;
                }
                else if (isGroup) {
                    System.out.println("Group: " + result + "\n");
                    isGroup = false;
                }
            }
        };
        saxParser.parse(fileName, handler);
    }

    @Override
    public void label(long id, String nameOfGroup) throws Exception {
        System.out.println("работает ContactDAOsax - edit");
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(id)) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("User");
        Element lang;
        // проходим по каждому элементу Employee
        for (int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("ID").item(0).getFirstChild();
            long idL = Long.parseLong(nameID.getTextContent());

            if (idL == id) {
                Node group = lang.getElementsByTagName("Group").item(0).getFirstChild();
                group.setNodeValue(nameOfGroup);
            }
        }
        writeDocument(document);
    }

    @Override
    public void deleteLabel(long id) throws Exception {
        System.out.println("работает ContactDAOsax - deleteLabel");
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(id)) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("User");
        Element lang;
        // проходим по каждому элементу Employee
        for (int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("ID").item(0).getFirstChild();
            long idL = Long.parseLong(nameID.getTextContent());

            if (idL == id) {
                Node group = lang.getElementsByTagName("Group").item(0).getFirstChild();
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