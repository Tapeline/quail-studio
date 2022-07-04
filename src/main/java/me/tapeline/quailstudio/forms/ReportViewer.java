package me.tapeline.quailstudio.forms;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportViewer extends JFrame {

    private JPanel root;
    private JTree tree1;
    private JList list1;
    public JSONObject nodeReport;
    public JSONArray memoryReport;
    public DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("code");

    public ReportViewer(File report) throws IOException {
        super("Quail::Studio Report Viewer");

        String jsonString = FileUtils.readFileToString(report, "UTF-8");
        JSONObject rootJson = new JSONObject(jsonString);
        nodeReport = rootJson.getJSONObject("nodeReport");
        memoryReport = rootJson.getJSONArray("memoryReport");
        topNode = traverseJson("Nodes", nodeReport);
        tree1.setModel(new DefaultTreeModel(topNode));

        setSize(800, 800);
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        List<String> mem = new ArrayList<>();
        for (Object o : memoryReport) {
            JSONObject oo = ((JSONObject) o);
            mem.add("[" + oo.getString("scope") + "] " + oo.getString("var") +
                    " = " + oo.getString("new") + " (before: " + oo.getString("old") + ")");
        }
        list1.setListData(mem.toArray(new String[1]));
        setVisible(true);
    }

    private DefaultMutableTreeNode traverseJson(String k, Object object) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(k);
        if (object instanceof JSONObject) {
            for (String key : ((JSONObject) object).keySet()) {
                node.add(traverseJson(key, ((JSONObject) object).get(key)));
            }
        } else if (object instanceof JSONArray) {
            for (int i = 0; i < ((JSONArray) object).length(); i++) {
                node.add(traverseJson("" + i, ((JSONArray) object).get(i)));
            }
        } else {
            node.add(new DefaultMutableTreeNode(object));
        }
        return node;
    }
}
