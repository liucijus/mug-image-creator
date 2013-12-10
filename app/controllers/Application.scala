package controllers

import play.api._
import play.api.mvc._
import lt.overdrive.mugs.imagegenerator.ImageGenerator
import javax.imageio.ImageIO
import java.io.ByteArrayOutputStream

object Application extends Controller {

  def index(text: String) = Action { implicit req: RequestHeader =>
    Ok(views.html.index(text))
  }

  def render(text: String, department: String) = Action {
    import play.api.Play.current
    val file = Play.application.getFile(Play.configuration.getString("logo.image").get)
    val image = ImageGenerator.generate(ImageIO.read(file), text, department)
    val out = new ByteArrayOutputStream()
    ImageIO.write(image, "PNG", out)
    Ok(out.toByteArray).as("image/png")
  }
}