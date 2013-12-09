package controllers

import play.api._
import play.api.mvc._
import lt.overdrive.mugs.imagegenerator.ImageGenerator
import javax.imageio.ImageIO
import java.io.{ByteArrayOutputStream, OutputStream, BufferedOutputStream}
import scalax.io.managed.WritableByteChannelResource
import play.api.http.Writeable
import play.core.Router

object Application extends Controller {

  def index(text: String) = Action {
    Ok(views.html.index(text))
  }

  def render(text: String, department: String) = Action {
    import play.api.Play.current
    val file = Play.application.getFile("public/images/sl_c_pos_letter_250px_A4.png")
    val image = ImageGenerator.generate(ImageIO.read(file), text, department)
    val out = new ByteArrayOutputStream()
    ImageIO.write(image, "PNG", out)
    Ok(out.toByteArray).as("image/png")
  }
}