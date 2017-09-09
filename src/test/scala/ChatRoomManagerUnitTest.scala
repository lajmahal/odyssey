import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FreeSpecLike, Matchers}

class ChatRoomManagerUnitTest(_system: ActorSystem) extends TestKit(_system)
  with FreeSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("ChatRoomManagerUnitTest"))

  override def afterAll(): Unit = shutdown(system)

  "A chat room manager" - {
    "should reply to registration requests" in {
      val probe = TestProbe()
      val participantActor = system.actorOf(ChatRoomParticipant.props("participant"))

      participantActor.tell(ChatRoomManager.RequestTrackParticipant("participant"), probe.ref)
      probe.expectMsg(ChatRoomManager.ParticipantRegistered)
      probe.lastSender shouldBe participantActor
    }
  }
}
