package run.yuyang.trotsky.model.conf;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;
import run.yuyang.trotsky.model.param.MDParam;

/**
 * @author YuYang
 */
@Data
@RegisterForReflection
@NoArgsConstructor
public class IntroConf {

    private String name;

    private String father;

    private String path;

    public IntroConf(DirConf dirConf) {
        this.father = dirConf.getFather();
        this.name = dirConf.getName() + ".md";
        this.path = dirConf.getPath() + ".md";
    }

}
