package com.example.helbertmonteiro.noticiario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    private EditText noticia;
    private Button   publicar;

    private String mensagem, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noticia  = (EditText) findViewById(R.id.etNoticia);
        publicar = (Button) findViewById(R.id.btPublicar);

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensagem = String.valueOf(noticia.getText());

                if(mensagem.toLowerCase().contains("palmeiras")){
                    System.out.println(mensagem);
                    try {
                        newTask(mensagem, "Palmeiras");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                if(mensagem.toLowerCase().contains("corinthians")){
                    try {
                        newTask(mensagem, "Corinthians");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

    }

    private void newTask(final String mensagem, final String time) throws java.io.IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectionFactory factory = new ConnectionFactory();
                try {
                    factory.setUri("amqp://vcnvarpt:tGnxUnUXW5g6UVawmymIgCZAN8Bm5PoA@woodpecker.rmq.cloudamqp.com/vcnvarpt");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                Connection connection = null;
                try {
                    connection = factory.newConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                Channel channel = null;
                try {
                    channel = connection.createChannel();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    channel.queueDeclare(time, true, false, false, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String message = mensagem;

                try {
                    channel.basicPublish( "", time, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(" [x] Sent '" + message + "'");

                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.interrupt();
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
