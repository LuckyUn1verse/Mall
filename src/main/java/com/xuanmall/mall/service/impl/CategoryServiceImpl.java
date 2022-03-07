package com.xuanmall.mall.service.impl;

import com.xuanmall.mall.dao.CategoryMapper;
import com.xuanmall.mall.pojo.Category;
import com.xuanmall.mall.service.ICategoryService;
import com.xuanmall.mall.vo.CategoryVo;
import com.xuanmall.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.xuanmall.mall.consts.MallConst.ROOT_PARENT_ID;

@Service
public class CategoryServiceImpl implements ICategoryService {
    /*
    *
    * */
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<CategoryVo> categoryVoList=new ArrayList<>();
        List<Category> categories=categoryMapper.selectAll();
        //获得所有类目的表项

        //查出parent_id=0的表项，即大类
        for(Category category:categories){
            if(category.getParentId().equals(ROOT_PARENT_ID)){
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category,categoryVo);
                categoryVoList.add(categoryVo);
            }
        }
       categoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());

//        //lambda+stream写法
//        List<Category> categories=categoryMapper.selectAll();
//        List<CategoryVo> categoryVoList = categories.stream()
//                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
//                .map(this::category2CategoryVo)
//                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
//                .collect(Collectors.toList());

        //查询子目录,依靠小类的父id是否等于大类id来确定子类
        findSubCategory(categoryVoList,categories);

        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories=categoryMapper.selectAll();
        findSubCategoryId(id, resultSet,categories);
    }
    private void findSubCategoryId(Integer id, Set<Integer> resultSet,List<Category> categories){
        for (Category category : categories) {
            if(category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }

    private void findSubCategory(List<CategoryVo> categoryVoList,List<Category> categories){
        //查询子目录,依靠小类的父id是否等于大类id来确定子类
        for(CategoryVo categoryVo:categoryVoList){
            List<CategoryVo> subCategoryVoList=new ArrayList<>();

            for(Category category:categories){
                //如果查到内容，设置subCategory，继续往下查
                if(categoryVo.getId().equals(category.getParentId())){
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                //SortOrder越大，优先级越高

                //subCategoryVoList 获得的子目录 setSubCategories设置大类的子目录
                categoryVo.setSubCategories(subCategoryVoList);

                findSubCategory(subCategoryVoList,categories);
                //多级目录递归查找
            }
        }
    }

    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }


}
