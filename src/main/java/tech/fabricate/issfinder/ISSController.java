/*
 * issfinder by Daniel Pfeifer (RedBridge Group).
 *
 * To the extent possible under law, the person who associated CC0 with
 * issfinder has waived all copyright and related or neighboring rights
 * to issfinder.
 *
 * You should have received a copy of the CC0 legalcode along with this
 * work.  If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package tech.fabricate.issfinder;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import tech.fabricate.issfinder.model.Daylight;
import tech.fabricate.issfinder.model.History;
import tech.fabricate.issfinder.remote.OpenNotifyProxy;
import tech.fabricate.issfinder.remote.SunriseSunsetProxy;

@Named("controller")
@ViewScoped
public class ISSController implements Serializable {
  @PersistenceUnit
  private EntityManagerFactory emf;
  @Inject
  private Config config;
  @Inject
  private OpenNotifyProxy openNotifyProxy;
  @Inject
  private SunriseSunsetProxy sunriseSunsetProxy;

  private double latitude;
  private double longitude;
  private String result;

  @PostConstruct
  private void prefillLatLon() {
    latitude = config.getLatitude();
    longitude = config.getLongitude();
  }

  @Transactional
  public void search() {
    final EntityManager em = emf.createEntityManager();
    try {
      em.persist(new History(latitude, longitude));
    } finally {
      em.close();
    }

    result = getIssPassTime(latitude, longitude)
        .map(dateTime ->
            "Next time you will see the ISS is on "
                + dateTime.withZoneSameInstant(ZoneId.of("Europe/Stockholm")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                + ". Yay :)")
        .orElse("Sooooo sorry, but after doing plenty of checking we can't seem to find the next ISS-flyby :(");
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public List<History> getHistory() {
    final EntityManager em = emf.createEntityManager();
    try {
      return em.createQuery("select h from History h order by h.date desc", History.class).getResultList();
    } finally {
      em.close();
    }
  }

  private Optional<ZonedDateTime> getIssPassTime(final double lat, final double lon) {
    final Set<Instant> instants = openNotifyProxy.fetchIssPassDates(lat, lon);

    for (final Instant instant : instants) {
      final ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
      final Daylight daylightForDate = sunriseSunsetProxy.fetchDaylightForDate(config.getLatitude(), config.getLongitude(), dateTime);

      if (daylightForDate.isNight(dateTime)) {
        return Optional.of(dateTime);
      }
    }

    return Optional.empty();
  }
}
