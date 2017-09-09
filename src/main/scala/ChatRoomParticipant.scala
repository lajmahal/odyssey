import akka.actor.{Actor, ActorLogging, Props}

object ChatRoomParticipant {
  def props(participantId: String): Props = Props(new ChatRoomParticipant(participantId))
}

class ChatRoomParticipant(participantId: String) extends Actor with ActorLogging {

  override def preStart(): Unit = log.info(s"Participant actor $participantId started")
  override def postStop(): Unit = log.info(s"Participant actor $participantId stopped")

  override def receive: Receive = {
    case ChatRoomManager.RequestTrackParticipant(`participantId`) =>
      sender() ! ChatRoomManager.ParticipantRegistered
  }
}
