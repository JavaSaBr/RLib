package javasabr.rlib.network;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.Arrays;
import javasabr.rlib.network.BaseNetworkTest.MockConnection;
import javasabr.rlib.network.packet.impl.AbstractReadableNetworkPacket;
import javasabr.rlib.network.packet.impl.AbstractWritableNetworkPacket;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NetworkPacketTest {

  @Builder
  @ToString
  @EqualsAndHashCode(exclude = {"byteArrayField", "bytesField1", "bytesField2"})
  private static class TestReadablePacket
      extends AbstractReadableNetworkPacket<MockConnection> {

    private int byteField;
    private int byteUnsignedField;
    private int shortField;
    private int shortUnsignedField;
    private int intField;
    private long intUnsignedField;
    private long longField;
    private float floatField;
    private double doubleField;
    private byte[] byteArrayField;
    private byte[] bytesField1;
    private byte[] bytesField2;
    private String stringField;

    @Override
    protected void readImpl(MockConnection connection, ByteBuffer buffer) {
      super.readImpl(connection, buffer);
      byteField = readByte(buffer);
      byteUnsignedField = readByteUnsigned(buffer);
      shortField = readShort(buffer);
      shortUnsignedField = readShortUnsigned(buffer);
      intField = readInt(buffer);
      intUnsignedField = readIntUnsigned(buffer);
      longField = readLong(buffer);
      floatField = readFloat(buffer);
      doubleField = readDouble(buffer);
      byteArrayField = readByteArray(buffer);
      int bytesCount = readInt(buffer);
      bytesField1 = new byte[bytesCount];
      readBytes(buffer, bytesField1);
      bytesCount = readInt(buffer);
      bytesField2 = new byte[bytesCount];
      readBytes(buffer, bytesField2, 0, bytesCount);
      stringField = readString(buffer, Integer.MAX_VALUE);
    }
  }

  @Builder
  private static class TestWritablePacket
      extends AbstractWritableNetworkPacket<MockConnection> {

    private int byteField;
    private int byteUnsignedField;
    private int shortField;
    private int shortUnsignedField;
    private int intField;
    private long intUnsignedField;
    private long longField;
    private float floatField;
    private double doubleField;
    private byte[] byteArrayField;
    private byte[] bytesField1;
    private byte[] bytesField2;
    private String stringField;

    @Override
    protected void writeImpl(MockConnection connection, ByteBuffer buffer) {
      super.writeImpl(connection, buffer);
      writeByte(buffer, byteField);
      writeByte(buffer, byteUnsignedField);
      writeShort(buffer, shortField);
      writeShort(buffer, shortUnsignedField);
      writeInt(buffer, intField);
      writeInt(buffer, (int) intUnsignedField);
      writeLong(buffer, longField);
      writeFloat(buffer, floatField);
      writeDouble(buffer, doubleField);
      writeByteArray(buffer, byteArrayField);
      writeInt(buffer, bytesField1.length);
      writeBytes(buffer, bytesField1);
      writeInt(buffer, bytesField2.length);
      writeBytes(buffer, bytesField2, 0, bytesField2.length);
      writeString(buffer, stringField);
    }
  }

  @Test
  @DisplayName("should write packet correctly")
  void shouldWritePacketCorrectly() {

    // given:
    TestWritablePacket packet = TestWritablePacket
        .builder()
        .byteField(100)
        .byteUnsignedField(200)
        .shortField(25600)
        .shortUnsignedField(44000)
        .intField(500_400)
        .intUnsignedField(3_345_123_436L)
        .longField(4_564_869_376L)
        .floatField(1.55F)
        .doubleField(5.23)
        .byteArrayField(new byte[] {4, 6, 9})
        .bytesField1(new byte[] {9, 1, 5, 8, 9})
        .bytesField2(new byte[] {3, 1, 1, 8, 9, 9, 5})
        .stringField("test string")
        .build();

    var buffer = ByteBuffer.allocate(256);
    byte[] expected = {100, -56, 100, 0, -85, -32, 0, 7, -94, -80, -57, 98, -120, 108,
                       0, 0, 0, 1, 16, 22, 97, 0, 63, -58, 102, 102, 64, 20, -21, -123,
                       30, -72, 81, -20, 0, 0, 0, 3, 4, 6, 9, 0, 0, 0, 5, 9, 1, 5,
                       8, 9, 0, 0, 0, 7, 3, 1, 1, 8, 9, 9, 5, 0, 0, 0, 11, 0, 116,
                       0, 101, 0, 115, 0, 116, 0, 32, 0, 115, 0, 116, 0, 114, 0,
                       105, 0, 110, 0, 103};

    // when:
    packet.write(BaseNetworkTest.MOCK_CONNECTION, buffer);
    buffer.flip();

    // then:
    byte[] wroteBytes = Arrays.copyOf(buffer.array(), buffer.limit());
    assertThat(wroteBytes).isEqualTo(expected);
  }

  @Test
  @DisplayName("should read packet correctly")
  void shouldReadPacketCorrectly() {

    // given:
    byte[] data = {100, -56, 100, 0, -85, -32, 0, 7, -94, -80, -57, 98, -120, 108,
                   0, 0, 0, 1, 16, 22, 97, 0, 63, -58, 102, 102, 64, 20, -21, -123,
                   30, -72, 81, -20, 0, 0, 0, 3, 4, 6, 9, 0, 0, 0, 5, 9, 1, 5,
                   8, 9, 0, 0, 0, 7, 3, 1, 1, 8, 9, 9, 5, 0, 0, 0, 11, 0, 116,
                   0, 101, 0, 115, 0, 116, 0, 32, 0, 115, 0, 116, 0, 114, 0,
                   105, 0, 110, 0, 103};

    TestReadablePacket packet = TestReadablePacket.builder().build();

    TestReadablePacket expected = TestReadablePacket
        .builder()
        .byteField(100)
        .byteUnsignedField(200)
        .shortField(25600)
        .shortUnsignedField(44000)
        .intField(500_400)
        .intUnsignedField(3_345_123_436L)
        .longField(4_564_869_376L)
        .floatField(1.55F)
        .doubleField(5.23)
        .byteArrayField(new byte[] {4, 6, 9})
        .bytesField1(new byte[] {9, 1, 5, 8, 9})
        .bytesField2(new byte[] {3, 1, 1, 8, 9, 9, 5})
        .stringField("test string")
        .build();

    // when:
    packet.read(BaseNetworkTest.MOCK_CONNECTION, ByteBuffer.wrap(data), data.length);

    // then:
    assertThat(packet).isEqualTo(expected);
    assertThat(packet.byteArrayField).isEqualTo(expected.byteArrayField);
    assertThat(packet.bytesField1).isEqualTo(expected.bytesField1);
    assertThat(packet.bytesField2).isEqualTo(expected.bytesField2);
  }
}
