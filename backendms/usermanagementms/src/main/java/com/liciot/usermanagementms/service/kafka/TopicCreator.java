package com.liciot.usermanagementms.service.kafka;

import com.liciot.usermanagementms.apiprovider.ApiProvider;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class TopicCreator {

@Autowired
    ApiProvider apiProvider;

    public boolean createTopics(List<String> topicsNames)
    {
        System.out.println("[Will create topics] "+ topicsNames);
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, apiProvider.getLiciotKafkaBootstrapServers());
        //properties.put(AdminClientConfig., "localhost:2181");

        AdminClient kafkaAdminClient = AdminClient.create(properties);
        DescribeClusterResult describeClusterResult = kafkaAdminClient.describeCluster();

        try {
            int numberOfNodes = describeClusterResult.nodes().get().size();
            if(numberOfNodes<3 )
            {
                return false;
            }

            int numberOfPartitions = 1;// la mine nu se renteaza sa fie mai multe partitii pemntru ca toti trebuie sa consume toate mesajele nu ma prea folosescde acea situati in  care am mai mlte repliic alea aceluiai serviciu pentru marirewa throutputului atntie in acea situatie e super, deci 1 si multe replici
            short numberOfReplicas = (short)3;
            CreateTopicsResult createTopicsResult = kafkaAdminClient.createTopics(topicsNames.stream().map((topicName)->{return new NewTopic(topicName,numberOfPartitions,numberOfReplicas);}).collect(Collectors.toList()));

            //ori sleep ee c enu este ok ori cu is fone ori cea mai buna cu get altfel daca threadul aceta se termina din care apelez se termina si celelalt poate?? si nu se ami creeaza topiccurile
            //ex cu minu dar nu stiu daca la terminarea pro3esului prioncipal th principsl sau a celui parinte
              createTopicsResult.all().get();// dasca apare oric e fel de problema pe parcurs arucnca exceptie deci get de kla acest KafkaFuture returneazqaa doar daca toto a fost fwacut cu success
              //chiar si dacac unul sau mai multe exista deja da exceptie
            //este specificat foarte clar in doc ca ret suces numai daa toate topicurile s-au creeA
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
return true;

    }
}
