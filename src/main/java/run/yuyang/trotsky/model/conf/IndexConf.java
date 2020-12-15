package run.yuyang.trotsky.model.conf;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import run.yuyang.trotsky.model.base.Pair;

import java.util.List;

@Data
@RegisterForReflection
@NoArgsConstructor
public class IndexConf {

    private String title;

    private String description;

    private List<Pair> links;

    private List<Pair> navs;

}
