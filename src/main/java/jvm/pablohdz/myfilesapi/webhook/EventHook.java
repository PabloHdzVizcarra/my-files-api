package jvm.pablohdz.myfilesapi.webhook;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

public class EventHook {
  private String id;
  private String resourceType;
  private String eventType;
  private String name;
  private String dateAdded;
  private String uri;
  private Collection<String> notes;

  public EventHook() {
    TimeZone timeZone = TimeZone.getDefault();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    dateFormat.setTimeZone(timeZone);
    this.dateAdded = dateFormat.format(new Date());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDateAdded() {
    return dateAdded;
  }

  public void setDateAdded(String dateAdded) {
    this.dateAdded = dateAdded;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Collection<String> getNotes() {
    return notes;
  }

  public void setNotes(Collection<String> notes) {
    this.notes = notes;
  }
}
