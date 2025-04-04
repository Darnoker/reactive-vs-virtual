package computerdatabase.common

import scala.util.Random

object ApiUtils {

  def generateCreateReviewBody(productId: String): String = {
    val reviewer = "Reviewer" + scala.util.Random.nextInt(1000)
    val rating = Random.nextInt(5) + 1
    val comment = "comment " + scala.util.Random.alphanumeric.take(15).mkString

    s"""{"productId": "$productId", "reviewer": "$reviewer", "rating": $rating, "comment": "$comment"}"""
  }

  def generateCreateOrderBody(userId: String, productIds: Seq[String]): String = {
    println(s"LENGTH OF PRODUCTS LIST: ${productIds.length}")
    val productsJson = productIds.map { pid =>
      val quantity = Random.nextInt(5) + 1
      val detailColor = Random.alphanumeric.take(1000).mkString
      val detailWarranty = Random.alphanumeric.take(1000).mkString

      s"""{"productId": "$pid", "quantity": $quantity, "details": {"color" : "$detailColor", "warranty" : "$detailWarranty"}}"""
    }.mkString(", ")

    // Losowo ustalamy pozostałe dane zamówienia
    val total = BigDecimal(Random.nextDouble() * 100).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    val street = "Street " + (Random.nextInt(100) + 1)
    val city = "City" + (Random.nextInt(50) + 1)
    val country = "Country" + (Random.nextInt(10) + 1)
    val zipCode = (Random.nextInt(90000) + 10000).toString
    val shippingMethod = if (Random.nextBoolean()) "Standard" else "Express"
    val paymentMethod = if (Random.nextBoolean()) "CreditCard" else "PayPal"

    // Budujemy finalny JSON
    s"""{
       |  "userId": "$userId",
       |  "products": [ $productsJson ],
       |  "total": $total,
       |  "street": "$street",
       |  "city": "$city",
       |  "country": "$country",
       |  "zipCode": "$zipCode",
       |  "shippingMethod": "$shippingMethod",
       |  "paymentMethod": "$paymentMethod"
       |}""".stripMargin
  }
}
