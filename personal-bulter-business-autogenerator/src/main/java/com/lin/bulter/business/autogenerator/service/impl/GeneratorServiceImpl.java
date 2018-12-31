package com.lin.bulter.business.autogenerator.service.impl;

import com.google.common.io.Files;
import com.lin.bulter.business.autogenerator.GeneratorService;
import com.lin.bulter.common.dto.autogenerator.GenerateParam;
import com.lin.bulter.common.utils.JGitUtils;
import com.lin.bulter.common.utils.StringUtil;
import com.lin.bulter.common.utils.VelocityUtils;
import com.lin.bulter.common.utils.ZipUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    private static Logger logger = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    private static final String oldBasePackage = "com.sprucetec.product.base";
    @Value("${generate.oldGitTemplateBasePath}")
    private String oldGitTemplateBasePath;             // git模板下载目录
    @Value("${generate.newProjectCodeBasePath}")
    private String newProjectCodeBasePath;             // 代码生成目录
    @Value("${generate.zipBasePath}")
    private String zipBasePath;                        // zip文件上传web服务器目录

    @Override
    public String generatorProject(GenerateParam param) {
        return generate(param);
    }

    /**
     * 自动生成
     */
    private String generate(GenerateParam param) {
        String newProjectName = param.getProjectName();
        String gitRepository = param.getGitRepository();
        String branchName = param.getBranchName();
        // 下载github上的工程模板
        JGitUtils.cloneGitTemplate(gitRepository, oldGitTemplateBasePath, branchName);
        logger.info(">>>>>>>>git工程下载完毕, 下载地址: {}", oldGitTemplateBasePath);
        // 如果新工程已经存在，删除
        String newFilePath = newProjectCodeBasePath + File.separator + newProjectName;
        File newFile = new File(newFilePath);
        if (newFile.exists()) {
            JGitUtils.delFolder(newFilePath);
        }
        // 重新生成新工程
        compileAndCloneGitProject(param, new File(oldGitTemplateBasePath));
        logger.info(">>>>>>>>自动生成完毕, 生成地址: {}", newProjectCodeBasePath);
        // 压缩zip
        String zipFileName = DigestUtils.md5DigestAsHex((newProjectName + System.currentTimeMillis()).getBytes()) + ".zip";
        String zipPath = zipBasePath + File.separator + zipFileName;
        File file = new File(zipPath);
        try {
            FileOutputStream fos1 = new FileOutputStream(file);
            ZipUtils.toZip(newFilePath, fos1, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.info(">>>>>>>>代码zip文件生成完毕, 生成地址: {}", zipPath);
        // 压缩完毕后，删除新生成的文件
        JGitUtils.delFolder(newProjectCodeBasePath);
        logger.info(">>>>>>>>删除代码文件, 文件地址: {}", newProjectCodeBasePath);
        return zipFileName;
    }

    // 编译并克隆git
    private List<String> compileAndCloneGitProject(GenerateParam param, File file) {
        VelocityUtils instance = VelocityUtils.getInstance(oldGitTemplateBasePath);
        VelocityContext context = new VelocityContext();
        context.put("rootGroupId", param.getRootGroupId());
        context.put("rootArtifactId", param.getRootArtifactId());
        context.put("rootVersion", param.getVersion());
        context.put("projectName", param.getProjectName());
        context.put("basePackage", param.getBasePackage());
        List<String> resultfiles = new ArrayList<>();
        if (!file.exists()) {
            return null;
        }
        File[] fileList = file.listFiles();
        if (fileList != null) {
            File newWorkspaceDirectory = new File(newProjectCodeBasePath);
            if (!newWorkspaceDirectory.exists()) {
                newWorkspaceDirectory.mkdirs();
            }
            for (File tempFile : fileList) {
                if (tempFile.isDirectory()) {      // 文件夹
                    if (tempFile.getName().equals(".git") || tempFile.getName().equals(".idea")) {
                        continue;
                    }
                    String newFilePath = getNewFilePath(param, tempFile.getAbsolutePath(), true);
                    if(newFilePath!=null){
                        // 新建文件夹
                        File newFileDirectory = new File(newFilePath);
                        if (!newFileDirectory.exists()) {
                            newFileDirectory.mkdirs();
                        }
                    }
                    compileAndCloneGitProject(param, tempFile);
                } else if (tempFile.isFile()) {    // 文件
                    String oldFilePath = tempFile.getAbsolutePath();
                    String newFilePath = getNewFilePath(param, oldFilePath, false);
                    String newContent = instance.compileVelocityFile(getGitProjectPath(oldFilePath), context);
                    try {
                        String[] arr = newFilePath.split("/");
                        String[] newArr = Arrays.copyOf(arr, arr.length - 1);
                        StringBuilder sb = new StringBuilder();
                        for(String a : newArr){
                            sb.append(a);
                            sb.append("/");
                        }
                        File tmpFile = new File(sb.toString());
                        if (!tmpFile.exists()) {
                            tmpFile.mkdirs();
                        }
                        Files.write(newContent.getBytes(), new File(newFilePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resultfiles;
    }

    /**
     * 获得新工程的名称
     *
     * @param oldFilePath
     * @return
     */
    public String getNewFilePath(GenerateParam param, String oldFilePath, boolean isDirectory) {
        String newBasePackage = param.getBasePackage();
        VelocityUtils instance = VelocityUtils.getInstance(oldGitTemplateBasePath);
        VelocityContext context = new VelocityContext();
        context.put("rootGroupId", param.getRootGroupId());
        context.put("rootArtifactId", param.getRootArtifactId());
        context.put("rootVersion", param.getVersion());
        context.put("projectName", param.getProjectName());
        context.put("basePackage", param.getBasePackage());

        StringBuilder newFileBuild = new StringBuilder();
        if (File.separator.equals("\\")) {
            oldFilePath = oldFilePath.replaceAll("\\\\", "/");
        }
        String oldBasePath2 = oldGitTemplateBasePath.replaceAll("/", "@");
        String newBasePath2 = newProjectCodeBasePath.replaceAll("/", "@");
        String oldBasePackage2 = oldBasePackage.replaceAll("\\.", "@");
        String newBasePackage2 = newBasePackage.replaceAll("\\.", "@");

        String newfilePath = oldFilePath.replaceAll("/", "@")
                .replaceAll(oldBasePath2, newBasePath2)
                .replaceAll(oldBasePackage2, newBasePackage2)
                .replaceAll("@", "/");

        if(isDirectory){
            String[] oldPackageArr = oldBasePackage.split("\\.");
            boolean flag = false;
            for(String pack : oldPackageArr) {
                if(newfilePath.endsWith(pack)){
                    flag = true;
                    break;
                }
            }
            String tempOldBasePackage = oldBasePackage.replaceAll("\\.","/");
            if(flag && !oldFilePath.contains(tempOldBasePackage)){
               return null;
            }
        }

        return instance.compileVelocityString(newfilePath, context);
    }

    /**
     * 获得老git工程
     *
     * @param oldFilePath
     * @return
     */
    private String getGitProjectPath(String oldFilePath) {
        StringBuilder newFileBuild = new StringBuilder();
        if (File.separator.equals("\\")) {
            oldFilePath = oldFilePath.replaceAll("\\\\", "/");
        }
        String oldBasePath2 = oldGitTemplateBasePath.replaceAll("/", "@");

        String gitProjectPath = oldFilePath.replaceAll("/", "@")
                .replaceAll(oldBasePath2, "")
                .replaceAll("@", "/");

        return gitProjectPath.substring(1);
    }
}
