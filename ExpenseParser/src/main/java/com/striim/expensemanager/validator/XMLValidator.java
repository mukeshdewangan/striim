package com.striim.expensemanager.validator;

import com.striim.expensemanager.driver.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XMLValidator {
    private static final Logger logger = LoggerFactory.getLogger(XMLValidator.class);

    ValidatorHandler validatorHandler;
    public XMLValidator(String schemaFile) throws SAXException {
        File xsdFile = new File(schemaFile);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsdFile);
        validatorHandler = schema.newValidatorHandler();
    }

    public ValidatorHandler getValidatorHandler() {
        return validatorHandler;
    }

    public boolean isValidFile(String filePath, String... schemaFile) {
        try (
            InputStream xml = Files.newInputStream(Paths.get(filePath));
            InputStream xsd = Files.newInputStream(Paths.get(schemaFile[0]))
        ) {
            if (xml == null || xsd == null) {
                logger.info("Missing XML or XSD file.");
                return false;
            }

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(xml));
            logger.info("XML is valid against the XSD.");
            return true;

        } catch (Exception e) {
            logger.info("Validation failed: " + e.getMessage());
            return false;
        }
    }
}
