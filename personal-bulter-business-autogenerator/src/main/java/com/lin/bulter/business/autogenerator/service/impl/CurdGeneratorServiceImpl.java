package com.lin.bulter.business.autogenerator.service.impl;

import com.google.common.io.Files;
import com.lin.bulter.business.autogenerator.service.DatasourceInfoService;
import com.lin.bulter.business.autogenerator.utils.StringUtil;
import com.lin.bulter.common.dto.autogenerator.CurdEntityInfo;
import com.lin.bulter.common.dto.autogenerator.CurdParam;
import com.lin.bulter.common.dto.autogenerator.CurdParamExtend;
import com.lin.bulter.common.dto.datasourceinfo.TableColumnInfo;
import com.lin.bulter.common.utils.BeanUtil;
import com.lin.bulter.common.utils.JGitUtils;
import com.lin.bulter.common.utils.VelocityUtils;
import com.lin.bulter.common.utils.ZipUtils;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 增删改查自动生成
 */
@Service
public class CurdGeneratorServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(CurdGeneratorServiceImpl.class);

    @Value("${generate.oldGitTemplateBasePath}")
    private String oldGitTemplateBasePath;             // git模板下载目录

    @Value("${generate.newProjectCodeBasePath}")
    private String newProjectCodeBasePath;             // 代码生成目录

    @Value("${generate.zipBasePath}")
    private String zipBasePath;                        // zip文件上传web服务器目录

    @Autowired
    private DatasourceInfoService datasourceService;

    /**
     * 自动生成增删改查逻辑代码
     * @param param
     * @return
     */
    public String generatorCrud(CurdParam param) {
        // 初始化参数
        List<CurdParamExtend> curdParamExtends = initCurdParamExtendInfo(param);
        String gitRepository = curdParamExtends.get(0).getGitRepository();
        String gitProjectPath = curdParamExtends.get(0).getGitProjectPath();
        String branchName = curdParamExtends.get(0).getBranchName();
        String generatedCurdCodePath = curdParamExtends.get(0).getGeneratedCurdCodePath();

        // 下载github上的工程模板
        JGitUtils.cloneGitTemplate(gitRepository, gitProjectPath, branchName);
        logger.info(">>>>>>>>git工程下载完毕, 下载地址: {}", gitProjectPath);

        // 编译并生成文件
        for(CurdParamExtend curdParamExtend : curdParamExtends){
            compileAndGenerateCurdFile(curdParamExtend, new File(gitProjectPath));
        }
        logger.info(">>>>>>>>代码生成完毕，生成目录: {}", generatedCurdCodePath);

        // 压缩zip
        String zipFileName = DigestUtils.md5DigestAsHex((param.getParamPackage() + "curd" + System.currentTimeMillis()).getBytes()) + ".zip";
        String zipPath = zipBasePath + File.separator + zipFileName;
        File file = new File(zipPath);
        try {
            FileOutputStream fos1 = new FileOutputStream(file);
            ZipUtils.toZip(generatedCurdCodePath, fos1, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.info(">>>>>>>>代码zip文件生成完毕, 生成地址: {}", zipPath);

        // 压缩完毕后，删除新生成的文件
        JGitUtils.delFolder(generatedCurdCodePath);
        logger.info(">>>>>>>>删除代码文件, 文件地址: {}", generatedCurdCodePath);
        return zipFileName;
    }

    // 初始化参数
    private List<CurdParamExtend> initCurdParamExtendInfo(CurdParam param) {
        List<CurdParamExtend> resultList = new ArrayList<>();

        String newProjectName = param.getProjectName() + "_curd_code";
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
        // 要生成的project路径，若已经存在，删除
        String generatedCurdCodePath = newProjectCodeBasePath + File.separator + newProjectName;
        if (File.separator.equals("\\")) {
            generatedCurdCodePath = generatedCurdCodePath.replaceAll("\\\\", "/");
        }
        File generatedProjectFile = new File(generatedCurdCodePath);
        if (generatedProjectFile.exists()) {
            JGitUtils.delFolder(generatedCurdCodePath);
        }

        List<CurdEntityInfo> dbConfigs = param.getDbConfig();
        for(CurdEntityInfo curdEntityInfo : dbConfigs) {
            CurdParamExtend result = BeanUtil.copy(param, CurdParamExtend::new);
            // 组装velocityUtil实例 和 VelocityContext
            VelocityUtils velocityInstance = VelocityUtils.getInstance(oldGitProjectPath);
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("controllerPackage", param.getControllerPackage());
            velocityContext.put("servicePackage", param.getServicePackage());
            velocityContext.put("entityPackage", param.getEntityPackage());
            velocityContext.put("paramPackage", param.getParamPackage());
            velocityContext.put("mapperPackage", param.getMapperPackage());

    //        velocityContext.put("entityNameUp", param.getEntityName());
    //        velocityContext.put("entityName", StringUtil.lowerFirst(param.getEntityName()));
    //        velocityContext.put("tableName", param.getTableName());
    //        List<TableColumnInfo> tableColumns = datasourceService.getAllTableColumnsByTableName(param.getDatasourceId(), param.getDbName(), param.getTableName());
    //        velocityContext.put("columns", tableColumns);

            velocityContext.put("entityNameUp", curdEntityInfo.getEntityName());
            velocityContext.put("entityName", StringUtil.lowerFirst(curdEntityInfo.getEntityName()));
            velocityContext.put("tableName", curdEntityInfo.getTableName());
            List<TableColumnInfo> tableColumns = curdEntityInfo.getTableColumns();
            for(TableColumnInfo column : tableColumns) {
                column.setFieldName(StringUtil.camelName(column.getColumnName()));
                column.setFieldType(StringUtil.sqlType2JavaType(column.getDataType()));
            }
            velocityContext.put("columns", tableColumns);
            // 组装结果返回
            result.setGitProjectPath(oldGitProjectPath);
            result.setInstance(velocityInstance);
            result.setContext(velocityContext);
            result.setGeneratedCurdCodePath(generatedCurdCodePath);
            resultList.add(result);
        }
        return resultList;
    }

    // 编译并生成文件
    private void compileAndGenerateCurdFile(CurdParamExtend param, File file) {
        VelocityContext context = param.getContext();
        File[] fileList = file.listFiles();
        for (File tempFile : fileList) {
            if (tempFile.isDirectory()) {      // 文件夹
                if (tempFile.getName().equals(".git") || tempFile.getName().equals(".idea")) {
                    continue;
                }
                compileAndGenerateCurdFile(param, tempFile);
            }else if (tempFile.isFile()) {    // 文件
                String tempFileAbsolutePath = tempFile.getAbsolutePath();
                // controller层代码
                if(param.isController() && tempFileAbsolutePath.endsWith("Controller.java")){
                    generateControllerCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{}代码生成完毕！", context.get("entityNameUp")+"Controller");
                }
                // service层代码
                if(param.isService() && tempFileAbsolutePath.endsWith("Service.java")){
                    generateServiceCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{}代码生成完毕！",  context.get("entityNameUp")+"Service");
                }
                // service impl层代码
                if(param.isService() && tempFileAbsolutePath.endsWith("ServiceImpl.java")){
                    generateServiceImplCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{}代码生成完毕！",  context.get("entityNameUp")+"Service");
                }
                // param代码
                if(param.isMapper() && tempFileAbsolutePath.endsWith("Param.java")){
                    generateParamCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{}参数代码生成完毕！",  context.get("entityNameUp")+"Param");
                }
                // entity代码
                if(param.isMapper() && tempFileAbsolutePath.endsWith("Entity.java")){
                    generateEntityCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{}代码生成完毕！",  context.get("entityNameUp")+"Entity");
                }
                // mapper层代码
                if(param.isMapper() && tempFileAbsolutePath.endsWith("Mapper.java")){
                    generateMapperCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{}层代码生成完毕！",  context.get("entityNameUp")+"Mapper");
                }
                // mapper xml代码
                if(param.isMapper() && tempFileAbsolutePath.endsWith("Mapper.xml")){
                    generateMapperXmlCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{} xml代码生成完毕！",  context.get("entityNameUp")+"Mapper");
                }
                // mapper xml代码
                if(param.isMapper() && tempFileAbsolutePath.endsWith("MapperExtend.xml")){
                    generateMapperXmlExtendCode(param, tempFileAbsolutePath);
                    logger.info(">>>>>>>>{} xml 代码生成完毕！",  context.get("entityNameUp")+"Mapper extend");
                }
            }
        }
    }

    // 生成controller层代码
    private void generateControllerCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String controllerPackage = param.getControllerPackage();
        String controllerPackFilePath = controllerPackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + controllerPackFilePath + "/" + context.get("entityNameUp") + "Controller.java";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    // 生成service层代码
    private void generateServiceCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String servicePackage = param.getServicePackage();
        String servicePackFilePath = servicePackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + servicePackFilePath + "/" + context.get("entityNameUp") + "Service.java";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    // 生成service impl层代码
    private void generateServiceImplCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String servicePackage = param.getServicePackage();
        String servicePackFilePath = servicePackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + servicePackFilePath + "/impl/" + context.get("entityNameUp") + "ServiceImpl.java";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    // 生成param代码
    private void generateParamCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String paramPackage = param.getParamPackage();
        String paramPackFilePath = paramPackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + paramPackFilePath + "/" + context.get("entityNameUp") + "Param.java";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    // 生成entity代码
    private void generateEntityCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String entityPackage = param.getEntityPackage();
        String entityPackFilePath = entityPackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + entityPackFilePath + "/" + context.get("entityNameUp") + ".java";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    // 生成mapper层代码
    private void generateMapperCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String mapperPackage = param.getMapperPackage();
        String mapperPackageFilePath = mapperPackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + mapperPackageFilePath + "/" + context.get("entityNameUp") + "Mapper.java";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    // 生成xml代码
    private void generateMapperXmlCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String mapperPackage = param.getMapperPackage();
        String mapperPackageFilePath = mapperPackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + mapperPackageFilePath + "/xml/" + context.get("entityNameUp") + "Mapper.xml";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    // 生成xml extend 代码
    private void generateMapperXmlExtendCode(CurdParamExtend param, String templateFilePath) {
        VelocityUtils instance = param.getInstance();
        VelocityContext context = param.getContext();
        String gitProjectPath = param.getGitProjectPath();
        String generatedCurdCodePath = param.getGeneratedCurdCodePath();

        String mapperPackage = param.getMapperPackage();
        String mapperPackageFilePath = mapperPackage.replaceAll("\\.", "/");
        String newFilePath = generatedCurdCodePath + "/"  + mapperPackageFilePath + "/xml/extend/" + context.get("entityNameUp") + "Mapper.xml";

        String newContent = instance.compileVelocityFile(getGitProjectPath(gitProjectPath, templateFilePath), context);
        writeNewContentToFile(newFilePath, newContent);
    }

    /**
     * 将内容写入文件
     * @param newFilePath
     * @param newContent
     */
    private void writeNewContentToFile(String newFilePath, String newContent){
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

    /**
     * 获得老git模板工程内容
     *
     * @param oldFilePath
     * @return
     */
    private String getGitProjectPath(String gitProjectPath, String oldFilePath) {
        StringBuilder newFileBuild = new StringBuilder();
        if (File.separator.equals("\\")) {
            oldFilePath = oldFilePath.replaceAll("\\\\", "/");
        }
        String oldBasePath2 = gitProjectPath.replaceAll("/", "@");

        String gitProject = oldFilePath.replaceAll("/", "@")
                .replaceAll(oldBasePath2, "")
                .replaceAll("@", "/");

        return gitProject.substring(1);
    }
}
