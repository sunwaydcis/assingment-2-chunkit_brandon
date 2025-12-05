object Main:

  def main(args: Array[String]): Unit =
    val file = "data/Hotel_Dataset.csv"
    val bookings = CSVLoader.loadBookings(file)

    println("------ Hotel Booking Analysis ------")
    Q1_CountryMostBookings.answer(bookings)
    Q2_MostEconomicalHotel.answer(bookings)
    Q3_MostProfitableHotel.answer(bookings)
