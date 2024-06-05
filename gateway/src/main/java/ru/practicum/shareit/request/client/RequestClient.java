package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.baseclient.BaseClient;
import ru.practicum.shareit.request.dto.RequestCreateDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String BASE_PATH = "/requests";

    public RequestClient(@Value("${shareit-server.url}") String url, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(url + BASE_PATH))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> create(long userId, RequestCreateDto request) {
        return exchange("", HttpMethod.POST, request, getHeaders(userId), null);
    }

    public ResponseEntity<Object> getAll(long userId, Integer fromIndex, Integer size) {
        Map<String, Object> params = Map.of("from", fromIndex, "size", size);

        return exchange("/all?from={from}&size={size}", HttpMethod.GET, null, getHeaders(userId), params);
    }

    public ResponseEntity<Object> getById(long userId, long requestId) {
        return exchange("/" + requestId, HttpMethod.GET, null, getHeaders(userId), null);
    }

    public ResponseEntity<Object> getAllUserById(long userId) {
        return exchange("", HttpMethod.GET, null, getHeaders(userId), null);
    }
}
