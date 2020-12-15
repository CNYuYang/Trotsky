package run.yuyang.trotsky.model.conf;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * @author YuYang
 */
@Data
@RegisterForReflection
public class DirConf {

    private String name;

    private String father;

    private String path;

    private int id;

    private int note_nums;

    private int dir_nums;

    //notes.md深度为0、归纳的展示页面与归纳的深度相同(1-3)、归纳的笔记深度等级为 2-4
    private int depth;

    // 0-带展示页面的归类的展示页面 1-不带展示页面的归类的展示页面
    private int type;


    public DirConf() {

    }

    public static DirConf defaultConf() {
        DirConf dirConf = new DirConf();
        dirConf.setType(1);
        return dirConf;
    }

}
