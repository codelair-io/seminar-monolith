package tech.fabricate.issfinder.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class History {
  @Id
  @GeneratedValue
  private Long id;
  @Basic
  private double lat;
  @Basic
  private double lon;
  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  public History() {
  }

  public History(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
    this.date = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    History history = (History) o;
    return Double.compare(history.lat, lat) == 0 &&
        Double.compare(history.lon, lon) == 0 &&
        Objects.equals(id, history.id) &&
        Objects.equals(date, history.date);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, lat, lon, date);
  }
}
