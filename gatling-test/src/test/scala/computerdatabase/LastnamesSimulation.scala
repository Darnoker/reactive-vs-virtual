package computerdatabase

import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

class LastnamesSimulation extends Simulation {

  val lastnamesFeeder = csv("data/lastnames.csv").circular

  val scn = scenario("Users lastnames requests")
    .feed(lastnamesFeeder)
    .exec(ApiEndpoints.searchByLastname)

  setUp(
    scn.inject(
      rampUsers(1000)
        .during(180)
    )
  ).protocols(HttpProtocol.protocol)
}
