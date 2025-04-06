package computerdatabase

import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

class LastnamesSimulation extends Simulation {

  val lastnamesFeeder = csv("data/lastnames.csv").circular

  val scn = scenario("Get users by lastname - GET users/lastname/{lastname}")
    .feed(lastnamesFeeder)
    .exec(ApiEndpoints.searchByLastname)

  setUp(
    scn.inject(
      rampUsers(2500)
        .during(180)
    )
  ).protocols(HttpProtocol.protocol)
}
