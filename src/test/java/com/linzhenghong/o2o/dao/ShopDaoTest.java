package com.linzhenghong.o2o.dao;

import com.linzhenghong.o2o.BaseTest;
import com.linzhenghong.o2o.entity.Area;
import com.linzhenghong.o2o.entity.PersonInfo;
import com.linzhenghong.o2o.entity.Shop;
import com.linzhenghong.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    /**
     *
     */
    @Test
    public void testQueryShopList(){
        Shop shopCondition=new Shop();
        PersonInfo owner=new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> shopList=shopDao.queryShopList(shopCondition,0,5);
        int count=shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小"+shopList.size());
        System.out.println("店铺总数:"+count);
    }

    @Test
    public void testQueryShopListAndCount(){
        Shop shopCondition=new Shop();
        ShopCategory childCategory=new ShopCategory();
        ShopCategory parentCategory=new ShopCategory();
        parentCategory.setShopCategoryId(12L);
        childCategory.setParent(parentCategory);
        shopCondition.setShopCategory(childCategory);
        List<Shop> shopList=shopDao.queryShopList(shopCondition,0,6);
        int count=shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小"+shopList.size());
        System.out.println("店铺总数:"+count);
    }

    /**
     * 根据shopId查询店铺
     */
    @Test
    public void testQueryByShopId(){
        long shopId=1;
        Shop shop=shopDao.queryByShopId(shopId);
        System.out.println("店铺类别:"+shop.getShopCategory().getShopCategoryName()+", 店铺区域:"+shop.getArea().getAreaName());
    }

    /**
     * 新增店铺
     */
    @Test
    public void testInsertShop(){
        Shop shop=new Shop();
        PersonInfo owner=new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory=new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);
        shop.setOwner(owner);
        shop.setShopName("测试店铺");
        shop.setShopDesc("测试");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectNum=shopDao.insertShop(shop);
        System.out.println(effectNum==1);
    }

    /**
     * 更新店铺，记得把更新时间写上
     */
    @Test
    public void testUpdateShop(){
        Shop shop=new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        shop.setLastEditTime(new Date());
        int effectNum=shopDao.updateShop(shop);
        System.out.println(effectNum==1);
    }
}
