package com.ascending.training.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.ascending.training.init.AppInitializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInitializer.class)
public class MessageServiceMockAWSTest {
    @Autowired
    @Spy
    private Logger logger;

//    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AmazonSQS amazonSQS = Mockito.mock(AmazonSQS.class);


    private ReceiveMessageResult receiveMessageResult;

    @InjectMocks
    private MessageService messageService;

    private String queueName = "training_queue_ascending_com";
    private String fakeQueueUrl = "www.fakeQueueUrl.com/";
    private List<String> fakeQueueUrlList = Arrays.asList("www.myfakeQueueUrl1.com", "www.myfakeQueueUrl2.com", "www.myfakeQueueUrl3.com");
    private String msg = "This is a message for test";
    private List<Message> messages;

    @Before
    public void setUp() {
        logger.debug(">>>>>>>>>>> Start testing ...");
        messages = new ArrayList<>();
        messages.add(new Message());

        // Initialize annotated fields
        MockitoAnnotations.initMocks(this);

        // Stubbing
        GetQueueUrlResult getQueueUrlResult = Mockito.mock(GetQueueUrlResult.class);
        CreateQueueResult createQueueResult = Mockito.mock(CreateQueueResult.class);

        when(amazonSQS.getQueueUrl(anyString())).thenReturn(getQueueUrlResult);
        when(getQueueUrlResult.getQueueUrl()).thenReturn(fakeQueueUrl);
        when(amazonSQS.createQueue(any(CreateQueueRequest.class))).thenReturn(createQueueResult);
        when(createQueueResult.getQueueUrl()).thenReturn(fakeQueueUrl);

        // Since amazonSQS.listQueues().getQueueUrls() has to invoke amazonSQS.listQueues(), this statement will be counted into the wanted number of invocations
        ListQueuesResult listQueuesResult = Mockito.mock(ListQueuesResult.class);
        receiveMessageResult = Mockito.mock(ReceiveMessageResult.class);

        when(amazonSQS.listQueues()).thenReturn(listQueuesResult);
        when(listQueuesResult.getQueueUrls()).thenReturn(fakeQueueUrlList);
        when(amazonSQS.deleteQueue(any(DeleteQueueRequest.class))).thenReturn(new DeleteQueueResult());
        when(amazonSQS.sendMessage(any(SendMessageRequest.class))).thenReturn(new SendMessageResult());
        when(amazonSQS.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);
        when(receiveMessageResult.getMessages()).thenReturn(messages);
    }

    @After
    public void tearDown() {
        logger.debug(">>>>>>>>> End test");
    }

    @Test
    public void getQueueUrl() {
        String queueUrl = messageService.getQueueUrl(queueName);

        Assert.assertEquals(queueUrl, fakeQueueUrl);
        verify(amazonSQS, times(1)).getQueueUrl(anyString());
//        verify(logger, atLeast(1)).debug(anyString());
    }

    @Test
    public void createQueue() {
        when(amazonSQS.getQueueUrl(anyString()).getQueueUrl()).thenThrow(new QueueDoesNotExistException("The queue doesn't exist."));
        String queueUrl = messageService.createQueue(queueName);

        Assert.assertEquals(fakeQueueUrl, queueUrl);
        verify(amazonSQS, timeout(10).times(1)).createQueue(any(CreateQueueRequest.class));
    }

    @Test
    public void listQueues() {
        messageService.listQueues(queueName);
        verify(amazonSQS, times(1)).listQueues();
    }

    @Test
    public void deleteQueue() {
        messageService.deleteQueue(queueName);
        verify(amazonSQS, times(1)).deleteQueue(any(DeleteQueueRequest.class));
    }

    @Test
    public void sendMessage() {
        messageService.sendMessage(queueName, msg);
        verify(amazonSQS, times(1)).sendMessage(any());
    }

    @Test
    public void getMessages() {
        List<Message> messages = messageService.getMessages(queueName);

        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.size());
        verify(amazonSQS, times(1)).receiveMessage(any(ReceiveMessageRequest.class));
    }

    @Test
    public void listMessages() {
        messageService.listMessages(queueName);
        verify(amazonSQS, times(1)).receiveMessage(any(ReceiveMessageRequest.class));
        verify(receiveMessageResult, times(1)).getMessages();
    }

    @Test
    public void deleteMessages() {
        messageService.deleteMessage(queueName);

        InOrder inOrder = Mockito.inOrder(amazonSQS);
        inOrder.verify(amazonSQS, times(1)).receiveMessage(any(ReceiveMessageRequest.class));
        inOrder.verify(amazonSQS, times(1)).deleteMessage(any(DeleteMessageRequest.class));
    }

}
