package com.redhat.developer.demos.preference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

@RestController
public class PreferencesController {

    private static final String RESPONSE_STRING_FORMAT = "preference => %s\n";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate;

    @Value("${recommendations.api.url:http://recommendations:8080}")
    private String remoteURL;

    public PreferencesController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/")
    public ResponseEntity<Preference> getPreferences(HttpServletRequest httpServletRequest) {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            String header = httpServletRequest.getHeader("x-api-key");
//            while(headerNames.hasMoreElements()){
//                String headerKey = headerNames.nextElement();
//                headers.add(headerKey, httpServletRequest.getHeader(headerKey));
//            }
            headers.add("x-api-key", header);
            ResponseEntity<Recommendation> entity = restTemplate.exchange(
                    remoteURL, HttpMethod.GET, new HttpEntity<>(headers),
                    Recommendation.class);

            Recommendation recommendation = entity.getBody();

            Preference preference = new Preference();
            Random rand = new Random();
            Integer id = rand.nextInt(1000000);
            preference.setId(id);
            preference.setComment("user recommendation");
            preference.setDate(LocalDate.now().toString());
            preference.setRecommendation(recommendation);

            return ResponseEntity.ok(preference);
        } catch (HttpStatusCodeException ex) {
            logger.warn("Exception trying to get the response from recommendation service.", ex);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (RestClientException ex) {
            logger.warn("Exception trying to get the response from recommendation service.", ex);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private String createHttpErrorResponseString(HttpStatusCodeException ex) {
        String responseBody = ex.getResponseBodyAsString().trim();
        if (responseBody.startsWith("null")) {
            return ex.getStatusCode().getReasonPhrase();
        }
        return responseBody;
    }
//    @PostConstruct
//    public void addInterceptors() {
//        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//        interceptors.add(new RestTemplateInterceptor());
//        restTemplate.setInterceptors(interceptors);
//    }

}
