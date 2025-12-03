package net.testproj.db.pb;

import net.testproj.db.pb.schema.Tables;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDS {

    protected final DSLContext jooq;
    public UserDS(@Qualifier("publicDslContext") DSLContext dsl) {this.jooq = dsl;}

    public List<Product> list() {
        List<Product> products = jooq.select().from(Tables.PRODUCT).fetchInto(Product.class);
        products.get(0).setTitle("i am from ds service");
        return products;
    }

    public Product findById(int prdId){
        return jooq.select().from(Tables.PRODUCT).where(Tables.PRODUCT.ID.eq(prdId)).fetchInto(Product.class).get(0);
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