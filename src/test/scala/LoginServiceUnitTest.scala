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
  }
}