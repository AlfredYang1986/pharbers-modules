package com.pharbers.common.alFileHandler.alCsvOpt;

import java.io.Closeable;
import java.io.IOException;

public interface LineReader extends Closeable {

    String readLineWithTerminator() throws IOException;
}

