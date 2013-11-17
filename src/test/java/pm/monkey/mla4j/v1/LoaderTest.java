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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pm.monkey.mla4j.Container;
import pm.monkey.mla4j.FormatException;
import pm.monkey.mla4j.Header;

@RunWith(JUnit4.class)
public class LoaderTest {

    private static Loader loader;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() {
        loader = new Loader();
    }

    @Test
    public void testLoadWithTooSmallHeader() throws FormatException, IOException {
        byte[] data = new byte[] {
                0x00, 0x10,
                0x00
        };

        InputStream is = new ByteArrayInputStream(data);

        exception.expect(FormatException.class);
        exception.expectMessage("MLA header is too small");

        loader.load(is);
    }

    @Test
    public void testLoadWithInvalidHeaderSize() throws FormatException, IOException {
        byte[] data = new byte[] {
                0x00, 0x0F,
                0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x0F
        };

        InputStream is = new ByteArrayInputStream(data);

        exception.expect(FormatException.class);
        exception.expectMessage("MLA header specified invalid header size");

        loader.load(is);
    }

    @Test
    public void testLoadWithUnsupportedAudioFormat() throws FormatException, IOException {
        byte[] data = new byte[] {
                0x00, 0x10,
                0x0A,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x0F
        };

        InputStream is = new ByteArrayInputStream(data);

        exception.expect(FormatException.class);
        exception.expectMessage("MLA header specified unsupported audio format");

        loader.load(is);
    }

    @Test
    public void testLoadWithTruncatedExtraData() throws FormatException, IOException {
        byte[] data = new byte[] {
                0x00, 0x14,
                0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x0F,
                (byte)0xDE, (byte)0xAD, (byte)0xBE
        };

        InputStream is = new ByteArrayInputStream(data);

        exception.expect(FormatException.class);
        exception.expectMessage("MLA extra data is truncated");

        loader.load(is);
    }

    @Test
    public void testLoad() throws FormatException, IOException {
        byte[] data = new byte[] {
                0x00, 0x10,
                0x01,
                0x00, 0x06, 0x1B, (byte)0xD4,
                0x00, 0x49, (byte)0x8C, (byte)0xA6
        };

        InputStream is = new ByteArrayInputStream(data);

        Container container = loader.load(is);
        Header header = container.getHeader();

        assertEquals(16, header.getSize());
        assertEquals(400340, header.getLoopAreaOffset());
        assertEquals(4820134, header.getLoopAreaSize());
    }

    @Test
    public void testGetVersion() {
        assertEquals(1, loader.getVersion());
    }

}
