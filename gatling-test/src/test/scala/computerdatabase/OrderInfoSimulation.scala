package computerdatabase
import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

import io.gatling.core.Predef.{Simulation, csv, rampUsers, scenario}

class OrderInfoSimulation extends Simulation{

  val orderFeeder = csv("data/orderIds.csv").circular

  val scn = scenario("Order Products Request")
    .feed(orderFeeder)
    .exec(ApiEndpoints.orderInfo)

  setUp(
    scn.inject(
      rampUsers(50000)
        .during(360)
    )
  ).protocols(HttpProtocol.protocol)
}
