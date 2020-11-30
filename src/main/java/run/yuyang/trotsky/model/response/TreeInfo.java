package run.yuyang.trotsky.model.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Data;

@Data
@RegisterForReflection
@Builder
public class TreeInfo {

    private String name;

    private String path;

}
