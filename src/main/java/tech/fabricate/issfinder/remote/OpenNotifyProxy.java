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

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 * Proxy class to invoke {@code http://api.open-notify.org/iss-pass.json}-service.
 *
 * @author <a href="mailto:daniel@redbridge.se">Daniel Pfeifer</a>
 */
@ApplicationScoped
public class OpenNotifyProxy {
  private static final String API_URL = "http://api.open-notify.org/iss-pass.json";

  public Set<Instant> fetchIssPassDates(final double lat, final double lon) {
    return ClientBuilder.newClient()
        .target(API_URL)
        .queryParam("lat", lat)
        .queryParam("lon", lon)
        .queryParam("n", 100)
        .request(MediaType.APPLICATION_JSON)
        .buildGet()
        .invoke(JsonObject.class)
        .getJsonArray("response")
        .stream()
        .map(val -> ((JsonObject) val).getInt("risetime"))
        .map(Instant::ofEpochSecond)
        .collect(Collectors.toSet());
  }
}
