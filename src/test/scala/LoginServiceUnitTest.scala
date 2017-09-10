import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FreeSpecLike, Matchers}

class LoginServiceUnitTest(_system: ActorSystem) extends TestKit(_system)
  with FreeSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("LoginServiceUnitTest"))

  override def afterAll(): Unit = shutdown(system)

  "A login service" - {
    "should reply to login requests" in {
      val probe = TestProbe()
      val loginServiceActor = system.actorOf(LoginService.props())

      loginServiceActor.tell(LoginService.RequestLogin("user"), probe.ref)
      probe.expectMsg(LoginService.UserLoggedIn("user"))
      probe.lastSender shouldBe loginServiceActor
    }

    "should reply to active user list requests" - {
      "with an empty Set if no one logged in" in {
        val probe = TestProbe()
        val loginServiceActor = system.actorOf(LoginService.props())

        loginServiceActor.tell(LoginService.RequestActiveUserList, probe.ref)
        probe.expectMsg(LoginService.ReplyActiveUserList(Set.empty[String]))
        probe.lastSender shouldBe loginServiceActor
      }

      "with users if some are logged in" in {
        val probe = TestProbe()
        val loginServiceActor = system.actorOf(LoginService.props())

        loginServiceActor.tell(LoginService.RequestLogin("user1"), probe.ref)
        probe.expectMsg(LoginService.UserLoggedIn("user1"))
        probe.lastSender shouldBe loginServiceActor

        loginServiceActor.tell(LoginService.RequestLogin("user2"), probe.ref)
        probe.expectMsg(LoginService.UserLoggedIn("user2"))
        probe.lastSender shouldBe loginServiceActor

        loginServiceActor.tell(LoginService.RequestActiveUserList, probe.ref)
        probe.expectMsg(LoginService.ReplyActiveUserList(Set("user1", "user2")))
        probe.lastSender shouldBe loginServiceActor
      }
    }
  }
}
