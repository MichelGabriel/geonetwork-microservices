package org.fao.geonet.common.search.domain.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EsRecord {
  @JsonProperty("_id")
  private String uuid;
  @JsonProperty("_source")
  private EsRecordSource source;
  private boolean edit;
  private boolean owner;
  private boolean isPublishedToAll;
  private boolean view;
  private boolean notify;
  private boolean download;
  private boolean dynamic;
  private boolean featured;
  private boolean guestdownload;
  private boolean selected;
}
