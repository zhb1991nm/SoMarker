package org.zpd.jfxcommon.model;

import javafx.scene.control.TreeItem;
import org.zpd.jfxcommon.JFXFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhb on 16/10/2.
 */
public class FuctionTree {

    private TreeNode root;
    private int count = 0;

    public FuctionTree(JFXFunction rootFunction) {
        this.root = new TreeNode(null, null, rootFunction);
    }

    public TreeNode getRoot() {
        return this.root;
    }

    public Object size() {
        return Integer.valueOf(this.count);
    }

    public void addFunction(String[] packages, JFXFunction function) {
        if (packages.length == 0) {
            this.root.addFunction(function);
        } else {
            TreeNode n = this.root;
            String[] var4 = packages;
            int var5 = packages.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String packageName = var4[var6];
                if (n.containsChild(packageName)) {
                    n = n.getChild(packageName);
                } else {
                    FuctionTree.TreeNode newNode = new TreeNode(packageName);
                    n.addNode(newNode);
                    n = newNode;
                }
            }

            if (n.packageName.equals(packages[packages.length - 1])) {
                n.addFunction(function);
                ++this.count;
            }
        }
    }

    public String toString() {
        return this.root.toString();
    }

    public static class TreeNode {
        private final JFXFunction funtion;
        private final String packageName;
        private final TreeNode parent;
        private List<TreeNode> children;

        public TreeNode() {
            this(null, null, null);
        }

        public TreeNode(String packageName) {
            this(null, packageName, null);
        }

        public TreeNode(TreeNode parent, String packageName, JFXFunction funtion) {
            this.children = new ArrayList<>();
            this.packageName = packageName;
            this.parent = parent;
            this.funtion = funtion;
        }

        public boolean containsChild(String packageName) {
            if (packageName != null) {
                return false;
            } else {
                Iterator<TreeNode> var2 = this.children.iterator();
                TreeNode n;
                do {
                    if (!var2.hasNext()) {
                        return false;
                    }
                    n = var2.next();
                } while (!packageName.equals(n.packageName));
                return true;
            }
        }

        public TreeNode getChild(String packageName) {
            if (packageName == null) {
                return null;
            } else {
                Iterator<TreeNode> var2 = this.children.iterator();
                TreeNode n;
                do {
                    if (!var2.hasNext()) {
                        return null;
                    }
                    n = var2.next();
                } while (!packageName.equals(n.packageName));
                return n;
            }
        }

        public void addFunction(JFXFunction function) {
            this.children.add(new TreeNode(this, null, function));
        }

        public void addNode(TreeNode n) {
            this.children.add(n);
        }

        public JFXFunction getFunction() {
            return this.funtion;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public TreeItem<JFXFunction> createTreeItem() {
            TreeItem treeItem = null;
            if (this.funtion != null) {
                treeItem = new TreeItem(this.funtion);
            } else {
                treeItem = new TreeItem(new EmptyFunction(this.packageName));
            }

            treeItem.setExpanded(true);
            Iterator<TreeNode> var2 = this.children.iterator();

            while (var2.hasNext()) {
                TreeNode n = var2.next();
                treeItem.getChildren().add(n.createTreeItem());
            }
            return treeItem;
        }

        public String toString() {
            return this.funtion != null?" Function [ functionName: " + this.funtion.getFunctionName() + ", children: " + this.children + " ]":" Function [ packageName: " + this.packageName + ", children: " + this.children + " ]";
        }
    }
}
