import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets

case class Booking(
                    country: String,
                    hotel: String,
                    price: Double,
                    discount: Double,
                    profitMargin: Double,
                    rooms: Int
                  )

object CSVLoader:

  def loadBookings(path: String): List[Booking] =
    val bytes = Files.readAllBytes(Paths.get(path))
    val content = new String(bytes, StandardCharsets.UTF_8).replaceAll("\uFEFF", "")

    val lines = content.split("\r?\n").toList

    if lines.isEmpty then
      println("CSV file is empty!")
      return List()

    lines.drop(1).flatMap { line =>
      val cols = line.split(",").map(_.trim)

      if cols.length < 24 then
        println(s"Skipping invalid row (too few columns): $line")
        None
      else
        try
          Some(
            Booking(
              country = cols(6),
              hotel = cols(16),
              price = cols(20).replace("%", "").toDouble,
              discount = cols(21).replace("%", "").toDouble / 100,
              profitMargin = cols(23).toDouble,
              rooms = cols(15).toInt
            )
          )
        catch
          case e: Throwable =>
            println(s"Skipping invalid row (parse error): $line -> ${e.getMessage}")
            None
    }
