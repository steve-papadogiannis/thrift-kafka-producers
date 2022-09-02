package gr.papadogiannis.stefanos.thrift.api

import com.twitter.scrooge.{TArrayByteTransport, ThriftStruct}

object ThriftEncoders {

  object ByteArray extends DefaultThriftEncoder {

    type Output = Array[Byte]
    type Transport = TArrayByteTransport

    def createTransport[T <: ThriftStruct](obj: T): Transport =
      new TArrayByteTransport()

    def getOutput(transport: Transport): Output = transport.toByteArray

  }

}
