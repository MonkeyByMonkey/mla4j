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

package pm.monkey.mla4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Loader {

    private static final HashMap<Integer, VersionLoader> versionLoaders;

    static {
        versionLoaders = new HashMap<Integer, VersionLoader>();
        versionLoaders.put(1, new pm.monkey.mla4j.v1.Loader());
    }

    public Container load(InputStream is) throws FormatException, IOException {

        byte[] magic = new byte[4];

        int bytesRead = is.read(magic);

        if (bytesRead < 4
                || magic[0] != 0xF0
                || magic[1] != 0x9F
                || magic[2] != 0x99
                || magic[3] != 0x89) {

            throw new FormatException("MLA magic is missing");
        }

        int version = is.read();

        if (version == -1) {
            throw new FormatException("MLA version byte is missing");
        } else {

            VersionLoader loader = versionLoaders.get(version);

            if (loader == null) {
                throw new FormatException("MLA version " + version + " is not supported by this library");
            } else {
                return loader.load(is);
            }
        }
    }
}
