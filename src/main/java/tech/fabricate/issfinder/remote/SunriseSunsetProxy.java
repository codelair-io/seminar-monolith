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

package tech.fabricate.issfinder.remote;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import tech.fabricate.issfinder.model.Daylight;

/**
 * Proxy class to invoke {@code http://api.sunrise-sunset.org/json}-service.
 *
 * @author <a href="mailto:daniel@redbridge.se">Daniel Pfeifer</a>
 */
@ApplicationScoped
public class SunriseSunsetProxy {
  private static final String API_URL = "http://api.sunrise-sunset.org/json";

  public Daylight fetchDaylightForDate(final double lat, final double lon, final TemporalAccessor date) {
    final JsonObject response = ClientBuilder.newClient()
        .target(API_URL)
        .queryParam("lat", lat)
        .queryParam("lng", lon)
        .queryParam("date", date)
        .queryParam("formatted", 0)
        .request(MediaType.APPLICATION_JSON)
        .buildGet()
        .invoke(JsonObject.class)
        .getJsonObject("results");

    return new Daylight(ZonedDateTime.parse(response.getString("sunrise")), ZonedDateTime.parse(response.getString("sunset")));
  }
}
