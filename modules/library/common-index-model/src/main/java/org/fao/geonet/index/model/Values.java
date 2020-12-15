package org.fao.geonet.index.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Values {

  @XmlElement
  private String key;
  @XmlElement
  private List<String> values = new ArrayList<>();

  public String getKey() {
    return key;
  }

  public void setKey(String value) {
    key = value;
  }

  public List<String> getValues() {
    return values;
  }

}
