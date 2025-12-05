object Q1_CountryMostBookings:

  def answer(bookings: List[Booking]): Unit =
    if bookings.isEmpty then
      println("No valid booking data to analyze!")
    else
      val topCountry = bookings.groupBy(_.country).maxBy(_._2.size)._1
      println("┌──────────────────────────────────────────────┐")
      println("│            🧭 COUNTRY ANALYSIS               │")
      println("├──────────────────────────────────────────────┤")
      println(s"│  Country with the most bookings → $topCountry")
      println("└──────────────────────────────────────────────┘")
      println()

