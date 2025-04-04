package computerdatabase.common

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object HttpProtocol {

  val protocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .header("Connection", "keep-alive")
}
