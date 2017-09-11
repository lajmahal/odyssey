import LoginService._
import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FreeSpecLike, Matchers}

import scala.concurrent.duration.FiniteDuration

class LoginServiceUnitTest(_system: ActorSystem) extends TestKit(_system)
  with FreeSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("LoginServiceUnitTest"))

  override def afterAll(): Unit = shutdown(system)

  val probe = TestProbe()

  "A login service" - {
    "should reply to login requests" in {
      val loginServiceActor = system.actorOf(LoginService.props())

      loginServiceActor.tell(RequestLogin("user"), probe.ref)
      probe.expectMsg(UserLoggedIn("user"))
      probe.lastSender shouldBe loginServiceActor
    }

    "when receiving a logout request" - {
      "should respond successfully if the user was logged in" in {
        val loginServiceActor = system.actorOf(LoginService.props())

        loginServiceActor.tell(RequestLogin("user1"), probe.ref)
        probe.expectMsg(UserLoggedIn("user1"))

        loginServiceActor.tell(RequestLogout("user1"), probe.ref)
        probe.expectMsg(UserLoggedOut("user1"))
        probe.lastSender shouldBe loginServiceActor
      }

      "should not respond if the user was not logged in" in {
        val loginServiceActor = system.actorOf(LoginService.props())

        loginServiceActor.tell(RequestLogout("not-logged-in"), probe.ref)
        probe.expectNoMsg(FiniteDuration(500, "millis"))
      }
    }

    "should reply to active user list requests" - {
      "with an empty Set if no one logged in" in {
        val loginServiceActor = system.actorOf(LoginService.props())

        loginServiceActor.tell(RequestActiveUserList, probe.ref)
        probe.expectMsg(ReplyActiveUserList(Set.empty[String]))
        probe.lastSender shouldBe loginServiceActor
      }

      "with users if some are logged in" in {
        val loginServiceActor = system.actorOf(LoginService.props())

        loginServiceActor.tell(RequestLogin("user1"), probe.ref)
        probe.expectMsg(UserLoggedIn("user1"))
        probe.lastSender shouldBe loginServiceActor

        loginServiceActor.tell(RequestLogin("user2"), probe.ref)
        probe.expectMsg(UserLoggedIn("user2"))
        probe.lastSender shouldBe loginServiceActor

        loginServiceActor.tell(RequestActiveUserList, probe.ref)
        probe.expectMsg(ReplyActiveUserList(Set("user1", "user2")))
        probe.lastSender shouldBe loginServiceActor
      }
    }
  }
}
