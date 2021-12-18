package jvm.pablohdz.myfilesapi.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
  @Id
  @Column(name = "user_id", nullable = false, unique = true)
  private String id;

  @Column(name = "user_username", nullable = false, unique = true)
  private String username;

  @Column(name = "user_firstname", nullable = false)
  private String firstname;

  @Column(name = "user_lastname", nullable = false)
  private String lastname;

  @Column(name = "user_number_employee", nullable = false)
  private Integer numberEmployee;

  @Column(name = "user_email", unique = true, nullable = false)
  private String email;

  @Column(name = "user_password", nullable = false)
  private String password;

  @Column(name = "user_active", nullable = false)
  private Boolean active;

  @CreatedDate
  @Column(name = "user_create_at", nullable = false, updatable = false)
  private Date createdAt;

  @LastModifiedDate
  @Column(name = "user_update_at")
  private Date updateAt;

  public User() {
    this.id = "us_" + UUID.randomUUID();
    this.active = false;
  }

  public User(String username) {
    this.username = username;
  }

  @PrePersist
  protected void prePersist() {
    if (this.createdAt == null) createdAt = new Date();
    if (this.updateAt == null) updateAt = new Date();
  }

  @PreUpdate
  protected void preUpdate() {
    this.updateAt = new Date();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public Integer getNumberEmployee() {
    return numberEmployee;
  }

  public void setNumberEmployee(Integer numberEmployee) {
    this.numberEmployee = numberEmployee;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(Date updateAt) {
    this.updateAt = updateAt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
