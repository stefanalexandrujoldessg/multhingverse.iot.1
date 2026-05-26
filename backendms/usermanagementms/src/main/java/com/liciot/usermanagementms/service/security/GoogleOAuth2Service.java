package com.liciot.usermanagementms.service.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.usermanagementms.dto.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleOAuth2Service {

    private static final String GOOGLE_TOKEN_ENDPOINT  = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_ENDPOINT = "https://www.googleapis.com/oauth2/v2/userinfo";

    @Value("${google.oauth2.client-id}")
    private String clientId;

    @Value("${google.oauth2.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GoogleUserInfo exchangeCodeForUserInfo(String code, String redirectUri) throws Exception {
        String accessToken = exchangeCodeForAccessToken(code, redirectUri);
        return fetchUserInfo(accessToken);
    }

    private String exchangeCodeForAccessToken(String code, String redirectUri) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code",          code);
        params.add("client_id",     clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri",  redirectUri);
        params.add("grant_type",    "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_ENDPOINT, request, String.class);

        JsonNode json = objectMapper.readTree(response.getBody());
        if (!json.has("access_token")) {
            throw new Exception("Google token exchange failed: " + response.getBody());
        }
        return json.get("access_token").asText();
    }

    private GoogleUserInfo fetchUserInfo(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        JsonNode json = objectMapper.readTree(response.getBody());

        GoogleUserInfo info = new GoogleUserInfo();
        info.setGoogleId(json.get("id").asText());
        info.setEmail(json.get("email").asText());
        info.setName(json.has("name") ? json.get("name").asText() : json.get("email").asText());
        return info;
    }
}
