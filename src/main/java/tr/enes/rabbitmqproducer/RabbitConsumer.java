/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.enes.rabbitmqproducer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author enes
 */
public class RabbitConsumer {

    private final static String QUEUE_NAME = "hello";
    static int msgCount = 0;

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException,
            NamingException {

        ConnectionFactory factory = new ConnectionFactory();

        Connection connection = null;
        try {
            factory.setHost("localhost");
            connection = factory.newConnection();

        } catch (TimeoutException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //String message = new String(body, "UTF-8");
                //System.out.println(" [x] Received '" + message + "'");

                msgCount++;
                if (msgCount % 1000 == 0) {
                    System.out.println(msgCount);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }

}
