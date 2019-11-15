package com.darthShana.model;

import io.ebean.annotation.Encrypted;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Customer entity bean.
 */
@Entity
@Table(name = "customer")
public class Customer extends BaseModel {

  @NotNull
  private String name;

  @Encrypted(dbEncryption = false)
  private String mothersMaidenName;

  @Encrypted(dbEncryption = false)
  private Date dateOfBirth;

  private LocalDate registered;

  public Customer(String name) {
    this.name = name;
  }

  public String toString() {
    return "id:" + id + " name:" + name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getRegistered() {
    return registered;
  }

  public void setRegistered(LocalDate registered) {
    this.registered = registered;
  }

  public String getMothersMaidenName() {
    return mothersMaidenName;
  }

  public void setMothersMaidenName(String mothersMaidenName) {
    this.mothersMaidenName = mothersMaidenName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
}
