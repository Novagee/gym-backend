package com.jy.domain.rest;


public class MessageDetailVO extends MessageVO {
	private String receiverName;
	private String receiverPic;

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPic() {
		return receiverPic;
	}

	public void setReceiverPic(String receiverPic) {
		this.receiverPic = receiverPic;
	}

}
