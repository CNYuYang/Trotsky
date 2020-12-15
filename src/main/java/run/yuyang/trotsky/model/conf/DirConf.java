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
public class DirConf {

    private String name;

    private String father;

    private String path;

    private int note_nums;

    private int dir_nums;

    //notes.md深度为0、归纳的展示页面与归纳的深度相同(1-3)、归纳的笔记深度等级为 2-4
    private int depth;

    // 0-带展示页面的归类的展示页面 1-不带展示页面的归类的展示页面
    private boolean have_intro;

    public void addNote() {
        note_nums++;
    }

    public void delNote() {
        note_nums--;
    }

    public void addDir() {
        dir_nums++;
    }

    public void delDir() {
        dir_nums--;
    }
}
