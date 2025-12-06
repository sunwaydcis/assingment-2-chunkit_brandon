package hoteldata

import hoteldata.DataSource.ReportBlueprint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class Q2_MostEconomicalHotel extends ReportBlueprint:

  private val dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy")

  private def nights(start: String, end: String): Int =
    try
      val d1 = LocalDate.parse(start, dateFormat)
      val d2 = LocalDate.parse(end, dateFormat)
      Math.max(1, ChronoUnit.DAYS.between(d1, d2).toInt)
    catch
      case _: Throwable => 1

  override def execute(data: List[Map[String, String]]): Unit =

    // Group hotels
    val hotelGroups = data.foldLeft(Map.empty[(String,String,String), Vector[Map[String,String]]]) {
      case (acc, row) =>
        val key = (
          row.getOrElse("Destination Country","Unknown"),
          row.getOrElse("Destination City","Unknown"),
          row.getOrElse("Hotel Name","Unknown")
        )
        acc.updatedWith(key)(_.map(_ ++ Vector(row)).orElse(Some(Vector(row))))
    }

    if hotelGroups.isEmpty then
      println("No hotel data available.")
      return

    // Compute scores for each hotel
    val scoredHotels = hotelGroups.map { case ((country, city, hotel), rows) =>

      val pricePerRoomPerDay = rows.map { r =>
        val bookingPrice = DataSource.Conversions.toDouble(r.getOrElse("Booking Price[SGD]", "0"))
        val rooms = Math.max(1, DataSource.Conversions.toInt(r.getOrElse("Rooms", "1")))
        val daysBooked = Math.max(1, DataSource.Conversions.toInt(r.getOrElse("No of Days", "1")))
        bookingPrice / (rooms * daysBooked)
      }.sum / rows.size

      val avgDiscount = rows.map(r => DataSource.Conversions.toDouble(r.getOrElse("Discount","0")) / 100.0).sum / rows.size
      val avgProfit   = rows.map(r => DataSource.Conversions.toDouble(r.getOrElse("Profit Margin","0"))).sum / rows.size

      (country, city, hotel, pricePerRoomPerDay, avgDiscount, avgProfit)
    }.toVector

    // Normalize scores
    def normalizeLower(values: Vector[Double]) =
      val minV = values.min
      val maxV = values.max
      values.map(v => if maxV != minV then 1 - ((v - minV)/(maxV - minV)) else 1.0)

    def normalizeHigher(values: Vector[Double]) =
      val minV = values.min
      val maxV = values.max
      values.map(v => if maxV != minV then (v - minV)/(maxV - minV) else 1.0)

    val priceScores    = normalizeLower(scoredHotels.map(_._4))
    val discountScores = normalizeHigher(scoredHotels.map(_._5))
    val profitScores   = normalizeLower(scoredHotels.map(_._6))

    val finalScores = scoredHotels.zip(priceScores.zip(discountScores.zip(profitScores))).map {
      case ((c, ci, h, _, _, _), (pScore, (dScore, prScore))) =>
        val avgScore = (pScore + dScore + prScore)/3
        (c, ci, h, avgScore, pScore, dScore, prScore)
    }

    val best = finalScores.maxBy(_._4)
    val scoreStr = f"${best._4 * 100}%.2f%%"
    
    println("╔════════════════════════════════════════════╗")
    println("║       💸 MOST ECONOMICAL HOTEL             ║")
    println("╠════════════════════════════════════════════╣")
    println(f"║ Hotel   → ${best._3}%-32s ║")
    println(f"║ City    → ${best._2}%-32s ║")
    println(f"║ Country → ${best._1}%-32s ║")
    println(f"║ Score   → $scoreStr%-32s ║")
    println("╚════════════════════════════════════════════╝")
    println()
end Q2_MostEconomicalHotel
