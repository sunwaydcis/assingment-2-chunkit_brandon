package hoteldata

import hoteldata.DataSource.ReportBlueprint

class Q3_MostProfitableHotel extends ReportBlueprint:

  override def execute(data: List[Map[String, String]]): Unit =

    // Aggregate
    val hotelStats = data.foldLeft(Map.empty[(String,String,String),(Int,Double,Int)]) { (acc,row) =>
      val key = (
        row.getOrElse("Destination Country","Unknown"),
        row.getOrElse("Destination City","Unknown"),
        row.getOrElse("Hotel Name","Unknown")
      )

      val visitors = DataSource.Conversions.toInt(row.getOrElse("No. Of People","1"))
      val profit   = DataSource.Conversions.toDouble(row.getOrElse("Profit Margin","0"))

      val prev = acc.getOrElse(key,(0,0.0,0))
      acc.updated(key, (prev._1 + visitors, prev._2 + profit, prev._3 + 1))
    }

    if hotelStats.isEmpty then
      println("No hotel data available.")
      return

    // Compute average profit per booking
    val hotelScores = hotelStats.map { case ((c,ci,h),(visitors,totalProfit,count)) =>
      val avgProfit = if count > 0 then totalProfit / count else 0.0
      (c, ci, h, visitors, avgProfit)
    }

    // Normalize scores
    val visitorsRange = hotelScores.map(_._4)
    val profitRange   = hotelScores.map(_._5)

    val minV = visitorsRange.min
    val maxV = visitorsRange.max
    val minP = profitRange.min
    val maxP = profitRange.max

    val finalScores = hotelScores.map { case (c,ci,h,v,p) =>
      val visitorScore = if maxV != minV then (v - minV).toDouble / (maxV - minV) else 1.0
      val profitScore  = if maxP != minP then (p - minP)/(maxP - minP) else 1.0
      val finalScore   = (visitorScore + profitScore)/2
      (c, ci, h, finalScore)
    }

    val best = finalScores.maxBy(_._4)

    // Prepare score string
    val scoreStr = f"${best._4 * 100}%.2f%%"

    println("╔════════════════════════════════════╗")
    println("║     💼 MOST PROFITABLE HOTEL       ║")
    println("╠════════════════════════════════════╣")
    println(f"║ Hotel   → ${best._3}%-24s ║")
    println(f"║ City    → ${best._2}%-24s ║")
    println(f"║ Country → ${best._1}%-24s ║")
    println(f"║ Score   → $scoreStr%-24s ║")
    println("╚════════════════════════════════════╝")
    println()
end Q3_MostProfitableHotel
