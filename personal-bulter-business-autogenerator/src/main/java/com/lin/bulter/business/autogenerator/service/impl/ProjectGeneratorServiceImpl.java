package com.lin.bulter.business.autogenerator.service.impl;

import com.google.common.io.Files;
import com.lin.bulter.common.dto.autogenerator.GenerateParam;
import com.lin.bulter.common.dto.autogenerator.GenerateProjectExtend;
import com.lin.bulter.common.utils.BeanUtil;
import com.lin.bulter.common.utils.JGitUtils;
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

/**
 * 工程自动生成
 */
@Service
public class ProjectGeneratorServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(ProjectGeneratorServiceImpl.class);

//    private static final String oldBasePackage = "com.sprucetec.product.base";
//    private static final String oldBasePackage = "com.lin.bulter";

    @Value("${generate.oldGitTemplateBasePath}")
    private String oldGitTemplateBasePath;             // git模板下载目录

    @Value("${generate.newProjectCodeBasePath}")
    private String newProjectCodeBasePath;             // 代码生成目录

    @Value("${generate.zipBasePath}")
    private String zipBasePath;                        // zip文件上传web服务器目录

    /**
     * 自动生成工程骨架
     * @param param
     * @return
     */
    public String generatorProject(GenerateParam param) {
        // 组装参数
        GenerateProjectExtend generateParamExtend = initProjectExtendInfo(param);
        String gitRepository = generateParamExtend.getGitRepository();
        String gitProjectPath = generateParamExtend.getGitProjectPath();
        String generatedProjectPath = generateParamExtend.getGeneratedProjectPath();
        String newProjectName = generateParamExtend.getProjectName();

        // 下载github上的工程模板
        String branchName = param.getBranchName();
        JGitUtils.cloneGitTemplate(gitRepository, gitProjectPath, branchName);
        logger.info(">>>>>>>>git工程下载完毕, 下载地址: {}", gitProjectPath);

        // 重新生成新工程
        compileAndCloneGitProject(generateParamExtend, new File(gitProjectPath));
        logger.info(">>>>>>>>自动生成完毕, 生成地址: {}", generatedProjectPath);

        // 压缩zip
        String zipFileName = DigestUtils.md5DigestAsHex((newProjectName + System.currentTimeMillis()).getBytes()) + ".zip";
        String zipPath = zipBasePath + File.separator + zipFileName;
        File file = new File(zipPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ZipUtils.toZip(generatedProjectPath, fos, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.info(">>>>>>>>代码zip文件生成完毕, 生成地址: {}", zipPath);

        // 压缩完毕后，删除新生成的文件
        JGitUtils.delFolder(generatedProjectPath);
        logger.info(">>>>>>>>删除代码文件, 文件地址: {}", generatedProjectPath);
        return zipFileName;
    }

    // 初始化参数
    private GenerateProjectExtend initProjectExtendInfo(GenerateParam param) {
        GenerateProjectExtend result = BeanUtil.copy(param, GenerateProjectExtend::new);

        String newProjectName = param.getProjectName();
        String gitRepository = param.getGitRepository();
        // git工程模板名字
        String[] githubArr = gitRepository.replaceAll("https://github.com/","")
                .replaceAll(".git", "").split("/");
        StringBuilder sb = new StringBuilder();
        sb.append("github");
        for(String str : githubArr){
            sb.append("-").append(str);
        }
        String gitTemplateName = sb.toString();
        // git工程文件路径
        String oldGitProjectPath = oldGitTemplateBasePath + File.separator + gitTemplateName;
        if (File.separator.equals("\\")) {
            oldGitProjectPath = oldGitProjectPath.replaceAll("\\\\", "/");
        }
        File oldGitProjectFile = new File(oldGitProjectPath);
        if (!oldGitProjectFile.exists()) {
            oldGitProjectFile.mkdirs();
        }

        // 组装velocityUtil实例 和 VelocityContext
        VelocityUtils velocityInstance = VelocityUtils.getInstance(oldGitProjectPath);
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("rootGroupId", param.getRootGroupId());
        velocityContext.put("rootArtifactId", param.getRootArtifactId());
        velocityContext.put("rootVersion", param.getVersion());
        velocityContext.put("projectName", param.getProjectName());
        velocityContext.put("basePackage", param.getBasePackage());

        // 要生成的project路径，若已经存在，删除
        String generatedProjectPath = newProjectCodeBasePath + File.separator + newProjectName;
        if (File.separator.equals("\\")) {
            generatedProjectPath = generatedProjectPath.replaceAll("\\\\", "/");
        }
        File generatedProjectFile = new File(generatedProjectPath);
        if (generatedProjectFile.exists()) {
            JGitUtils.delFolder(generatedProjectPath);
        }

        // 组装结果返回

        result.setGitProjectPath(oldGitProjectPath);
        result.setInstance(velocityInstance);
        result.setContext(velocityContext);
        result.setGeneratedProjectPath(generatedProjectPath);
        return result;
    }

    // 编译并克隆git
    private List<String> compileAndCloneGitProject(GenerateProjectExtend generateParamExtend, File gitProjectFile) {
        List<String> resultfiles = new ArrayList<>();
        VelocityUtils velocityInstance = generateParamExtend.getInstance();
        VelocityContext velocityContext = generateParamExtend.getContext();
        // git工程文件
        if (!gitProjectFile.exists()) {
            return null;
        }
        File[] fileList = gitProjectFile.listFiles();
        if (fileList != null) {
            for (File tempFile : fileList) {
                if (tempFile.isDirectory()) {      // 文件夹
                    if (tempFile.getName().equals(".git") || tempFile.getName().equals(".idea")) {
                        continue;
                    }
                    String newFilePath = getNewFilePath(generateParamExtend, tempFile.getAbsolutePath(), true);
                    if(newFilePath!=null){
                        // 新建文件夹
                        File newFileDirectory = new File(newFilePath);
                        if (!newFileDirectory.exists()) {
                            newFileDirectory.mkdirs();
                        }
                    }
                    compileAndCloneGitProject(generateParamExtend, tempFile);
                } else if (tempFile.isFile()) {    // 文件
                    String oldFilePath = tempFile.getAbsolutePath();
                    String newFilePath = getNewFilePath(generateParamExtend, oldFilePath, false);
                    String newContent = velocityInstance.compileVelocityFile(getGitProjectPath(generateParamExtend.getGitProjectPath(), oldFilePath), velocityContext);
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
     * 获得新工程的文件目录
     *
     * @param oldFilePath
     * @return
     */
    private String getNewFilePath(GenerateProjectExtend generateParamExtend, String oldFilePath, boolean isDirectory) {
        VelocityUtils velocityInstance = generateParamExtend.getInstance();
        VelocityContext velocityContext = generateParamExtend.getContext();
        String gitProjectPath = generateParamExtend.getGitProjectPath();
        String newBasePackage = generateParamExtend.getBasePackage();
        String oldBasePackage = generateParamExtend.getOldBasePackage();

        StringBuilder newFileBuild = new StringBuilder();
        if (File.separator.equals("\\")) {
            oldFilePath = oldFilePath.replaceAll("\\\\", "/");
        }
        String oldBasePath2 = gitProjectPath.replaceAll("/", "@");
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

        return velocityInstance.compileVelocityString(newfilePath, velocityContext);
    }

    /**
     * 获得老git工程
     *
     * @param oldFilePath
     * @return
     */
    private String getGitProjectPath(String baseFilePath, String oldFilePath) {
        StringBuilder newFileBuild = new StringBuilder();
        if (File.separator.equals("\\")) {
            oldFilePath = oldFilePath.replaceAll("\\\\", "/");
        }
        String oldBasePath2 = baseFilePath.replaceAll("/", "@");

        String gitProjectPath = oldFilePath.replaceAll("/", "@")
                .replaceAll(oldBasePath2, "")
                .replaceAll("@", "/");

        return gitProjectPath.substring(1);
    }
}
