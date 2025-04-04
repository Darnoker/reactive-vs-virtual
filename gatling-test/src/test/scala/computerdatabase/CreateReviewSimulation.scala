package computerdatabase

import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

class CreateReviewSimulation extends Simulation {

  val productsIdsFeeder = csv("data/productsIds.csv").circular

  val scn = scenario("Create review requests")
    .feed(productsIdsFeeder)
    .exec(ApiEndpoints.createReview)

  setUp(
    scn.inject(
      rampUsers(10000)
        .during(120)
    )
  ).protocols(HttpProtocol.protocol)
}
