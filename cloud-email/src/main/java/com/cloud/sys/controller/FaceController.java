package com.cloud.sys.controller;

import cn.hutool.core.codec.Base64;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.FaceEngineService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Api(value = "人脸识别功能",tags = "人脸识别功能")
@Controller
public class FaceController {

    public final static Logger logger = LoggerFactory.getLogger(FaceController.class);


    @Autowired
    private FaceEngineService faceEngineService;

    @GetMapping("/fase")
    @ApiOperation(value = "跳转到人脸识别页面",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", paramType = "query", required = true)
    })
    public ModelAndView toList(Long userid) {
        ModelAndView model = new ModelAndView();
        model.addObject("userid",userid);
        model.setViewName("fase_recognition/fase_index");
        return model;
    }
    /*
    人脸添加
     */
    @RequestMapping(value = "/faceAdd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加人脸识别信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "base64File  字符串", paramType = "query", required = true),
            @ApiImplicitParam(name = "userid", value = "用户id", paramType = "query", required = true)
    })
    public Result<Object> faceAdd(@RequestParam("file") String file, @RequestParam("userid") Long userid) {
        Result result = Result.getInstance();
        try {
            if (file == null) {
                return result;
            }

            byte[] decode = Base64.decode(base64Process(file));
            ImageInfo imageInfo = ImageFactory.getRGBData(decode);

            //人脸特征获取
            byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
            if (bytes == null) {
                return result;
            }

            UserEntity userFaceInfo = new UserEntity();
            //userFaceInfo.setName(name);
            //userFaceInfo.setGroupId(groupId);
            //userFaceInfo.setFaceFeature(bytes);
            //userFaceInfo.setId(ShiroUtils.getLoginUserId());
            //userFaceInfo.setFaceId(RandomUtil.randomString(10));

            //人脸特征插入到数据库
            //userFaceInfoService.insertSelective(userFaceInfo);
            LambdaUpdateWrapper<UserEntity> lambda = new UpdateWrapper<UserEntity>().lambda();
            lambda.set(UserEntity::getFaceFeature, bytes);
            lambda.eq(UserEntity::getId, userid);
            userFaceInfo.update(lambda);
            //logger.info("faceAdd:" + name);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("人脸识别图片已上传!!!!!!");
            return result;
        } catch (Exception e) {
            logger.error("", e);
        }
        return result;
    }

    /*
    人脸识别
     */
    @RequestMapping(value = "/faceSearch", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "当前登录人脸识别验证",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "base64File  字符串", paramType = "query", required = true)
    })
    public Result faceSearch(String file) throws Exception {
        Result result = Result.getInstance();
        byte[] decode = Base64.decode(base64Process(file));
        BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(decode));
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);


        //人脸特征获取
        byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
        if (bytes == null) {
            return result;
        }
        //人脸比对，获取比对结果
        List<UserEntity> userFaceInfoList = faceEngineService.compareFaceFeature(bytes);

        if (0 < userFaceInfoList.size()){
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("人脸识别图片已上传!!!!!!");
            result.setData(userFaceInfoList.get(0));
        }else {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("人脸匹配失败!!!!!!");
        }

//        if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
//            UserEntity faceUserInfo = userFaceInfoList.get(0);
//            UserEntity faceSearchResDto = new UserEntity();
//            BeanUtil.copyProperties(faceUserInfo, faceSearchResDto);
//            List<UserEntity> processInfoList = faceEngineService.process(imageInfo);
//            if (CollectionUtil.isNotEmpty(processInfoList)) {
//                //人脸检测
//                List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);
//                int left = faceInfoList.get(0).getRect().getLeft();
//                int top = faceInfoList.get(0).getRect().getTop();
//                int width = faceInfoList.get(0).getRect().getRight() - left;
//                int height = faceInfoList.get(0).getRect().getBottom() - top;
//
//                Graphics2D graphics2D = bufImage.createGraphics();
//                graphics2D.setColor(Color.RED);//红色
//                BasicStroke stroke = new BasicStroke(5f);
//                graphics2D.setStroke(stroke);
//                graphics2D.drawRect(left, top, width, height);
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                ImageIO.write(bufImage, "jpg", outputStream);
//                byte[] bytes1 = outputStream.toByteArray();
//                //faceSearchResDto.setImage("data:image/jpeg;base64," + Base64Utils.encodeToString(bytes1));
//                //faceSearchResDto.setAge(processInfoList.get(0).getAge());
//                //faceSearchResDto.setGender(processInfoList.get(0).getGender().equals(1) ? "女" : "男");
//
//            }

//            return result;
//        }
        return result;
    }


    @RequestMapping(value = "/detectFaces", method = RequestMethod.POST)
    @ResponseBody
    public List<FaceInfo> detectFaces(String image) throws IOException {
        byte[] decode = Base64.decode(image);
        InputStream inputStream = new ByteArrayInputStream(decode);
        ImageInfo imageInfo = ImageFactory.getRGBData(inputStream);

        if (inputStream != null) {
            inputStream.close();
        }
        List<FaceInfo> faceInfoList = faceEngineService.detectFaces(imageInfo);

        return faceInfoList;
    }


    private String base64Process(String base64Str) {
        if (!StringUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }

            return base64Str;
        } else {
            return "";
        }
    }
}
