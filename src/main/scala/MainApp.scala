package hoteldata

import hoteldata._

object MainApp:

  def main(args: Array[String]): Unit =

    // Load CSV from resources folder
    val resource = getClass.getResource("/Hotel_Dataset.csv") // notice the "/" at start

    if resource == null then
      println("⚠ File not found in resources! Make sure it's in src/main/resources/")
      return

    val bookings = DataSource.load(resource.getPath)

    if bookings.isEmpty then
      println("No booking data found. Exiting.")
      return

    println(s"Total bookings loaded: ${bookings.size}")

    // Run reports
    val reports: List[DataSource.ReportBlueprint] = List(
      new Q1_HighestBookingCountry,
      new Q2_MostEconomicalHotel,
      new Q3_MostProfitableHotel
    )

    reports.foreach { report =>
      try
        report.execute(bookings)
      catch
        case e: Exception =>
          println(s"⚠ Error running report ${report.getClass.getSimpleName}: ${e.getMessage}")
    }

    println("\nAll reports executed successfully.")
end MainApp
