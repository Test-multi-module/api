package com.db.ds;

import com.db.model.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.db.schema.generated.Tables.PRODUCT;

@Service
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductServiceDS {

    protected final DSLContext jooq;

    public List<Product> list() {
        return jooq.select().from(PRODUCT).fetchInto(Product.class);
    }

    public Product findById(int prdId){
        return jooq.select().from(PRODUCT).where(PRODUCT.ID.eq(prdId)).fetchInto(Product.class).get(0);
    }

    public void addProduct(Product product){

    }

    // TODO: rework without filter, but with conditions
    public List<Product> searchProduct(List<Condition> conditions){
        List<Product> test = new ArrayList<>();
        return test;
    }

    private boolean categoryFound(String filterCategory, String productCategories) {
        boolean categoryFound = false;
        String[] categoriesArray = productCategories.split(",");
        for(int i = 0 ; i < categoriesArray.length; i++){
            categoriesArray[i] = categoriesArray[i].trim();
        }

        for(String category: categoriesArray){
            if (filterCategory.toLowerCase().equals(category.toLowerCase())){
                categoryFound = true;
                break;
            }
        }
        return categoryFound;
    }

    public Product update(Product product, int id){//todo
        return null;
    }

    public void delete(int id){}
}