package com.testproj.api.services;

import com.testproj.api.dtos.filters.ProductFilter;
import com.testproj.api.dtos.models.ProductDTO;
import com.testproj.db.pb.schema.model.Product;

import com.testproj.db.pb.ProductDS;
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
    private final ProductDS productDS;//TODO final or Autowire
    public List<ProductDTO> list() {
        return  beanMapper.map(productDS.list(), ProductDTO.class);
    }

    public ProductDTO findById(Integer prdId){
        return beanMapper.map(productDS.findById(prdId), ProductDTO.class);
    }

    public void addProduct(ProductDTO product){
        productDS.addProduct(beanMapper.map(product, Product.class));
    }

    public List<ProductDTO> searchProduct(ProductFilter productFilter){
        List<Condition> conditions = new ArrayList<>();//TODO: processing of conditions
        return beanMapper.map(productDS.searchProduct(conditions), ProductDTO.class);
    }

    public ProductDTO update(ProductDTO productDTO, Integer id){
        return beanMapper.map(productDS.update(beanMapper.map(productDTO, Product.class),id), ProductDTO.class);}

    public void delete(Integer id){
        productDS.delete(id);}
}