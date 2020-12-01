package run.yuyang.trotsky.model.conf;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * @author YuYang
 */
@Data
@RegisterForReflection
public class TargetConf {

    private String noteName;

    private Integer target;

    private Integer done;

    private String color;

}
