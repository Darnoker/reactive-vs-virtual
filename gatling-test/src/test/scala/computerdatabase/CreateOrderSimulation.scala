package computerdatabase
import computerdatabase.common.{ApiEndpoints, HttpProtocol}
import io.gatling.core.Predef._

import scala.util.Random

class CreateOrderSimulation extends Simulation {

  val userIds = csv("data/userIds.csv").circular

  val productsIdsFeeder = csv("data/productsIds.csv").circular

  val scn = scenario("Create order - POST /order/manage/add")
    .feed(userIds)
    .exec(session => session.set("repeatCount", 50))
    .exec(session => session.set("productIds", Seq.empty[String]))
    .repeat(session => session("repeatCount").as[Int], "counter") {
      feed(productsIdsFeeder)
        .exec(session => {
          val currentProductIds = session("productIds").as[Seq[String]]
          val newProductId = session("productId").as[String]
          session.set("productIds", currentProductIds :+ newProductId)
        })
    }
    .exec(ApiEndpoints.createOrder)

  setUp(
    scn.inject(
      rampUsers(15000)
          .during(180)
    )
  ).protocols(HttpProtocol.protocol)
}
