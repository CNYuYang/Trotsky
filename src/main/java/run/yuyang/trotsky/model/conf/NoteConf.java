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
public class NoteConf {

    private String name;

    private String father;

    private String path;

    //notes.md深度为0、归纳的展示页面与归纳的深度相同(1-3)、归纳的笔记深度等级为 2-4
    private Integer depth;

    //若上级分类有展示页面，则可以不展
    private Boolean show;

}
