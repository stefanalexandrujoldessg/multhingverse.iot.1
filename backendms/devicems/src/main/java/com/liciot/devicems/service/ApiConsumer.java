package com.liciot.devicems.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class  ApiConsumer  {
    public <T> T getForObject(String url, Class<T> cl, HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println("[ApiConsumer] will make request");
        ResponseEntity<T> response = restTemplate.exchange(url,
                HttpMethod.GET,entity, cl);

        System.out.println("[ApiConsumer]: responseBody: "+response.getBody());
        return response.getBody(); //this returns List of Employee
        //restTemplate.exchange()
       // return restTemplate.getForObject(url,cl);
    }

    public <T> ResponseEntity<T> getFullResponseForObject(String url, Class<T> cl, HttpHeaders headers) {
        RestTemplate restTemplate = new RestTemplate();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        System.out.println("[ApiConsumer] will make request");
        ResponseEntity<T> response = restTemplate.exchange(url,
                HttpMethod.GET,entity, cl);

        System.out.println("[ApiConsumer]: responseBody: "+response.getBody());
        return response; //this returns List of Employee
        //restTemplate.exchange()
        // return restTemplate.getForObject(url,cl);
    }

}