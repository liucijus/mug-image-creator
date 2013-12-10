package lt.overdrive.mugs.imagegenerator

import java.awt.image.BufferedImage
import java.awt._
import java.awt.font.TextAttribute
import scala.collection.JavaConversions

object ImageGenerator {
  private val FontFileName: String = "SwedbankSans-Regular.ttf"
  private val FontTracking: Double = -.045
  private val TitleTextFontSize: Float = 97f
  private val DepartmentTextFontSize: Float = 48f
  private val ImageWidth: Int = 615
  private val ImageHeight: Int = 400

  private val TitleTopPosition: Int = 141
  private val DepartmentTopPosition: Int = 266
  private val LogoTopPosition: Int = 280
  
  def generate(logo: BufferedImage, text: String, department: String): BufferedImage = {
    val painter = new TextPainter(logo)
    painter.createGraphics()
    painter.render(text, department)
    painter.disposeGraphics()
    painter.getImage
  }

  private class TextPainter(logoImage: BufferedImage) {
    private val buffer: BufferedImage = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_4BYTE_ABGR)
    private var graphics2d: Graphics2D = null

    def createGraphics() = {
      graphics2d = buffer.createGraphics()
      graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    }

    def disposeGraphics() = graphics2d.dispose()

    private def renderTemplate() = graphics2d.drawImage(logoImage, widthFromLeft(logoImage.getWidth), LogoTopPosition, null)

    private def widthFromLeft(width: Int): Int = (ImageWidth - width) / 2

    private def createFont(size: Float): Font = {
      val stream = getClass.getClassLoader.getResourceAsStream(FontFileName)
      val font = Font.createFont(Font.TRUETYPE_FONT, stream)
      val attributes = Map(TextAttribute.TRACKING -> FontTracking)
      font.deriveFont(Font.BOLD, size).deriveFont(JavaConversions.mapAsJavaMap(attributes))
    }

    private def renderTitleText(text: String) = renderText(TitleTextFontSize, text, TitleTopPosition)

    private def renderDepartmentText(department: String) = renderText(DepartmentTextFontSize, department, DepartmentTopPosition)

    private def renderText(size: Float, department: String, topPosition: Int) {
      graphics2d.setPaint(Color.black)
      graphics2d.setFont(createFont(size))
      val fm = graphics2d.getFontMetrics
      graphics2d.drawString(department, widthFromLeft(fm.stringWidth(department)), topPosition)
    }

    def render(text: String, department: String) = {
      renderTemplate()
      renderTitleText(text)
      renderDepartmentText(department)
    }

    def getImage = buffer
  }
}


