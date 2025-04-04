package computerdatabase.common

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object ApiEndpoints {

  val orderProducts = http("Order products")
    .get(session => s"/orders/products/${session("orderId").as[String]}")
    .check(status.is(200))

  val searchByLastname = http("User by lastname")
    .get(session => s"/users/lastname/${session("lastname").as[String]}")
    .check(status.is(200))

  val orderInfo = http("Order info")
    .get(session => s"/order/manage/${session("orderId").as[String]}")
    .check(status.is(200))

  val createReview = http("Create Review")
    .post("/products/add-review")
    .body(StringBody(session => {
      val productId = session("productId").as[String]
      ApiUtils.generateCreateReviewBody(productId)
    }))
    .asJson
    .check(status.is(200))

  val createOrder = http("Create order")
    .post("/order/manage/add")
    .body(StringBody(session => {
      val userId = session("userId").as[String]
      val productIds = session("productIds").as[List[String]]
      ApiUtils.generateCreateOrderBody(userId, productIds)
    }))
}
