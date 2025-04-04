package computerdatabase
import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

class CreateOrderSimulation extends Simulation {

  val userIds = csv("data/userIds.csv").circular

  val productsIdsFeeder = csv("data/productsIds.csv").circular

  val scn = scenario("Create order with multiple productIds")
    .feed(userIds)
    .exec(session => session.set("repeatCount", 200))
    .exec(session => session.set("productIds", Seq.empty[String]))
    .repeat(session => session("repeatCount").as[Int], "counter") {
      feed(productsIdsFeeder)
        .exec(session => {
          val currentList = session("productIds").as[Seq[String]]
          val newProductId = session("productId").as[String]
          session.set("productIds", currentList :+ newProductId)
        })
    }
    .exec(ApiEndpoints.createReview)

  setUp(
    scn.inject(
      rampUsers(20000)
        .during(180)
    )
  ).protocols(HttpProtocol.protocol)

}
