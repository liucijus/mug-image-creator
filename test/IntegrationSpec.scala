import org.specs2.runner._
import org.junit.runner._

import play.api.libs.ws.{Response, WS}
import play.api.test._

@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends PlaySpecification {
  "Application" should {
    "should show image in frontpage" in new WithBrowser {
      browser.goTo("http://localhost:" + port)
      val img = browser.$("#mug_image")
      val imageUrl = img.getAttribute("src")
      val response: Response = await(WS.url(imageUrl).get())

      response.status must equalTo(OK)
      response.getAHCResponse.getContentType must equalTo("image/png")
      response.getAHCResponse.getResponseBodyAsBytes.size must beGreaterThan(0)
    }
  }
}
