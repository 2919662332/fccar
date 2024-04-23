package cn.itsource.template;

import cn.hutool.core.codec.PunyCode;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.itsource.exception.GlobalException;
import cn.itsource.pojo.config.ParamsConfig;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

public class OssTemplate {

    private final ParamsConfig paramsConfig;
    @Resource
    private AuthTemplate authTemplate;

    public OssTemplate(ParamsConfig paramsConfig) {
        this.paramsConfig = paramsConfig;
    }


    //方法

    public String getImgUrl(MultipartFile file) throws IOException {
        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(paramsConfig.getAccessKeyId(), paramsConfig.getAccessKeySecret());
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = paramsConfig.getEndpoint();
        // 填写Bucket名称，
        String bucketName = paramsConfig.getBucketName();
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String extName = FileNameUtil.extName(file.getOriginalFilename());
        String UUname = IdUtil.fastSimpleUUID();
        String objectName = UUname + "." + extName;
        String ImgUrl = String.format(paramsConfig.getRespUrl(),paramsConfig.getBucketName(),objectName);

        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        InputStream inputStream = file.getInputStream();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        try {
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException | ClientException e) {
            throw new GlobalException("上传文件出错！");
        }
        /**
         * 1.先上传到云
         * 2.判断图片是否违规
         * 3.删除图片
         */
        return ImgUrl;
    }
    public String uploadAggrementFile(MultipartFile file) throws IOException {
        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(paramsConfig.getAccessKeyId(), paramsConfig.getAccessKeySecret());
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = paramsConfig.getEndpoint();
        // 填写Bucket名称，
        String bucketName = paramsConfig.getBucketName();
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String extName = FileNameUtil.extName(file.getOriginalFilename());
        String UUname = IdUtil.fastSimpleUUID();
        String objectName = UUname + "." + extName;
        String ImgUrl = String.format(paramsConfig.getRespUrl(),paramsConfig.getBucketName(),objectName);

        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        InputStream inputStream = file.getInputStream();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        try {
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException | ClientException e) {
            throw new GlobalException("上传文件出错！");
        }
        /**
         * 1.先上传到云
         * 2.判断图片是否违规
         * 3.删除图片
         */
        return ImgUrl;
    }
}
