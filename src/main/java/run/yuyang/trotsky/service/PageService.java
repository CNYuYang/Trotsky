package run.yuyang.trotsky.service;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import run.yuyang.trotsky.model.conf.DirConf;
import run.yuyang.trotsky.model.conf.NoteConf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author YuYang
 */
@ApplicationScoped
public class PageService {

    @Inject
    ConfService confService;

    @Inject
    FileService fileService;

    @Inject
    Vertx vertx;

    /**
     * 更新Notes页面
     */
    public String updateNotesPage() {

        Map<String, List<DirConf>> dirs = new HashMap<>();
        confService.getNoteDirs().forEach((k, v) -> {
            if (v.getDepth() > 0) {
                if (!dirs.containsKey(v.getFather())) {
                    dirs.put(v.getFather(), new LinkedList<>());
                }
                dirs.get(v.getFather()).add(v);
            }
        });
        Map<String, List<NoteConf>> notes = new HashMap<>();
        confService.getNoteConfs().forEach((k, v) -> {
            if (v.getType() == 0 && v.getShow()) {
                if (!notes.containsKey(v.getFather())) {
                    notes.put(v.getFather(), new LinkedList<>());
                }
                notes.get(v.getFather()).add(v);
            }
        });
        boolean[] flag = new boolean[]{true, true};
        StringBuilder builder = getTree("notes", 0, dirs, notes, flag);

        return builder.substring(2);
    }


    //markdown中的空格&#160;
    private StringBuilder getTree(String name, int depth, Map<String, List<DirConf>> dirs, Map<String, List<NoteConf>> notes, boolean[] flag) {
        StringBuilder builder = new StringBuilder("");
        if (!dirs.containsKey(name)) {
            return builder;
        }
        int count = dirs.get(name).size();
        int process = 0;
        while (process != count) {
            //第一层
            DirConf nowDir = dirs.get(name).get(process);
            boolean isEnd = (process + 1) == count;
            int tempDepth = depth;
            StringBuilder newLine = new StringBuilder("");
            while (tempDepth != 0) {
                if (flag[depth - tempDepth]) {
                    builder.append("|");
                    newLine.append("|");
                } else {
                    builder.append("&#160;");
                    newLine.append("&#160;");
                }
                builder.append("&#160;&#160;&#160;&#160;");
                newLine.append("&#160;&#160;&#160;&#160;");
                tempDepth--;
            }

            if (isEnd) {
                builder.append("|").append("\n\n").append(newLine).append("└── ");
            } else {
                builder.append("|").append("\n\n").append(newLine).append("├── ");
            }
            if (nowDir.getType() == 1) {
                builder.append(nowDir.getName());
            } else {
                builder.append("[").append(nowDir.getName()).append("](").append(nowDir.getPath()).append(".md)");
            }
            if (isEnd && depth < 2) {
                flag[depth] = false;
            }
            builder.append("\n\n");
            builder.append(getNotes(depth + 1, notes.get(nowDir.getName()), flag, !dirs.containsKey(nowDir.getName())));
            StringBuilder append = getTree(nowDir.getName(), depth + 1, dirs, notes, flag);
            builder.append(append);
            process++;
            if (isEnd && depth < 2) {
                flag[depth] = true;
            }
        }
        return builder;
    }


    private StringBuilder getNotes(int depth, List<NoteConf> list, boolean[] flag, boolean isFatherEnd) {
        StringBuilder builder = new StringBuilder("");
        if (null == list || list.size() == 0) {
            return builder;
        }
        int count = list.size();
        int process = 0;
        while (count != process) {
            boolean isEnd = (process + 1) == count;
            int tempDepth = depth;
            StringBuilder newLine = new StringBuilder("");
            while (tempDepth != 0) {
                if (flag[depth - tempDepth]) {
                    builder.append("|");
                    newLine.append("|");
                } else {
                    builder.append("&#160;");
                    newLine.append("&#160;");
                }
                builder.append("&#160;&#160;&#160;&#160;");
                newLine.append("&#160;&#160;&#160;&#160;");
                tempDepth--;
            }
            if (isEnd && isFatherEnd) {
                builder.append("|").append("\n\n").append(newLine).append("└── ");
            } else {
                builder.append("|").append("\n\n").append(newLine).append("├── ");
            }
            builder.append("[").append(list.get(process).getName().replace(".md", "")).append("](").append(list.get(process).getPath()).append(")");
            builder.append("\n\n");
            process++;
        }
        return builder;
    }

    public void updateCoverPage() {
        String template = fileService.readFile("template/_coverpage.md");
        template = template.replace("{{description}}", confService.getIndexConf().getDescription());
        StringBuilder builder = new StringBuilder();
        confService.getIndexConf().getLinks().forEach(obj -> {
            builder.append("[").append(obj.getKey()).append("]").append("(").append(obj.getValue()).append(") ");
        });
        template = template.replace("{{links}}", builder.toString());
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + "/_coverpage.md", Buffer.buffer(template));
    }

    public void updateIndexPage() {
        String template = fileService.readFile("template/index.html");
        template = template.replace("{{title}}", confService.getIndexConf().getTitle());
        StringBuilder builder = new StringBuilder();
        confService.getIndexConf().getNavs().forEach(obj -> {
            //<a href="#/notes">笔记</a>
            builder.append("<a href=\"").append(obj.getValue()).append("\">").append(obj.getKey()).append("</a>");
        });
        template = template.replace("{{navs}}", builder.toString());
        vertx.fileSystem().writeFileBlocking(confService.getWorkerPath() + "/index.html", Buffer.buffer(template));
    }


    public Map autoGeneratedConf() {
        Map<String, List> map = new HashMap<>();
        map.put("note", new LinkedList<NoteConf>());
        map.put("dir", new LinkedList<DirConf>());
        LinkedList<File> tempDir = new LinkedList<>();
        LinkedList<Integer> depthList = new LinkedList<>();
        String path = confService.getWorkerPath();
        tempDir.add(new File(path + "/notes"));
        depthList.add(0);
        int dirCount = 0;
        int noteCount = 0;
        while (!tempDir.isEmpty()) {
            File file = tempDir.poll();
            File[] files = file.listFiles();
            int depth = depthList.poll();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    tempDir.add(files[i]);
                    depthList.add(depth + 1);
                    DirConf dirConf = new DirConf();
                    dirConf.setDepth(depth + 1);
                    dirConf.setFather(file.getName());
                    dirConf.setId(dirCount++);
                    dirConf.setName(files[i].getName());
                    dirConf.setPath(files[i].getAbsolutePath().replace(path, ""));
                    dirConf.setType(0);
                    map.get("dir").add(dirConf);
                } else {
                    if (files[i].getName().matches(".*.md$")) {
                        NoteConf noteConf = new NoteConf();
                        noteConf.setDepth(depth + 1);
                        noteConf.setType(0);
                        noteConf.setId(noteCount++);
                        noteConf.setFather(file.getName());
                        noteConf.setName(files[i].getName());
                        noteConf.setShow(true);
                        noteConf.setPath(files[i].getAbsolutePath().replace(path, ""));
                        map.get("note").add(noteConf);
                    }
                }
            }
        }
        return map;
    }

}
