package br.com.ig.shion

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.RequestBuilder
import jodd.util.RandomString
import org.slf4j.LoggerFactory

import scala.util.Random

class ShionSimulation extends Simulation {
  val logger = LoggerFactory.getLogger(classOf[Nothing])

  val httpProtocol = http
    .baseURL("http://localhost:8080")
 //   .acceptHeader("application/hal+json")

  val feeder = Iterator.continually(Map("newIsbn" -> (newIsbn()), "newTitle" -> (newTitle())))

  val scn = scenario("RecordedSimulation").feed(feeder)
 //   .exec(
//      http("get_root")
 //       .get("/planets")
 //       .header("Content-Type", "application/json")
 //       .check(status.is(200))
 //   )
  
    .exec(
    http("get_currency_conversion")
      .get("/currencyConversion?from=BRL&to=USD&amount=100")
      .header("Content-Type", "application/json")
      .check(status.is(200))
  )


//    .exec(
//      http("create_book")
//        .post("/planet")
//        .notSilent
//        .header("Content-Type", "application/json")
//        .body(StringBody("{ \"title\": \"${newTitle}\", \"isbn\": ${newIsbn} }"))
//        .check(status.is(201))
//    )

//    .exec(http("get_books_paginated")
//      .get("/planet?name=terra")
//      .check(status.is(200))
//    )

  // setUp(scn.inject(atOnceUsers(100))).protocols(httpProtocol)
  setUp(scn.inject(constantUsersPerSec(1000)during (30))).protocols(httpProtocol)

  //

  def newIsbn() = Random.nextInt(Integer.MAX_VALUE)

  def newTitle() = RandomString.getInstance().randomAlpha(10)

}
