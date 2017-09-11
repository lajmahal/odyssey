package odyssey

import akka.actor.{Actor, ActorLogging, Props}
import odyssey.LoginService._

object LoginService {
  def props(): Props = Props(new LoginService)

  final case class RequestLogin(userId: String)
  final case class UserLoggedIn(userId: String)

  final case class RequestLogout(userId: String)
  final case class UserLoggedOut(userId: String)

  case object RequestActiveUserList
  final case class ReplyActiveUserList(users: Set[String])
}

class LoginService extends Actor with ActorLogging {
  private var loggedInUsers: Set[String] = Set.empty[String]

  override def preStart(): Unit = log.info("odyssey.LoginService started")
  override def postStop(): Unit = log.info("odyssey.LoginService stopped")

  override def receive: Receive = {
    case RequestLogin(userId) =>
      loggedInUsers += userId
      sender() ! UserLoggedIn(userId)

    case RequestLogout(userId) =>
      if (loggedInUsers.contains(userId)) {
        loggedInUsers -= userId
        sender() ! UserLoggedOut(userId)
      } else {
        log.warning(s"Cannot log out $userId - they were never logged in!")
      }

    case RequestActiveUserList => sender() ! ReplyActiveUserList(loggedInUsers)
  }
}
