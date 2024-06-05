package ru.practicum.shareit.baseclient;

import io.micrometer.core.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.util.CustomHeader;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BaseClient {

    protected final RestTemplate rest;

    protected ResponseEntity<Object> exchange(String path,
                                              HttpMethod method,
                                              @Nullable Object body,
                                              HttpHeaders headers,
                                              @Nullable Map<String, Object> params) {
        try {
            HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);

            if (params != null) {
                return rest.exchange(path, method, httpEntity, Object.class, params);
            } else {
                return rest.exchange(path, method, httpEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBodyAsByteArray());
        }
    }

    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }

    protected HttpHeaders getHeaders(long userId) {
        HttpHeaders headers = getHeaders();

        headers.set(CustomHeader.USERID.getName(), String.valueOf(userId));

        return headers;
    }

}