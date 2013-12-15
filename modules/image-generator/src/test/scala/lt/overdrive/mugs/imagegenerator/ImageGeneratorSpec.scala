package lt.overdrive.mugs.imagegenerator

import org.specs2.mutable.Specification
import javax.imageio.ImageIO
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import java.awt.image.BufferedImage
import org.specs2.matcher.Matcher

@RunWith(classOf[JUnitRunner])
class ImageGeneratorSpec extends Specification {
  "ImageGenerator" should {
    "Generate image with the same pixels" in {
      val (logo, expectedImage) = (loadImage("logo.png"), loadImage("expected.png"))

      val generatedImage = ImageGenerator.generate(logo, "Title", "Department")

      generatedImage must haveSamePixels(expectedImage)
    }
  }

  def loadImage(s: String) = ImageIO.read(getClass.getClassLoader.getResourceAsStream(s))

  def haveSamePixels(expected: BufferedImage): Matcher[BufferedImage] = {
    (arePixelsEqual(_: BufferedImage, expected), "images are different")
  }

  def arePixelsEqual(image1: BufferedImage, image2: BufferedImage) = {
    val size1, (width, height) = (image1.getWidth, image1.getHeight)
    val size2 = (image2.getWidth, image2.getHeight)

    def notEqualPoint: ((Int, Int)) => Boolean = {
      case (w, h) => image1.getRGB(w, h) != image2.getRGB(w, h)
    }

    def points = for {w <- 0 until width; h <- 0 until height} yield (w, h)

    size1 == size2 && !points.exists(notEqualPoint)
  }
}
