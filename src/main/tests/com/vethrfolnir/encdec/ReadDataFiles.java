/*
 * This file is strictly bounded by the creators of Vethrfolnir and its prohibited
 * for commercial use, or any use what so ever.
 * Copyright © Vethrfolnir Project 2013
 */
package com.vethrfolnir.encdec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.DataInputStream;
import java.io.File;
import java.net.URL;
import java.nio.ByteOrder;

import corvus.corax.tools.PrintData;

/**
 * @author Vlad
 *
 */
public class ReadDataFiles {

	public static void main(String[] args) throws Exception {
		
		File dir = new File("./res/");
		
		for (String filename :  dir.list()) {
			File file = new File("./res/", filename);
			
			if(!file.isDirectory()) {
				System.out.println("File --- "+file.getName());
				readFile(file.toURI().toURL());
			}
		}
	}
	// 1057531803 -497237369 -1814922567 551462847
	//static BigInteger[] biXor = { new BigInteger("1057531803"), new BigInteger("0xE25CC287"), new BigInteger("0x93D27AB9"), new BigInteger("0x20DEA7BF") };
	static long[] xor_tab_datfile = new long[] { 0x3F08A79B, 0xE25CC287, 0x93D27AB9, 0x20DEA7BF };
	//static long[] xor_tab_datfile = new long[] { 0x3F08A79B, 0xE25CC287, 0x93D27AB9, 0x20DEA7BF };

	public static long[] readFile(URL url) {
		
		ByteBuf buff = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

		try (DataInputStream is = new DataInputStream(url.openStream())) {
			buff.writeBytes(is, is.available());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		int bits2 = buff.readUnsignedShort();
		
		System.out.println("First two bits: "+bits2 + " hex: 0x"+PrintData.fillHex(bits2, 2));

		long[] out_dat = new long[12];

		buff.readerIndex(6);
		int pointer = 0;
		for (int i = 0; i < 3; i++) {
			long[] buf = new long[4];
			
			for (int j = 0; j < 4; j++) {
				buf[j] = buff.readUnsignedInt();
			}
			
			out_dat[pointer++] = buf[0] ^ (xor_tab_datfile[0]);
			out_dat[pointer++] = buf[1] ^ (xor_tab_datfile[1] & 0xFFFFFFFFL);
			out_dat[pointer++] = buf[2] ^ (xor_tab_datfile[2] & 0xFFFFFFFFL);
			out_dat[pointer++] = buf[3] ^ (xor_tab_datfile[3]);
		}
		
		for (int i = 0; i < out_dat.length; i++) {
			System.out.print(" "+(out_dat[i]));
		}
		
		System.out.println();
		return null;
	}
}