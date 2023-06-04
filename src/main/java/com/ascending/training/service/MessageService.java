package com.ascending.training.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {
    @Autowired
    private Logger logger;
    @Autowired
    private AmazonSQS amazonSQS;

    public String getQueueUrl(String queueName) {
        return amazonSQS.getQueueUrl(queueName).getQueueUrl();
    }

    public String createQueue(String queueName) {
        String queueUrl = null;

        try {
            queueUrl = getQueueUrl(queueName);
        } catch (AmazonSQSException e) {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
            queueUrl = amazonSQS.createQueue(createQueueRequest).getQueueUrl();
        }

        return queueUrl;
    }

    public void listQueues(String queueName) {
        logger.debug("Listing all queues in your account.\n");

        ListQueuesResult lqr = amazonSQS.listQueues();

        for (String queueUrl : lqr.getQueueUrls()) {
            logger.debug("    QueueUrl : " + queueUrl);
        }
    }

    public boolean deleteQueue(String queueName) {
        boolean isSuccess = false;

        try {
            logger.debug("Deleting a queue.");
            amazonSQS.deleteQueue(new DeleteQueueRequest(queueName));
            isSuccess = true;
        } catch (AmazonServiceException ase) {
            logger.debug("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            logger.debug("Error Message :      " + ase.getMessage());
            logger.debug("HTTP Status Code :   " + ase.getStatusCode());
            logger.debug("AWS Error Code :     " + ase.getErrorCode());
            logger.debug("Error Type :         " + ase.getErrorType());
            logger.debug("Request ID :         " + ase.getRequestId());
        }

        return isSuccess;
    }

    public void sendMessage(String queueName, String msg) {
        try {
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            MessageAttributeValue messageAttributeValue = new MessageAttributeValue();

            messageAttributeValue.withDataType("String").withStringValue("File URL in S3");
            messageAttributes.put("message", messageAttributeValue);

            String queueUrl = getQueueUrl(queueName);
            SendMessageRequest sendMessageRequest = new SendMessageRequest();

            sendMessageRequest.withQueueUrl(queueUrl)
                    .withMessageBody(msg)
                    .withMessageAttributes(messageAttributes);
            amazonSQS.sendMessage(sendMessageRequest);
        } catch (AmazonServiceException ase) {
            logger.debug("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            logger.debug("Error Message :      " + ase.getMessage());
            logger.debug("HTTP Status Code :   " + ase.getStatusCode());
            logger.debug("AWS Error Code :     " + ase.getErrorCode());
            logger.debug("Error Type :         " + ase.getErrorType());
            logger.debug("Request ID :         " + ase.getRequestId());
        }
    }

    public List<Message> getMessages(String queueName) {
        String queueUrl = getQueueUrl(queueName);
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();

        return messages;
    }

    public void listMessages(String queueName) {
        logger.debug("Receiving messages from my standard queue.");

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueName);
        ReceiveMessageResult receiveMessageResult = amazonSQS.receiveMessage(receiveMessageRequest);
        List<Message> messages = receiveMessageResult.getMessages();

        for (Message message : messages) {
            logger.debug("Message");
            logger.debug("  MessageId :       " + message.getMessageId());
            logger.debug("  ReceiptHandle :   " + message.getReceiptHandle());
            logger.debug("  MD5OfBody :       " + message.getMD5OfBody());
            logger.debug("  Body :            " + message.getBody());

            for (Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
                logger.debug("Attribute");
                logger.debug("  Name :        " + entry.getKey());
                logger.debug("  Value :       " + entry.getValue());
            }
        }
    }

    public boolean deleteMessage(String queueName) {
        boolean isSuccess = false;

        try {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueName);
            List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();

            logger.debug("Deleting a message.");

            String messageReceiptHandle = messages.get(0).getReceiptHandle();

            amazonSQS.deleteMessage(new DeleteMessageRequest(queueName, messageReceiptHandle));
            isSuccess = true;
        } catch (AmazonServiceException ase) {
            logger.debug("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            logger.debug("Error Message :      " + ase.getMessage());
            logger.debug("HTTP Status Code :   " + ase.getStatusCode());
            logger.debug("AWS Error Code :     " + ase.getErrorCode());
            logger.debug("Error Type :         " + ase.getErrorType());
            logger.debug("Request ID :         " + ase.getRequestId());
        }

        return isSuccess;
    }
}
