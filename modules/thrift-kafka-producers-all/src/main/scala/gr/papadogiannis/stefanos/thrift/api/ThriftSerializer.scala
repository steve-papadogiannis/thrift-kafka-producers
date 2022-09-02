package gr.papadogiannis.stefanos.thrift.api

import com.twitter.scrooge.{ThriftStruct, ThriftStructCodec}
import org.apache.kafka.common.serialization.Serializer

import java.util

final class ThriftSerializer[V <: ThriftStruct](codec: ThriftStructCodec[V]) extends Serializer[V] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = { }

  override def close(): Unit = { }

  def serialize(topic: String, data: V): Array[Byte] = ThriftEncoders.ByteArray(data)
}