package com.testproj.api.services;

import com.testproj.api.dtos.filters.ProductFilter;
import com.testproj.api.dtos.models.ProductDTO;
import com.testproj.db.model.Product;

import com.testproj.db.ds.ProductServiceDS;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.jooq.Condition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class ProductService {
    private final BeanMapper beanMapper;
    private final ProductServiceDS productServiceDS;//TODO final or Autowire
    public List<ProductDTO> list() {
        return  beanMapper.map(productServiceDS.list(), ProductDTO.class);
    }

    public ProductDTO findById(Integer prdId){
        return beanMapper.map(productServiceDS.findById(prdId), ProductDTO.class);
    }

    public void addProduct(ProductDTO product){
        productServiceDS.addProduct(beanMapper.map(product, Product.class));
    }

    public List<ProductDTO> searchProduct(ProductFilter productFilter){
        List<Condition> conditions = new ArrayList<>();//TODO: processing of conditions
        return beanMapper.map(productServiceDS.searchProduct(conditions), ProductDTO.class);
    }

    public ProductDTO update(ProductDTO productDTO, Integer id){
        return beanMapper.map(productServiceDS.update(beanMapper.map(productDTO, Product.class),id), ProductDTO.class);}

    public void delete(Integer id){productServiceDS.delete(id);}
}