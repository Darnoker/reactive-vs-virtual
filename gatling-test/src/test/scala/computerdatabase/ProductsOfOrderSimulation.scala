package computerdatabase

import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

class ProductsOfOrderSimulation extends Simulation {

  val orderFeeder = csv("data/orderIds.csv").circular

  val scn = scenario("Get products for order - GET /orders/products/{orderId}")
    .feed(orderFeeder).exec(ApiEndpoints.productsForOrder)

  setUp(
    scn.inject(
      rampUsers(50000)
        .during(360)
    )
  ).protocols(HttpProtocol.protocol)
}
