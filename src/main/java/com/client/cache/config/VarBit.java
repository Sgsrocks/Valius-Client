package com.client.cache.config;

import com.client.cache.Archive;
import com.client.io.Buffer;

public final class VarBit {

	public static void unpackConfig(Archive streamLoader) {
		Buffer stream = new Buffer(streamLoader.getDataForName("varbit.dat"));
		int cacheSize = stream.readUShort();
		if (cache == null)
			cache = new VarBit[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new VarBit();
			cache[j].readValues(stream);
		}

		if (stream.position != stream.payload.length)
			System.out.println("varbit load mismatch");
	}

	private void readValues(Buffer stream) {
		int opcode = stream.readUnsignedByte();

		if (opcode == 0) {
			return;
		} else if (opcode == 1) {
		setting = stream.readUShort();
		low = stream.readUnsignedByte();
		high = stream.readUnsignedByte();
		} else {
			System.out.println(opcode);
		}
	}

	private VarBit() {
		
	}

	public static VarBit cache[];
	public int setting;
	public int low;
	public int high;
	
}
