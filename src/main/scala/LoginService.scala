import LoginService.{RequestLogin, UserLoggedIn}
import akka.actor.{Actor, ActorLogging, Props}

object LoginService {
  def props(): Props = Props(new LoginService)

  final case class RequestLogin(userId: String)
  final case class UserLoggedIn(userId: String)
}

class LoginService extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("LoginService started")
  override def postStop(): Unit = log.info("LoginService stopped")

  override def receive: Receive = {
    case RequestLogin(userId) => sender() ! UserLoggedIn(userId)
  }
}
