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

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ConfigIT {

  @Inject
  private Config config;

  @Deployment
  public static WebArchive createArchive() {
    return ShrinkWrap
        .create(WebArchive.class)
        .addClass(Config.class);
  }

  @Test
  public void testWhetherDefaultConfigIsCorrect() {
    assert config.getLatitude() == 59.334591 &&
        config.getLongitude() == 18.06324;
  }
}
