package tech.fabricate.issfinder;

import java.net.URL;
import java.nio.charset.Charset;
import org.apache.http.HttpHeaders;
import org.apache.http.client.fluent.Request;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.fabricate.issfinder.model.Daylight;
import tech.fabricate.issfinder.remote.OpenNotifyProxy;
import tech.fabricate.issfinder.remote.SunriseSunsetProxy;

/**
 * Simple test to check whether the REST resource returns a JSON object.
 * <p>
 * Since the service at any given test-moment can return a possible finding (or not), we only check that the resource doesn't throw an
 * error that indicates that the code itself doesn't work as we want it to. In short: There is no mocking of the services that we rely on
 * to give the user a proper answer, we just look for indicators that the logic triggers auxiliary errors.
 *
 * @author <a href="mailto:daniel@redbridge.se">Daniel Pfeifer</a>
 */
@RunWith(Arquillian.class)
public class ISSVisibleResourceIT {

  @ArquillianResource
  private URL baseURL;

  @Deployment(testable = false)
  public static WebArchive createArchive() {
    return ShrinkWrap.create(WebArchive.class)
        .addClass(Daylight.class)
        .addClass(OpenNotifyProxy.class)
        .addClass(SunriseSunsetProxy.class)
        .addClass(Config.class)
        .addClass(ISSVisibleResource.class);
  }

  @Test
  @RunAsClient
  public void testDoGetJson() throws Exception {
    final URL resourceURL = new URL(baseURL, "/issvisible");
    final String responseBody = Request.Get(resourceURL.toURI())
        .addHeader(HttpHeaders.ACCEPT, "application/json; charset=UTF-8")
        .execute()
        .returnContent()
        .asString(Charset.forName("UTF-8"));

    assert responseBody.contains("\"issVisible\"") && responseBody.contains("\"status\"");
  }
}
