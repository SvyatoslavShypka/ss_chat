package core.writer;

import core.IThreadControl;

import java.io.ObjectOutputStream;

public interface IWriterThread extends IThreadControl {
    void sendMessage(ObjectOutputStream writer, Object message);
}
