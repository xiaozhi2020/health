package cn.itcast.jobs;


import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisConstant;
import cn.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    //根据Redis中 保存的两个集合进行差值计算,获得垃圾图片名称集合
    public void clearImg(){
        Set<String> picNameSet = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (picNameSet!=null){
            for (String picName : picNameSet) {
                //删除七牛云上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //删除Redis里面的图片
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
                System.out.println("自定义任务清理垃圾图片:"+picName);
            }
        }

    }
}
