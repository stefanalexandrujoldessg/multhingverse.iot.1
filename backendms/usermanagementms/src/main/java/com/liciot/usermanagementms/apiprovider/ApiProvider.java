package com.liciot.usermanagementms.apiprovider;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public  class ApiProvider {
    private   String liciotDatabaseUrl ;
    private String liciotKafkaBootstrapServers;
    public ApiProvider()
    {


        for(Map.Entry<String,String> entr:System.getenv().entrySet())
        {
            System.out.println(entr.getKey()+" "+entr.getValue());
        }
        try {

            String host = System.getenv("LICIOT_DATABASE_HOST");
            if(host!=null)
            {
                this.liciotDatabaseUrl = "jdbc:postgresql://"+host+":5432/DB_DB";
            }
            else
            {
                this.liciotDatabaseUrl = "jdbc:postgresql://localhost:5432/DB_DB";

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();;
            this.liciotDatabaseUrl = "jdbc:postgresql://localhost:5432/DB_DB";
        }


        try {

            String servers = System.getenv("LICIOT_KAFKA_BOOTSTRAP_SERVERS");
            if(servers!=null)
            {
                this.liciotKafkaBootstrapServers = servers;
            }
            else
            {
                this.liciotKafkaBootstrapServers ="localhost:19092;localhost:29092";

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();;
            this.liciotKafkaBootstrapServers ="localhost:19092;localhost:29092";
        }
    }

    public String getLiciotDatabaseUrl() {
        return liciotDatabaseUrl;
    }

    public void setLiciotDatabaseUrl(String liciotDatabaseUrl) {
        this.liciotDatabaseUrl = liciotDatabaseUrl;
    }

    public String getLiciotKafkaBootstrapServers() {
        return liciotKafkaBootstrapServers;
    }

    public void setLiciotKafkaBootstrapServers(String liciotKafkaBootstrapServers) {
        this.liciotKafkaBootstrapServers = liciotKafkaBootstrapServers;
    }
}
