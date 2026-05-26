package com.liciot.humanms.service;

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
        //specialSystem.out.println("[ApiConsumer] will make request");
        ResponseEntity<T> response = restTemplate.exchange(url,
                HttpMethod.GET,entity, cl);
        //specialSystem.out.println("[ApiConsumer]: responseBody: "+response.getBody());
        return response.getBody(); //this returns List of Employee
        //restTemplate.exchange()
       // return restTemplate.getForObject(url,cl);
    }



}