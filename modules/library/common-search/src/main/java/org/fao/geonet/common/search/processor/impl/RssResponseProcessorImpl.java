/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.common.search.processor.SearchResponseProcessor;
import org.springframework.stereotype.Component;

@Component
public class RssResponseProcessorImpl implements SearchResponseProcessor {
  /**
   * Process the search response and return RSS feed.
   *
   */
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, boolean addPermissions) throws Exception {
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
    XMLStreamWriter generator = xmlOutputFactory.createXMLStreamWriter(streamToClient);
    generator.writeStartDocument("UTF-8", "1.0");
    generator.writeStartElement("rss");
    generator.writeAttribute("version", "2.0");

    generator.writeStartElement("channel");
    writeChannelProperties(generator);

    JsonParser parser = ResponseParser.jsonFactory.createParser(streamFromServer);
    parser.nextToken();

    new ResponseParser().matchHits(parser, generator, doc -> {
      writeItem(generator, doc);
    });

    generator.writeEndElement();
    generator.writeEndElement();
    generator.writeEndDocument();
    generator.flush();
    generator.close();
  }

  private void writeChannelProperties(XMLStreamWriter generator) throws XMLStreamException {
    String title = "GeoNetwork opensource";
    String link = "http://localhost:8080/geonetwork";
    String description = "Search for datasets, services and maps...";

    generator.writeStartElement("title");
    generator.writeCharacters(title);
    generator.writeEndElement();
    generator.writeStartElement("link");
    generator.writeCharacters(link);
    generator.writeEndElement();
    generator.writeStartElement("description");
    generator.writeCharacters(description);
    generator.writeEndElement();
  }

  private void writeItem(XMLStreamWriter generator,
      com.fasterxml.jackson.databind.node.ObjectNode doc) throws XMLStreamException {

    Map<String, String> itemProperties = new HashMap<>();
    itemProperties.put("title", "/_source/resourceTitleObject/default");
    itemProperties.put("link", "/_id");
    itemProperties.put("description", "/_source/resourceAbstractObject/default");

    generator.writeStartElement("item");

    for (Entry<String, String> entry : itemProperties.entrySet()) {
      String property = entry.getKey();
      String path = entry.getValue();
      generator.writeStartElement(property);
      JsonNode node = doc.at(path);
      if (node != null) {
        String value = node.asText();
        if (property.equals("link")) {
          value = "http://localhost:8080/geonetwork/srv/api/records/" + value;
        }
        generator.writeCharacters(value);
      }
      generator.writeEndElement();
    }
    generator.writeEndElement();
  }
}
