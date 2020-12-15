package run.yuyang.trotsky.model.conf;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YuYang
 */
@Data
@RegisterForReflection
@NoArgsConstructor
public class UserConf {

    private String version;

    private String email;

    private String password;

    private String nickName;

    private String buildTime;

}
