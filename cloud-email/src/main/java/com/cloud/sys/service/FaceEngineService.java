package com.cloud.sys.service;

import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.UserEntity;
import com.cloud.sys.mapper.FaceEngineFactory;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@Service
@Slf4j
public class FaceEngineService {

   @Autowired
   private UserServiceImpl userService;

    @Value("${config.arcface-sdk.sdk-lib-path}")
    public String sdkLibPath;
    @Value("${config.arcface-sdk.app-id}")
    public String appId;

    @Value("${config.arcface-sdk.sdk-key}")
    public String sdkKey;

    @Value("${config.arcface-sdk.thread-pool-size}")
    public Integer threadPoolSize;


    private Integer passRate = 80;

    private ExecutorService executorService;



    private GenericObjectPool<FaceEngine> faceEngineObjectPool;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(threadPoolSize);
        poolConfig.setMaxTotal(threadPoolSize);
        poolConfig.setMinIdle(threadPoolSize);
        poolConfig.setLifo(false);

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);


        faceEngineObjectPool = new GenericObjectPool(new FaceEngineFactory(sdkLibPath, appId, sdkKey, engineConfiguration), poolConfig);//底层库算法对象池

    }


    private int plusHundred(Float value) {
        BigDecimal target = new BigDecimal(value);
        BigDecimal hundred = new BigDecimal(100f);
        return target.multiply(hundred).intValue();

    }

    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();

            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();

            //人脸检测
            faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            return faceInfoList;
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return null;
    }


    public List<UserEntity> process(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            //人脸检测
            faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            int processResult = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, FunctionConfiguration.builder().supportAge(true).supportGender(true).build());
            List<UserEntity> processInfoList = Lists.newLinkedList();

            List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
            //性别提取
            int genderCode = faceEngine.getGender(genderInfoList);
            //年龄提取
            List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
            int ageCode = faceEngine.getAge(ageInfoList);
            for (int i = 0; i < genderInfoList.size(); i++) {
                UserEntity processInfo = new UserEntity();
                //processInfo.set(genderInfoList.get(i).getGender());
                //processInfo.setAge(ageInfoList.get(i).getAge());
                processInfoList.add(processInfo);
            }
            return processInfoList;

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }

        return null;

    }

    /**
     * 人脸特征
     *
     * @param imageInfo
     * @return
     */
    public byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException {

        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();

            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();

            //人脸检测
            int i = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);

            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                FaceFeature faceFeature = new FaceFeature();
                //提取人脸特征
                faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);

                return faceFeature.getFeatureData();
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }

        }

        return null;
    }

    public List<UserEntity> compareFaceFeature(byte[] faceFeature) throws InterruptedException, ExecutionException {
        List<UserEntity> resultFaceInfoList = Lists.newLinkedList();//识别到的人脸列表

        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature);
        LambdaQueryWrapper<UserEntity> lambda = new QueryWrapper<UserEntity>().lambda();
        lambda.eq(UserEntity::getId,ShiroUtils.getLoginUserId());
        List<UserEntity> faceInfoList = userService.list(lambda); //从数据库中取出人脸库
        List<List<UserEntity>> UserEntityPartList = Lists.partition(faceInfoList, 1000);//分成1000一组，多线程处理
        CompletionService<List<UserEntity>> completionService = new ExecutorCompletionService(executorService);
        for (List<UserEntity> part : UserEntityPartList) {
            completionService.submit(new CompareFaceTask(part, targetFaceFeature));
        }
        for (int i = 0; i < UserEntityPartList.size(); i++) {
            List<UserEntity> UserEntityList = completionService.take().get();
            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                resultFaceInfoList.addAll(UserEntityList);
            }
        }

        resultFaceInfoList.sort((h1, h2) -> h2.getSimilarValue().compareTo(h1.getSimilarValue()));//从大到小排序
        return resultFaceInfoList;
    }


    private class CompareFaceTask implements Callable<List<UserEntity>> {

        private List<UserEntity> UserEntityList;
        private FaceFeature targetFaceFeature;


        public CompareFaceTask(List<UserEntity> UserEntityList, FaceFeature targetFaceFeature) {
            this.UserEntityList = UserEntityList;
            this.targetFaceFeature = targetFaceFeature;
        }

        @Override
        public List<UserEntity> call() throws Exception {
            FaceEngine faceEngine = null;
            List<UserEntity> resultFaceInfoList = Lists.newLinkedList();//识别到的人脸列表
            try {
                faceEngine = faceEngineObjectPool.borrowObject();
                for (UserEntity UserEntity : UserEntityList) {
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(UserEntity.getFaceFeature());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    Integer similarValue = plusHundred(faceSimilar.getScore());//获取相似值
                    if (similarValue > passRate) {//相似值大于配置预期，加入到识别到人脸的列表

                        UserEntity info = new UserEntity();
                        //info.setName(UserEntity.getName());
                        //info.setFaceId(UserEntity.getFaceId());
                        info.setSimilarValue(similarValue);
                        resultFaceInfoList.add(info);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                if (faceEngine != null) {
                    faceEngineObjectPool.returnObject(faceEngine);
                }
            }

            return resultFaceInfoList;
        }

    }
}
