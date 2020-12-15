package run.yuyang.trotsky.service.impl;

import run.yuyang.trotsky.service.AuthService;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    private static String token = "";

    @Override
    public String token() {
        token = UUID.randomUUID().toString();
        return token;
    }

    @Override
    public boolean auth(String token) {
        if (null != token && !"".equals(token)) {
            return this.token.equals(token);
        }
        return false;
    }
}
