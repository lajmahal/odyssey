import scala.concurrent.duration.FiniteDuration

package object odyssey {
  implicit class FiniteDurationPimp(i: Int) {
    def millis = FiniteDuration(i, "millis")
  }
}
