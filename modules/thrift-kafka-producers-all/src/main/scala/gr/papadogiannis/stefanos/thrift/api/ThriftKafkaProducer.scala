package gr.papadogiannis.stefanos.thrift.api

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import com.twitter.scrooge.{ThriftStruct, ThriftStructCodec}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.Future
import scala.util.{Failure, Success}

abstract class ThriftKafkaProducer[T <: ThriftStruct](codec: ThriftStructCodec[T], topic: String, bootstrapServers: String = "localhost:9092")
    extends App {

  implicit val system: ActorSystem = ActorSystem("ThriftKafkaConsumerActorSystem")

  val config = system.settings.config.getConfig("akka.kafka.producer")

  val producerSettings =
    ProducerSettings(config, new StringSerializer, new ThriftSerializer(codec))
      .withBootstrapServers(bootstrapServers)

  def produceMsg(): T

  val control: Future[Done] =
    Source(1 to 1)
      .map { _ =>
        new ProducerRecord[String, T](topic, produceMsg())
      }
      .runWith(Producer.plainSink(producerSettings))

  system.registerOnTermination {
    import system.dispatcher
    control.onComplete {
      case Success(value) => println(value)
      case Failure(exception) => println(exception)
    }
  }

}
