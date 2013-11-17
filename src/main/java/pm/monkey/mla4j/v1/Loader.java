/**
 * Copyright (c) 2013 Monkey By Monkey
 * 
 * This file is part of mla4j.
 * mla4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * mla4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with mla4j. If not, see <http://www.gnu.org/licenses/>.
 */

package pm.monkey.mla4j.v1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import pm.monkey.mla4j.AudioFormat;
import pm.monkey.mla4j.Container;
import pm.monkey.mla4j.FormatException;
import pm.monkey.mla4j.VersionLoader;

public class Loader implements VersionLoader {

    @Override
    public Container load(InputStream is) throws FormatException, IOException {

        byte[] headerBytes = new byte[11];

        int bytesRead = is.read(headerBytes);

        if (bytesRead < 11) {
            throw new FormatException("MLA header is too small");
        } else {
            short headerSize = ByteBuffer.wrap(headerBytes, 0, 2).getShort();

            if (headerSize < 16) {
                throw new FormatException("MLA header specified invalid header size");
            }

            AudioFormat audioFormat = audioFormatFromHeaderValue(headerBytes[2]);

            if (audioFormat == null) {
                throw new FormatException("MLA header specified unsupported audio format");
            }

            int loopAreaOffset = ByteBuffer.wrap(headerBytes, 3, 4).getInt();
            int loopAreaSize = ByteBuffer.wrap(headerBytes, 7, 4).getInt();

            int extraDataSize = headerSize - 16;
            byte[] extraData = new byte[extraDataSize];

            bytesRead = Math.max(0, is.read(extraData));

            if (bytesRead < extraDataSize) {
                throw new FormatException("MLA extra data is truncated");
            }

            Header header = new Header();
            header.setSize(headerSize);
            header.setAudioFormat(audioFormat);
            header.setLoopAreaOffset(loopAreaOffset);
            header.setLoopAreaSize(loopAreaSize);
            header.setExtraData(extraData);

            Container container = new pm.monkey.mla4j.v1.Container();
            container.setHeader(header);

            return container;
        }
    }

    @Override
    public int getVersion() {
        return 1;
    }

    private static AudioFormat audioFormatFromHeaderValue(int audioFormat) {
        switch (audioFormat) {
        case 0:
            return AudioFormat.UNSPECIFIED;
        case 1:
            return AudioFormat.OGG_VORBIS;
        case 2:
            return AudioFormat.FLAC;
        case 3:
            return AudioFormat.PCM_S16LE;
        default:
            return null;
        }
    }

}
