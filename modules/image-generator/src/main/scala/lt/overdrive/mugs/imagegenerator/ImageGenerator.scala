package lt.overdrive.mugs.imagegenerator

import java.awt.image.BufferedImage
import java.awt._
import java.awt.font.TextAttribute
import scala.collection.JavaConversions
import java.io.InputStream

object ImageGenerator {
  def generate(template: BufferedImage, text: String, department: String): BufferedImage = {
    val painter = new TextPainter(template)
    painter.createGraphics()
    painter.render(text, department)
    painter.disposeGraphics()
    painter.getImage
  }
}

class TextPainter(logoImage: BufferedImage) {
  private val FontFileName: String = "SwedbankSans-Regular.ttf"
  private val FontTracking: Double = -.045
  private val TitleTextFontSize: Float = 97f
  private val DepartmentTextFontSize: Float = 48f
  private val ImageWidth: Int = 615
  private val ImageHeight: Int = 400

  private val buffer: BufferedImage = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_4BYTE_ABGR)
  private var graphics2d: Graphics2D = null

  def createGraphics() = {
    graphics2d = buffer.createGraphics()
    graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
  }

  def disposeGraphics() = graphics2d.dispose()

  private def renderTemplate() = {
    graphics2d.fillRect(0, 0, buffer.getWidth, buffer.getHeight)
    val x = (ImageWidth - logoImage.getWidth) / 2
    graphics2d.drawImage(logoImage, x, 280, null)
  }

  private def createFont(size: Float): Font = {
    val stream: InputStream = getClass.getClassLoader.getResourceAsStream(FontFileName)
    val font: Font = Font.createFont(Font.TRUETYPE_FONT, stream)
    val attributes: Map[TextAttribute, Double] = Map(TextAttribute.TRACKING -> FontTracking)
    font.deriveFont(Font.BOLD, size).deriveFont(JavaConversions.mapAsJavaMap(attributes))
  }

  private def renderTitleText(text: String) = renderText(TitleTextFontSize, text, 141)

  private def renderDepartmentText(department: String) = renderText(DepartmentTextFontSize, department, 266)

  private def renderText(size: Float, department: String, y: Int) {
    graphics2d.setPaint(Color.black)
    graphics2d.setFont(createFont(size))
    val fm: FontMetrics = graphics2d.getFontMetrics
    val x: Int = ImageWidth / 2 - fm.stringWidth(department) / 2
    graphics2d.drawString(department, x, y)
  }

  def render(text: String, department: String) = {
    renderTemplate()
    renderTitleText(text)
    renderDepartmentText(department)
  }

  def getImage = buffer
}
