package com.example.demo;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {
    private static final String API = "currency.api.url";
    public static String getAPI() {
        return API;
    }


    private StringBuffer getResponse(String url) throws IOException {
        System.out.println(url);
        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return response;
    }

    List<Record> records = new LinkedList<>();

    private List<Record> parseResponse(StringBuffer response) {
        List<Record> records = new LinkedList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource source;
        try {
            builder = factory.newDocumentBuilder();
            source = new InputSource(new StringReader(response.toString()));
            Document doc = builder.parse(source);
            NodeList recordList = doc.getElementsByTagName("item");

            for (int i = 0; i < recordList.getLength(); i++) {
                Node item = recordList.item(i);
                String bankName = item.getChildNodes().item(1).getTextContent();
                String date = item.getChildNodes().item(3).getTextContent();
                String currency = item.getChildNodes().item(5).getTextContent();
                String sell = item.getChildNodes().item(7).getTextContent();
                String buy;
                try {
                    buy = item.getChildNodes().item(9).getTextContent();
                } catch (NullPointerException e) {
                    continue;
                }
                Record record = new Record();
                record.setBank(bankName);
                record.setDate(date);
                record.setCurrency(currency);
                record.setSell(Float.parseFloat(sell));
                try {
                    record.setBuy(Float.parseFloat(buy));
                } catch (NullPointerException e) {
                    continue;
                }
                records.add(record);
            }
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }
        return records;
    }

    @RequestMapping("/")
    public String getMainPage(Model model) throws IOException {
        StringBuffer response = getResponse(PropertyReader.getProperty(API));
        records = parseResponse(response);
        model.addAttribute("records", records);
        return "mainpage";
    }
}
