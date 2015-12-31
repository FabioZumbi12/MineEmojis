/*
 * Copyright 2015 Marvin Schäfer (inventivetalent). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package de.inventivegames.rpapi;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class to send resource packs with a individual hash
 *
 * @author inventivetalent
 */
public abstract class ResourcePackAPI {

	/**
	 * @param p   Receiver of the resource pack
	 * @param url URL to the resource pack
	 */
	public static void setResourcepack(Player p, String url) {
		setResourcepack(p, url, "thepluginauthorislazy");
	}

	/**
	 * @param p    Receiver of the resource pack
	 * @param url  URL to the resource pack
	 * @param hash resource pack hash (unique or random string, will be returned in the {@link ResourcePackStatusEvent})
	 */
	public static void setResourcepack(Player p, String url, String hash) {
		if (p == null) { throw new IllegalArgumentException("player cannot be null"); }
		if (url == null) { throw new IllegalArgumentException("url cannot be null"); }
		if (hash == null) { throw new IllegalArgumentException("hash cannot be null"); }
		if (hash.length() > 40) { throw new IllegalArgumentException("hash cannot be longer than 40 characters"); }
		try {
			if (serverVersion < 1.8) {
				Object payload = Reflection.getNMSClass("PacketPlayOutCustomPayload").getConstructor(String.class, byte[].class).newInstance("MC|RPack", url.getBytes());
				sendPacket(p, payload);
			}
			if (serverVersion >= 1.8 || getVersion(p) >= 36) {
				Object packSend = packetPlayOutResourcePackSend.getConstructor(String.class, String.class).newInstance(url, hash);
				sendPacket(p, packSend);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	protected ResourcePackAPI() {
	}

	private static void sendPacket(Player p, Object packet) {
		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			nmsSendPacket.invoke(connection, packet);
		} catch (final Exception e) {
			System.err.println("[TitleManager] Error while sending title to Player " + p.getName() + ": " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	private static int getVersion(Player p) {
		try {
			final Object handle = Reflection.getHandle(p);
			final Object connection = nmsFieldPlayerConnection.get(handle);
			final Object network = nmsFieldNetworkManager.get(connection);
			final Object channel;
			if (serverVersion == 1.7) {
				channel = nmsFieldNetworkManagerM.get(network);
			} else {
				channel = nmsFieldNetworkManagerI.get(network);
			}
			final Object version = serverVersion == 1.7 ? nmsNetworkGetVersion.invoke(network, channel) : 47;
			return (int) version;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 180;
	}

	private static boolean initialized;

	private static Class<?> nmsPlayerConnection;
	private static Class<?> nmsEntityPlayer;
	private static Class<?> ioNettyChannel;

	private static Class<?> packetPlayOutResourcePackSend;

	private static Method nmsSendPacket;
	private static Method nmsNetworkGetVersion;

	private static Field nmsFieldPlayerConnection;
	private static Field nmsFieldNetworkManager;
	private static Field nmsFieldNetworkManagerI;
	private static Field nmsFieldNetworkManagerM;

	private static double serverVersion = 1.7;

	static {
		if (!initialized) {
			String ver = Reflection.getVersion();
			if (ver.contains("1_7")) {
				serverVersion = 1.7;
			}
			if (ver.contains("1_8")) {
				serverVersion = 1.8;
			}
			if (ver.contains("1_8_R2")) {
				serverVersion = 1.83;
			}
			if (ver.contains("1_8_R3")) {
				serverVersion = 1.85;
			}
			try {
				nmsPlayerConnection = Reflection.getNMSClass("PlayerConnection");
				nmsEntityPlayer = Reflection.getNMSClass("EntityPlayer");
				ioNettyChannel = serverVersion == 1.7 ? Class.forName("net.minecraft.util.io.netty.channel.Channel") : Class.forName("io.netty.channel.Channel");

				nmsFieldPlayerConnection = Reflection.getField(nmsEntityPlayer, "playerConnection");
				nmsFieldNetworkManager = Reflection.getField(nmsPlayerConnection, "networkManager");
				nmsFieldNetworkManagerI = Reflection.getField(nmsFieldNetworkManager.getType(), "i");
				nmsFieldNetworkManagerM = Reflection.getField(nmsFieldNetworkManager.getType(), "m");

				packetPlayOutResourcePackSend = serverVersion < 1.8 ? Class.forName("org.spigotmc.ProtocolInjector$PacketPlayResourcePackSend") : Reflection.getNMSClass("PacketPlayOutResourcePackSend");

				nmsSendPacket = Reflection.getMethod(nmsPlayerConnection, "sendPacket");
				nmsNetworkGetVersion = Reflection.getMethod(nmsFieldNetworkManager.getType(), "getVersion", ioNettyChannel);

				initialized = true;
			} catch (Exception e) {
			}
		}
	}
}
