package run.yuyang.trotsky.service;

public interface AuthService {

    String token();

    boolean auth(String token);

}
