package tech.fabricate.issfinder.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DaylightTest {
  private final ZoneId zoneId = ZoneId.of("Europe/Stockholm");
  private final ZonedDateTime sunRise = ZonedDateTime.of(2018, 4, 4, 6, 0, 0, 0, zoneId);
  private final ZonedDateTime sunSet = ZonedDateTime.of(2018, 4, 4, 20, 0, 0, 0, zoneId);

  @Test
  public void isNight() {
    final Daylight daylight = new Daylight(sunRise, sunSet);
    assertTrue(daylight.isNight(ZonedDateTime.of(2018, 4, 4, 3, 0, 0, 0, zoneId)));
    assertFalse(daylight.isNight(ZonedDateTime.of(2018, 4, 4, 8, 0, 0, 0, zoneId)));
  }

  @Test
  public void isDay() {
    final Daylight daylight = new Daylight(sunRise, sunSet);
    assertTrue(daylight.isDay(ZonedDateTime.of(2018, 4, 4, 8, 0, 0, 0, zoneId)));
    assertFalse(daylight.isDay(ZonedDateTime.of(2018, 4, 4, 3, 0, 0, 0, zoneId)));
  }
}
