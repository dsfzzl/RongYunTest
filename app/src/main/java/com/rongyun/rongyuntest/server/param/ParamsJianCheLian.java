package com.rongyun.rongyuntest.server.param;

/**
 * Created by Administrator on 2018/5/10.
 */

public class ParamsJianCheLian {
    //base64编码后的图片数据，需urlencode，编码后的图片大小不超过2M
    public String image;
    //最多处理人脸的数目，默认值为1，仅检测图片中面积最大的那个人脸
    public long  max_face_num = 1;
    //包括age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities信息，逗号分隔，默认只返回人脸框、概率和旋转角度
    public String face_field = "age,beauty,expression,faceshape,gender,glasses,landmark,race,quality,facetype,parsing" ;

    public String image_type = "BASE64";

    public ParamsJianCheLian(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ParamsJianCheLian{" +
                "image='" + image + '\'' +
                ", max_face_num=" + max_face_num +
                ", face_field='" + face_field + '\'' +
                ", image_type='" + image_type + '\'' +
                '}';
    }
}
