package jvm.pablohdz.myfilesapi.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/** Represents a CSV file stored */
@Entity
@Table(name = "file")
public class MyFile {
  @Id
  @Column(name = "file_id", nullable = false)
  private String id;

  @Column(name = "file_storage_id", nullable = false)
  private String storageId;

  @Column(name = "file_name", unique = true)
  private String name;

  @Column(name = "file_version")
  private Double version;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @CreatedDate
  @Column(name = "user_create_at", nullable = false, updatable = false)
  private Long createdAt;

  @LastModifiedDate
  @Column(name = "user_update_at")
  private Long updateAt;

  public MyFile() {
    this.id = "file_" + UUID.randomUUID();
  }

  public MyFile(String storageId) {
    this.storageId = storageId;
  }

  public MyFile(String id, String filename) {
    this.id = id;
    this.name = filename;
  }

  @PrePersist
  protected void prePersist() {
    if (this.createdAt == null) createdAt = new Date().getTime();
    if (this.updateAt == null) updateAt = new Date().getTime();
  }

  @PreUpdate
  protected void preUpdate() {
    this.updateAt = new Date().getTime();
  }

  public String getId() {
    return id;
  }

  public String getStorageId() {
    return storageId;
  }

  public void setStorageId(String url) {
    this.storageId = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getVersion() {
    return version;
  }

  public void setVersion(Double version) {
    this.version = version;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public Long getUpdateAt() {
    return updateAt;
  }
}
