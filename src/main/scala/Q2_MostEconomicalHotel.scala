object Q2_MostEconomicalHotel {
  def answer(bookings: List[Booking]): Unit = {
    if bookings.isEmpty then
      println("No valid booking data to analyze!")
    else
      val hotelStats = bookings.groupBy(_.hotel).map { case (hotel, list) =>
        // Step 1: price per room per booking
        val avgPrice = list.map(b => b.price / b.rooms.toDouble)

        // Step 2: normalize if more than one booking
        val avgDiscount = if avgPrice.size > 1 then
          val minPrice = avgPrice.min
          val maxPrice = avgPrice.max
          val range = maxPrice - minPrice
          if range == 0 then avgPrice.map(_ => 0.0) // avoid division by zero
          else avgPrice.map(p => (p - minPrice) / range)
        else
          avgPrice.map(_ => 0.0) // only one booking

        // Step 3: average normalized percentage
        val avgProfitMargin = avgDiscount.sum / avgDiscount.size

        // Step 4: score = average normalized cost
        val score = avgProfitMargin

        hotel -> score
      }

      // Step 5: hotel with lowest normalized cost
      val cheapestHotel = hotelStats.minBy(_._2)

      println("╔════════════════════════════════════╗")
      println("║     💸 MOST ECONOMICAL HOTEL       ║")
      println("╠════════════════════════════════════╣")
      println(f"║ Hotel → ${cheapestHotel._1}")
      println(f"║ Score → ${cheapestHotel._2}%.2f")
      println("╚════════════════════════════════════╝")
      println()


  }
}
