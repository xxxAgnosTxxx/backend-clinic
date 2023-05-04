package com.prokhorov.clinic.service;

import com.prokhorov.clinic.dao.entity.TokenDao;
import com.prokhorov.clinic.entity.Person;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class TokenService {
    private static ConcurrentMap<UUID, TokenDao> tokenMap = new ConcurrentHashMap<>();

    public UUID getToken(Person person) {
        Optional<Map.Entry<UUID, TokenDao>> entry = tokenMap.entrySet().stream()
                .filter(e -> e.getValue().getPerson().equals(person))
                .findFirst();
        if (entry.isPresent()) {
            UUID token = entry.get().getKey();
            if(isAgeingToken(entry.get().getKey())){
                return refreshToken(token);
            }
            return token;
        } else {
            UUID id = UUID.randomUUID();
            tokenMap.put(id, new TokenDao(person));
            return id;
        }
    }

    public UUID refreshToken(UUID uuid) {
        Person person = tokenMap.get(uuid).getPerson();
        tokenMap.remove(uuid);
        UUID id = UUID.randomUUID();
        tokenMap.put(id, new TokenDao(person));
        return id;
    }

    public boolean isOldToken(UUID uuid) {
        if (!tokenMap.containsKey(uuid)) return true;
        TokenDao dao = tokenMap.get(uuid);
        return !dao.getStartedTime().isAfter(LocalDateTime.now().minusHours(24));
    }

    public boolean isAgeingToken(UUID uuid) {
        if (!tokenMap.containsKey(uuid)) return true;
        TokenDao dao = tokenMap.get(uuid);
        return !dao.getStartedTime().isAfter(LocalDateTime.now().minusHours(23));
    }

    public Person getPerson(UUID uuid) {
        return tokenMap.entrySet().stream()
                .filter(s -> s.getKey().equals(uuid))
                .findFirst()
                .get()
                .getValue()
                .getPerson();
    }

    public boolean isExist(Person person){
        return tokenMap.entrySet().stream()
                .anyMatch(s-> s.getValue().getPerson().equals(person));
    }

    @Scheduled(fixedDelay = 8640000)
    private void removeTokens() {
        tokenMap.forEach((key, value) -> {
            if (isOldToken(key)) {
                refreshToken(key);
            }
        });
    }
}
