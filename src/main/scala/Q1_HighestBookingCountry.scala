package hoteldata

import hoteldata.DataSource.ReportBlueprint

class Q1_HighestBookingCountry extends ReportBlueprint:

  override def execute(data: List[Map[String, String]]): Unit =

    // Collect all countries from the data
    val countries = data.flatMap(_.get("Destination Country"))

    if countries.isEmpty then
      println("No country data available.")
      return

    // Count occurrences of each country
    val countryCounts = countries.groupMapReduce(identity)(_ => 1)(_ + _)

    // Find the country with the most bookings
    val (topCountry, topBookings) = countryCounts.maxBy(_._2)
    
    println("┌──────────────────────────────────────────────┐")
    println("│            🧭 COUNTRY ANALYSIS               │")
    println("├──────────────────────────────────────────────┤")
    println(f"│  Country with the most bookings → ${topCountry}%-1s │")
    println(f"│  Total Bookings                     → ${topBookings}%6d │")
    println("└──────────────────────────────────────────────┘")
    println()
end Q1_HighestBookingCountry
