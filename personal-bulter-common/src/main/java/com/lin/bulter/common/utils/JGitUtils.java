package com.lin.bulter.common.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class JGitUtils {

    public static Git git;

    public static void cloneGitTemplate(String gitRepository, String gitBasePath, String branchName) {
        File gitFile = new File(gitBasePath);
        try {
//            if (gitFile.exists() && gitFile.listFiles()!=null && gitFile.listFiles().length>0) {
//                git = Git.open(gitFile);
//            } else {
//                git = Git.cloneRepository().setCredentialsProvider(credentialsProvider).setURI(gitRepository).setDirectory(gitFile).call();
//            }
            if(!gitFile.exists() || gitFile.listFiles()==null || gitFile.listFiles().length==0){
                CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("chenglin198637@126.com", "lin8637aa");
                git = Git.cloneRepository().setCredentialsProvider(credentialsProvider).setURI(gitRepository).setDirectory(gitFile).call();
                ListBranchCommand listBranchCommand = git.branchList();
                String branch = listBranchCommand.getRepository().getBranch();
                if (branchName!=null && !"master".equals(branchName) && branch != null && !branch.equals(branchName)) {
                    git.checkout().setCreateBranch(true).setName(branchName).call();
                }
                git.pull().setRemote("origin").setCredentialsProvider(credentialsProvider).call();
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        JGitUtils.cloneGitTemplate("https://github.com/linners/springboot-multi-template-web-dubbo.git","d:/tmp3", "master");
    }
}
