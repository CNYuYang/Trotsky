package run.yuyang.trotsky.model.conf;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import run.yuyang.trotsky.model.param.MDParam;

/**
 * @author YuYang
 */
@Data
@RegisterForReflection
public class NoteConf {

    private String name;

    private String father;

    private String path;

    private Integer id;

    //notes.md深度为0、归纳的展示页面与归纳的深度相同(1-3)、归纳的笔记深度等级为 2-4
    private Integer depth;

    // 0-文章 1-带展示页面的归类的展示页面
    private Integer type;

    //若上级归纳有展示页面，则可以不展示，而
    private Boolean show;

    public NoteConf() {

    }

    public static NoteConf map(MDParam param, String fatherDir, Integer id, Integer depth) {
        NoteConf noteConf = new NoteConf();
        noteConf.setFather(param.getFather());
        noteConf.setShow(param.getShow());
        noteConf.setPath(fatherDir + "/" + param.getName());
        noteConf.setName(param.getName());
        noteConf.setId(id);
        noteConf.setDepth(depth);
        noteConf.setType(0);
        return noteConf;
    }

}
