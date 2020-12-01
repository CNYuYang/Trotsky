package run.yuyang.trotsky.model.conf;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * @author YuYang
 */
@Data
@RegisterForReflection
public class UserConf {

    private String version;

    private String email;

    private String password;

    private String nickName;

    private String buildTime;

}
