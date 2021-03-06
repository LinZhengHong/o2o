package com.linzhenghong.o2o.web.shopadmin;

import com.linzhenghong.o2o.dto.ProductCategoryExecution;
import com.linzhenghong.o2o.dto.Result;
import com.linzhenghong.o2o.entity.ProductCategory;
import com.linzhenghong.o2o.entity.Shop;
import com.linzhenghong.o2o.enums.ProductCategoryStateEnum;
import com.linzhenghong.o2o.service.ProductCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商铺类别控制器
 * @author LinZhenHong
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
        //To be removed
        Shop shop=new Shop();
        shop.setShopId(1L);
        request.getSession().setAttribute("currentShop",shop);

        Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list=null;
        if (currentShop!=null&&currentShop.getShopId()>0){
            list=productCategoryService.getProductCategoryList(shop.getShopId());
            return new Result<List<ProductCategory>>(true,list);
        }else{
            ProductCategoryStateEnum productCategoryStateEnum=ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,productCategoryStateEnum.getState(),productCategoryStateEnum.getStateInfo());
        }
    }

    @RequestMapping(value = "/addproductcategorys",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategories(@RequestBody List<ProductCategory> productCategories,HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory productCategory:productCategories){
            productCategory.setShopId(currentShop.getShopId());
        }
        if(productCategories!=null&&productCategories.size()>0){
            try{
                ProductCategoryExecution productCategoryExecution= productCategoryService.batchAddProductCategory(productCategories);
                if(productCategoryExecution.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",productCategoryExecution.getStateInfo());
                }
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少输入一个商品类别");
        }
        return modelMap;
    }

    @RequestMapping(value = "/removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个商品类别");
        }
        return modelMap;
    }

}
