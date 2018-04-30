package tech.fabricate.issfinder.model;

import java.time.ZonedDateTime;

public class Daylight {
  private final ZonedDateTime sunRise;
  private final ZonedDateTime sunSet;

  public Daylight(final ZonedDateTime sunRise, final ZonedDateTime sunSet) {
    this.sunRise = sunRise;
    this.sunSet = sunSet;
  }

  public boolean isNight(final ZonedDateTime instant) {
    return !isDay(instant);
  }

  public boolean isDay(final ZonedDateTime instant) {
    return instant.isAfter(sunRise) && instant.isBefore(sunSet);
  }
}
