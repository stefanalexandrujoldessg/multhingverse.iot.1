package com.liciot.humanms.apiprovider;


import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public  class ApiProvider {
    private   String liciotUserManagementMicroservice;
    private String liciotKafkaBootstrapServers;
    public ApiProvider()
    {


        for(Map.Entry<String,String> entr:System.getenv().entrySet())
        {
            System.out.println(entr.getKey()+" "+entr.getValue());
        }
        try {

            String liciotUserManagementMicroservice = System.getenv("LICIOT_USERMANAGEMENTMICROSERVICE_HOST");
            if(liciotUserManagementMicroservice!=null)
            {
                this.liciotUserManagementMicroservice = liciotUserManagementMicroservice;
            }
            else
            {
                this.liciotUserManagementMicroservice = "http://localhost:9090";

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();;
            this.liciotUserManagementMicroservice = "http://localhost:9090";
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

    public String getLiciotUserManagementMicroservice() {
        return liciotUserManagementMicroservice;
    }

    public void setLiciotUserManagementMicroservice(String liciotUserManagementMicroservice) {
        this.liciotUserManagementMicroservice = liciotUserManagementMicroservice;
    }

    public String getLiciotKafkaBootstrapServers() {
        return liciotKafkaBootstrapServers;
    }

    public void setLiciotKafkaBootstrapServers(String liciotKafkaBootstrapServers) {
        this.liciotKafkaBootstrapServers = liciotKafkaBootstrapServers;
    }
}
