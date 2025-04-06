package computerdatabase

import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

class CreateReviewSimulation extends Simulation {

  val productsIdsFeeder = csv("data/productsIds.csv").circular

  val scn = scenario("Create review - POST /products/add-review")
    .feed(productsIdsFeeder)
    .exec(ApiEndpoints.createReview)

  setUp(
    scn.inject(
      rampUsers(25000)
        .during(180)
    )
  ).protocols(HttpProtocol.protocol)
}
