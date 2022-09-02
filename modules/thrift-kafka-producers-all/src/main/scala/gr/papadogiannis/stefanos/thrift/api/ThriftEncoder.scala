package gr.papadogiannis.stefanos.thrift.api

import com.twitter.scrooge.ThriftStruct
import org.apache.thrift.protocol.{TCompactProtocol, TProtocol}
import org.apache.thrift.transport.TTransport

trait ThriftEncoder {

  type Output
  type Transport <: TTransport
  type Protocol <: TProtocol

  def createProtocol[T <: ThriftStruct](obj: T, transport: Transport): Protocol

  def getOutput(transport: Transport): Output

  def createTransport[T <: ThriftStruct](obj: T): Transport

}

trait DefaultThriftEncoder extends ThriftEncoder {

  type Protocol = TProtocol

  def createProtocol[T <: ThriftStruct](obj: T, transport: Transport): Protocol =
    (new TCompactProtocol.Factory).getProtocol(transport)

  def apply[T <: ThriftStruct](obj: T): Output = {
    val transport = createTransport(obj)
    val protocol = createProtocol(obj, transport)
    obj.write(protocol)
    getOutput(transport)
  }

}
