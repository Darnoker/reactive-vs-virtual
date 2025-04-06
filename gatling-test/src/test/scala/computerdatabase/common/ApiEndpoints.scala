package computerdatabase.common

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object ApiEndpoints {

  val productsForOrder = http("Get products for order - GET /orders/products/{orderId}")
    .get(session => s"/orders/products/${session("orderId").as[String]}")
    .check(status.is(200))

  val searchByLastname = http("Get users by lastname - GET users/lastname/{lastname}")
    .get(session => s"/users/lastname/${session("lastname").as[String]}")
    .check(status.is(200))

  val orderInfo = http("Get order information about user and products - GET order/manage/{orderId}")
    .get(session => s"/order/manage/${session("orderId").as[String]}")
    .check(status.is(200))

  val createReview = http("Create review - POST /products/add-review")
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
