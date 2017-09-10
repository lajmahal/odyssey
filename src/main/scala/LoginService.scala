import LoginService.{ReplyActiveUserList, RequestActiveUserList, RequestLogin, UserLoggedIn}
import akka.actor.{Actor, ActorLogging, Props}

object LoginService {
  def props(): Props = Props(new LoginService)

  final case class RequestLogin(userId: String)
  final case class UserLoggedIn(userId: String)

  case object RequestActiveUserList
  final case class ReplyActiveUserList(users: Set[String])
}

class LoginService extends Actor with ActorLogging {
  private var loggedInUsers: Set[String] = Set.empty[String]

  override def preStart(): Unit = log.info("LoginService started")
  override def postStop(): Unit = log.info("LoginService stopped")

  override def receive: Receive = {
    case RequestLogin(userId) =>
      loggedInUsers += userId
      sender() ! UserLoggedIn(userId)

    case RequestActiveUserList => sender() ! ReplyActiveUserList(loggedInUsers)
  }
}
