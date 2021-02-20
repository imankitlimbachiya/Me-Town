package com.app.metown.Models;

public class ConversationModel {

    String ConversationID, SenderUserID, SenderName, SenderProfilePicture, ProductImages, LastMessageBody, LastMessageType;

    public String getConversationID() {
        return ConversationID;
    }

    public void setConversationID(String conversationID) {
        ConversationID = conversationID;
    }

    public String getSenderUserID() {
        return SenderUserID;
    }

    public void setSenderUserID(String senderUserID) {
        SenderUserID = senderUserID;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderProfilePicture() {
        return SenderProfilePicture;
    }

    public void setSenderProfilePicture(String senderProfilePicture) {
        SenderProfilePicture = senderProfilePicture;
    }

    public String getProductImages() {
        return ProductImages;
    }

    public void setProductImages(String productImages) {
        ProductImages = productImages;
    }

    public String getLastMessageBody() {
        return LastMessageBody;
    }

    public void setLastMessageBody(String lastMessageBody) {
        LastMessageBody = lastMessageBody;
    }

    public String getLastMessageType() {
        return LastMessageType;
    }

    public void setLastMessageType(String lastMessageType) {
        LastMessageType = lastMessageType;
    }
}