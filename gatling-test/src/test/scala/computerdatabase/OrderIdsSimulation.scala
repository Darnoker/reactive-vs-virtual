package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.concurrent.ThreadLocalRandom

class OrderIdsSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .header("Connection", "keep-alive")

  val orderFeeder = csv("orderIds.csv").circular

  val scn = scenario("Order Products Request")
    .feed(orderFeeder)
    .exec(
      http("Order products")
        .get("/orders/products/{orderId}")
        .check(status.is(200))
    )

  setUp(
    scn.inject(
      rampUsers(1000)
        .during(15)
    )
  ).protocols(httpProtocol)
}
