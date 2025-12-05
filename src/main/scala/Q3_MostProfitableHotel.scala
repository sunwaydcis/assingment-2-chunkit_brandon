object Q3_MostProfitableHotel:

  def answer(bookings: List[Booking]): Unit =
    val hotelProfit =
      bookings
        .groupBy(_.hotel)
        .view
        .mapValues(list =>
          list.map(_.profitMargin).sum
        )
        .toMap

    val topProfit = hotelProfit.maxBy(_._2)

    println("╔════════════════════════════════════╗")
    println("║     💼 MOST PROFITABLE HOTEL       ║")
    println("╠════════════════════════════════════╣")
    println(s"║ Hotel → ${topProfit._1}")
    println(f"║ Profit → ${topProfit._2}%.2f")
    println("╚════════════════════════════════════╝")
    println()



