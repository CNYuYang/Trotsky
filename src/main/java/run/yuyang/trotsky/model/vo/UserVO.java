package run.yuyang.trotsky.model.vo;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

/**
 * @author YuYang
 */
@Data
@RegisterForReflection
@Builder
public class UserVO {

    private String email;

    private String nickName;

}
