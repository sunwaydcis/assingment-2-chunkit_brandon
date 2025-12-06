package hoteldata

import com.github.tototoshi.csv.*
import java.io.File


// Central object for loading CSV hotel data.
object DataSource:


  // Reads the CSV file and returns all rows as maps.
  def load(path: String): List[Map[String, String]] =
    val file = new File(path)

    if !file.exists() then
      println(s"⚠ File not found: $path")
      return Nil

    val reader = CSVReader.open(file)

    // Use allWithHeaders() to convert each row into (header -> value)
    val rows =
      try reader.allWithHeaders()
      catch case e: Throwable =>
        println(s"⚠ Error reading CSV: ${e.getMessage}")
        Nil
      finally
        reader.close()

    rows
  end load

  // Safe numeric conversions for later analyses.
  object Conversions:
    def toDouble(v: String): Double =
      val cleaned = v.trim.replace("%", "")
      try cleaned.toDouble
      catch case _ => 0.0

    def toInt(v: String): Int =
      try v.trim.toInt
      catch case _ => 0
  end Conversions

  trait ReportBlueprint:
    def execute(data: List[Map[String, String]]): Unit
  end ReportBlueprint

end DataSource
