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

import pm.monkey.mla4j.AudioFormat;

public final class Header implements pm.monkey.mla4j.Header {

    private short size;

    private AudioFormat audioFormat;

    private int loopAreaOffset;

    private int loopAreaSize;

    private byte[] extraData;

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    @Override
    public int getLoopAreaOffset() {
        return loopAreaOffset;
    }

    public void setLoopAreaOffset(int loopAreaOffset) {
        this.loopAreaOffset = loopAreaOffset;
    }

    @Override
    public int getLoopAreaSize() {
        return loopAreaSize;
    }

    public void setLoopAreaSize(int loopAreaSize) {
        this.loopAreaSize = loopAreaSize;
    }

    public byte[] getExtraData() {
        return extraData;
    }

    public void setExtraData(byte[] extraData) {
        this.extraData = extraData;
    }

}
