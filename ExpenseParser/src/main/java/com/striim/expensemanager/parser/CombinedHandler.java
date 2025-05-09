package com.striim.expensemanager.parser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


// Custom ContentHandler to forward SAX events to both validator and logic
public class CombinedHandler extends DefaultHandler {
    private final ContentHandler[] handlers;

    public CombinedHandler(ContentHandler... handlers) {
        this.handlers = handlers;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        for (ContentHandler h : handlers) h.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        for (ContentHandler h : handlers) h.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        for (ContentHandler h : handlers) h.endElement(uri, localName, qName);
    }
}