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

class TextPainter(template: BufferedImage, buffer: BufferedImage) {
  private var graphics2d: Graphics2D = null

  def this(template: BufferedImage) = {
    this(template, new BufferedImage(template.getWidth + 100, template.getHeight, BufferedImage.TYPE_4BYTE_ABGR))
  }

  def createGraphics() = {
    graphics2d = buffer.createGraphics()
    graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
  }

  def disposeGraphics() = graphics2d.dispose()

  private def renderTemplate() = {
    graphics2d.fillRect(0, 0, buffer.getWidth, buffer.getHeight)
    graphics2d.drawImage(template, 50, 0, null)
  }

  private def clearOldText() = {
    graphics2d.setColor(Color.white)
    graphics2d.fillRect(0, 0, buffer.getWidth, 280)
  }

  private def createFont(size: Float): Font = {
    val stream: InputStream = getClass.getClassLoader.getResourceAsStream("SwedbankSans-Regular.ttf")
    val font: Font = Font.createFont(Font.TRUETYPE_FONT, stream)
    val attributes: Map[TextAttribute, Double] = Map(TextAttribute.TRACKING -> -.045)
    font.deriveFont(Font.BOLD, size).deriveFont(JavaConversions.mapAsJavaMap(attributes))
  }

  private def renderText(text: String) = {
    graphics2d.setPaint(Color.black)
    graphics2d.setFont(createFont(97f))
    val fm: FontMetrics = graphics2d.getFontMetrics
    val x: Int = buffer.getWidth / 2 - fm.stringWidth(text) / 2
    val y: Int = 141
    graphics2d.drawString(text, x, y)
  }

  def renderDepartment(department: String) = {
    graphics2d.setPaint(Color.black)
    graphics2d.setFont(createFont(48f))
    val fm: FontMetrics = graphics2d.getFontMetrics
    val x: Int = buffer.getWidth / 2 - fm.stringWidth(department) / 2
    val y: Int = 266
    graphics2d.drawString(department, x, y)
  }

  def render(text: String, department: String) = {
    renderTemplate()
    clearOldText()
    renderText(text)
    renderDepartment(department)
  }

  def getImage = buffer
}
