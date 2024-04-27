package org.zpd.jfxcommon.model;

import org.zpd.jfxcommon.JFXFunction;

/**
 * Created by zhb on 16/10/2.
 */
public class Project {
    private final String name;
    private final String basePackage;
    private final FuctionTree fuctionTree;
    private WelcomePage welcomePage;

    public Project(String name, String basePackage) {
        this.name = name;
        this.basePackage = basePackage;
        this.fuctionTree = new FuctionTree(new EmptyFunction(name));
    }

    public void addFunction(String packagePath, JFXFunction function) {
        String packagesWithoutBase = "";

        try {
            if(!this.basePackage.equals(packagePath)) {
                packagesWithoutBase = packagePath.substring(this.basePackage.length() + 1);
            }
        } catch (StringIndexOutOfBoundsException var7) {
            System.out.println("packagePath: " + packagePath + ", basePackage: " + this.basePackage);
            var7.printStackTrace();
            return;
        }

        String[] packages = packagesWithoutBase.isEmpty()?new String[0]:packagesWithoutBase.split("\\.");

        for(int i = 0; i < packages.length; ++i) {
            String packageName = packages[i];
            if(!packageName.isEmpty()) {
                packageName = packageName.substring(0, 1).toUpperCase() + packageName.substring(1);
                packageName = packageName.replace("_", " ");
                packages[i] = packageName;
            }
        }

        this.fuctionTree.addFunction(packages, function);
    }

    public FuctionTree getFuctionTree() {
        return this.fuctionTree;
    }

    public void setWelcomePage(WelcomePage welcomePage) {
        if(null != welcomePage) {
            this.welcomePage = welcomePage;
        }

    }

    public WelcomePage getWelcomePage() {
        return this.welcomePage;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Project [ name: ");
        sb.append(this.name);
        sb.append(", function count: ");
        sb.append(this.fuctionTree.size());
        sb.append(", tree: ");
        sb.append(this.fuctionTree);
        sb.append(" ]");
        return sb.toString();
    }
}
