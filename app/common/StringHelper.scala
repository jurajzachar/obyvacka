package common

import com.twitter.util.Time
import java.util.Date
import net.sf.jfuzzydate.{FuzzyDateFormat, FuzzyDateFormatter}

object StringHelper extends StringHelper with TypeConversions

trait StringHelper {

  private object Slugifier {
    def slugify(text: String) = trimUnderscores(removeNonWord(text))

    def removeNonWord(text: String) = """\W+""".r.replaceAllIn(text, "_")

    def trimUnderscores(text: String) = """^\_|\_$""".r.replaceAllIn(text, "")
  }

  def slugify(text: String): String = Slugifier.slugify(text)

  def formatDate(date: Time) = date format "yyy/MM/dd"

  def getYear(date: Time) = date format "yyy"

  def getMonth(date: Time) = date format "MM"

  def rssDate(date: Time) = date format "E, dd MMM yyyy HH:mm:ss Z"

  def formatDistance(date: Date) = fuzzyFormatter formatDistance date

  private val fuzzyFormatter = FuzzyDateFormat.getInstance

  def pluralize(s: String, n: Int) = "%d %s%s".format(n, s, if (n > 1) "s" else "")
}