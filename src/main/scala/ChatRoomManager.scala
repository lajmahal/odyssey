import ChatRoomManager.{ParticipantRegistered, RequestTrackParticipant}
import akka.actor.{Actor, ActorLogging}

object ChatRoomManager {
  final case class RequestTrackParticipant(paticipantId: String)
  case object ParticipantRegistered
}

class ChatRoomManager extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("ChatRoomManager started")
  override def postStop(): Unit = log.info("ChatRoomManager stopped")

  override def receive: Receive = {
    case RequestTrackParticipant(_) => sender() ! ParticipantRegistered
  }
}
