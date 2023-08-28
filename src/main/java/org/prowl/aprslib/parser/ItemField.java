package org.prowl.aprslib.parser;

import java.util.Objects;

public class ItemField extends APRSData {
	private static final long serialVersionUID = 1L;
	private boolean live = true;
	private String itemName;

	/**
	 * parse an APRS item message
	 * 
	 * @return new ItemPacket instance with the parsed data
	 */
	public ItemField(byte[] msgBody) throws Exception {
		this.rawBytes = msgBody;
		String body = new String(msgBody);
		int name_length = body.indexOf("!") - 1;
		if (name_length < 1 || name_length > 9) {
			name_length = body.indexOf("_");
			if (name_length < 1 || name_length > 9)
				throw new Exception("Invalid ITEM packet, missing '!' or '_'.");
			this.live = false;
		} else
			this.live = true;
		this.itemName = new String(msgBody, 1, name_length).trim();
		int cursor = name_length + 2;
//		if (msgBody[cursor] > '0' && msgBody[cursor] < '9') {
//			this.position = PositionParser.parseUncompressed(msgBody, cursor);
//			cursor += 19;
//		} else {
//			this.position = PositionParser.parseCompressed(msgBody, cursor);
//			cursor += 12;
//		}
		setLastCursorPosition(cursor);
	//	comment = new String(msgBody, cursor, msgBody.length - cursor, "UTF-8").trim();
	}

	@Override
	public String toString() {
		if (rawBytes != null)
			return new String(rawBytes);
		return ")" + this.itemName + (live ? "!" : "_") + comment;
	}

	@Override
	public int compareTo(APRSData o) {
		if (this.hashCode() > o.hashCode()) {
			return 1;
		}
		if (this.hashCode() == o.hashCode()) {
			return 0;
		}
		return -1;
	}

	@Override
	public boolean hasFault() {
		return this.hasFault;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ItemField)) {
			return false;
		}
		ItemField itemField = (ItemField) o;
		return live == itemField.live && Objects.equals(itemName, itemField.itemName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(live, itemName);
	}

	/**
	 * Return this items name.
	 * @return
	 */
	public String getItemName() {
		return itemName;
	}

}
